package com.azry.sps.server.services.paymentlist;

import com.azry.sps.common.model.client.Client;
import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.common.model.paymentlist.PaymentListEntry;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class PaymentListManagerBean implements PaymentListManager {

	@PersistenceContext
	EntityManager em;

	@Override
	public PaymentList getPaymentList(String personalNumber) {
		try {
			return em.createQuery("SELECT p FROM PaymentList p JOIN FETCH p.entries e WHERE p.client.personalNumber = :personalNumber",
				PaymentList.class)
				.setParameter("personalNumber", personalNumber)
				.getSingleResult();
		}
		catch (NoResultException ex) {
			return null;
		}

	}

	@Override
	public PaymentList addPaymentListEntry(Client client, PaymentListEntry paymentListEntry) {
		PaymentList paymentList = getPaymentList(client.getPersonalNumber());
		if (paymentList == null) {
			paymentList = new PaymentList();
			paymentList.setClient(client);
		}
		paymentListEntry.setPaymentList(paymentList);
		paymentList.getEntries().add(paymentListEntry);
		return em.merge(paymentList);
	}
}
