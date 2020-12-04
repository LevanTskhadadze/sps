package com.azry.sps.api;

import com.azry.sps.api.model.Namespace;
import com.azry.sps.api.model.SpsApiException;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryRequest;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryResponse;
import com.azry.sps.api.model.getServices.GetServicesResponse;
import com.azry.sps.api.model.getclientcommission.GetClientCommissionRequest;
import com.azry.sps.api.model.getclientcommission.GetClientCommissionResponse;
import com.azry.sps.api.model.getinfo.GetInfoRequest;
import com.azry.sps.api.model.getinfo.GetInfoResponse;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoRequest;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoResponse;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListResponse;
import com.azry.sps.api.model.getservicegroups.GetServiceGroupsResponse;
import com.azry.sps.api.model.pay.PayRequest;
import com.azry.sps.api.model.removepaymentlistentry.RemovePaymentListEntryRequest;
import org.apache.cxf.annotations.EndpointProperties;
import org.apache.cxf.annotations.EndpointProperty;
import org.apache.cxf.annotations.SchemaValidation;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.RequestWrapper;
import java.rmi.RemoteException;

@Remote
@WebService(targetNamespace = Namespace.TN)
@EndpointProperties(value = {
	@EndpointProperty(key="schema-validation-enabled", value="true")
})
@SchemaValidation
public interface SpsApi extends java.rmi.Remote {

	@WebMethod(action = Namespace.TN + "/getInfo")
	GetInfoResponse getInfo(@WebParam(name = "GetInfoRequest", targetNamespace = Namespace.TN) @XmlElement(required = true) GetInfoRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/pay")
	void pay(@WebParam(name = "PayRequest", targetNamespace = Namespace.TN) @XmlElement(required = true) PayRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentInfo")
	GetPaymentInfoResponse getPaymentInfo(@WebParam(name = "GetPaymentInfoRequest", targetNamespace = Namespace.TN) @XmlElement(required = true) GetPaymentInfoRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentList")
	@RequestWrapper(className = "com.azry.mps.tbc.dealers.integration.dto.GetPaymentListRequest")
	GetPaymentListResponse getPaymentList(@WebParam(name = "personalNumber",  targetNamespace = Namespace.TN) @XmlElement(required = true) String personalNumber) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentListEntry")
	AddPaymentListEntryResponse addPaymentListEntry(@WebParam(name = "AddPaymentListEntryRequest", targetNamespace = Namespace.TN) @XmlElement(required = true) AddPaymentListEntryRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/removePaymentListEntry")
	void removePaymentListEntry(@WebParam(name = "removePaymentListEntryRequest",  targetNamespace = Namespace.TN) @XmlElement(required = true) RemovePaymentListEntryRequest request) throws RemoteException;

	@WebMethod(action = Namespace.TN + "/getServices")
	GetServicesResponse getServices() throws RemoteException;

	@WebMethod(action = Namespace.TN + "/getServiceGroups")
	GetServiceGroupsResponse getServiceGroups() throws RemoteException;

	@WebMethod(action = Namespace.TN + "/getServiceCommission")
	GetClientCommissionResponse getClientCommission(@WebParam(name = "getClientCommissionRequest",  targetNamespace = Namespace.TN) @XmlElement(required = true) GetClientCommissionRequest request) throws RemoteException;
}
