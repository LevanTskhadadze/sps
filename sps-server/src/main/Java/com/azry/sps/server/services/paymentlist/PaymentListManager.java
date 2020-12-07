package com.azry.sps.server.services.paymentlist;

import com.azry.sps.common.model.client.Client;
import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.common.model.paymentlist.PaymentListEntry;

import javax.ejb.Local;

@Local
public interface PaymentListManager {

	PaymentList getPaymentList(String personalNumber);

	PaymentListEntry addPaymentListEntry(Client client, PaymentListEntry paymentListEntry);

	Client getCLientFromAbonentCode(String abonentCode, long serviceId);

	void deletePaymentListEntry(long id);

	public void deletePaymentListEntriesByServiceId(long id);

    void addPaymentList(PaymentList paymentList);
}
