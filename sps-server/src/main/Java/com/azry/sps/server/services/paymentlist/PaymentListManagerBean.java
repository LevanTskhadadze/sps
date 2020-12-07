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
			return em.createQuery("SELECT p FROM PaymentList p LEFT JOIN FETCH p.entries e WHERE p.client.personalNumber = :personalNumber",
				PaymentList.class)
				.setParameter("personalNumber", personalNumber)
				.getSingleResult();
		}
		catch (NoResultException ex) {
			return null;
		}

	}

	@Override
	public PaymentListEntry addPaymentListEntry(Client client, PaymentListEntry paymentListEntry) {
		PaymentList paymentList = getPaymentList(client.getPersonalNumber());
		if (paymentList == null) {
			paymentList = new PaymentList();
			paymentList.setClient(client);
		}
		paymentListEntry.setPaymentList(paymentList);
		paymentList.getEntries().add(paymentListEntry);
		paymentList = em.merge(paymentList);
		return paymentList.getEntries().get(paymentList.getEntries().size() - 1);
	}

	@Override
	public void deletePaymentListEntry(long id) {
		PaymentListEntry entry = em.find(PaymentListEntry.class, id);
		if (entry != null) {
			em.remove(entry);
		}
	}

	@Override
	public Client getCLientFromAbonentCode(String abonentCode, long serviceId) {
		return em.createQuery("SELECT pl.client FROM PaymentListEntry ple JOIN FETCH PaymentList pl WHERE ple.serviceId = :serviceId", Client.class)
			.setParameter("serviceId", serviceId)
			.getSingleResult();
	}

	public void deletePaymentListEntriesByServiceId(long id) {
		em.createQuery("DELETE FROM PaymentListEntry p WHERE p.serviceId = :id")
			.setParameter("id", id)
			.executeUpdate();
	}

	@Override
	public void addPaymentList(PaymentList paymentList) {
		em.persist(paymentList);
	}
}
