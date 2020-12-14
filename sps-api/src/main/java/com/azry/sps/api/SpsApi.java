package com.azry.sps.api;

import com.azry.sps.api.model.Namespace;
import com.azry.sps.api.model.SpsApiException;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryRequest;
import com.azry.sps.api.model.addpaymentlistentry.AddPaymentListEntryResponse;
import com.azry.sps.api.model.getServices.GetServicesRequest;
import com.azry.sps.api.model.getServices.GetServicesResponse;
import com.azry.sps.api.model.calculateclientcommission.CalculateClientCommissionRequest;
import com.azry.sps.api.model.calculateclientcommission.CalculateClientCommissionResponse;
import com.azry.sps.api.model.getinfo.GetInfoRequest;
import com.azry.sps.api.model.getinfo.GetInfoResponse;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoRequest;
import com.azry.sps.api.model.getpaymentinfo.GetPaymentInfoResponse;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListRequest;
import com.azry.sps.api.model.getpaymentlist.GetPaymentListResponse;
import com.azry.sps.api.model.getservicegroups.GetServiceGroupsResponse;
import com.azry.sps.api.model.pay.PayRequest;
import com.azry.sps.api.model.removepaymentlistentry.RemovePaymentListEntryRequest;

import javax.ejb.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.rmi.RemoteException;

@Remote
@WebService(targetNamespace = Namespace.TN)
public interface SpsApi extends java.rmi.Remote {

	@WebMethod(action = Namespace.TN + "/getInfo")
	GetInfoResponse getInfo(@WebParam(name = "getInfoRequest", targetNamespace = Namespace.TN)@XmlElement(required = true) GetInfoRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/pay")
	void pay(@WebParam(name = "payRequest", targetNamespace = Namespace.TN)@XmlElement(required = true) PayRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentInfo")
	GetPaymentInfoResponse getPaymentInfo(@WebParam(name = "getPaymentInfoRequest", targetNamespace = Namespace.TN)@XmlElement(required = true) GetPaymentInfoRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentListEntry")
	GetPaymentListResponse getPaymentList(@WebParam(name = "getPaymentListRequest",  targetNamespace = Namespace.TN)@XmlElement(required = true) GetPaymentListRequest getPaymentListRequest) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/getPaymentListEntry")
	AddPaymentListEntryResponse addPaymentListEntry(@WebParam(name = "addPaymentListEntryRequest", targetNamespace = Namespace.TN)@XmlElement(required = true) AddPaymentListEntryRequest request) throws SpsApiException, RemoteException;

	@WebMethod(action = Namespace.TN + "/removePaymentListEntry")
	void removePaymentListEntry(@WebParam(name = "removePaymentListEntryRequest",  targetNamespace = Namespace.TN)@XmlElement(required = true) RemovePaymentListEntryRequest request) throws RemoteException, SpsApiException;

	@WebMethod(action = Namespace.TN + "/getServices")
	GetServicesResponse getServices(@WebParam(name = "getServicesRequest", targetNamespace = Namespace.TN)@XmlElement(required = true) GetServicesRequest request) throws RemoteException, SpsApiException;

	@WebMethod(action = Namespace.TN + "/getServiceGroups")
	GetServiceGroupsResponse getServiceGroups() throws RemoteException;

	@WebMethod(action = Namespace.TN + "/getServiceCommission")
	CalculateClientCommissionResponse calculateClientCommission(@WebParam(name = "calculateClientCommissionRequest",  targetNamespace = Namespace.TN) @XmlElement(required = true) CalculateClientCommissionRequest request) throws RemoteException, SpsApiException;
}
