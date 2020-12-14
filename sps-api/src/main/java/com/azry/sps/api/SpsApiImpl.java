package com.azry.sps.api;

import com.azry.sps.api.model.Namespace;
import com.azry.sps.api.model.SpsApiException;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryRequest;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryResponse;
import com.azry.sps.api.model.addpaymentlistentry.PaymentListEntryDTO;
import com.azry.sps.api.model.calculateclientcommission.CalculateClientCommissionRequest;
import com.azry.sps.api.model.calculateclientcommission.CalculateClientCommissionResponse;
import com.azry.sps.api.model.getServices.GetServicesRequest;
import com.azry.sps.api.model.getServices.GetServicesResponse;
import com.azry.sps.api.model.getServices.ServiceDTO;
import com.azry.sps.api.model.getinfo.GetInfoRequest;
import com.azry.sps.api.model.getinfo.GetInfoResponse;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoRequest;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoResponse;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListRequest;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListResponse;
import com.azry.sps.api.model.getservicegroups.GetServiceGroupsResponse;
import com.azry.sps.api.model.getservicegroups.ServiceGroupDTO;
import com.azry.sps.api.model.pay.PayRequest;
import com.azry.sps.api.model.pay.PaymentStatusDTO;
import com.azry.sps.api.model.removepaymentlistentry.RemovePaymentListEntryRequest;
import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.common.model.groups.ServiceGroup;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.common.model.paymentlist.PaymentListEntry;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.service.ServiceChannelInfo;
import com.azry.sps.fi.model.exception.FIConnectivityException;
import com.azry.sps.fi.model.exception.FIException;
import com.azry.sps.fi.service.BankIntegrationService;
import com.azry.sps.integration.sp.ProviderIntegrationService;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.server.services.channel.ChannelManager;
import com.azry.sps.server.services.clientcommission.ClientCommissionsManager;
import com.azry.sps.server.services.payment.PaymentManager;
import com.azry.sps.server.services.paymentlist.PaymentListManager;
import com.azry.sps.server.services.service.ServiceManager;
import com.azry.sps.server.services.servicecommission.ServiceCommissionsManager;
import com.azry.sps.server.services.servicegroup.ServiceGroupManager;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import org.jboss.ws.api.annotation.WebContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.persistence.NonUniqueResultException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Interceptors(SpsApiInterceptor.class)
@WebService(endpointInterface = "com.azry.sps.api.SpsApi", targetNamespace = Namespace.TN, portName = "SpsApiPort", serviceName = "SpsApiService")
@WebContext(contextRoot = "/sps-integration-api", urlPattern = "/sps-api")
public class SpsApiImpl implements SpsApi{

	@Inject
	ProviderIntegrationService providerIntegrationService;

	@Inject
	ServiceManager serviceManager;

	@Inject
	ChannelManager channelManager;

