package com.azry.sps.api;

import com.azry.sps.api.model.Namespace;
import com.azry.sps.api.model.SpsApiException;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryRequest;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryResponse;
import com.azry.sps.api.model.addpaymentlistentry.PaymentListDTO;
import com.azry.sps.api.model.getServices.GetServicesResponse;
import com.azry.sps.api.model.getServices.ServiceDTO;
import com.azry.sps.api.model.getclientcommission.GetClientCommissionRequest;
import com.azry.sps.api.model.getclientcommission.GetClientCommissionResponse;
import com.azry.sps.api.model.getinfo.GetInfoRequest;
import com.azry.sps.api.model.getinfo.GetInfoResponse;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoRequest;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoResponse;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListResponse;
import com.azry.sps.api.model.getservicegroups.GetServiceGroupsResponse;
import com.azry.sps.api.model.getservicegroups.ServiceGroupDTO;
import com.azry.sps.api.model.pay.FiExceptionMessages;
import com.azry.sps.api.model.pay.PayRequest;
import com.azry.sps.api.model.pay.PaymentStatusDTO;
import com.azry.sps.api.model.removepaymentlistentry.RemovePaymentListEntryRequest;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.common.model.groups.ServiceGroup;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.common.model.paymentlist.PaymentListEntry;
import com.azry.sps.api.dto.GetInfoRequest;
import com.azry.sps.api.dto.GetInfoResponse;
import com.azry.sps.api.dto.GetInfoStatus;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.integration.sp.ProviderIntegrationService;
import com.azry.sps.common.model.service.ServiceChannelInfo;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FiException;
import com.azry.sps.fi.service.BankIntegrationService;
import com.azry.sps.integration.sp.ServiceProviderIntegrationService;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.integration.sp.exception.SpIntegrationException;
import com.azry.sps.server.services.clientcommission.ClientCommissionsManager;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.paymentlist.PaymentListManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.azry.sps.server.services.servicecommission.ServiceCommissionsManager;
import com.azry.sps.server.services.servicegroup.ServiceGroupManager;
import org.jboss.ws.api.annotation.WebContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
@WebService(endpointInterface = "com.azry.sps.api.SpsApi", targetNamespace = Namespace.TN, portName = "SpsApiPort", serviceName = "SpsApiService")
@WebContext(contextRoot = "/", urlPattern = "/sps-api")
public class SpsApiImpl implements SpsApi{

	@Inject
	ProviderIntegrationService providerIntegrationService;

	@Inject
	ServiceManager serviceManager;

	@Inject
	ClientCommissionsManager clientCommissionsManager;

	@Inject
	ServiceCommissionsManager serviceCommissionsManager;

	@Inject
	PaymentManager paymentManager;

	@Inject
	PaymentListManager paymentListManager;

	@Inject
	ServiceGroupManager serviceGroupManager;

	@Inject
	BankIntegrationService bankIntegrationService;

	@Override
	public GetInfoResponse getInfo(GetInfoRequest request) throws SpsApiException {
		GetInfoResponse response = new GetInfoResponse();
		Service svc = serviceManager.getService(request.getServiceId());

		if (svc == null) {
			throw new SpsApiException("Service not found!");
		}


		try {
			AbonentInfo abonentInfo = serviceProviderIntegrationService.getInfo(svc.getServiceDebtCode(), request.getAbonentCode());
			response.setDebt(abonentInfo.getDebt());
			response.setInfoMessage(abonentInfo.getMessage());

			return response;
		} catch (SpIntegrationException ex) {
			throw new SpsApiException("Can not connect to service provider.");
		}

	}


	@Override
	public void pay(PayRequest request) throws SpsApiException {

		Service svc = serviceManager.getService(request.getServiceId());
		if (svc == null) {
			throw new SpsApiException("Invalid service ID.");
		}

		boolean hasChannel = false;

		for(ServiceChannelInfo channel : svc.getChannels()) {
			if (channel.getChannelId() == request.getChannelId()) {
				if (!channel.isActive()) {
					throw new SpsApiException("Selected Channel is not available for selected service");
				}
				else {
					hasChannel = true;
					break;
				}
			}
		}

		if (!hasChannel) {
			throw new SpsApiException("Selected channel does not support selected service");
		}

		ClientCommissions clCommission = clientCommissionsManager.getClientCommission(svc.getId(), request.getChannelId());
		BigDecimal clCommissionAmount;
		if (clCommission == null) {
			clCommissionAmount = BigDecimal.valueOf(0);
		}
		else {
			if (clCommission.getRateType() == CommissionRateType.PERCENT) {
				clCommissionAmount = BigDecimal.valueOf(clCommission.getCommission().floatValue() * request.getAmount().floatValue() / 100);
			}
			else {
				clCommissionAmount = clCommission.getCommission();
			}
		}


		ServiceCommissions svcCommission = serviceCommissionsManager.getCommissionForService(svc.getId());

		BigDecimal svcCommissionAmount;

		if (svcCommission == null) {
			svcCommissionAmount = BigDecimal.valueOf(0);
		}
		else {
			if (svcCommission.getRateType() == CommissionRateType.PERCENT) {
				svcCommissionAmount = BigDecimal.valueOf(svcCommission.getCommission().floatValue() * request.getAmount().floatValue() / 100);
			}
			else {
				svcCommissionAmount = svcCommission.getCommission();
			}
		}



		Payment payment = new Payment();
		payment.setAbonentCode(request.getAbonentCode());
		payment.setClCommission(clCommissionAmount);
		payment.setChannelId(request.getChannelId());
		payment.setServiceId(svc.getId());
		payment.setAgentPaymentId(request.getAgentPaymentId());
		payment.setStatus(PaymentStatus.CREATED);
		payment.setAmount(request.getAmount());
		payment.setSvcCommission(svcCommissionAmount);
		payment.setClCommission(clCommissionAmount);


		try {
			payment.setClient(bankIntegrationService.getClientWithAccount(request.getPersonalNumber()).toClient());
		} catch (FiException ex) {
			throw new SpsApiException("Error while interacting with bank: " + FiExceptionMessages.valueOf(ex.getCode()).getMessage());
		} catch (FIConnectivityException ex) {
			throw new SpsApiException("Could not connect to bank.");
		}

		paymentManager.addPayments(Collections.singletonList(payment));
	}

