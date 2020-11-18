package com.azry.sps.server.services.transactionorder;

import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class TransactionOrderManagerBean implements TransactionOrderManager {

	@PersistenceContext
	EntityManager em;


	@Override
	public TransactionOrder getTransaction(long paymentId, TransactionType type) {
		TypedQuery<TransactionOrder> query =
			em.createQuery("SELECT t FROM TransactionOrder t WHERE t.paymentId = :paymentId AND t.type = :type", TransactionOrder.class)
				.setParameter("paymentId", paymentId)
				.setParameter("type", type);
		try {
			return query.getSingleResult();
		}
		catch (Exception ex) {
			return null;
		}
	}


}