	@Inject
	TransactionOrderManager transactionOrderManager;

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
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}

		GetInfoResponse response = new GetInfoResponse();
		Service svc = serviceManager.getService(request.getServiceId());

		if (svc == null) {
			throw new SpsApiException("Service not found.");
		}

		try {
			AbonentInfo abonentInfo = providerIntegrationService.getInfo(svc.getServiceDebtCode(), request.getAbonentCode());

			response.setDebt(abonentInfo.getDebt());
			response.setInfoMessage(abonentInfo.getAbonentInfo());

			return response;
		} catch (SpConnectivityException ex) {
			throw new SpsApiException("Can not connect to service provider.");
		}

	}


	@Override
	public void pay(PayRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}
		Service svc = serviceManager.getService(request.getServiceId());
		if (svc == null) {
			throw new SpsApiException("Invalid service ID.");
		}

		checkChannelForService(svc, request.getChannelId());

		ServiceCommissions svcCommission = serviceCommissionsManager.getCommissionForService(svc.getId());

		BigDecimal svcCommissionAmount;

		if (svcCommission == null || svcCommission.getCommission() == null) {
			svcCommissionAmount = BigDecimal.valueOf(0);
		} else {
			if (svcCommission.getRateType() == CommissionRateType.PERCENT) {
				svcCommissionAmount = BigDecimal.valueOf(svcCommission.getCommission().floatValue() * request.getAmount().floatValue() / 100);
			} else {
				svcCommissionAmount = svcCommission.getCommission();
			}
		}

		if (compareBigDecimals(request.getAmount(), svc.getMinAmount(), 2) > 0) {
			throw new SpsApiException("Requested amount is less than minimal amount of the service.");
		}

		if (compareBigDecimals(request.getAmount(), svc.getMaxAmount(), 2) < 0) {
			throw new SpsApiException("Requested amount is more than maximal amount of the service.");
		}

		Payment payment = new Payment();
		payment.setAbonentCode(request.getAbonentCode());
		payment.setClCommission(request.getClientCommission());
		payment.setChannelId(request.getChannelId());
		payment.setServiceId(svc.getId());
		payment.setAgentPaymentId(request.getAgentPaymentId());
		payment.setStatus(PaymentStatus.CREATED);
		payment.setAmount(request.getAmount());
		payment.setSvcCommission(svcCommissionAmount);


		try {
			payment.setClient(bankIntegrationService.getClientWithAccount(request.getPersonalNumber()).toClient());
		} catch (FIException ex) {
			throw new SpsApiException("Error while interacting with bank: " + ex.getMessage());
		} catch (FIConnectivityException ex) {
			throw new SpsApiException("Could not connect to bank.");
		}
		Payment oldPayment;
		try {
			oldPayment = paymentManager.getPaymentByAgentId(payment.getAgentPaymentId());
		} catch (NonUniqueResultException ex) {
			throw new SpsApiException("There are multiple payments with given ID.");
		}

		if (oldPayment != null) {
			if (equalPayments(payment, oldPayment)) {
				return;
			} else {
				throw new SpsApiException("Payment ID is not unique.");
			}
		}

		paymentManager.addPayment(payment, request.getClientAccountBAN());

	}

	@Override
	public GetPaymentInfoResponse getPaymentInfo(GetPaymentInfoRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}
		Payment payment;
		try {
			payment = paymentManager.getPaymentByAgentId(request.getAgentPaymentId());
		} catch (NonUniqueResultException ex) {
			throw new SpsApiException("There are multiple payments with given ID.");
		}
		if (payment == null) {
			throw new SpsApiException("Payment not found.");
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
	public GetPaymentListResponse getPaymentList(GetPaymentListRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request");
		}
		PaymentList paymentList = paymentListManager.getPaymentList(request.getPersonalNumber());

		if (paymentList == null) {
			throw new SpsApiException("No payment list found for personal number " + request.getPersonalNumber());
		}

		GetPaymentListResponse response = new GetPaymentListResponse();
		response.setPaymentLists(new ArrayList<>());
		for (PaymentListEntry entry : paymentList.getEntries()) {
			response.getPaymentLists().add(PaymentListEntryDTO.listEntryToDTO(entry));
		}

		return response;
	}

	@Override
	public AddPaymentListEntryResponse addPaymentListEntry(AddPaymentListEntryRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}
		PaymentList paymentList = paymentListManager.getPaymentList(request.getPersonalNumber());


		if (paymentList == null) {
			paymentList = new PaymentList();
			try {
				paymentList.setClient(bankIntegrationService.getClientWithAccount(request.getPersonalNumber()).toClient());
			} catch (FIException ex) {
			throw new SpsApiException("Error while interacting with bank: " + ex.getMessage());
			} catch (FIConnectivityException ex) {
				throw new SpsApiException("Could not connect to bank.");
			}

			paymentListManager.addPaymentList(paymentList);
		}

		PaymentListEntry entry = PaymentListEntryDTO.dtoToListEntry(request.getPaymentListEntry());

		if (serviceManager.getService(entry.getServiceId()) == null) {
			throw new SpsApiException("Service not found.");
		}

		entry = paymentListManager.addPaymentListEntry(paymentList.getClient(), entry);

		AddPaymentListEntryResponse response = new AddPaymentListEntryResponse();
		response.setId(entry.getId());

		return response;
	}

	@Override
	public void removePaymentListEntry(RemovePaymentListEntryRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}
		paymentListManager.deletePaymentListEntry(request.getId());
	}

	@Override
	public GetServicesResponse getServices(GetServicesRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}

		if (channelManager.getChannel(request.getChannelId()) == null) {
			throw new SpsApiException("Channel not found");
		}
		List<Service> services = serviceManager.getServicesByChannelId(request.getChannelId());

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
	public CalculateClientCommissionResponse calculateClientCommission(CalculateClientCommissionRequest request) throws SpsApiException {
		if (!request.isValid()) {
			throw new SpsApiException("Invalid request.");
		}

		Service svc = serviceManager.getService(request.getServiceId());

		if (svc == null) {
			throw new SpsApiException("Service not available.");
		}

		Channel channel = channelManager.getChannel(request.getChannelId());
		if (channel == null) {
			throw new SpsApiException("Channel not available.");
		}

		checkChannelForService(svc, channel.getId());

		ClientCommissions commission = clientCommissionsManager.getClientCommission(request.getServiceId(), request.getChannelId());
		BigDecimal clCommissionAmount;
		CalculateClientCommissionResponse response = new CalculateClientCommissionResponse();

		if (commission == null) {
			clCommissionAmount = BigDecimal.valueOf(0);
			response.setClientCommission(clCommissionAmount);
			return response;
		}

		if (commission.getCommission() == null) {
			if (commission.getMinCommission() == null) {
				clCommissionAmount = new BigDecimal(0);
			} else {
				clCommissionAmount = BigDecimal.valueOf(commission.getMinCommission().floatValue());
			}
			response.setClientCommission(clCommissionAmount);
			return response;
		}

		if (commission.getRateType() == CommissionRateType.PERCENT) {
			clCommissionAmount = BigDecimal.valueOf(commission.getCommission().floatValue() * request.getAmount().floatValue() / 100);
		} else {
			clCommissionAmount = commission.getCommission();
		}

		if (commission.getMinCommission() != null) {
			if (compareBigDecimals(clCommissionAmount, commission.getMinCommission(), 2) > 0) {
				clCommissionAmount = BigDecimal.valueOf(commission.getMinCommission().floatValue());
			}

			if (compareBigDecimals(clCommissionAmount, commission.getMaxCommission(), 2) < 0) {
				clCommissionAmount = BigDecimal.valueOf(commission.getMaxCommission().floatValue());
			}
		}
		response.setClientCommission(clCommissionAmount);

		return response;
	}

	private void checkChannelForService(Service service, long channelId) throws SpsApiException {
		boolean hasChannel = service.isAllChannels();

		if (!hasChannel) {
			for (ServiceChannelInfo info : service.getChannels()) {
				if (info.getChannelId() == channelId) {
					if (!info.isActive()) {
						throw new SpsApiException("Selected Channel is not active for selected service");
					} else {
						hasChannel = true;
						break;
					}
				}
			}
		}

		if (!hasChannel) {
			throw new SpsApiException("Selected channel does not support selected service.");
		}

	}

	private long compareBigDecimals(BigDecimal first, BigDecimal second, int precision) {
		long exp = 1;

		for(int i = 0; i < precision; i ++){
			exp *= 10;
		}

		return second.multiply(new BigDecimal(exp)).longValue() - first.multiply(new BigDecimal(exp)).longValue();
	}



	public boolean equalPayments(Payment p1, Payment p2) {
		return p1.getAgentPaymentId().equals(p2.getAgentPaymentId())
			&& p1.getServiceId() == p2.getServiceId()
			&& p1.getChannelId() == p2.getChannelId()
			&& p1.getAbonentCode().equals(p2.getAbonentCode())
			&& compareBigDecimals(p1.getAmount(), p2.getAmount(), 2) == 0
			&& p1.getClient().equals(p2.getClient());
	}
}