	@Override
	public GetPaymentInfoResponse getPaymentInfo(GetPaymentInfoRequest request) throws SpsApiException {
		Payment payment = paymentManager.getPaymentByAgentId(request.getAgentPaymentId());

		if (payment == null) {
			throw new SpsApiException("Payment not found!");
		}

		GetPaymentInfoResponse response = new GetPaymentInfoResponse();

		response.setAbonentCode(payment.getAbonentCode());
		response.setAgentPaymentId(payment.getAgentPaymentId());
		response.setAmount(payment.getAmount());
		response.setChannelId(payment.getChannelId());
		response.setServiceId(payment.getServiceId());
		response.setClCommission(payment.getClCommission());
		response.setSvcCommission(payment.getSvcCommission());
		response.setStatus(PaymentStatusDTO.valueOf(payment.getStatus().name()));
		response.setStatusMessage(payment.getStatusMessage());
		return response;
	}

	@Override
	public GetPaymentListResponse getPaymentList(String personalNumber) throws SpsApiException {
		PaymentList paymentList = paymentListManager.getPaymentList(personalNumber);

		if (paymentList == null) {
			throw new SpsApiException("No payment list found for personal number " + personalNumber);
		}

		GetPaymentListResponse response = new GetPaymentListResponse();
		response.setPaymentLists(new ArrayList<>());
		for (PaymentListEntry entry : paymentList.getEntries()) {
			response.getPaymentLists().add(PaymentListDTO.listEntryToDTO(entry));
		}

		return response;
	}

	@Override
	public AddPaymentListEntryResponse addPaymentListEntry(AddPaymentListEntryRequest request) throws SpsApiException {

		PaymentList paymentList = paymentListManager.getPaymentList(request.getPersonalNumber());

		if (paymentList == null) {
			throw new SpsApiException("No payment list found for personal number " + request.getPersonalNumber());
		}
		PaymentListEntry entry = PaymentListDTO.dtoToListEntry(request.getPaymentList());
		entry = paymentListManager.addPaymentListEntry(paymentList.getClient(), entry);

		AddPaymentListEntryResponse response = new AddPaymentListEntryResponse();
		response.setId(entry.getId());

		return response;
	}

	@Override
	public void removePaymentListEntry(RemovePaymentListEntryRequest request) {
		paymentListManager.deletePaymentListEntry(request.getId());
	}

	@Override
	public GetServicesResponse getServices() {
		List<Service> services = serviceManager.getAllServices();

		GetServicesResponse response = new GetServicesResponse();
		response.setServices(ServiceDTO.entitiesToInfos(services));

		return response;
	}

	@Override
	public GetServiceGroupsResponse getServiceGroups() {
		List<ServiceGroup> serviceGroups = serviceGroupManager.getFilteredServiceGroups("");

		GetServiceGroupsResponse response = new GetServiceGroupsResponse();
		response.setServiceGroups(ServiceGroupDTO.entitiesToInfos(serviceGroups));
		return response;
	}

	@Override
	public GetClientCommissionResponse getClientCommission(GetClientCommissionRequest request) {
		ClientCommissions commission = clientCommissionsManager.getClientCommission(request.getServiceId(), request.getChannelId());

		GetClientCommissionResponse response = new GetClientCommissionResponse();

		response.setId(commission.getId());
		response.setAllChannels(commission.isAllChannels());
		response.setAllServices(commission.isAllServices());
		response.setChannelsIds(commission.getChannelsIds());
		response.setServicesIds(commission.getServicesIds());
		response.setMinCommission(commission.getMinCommission());
		response.setMaxCommission(commission.getMaxCommission());
		response.setPriority(commission.getPriority());
		response.setCommission(commission.getCommission());
		response.setRateType(commission.getRateType());
		return response;
	}
}
