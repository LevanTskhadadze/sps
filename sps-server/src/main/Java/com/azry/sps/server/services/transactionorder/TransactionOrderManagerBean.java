package com.azry.sps.server.services.transactionorder;

import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.service.Service;
import com.azry.sps.common.model.transaction.TransactionOrder;
import com.azry.sps.common.model.transaction.TransactionType;
import com.azry.sps.server.services.service.ServiceManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

@Stateless
public class TransactionOrderManagerBean implements TransactionOrderManager {

	@PersistenceContext
	EntityManager em;

	@Inject
	ServiceManager serviceManager;

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

	@Override
	public void changeTransactions(List<TransactionOrder> transactions) {
		for (TransactionOrder transaction : transactions) {
			if (transaction != null) {
				em.merge(transaction);
			}
		}
	}

	@Override
	public void addTransaction(TransactionOrder transaction) {
		em.persist(transaction);
	}

	@Override
	public void addTransactions(String sourceAccountBan, List<Payment> payments) {
		for (Payment payment : payments) {
			Service service = serviceManager.getService(payment.getServiceId());
			TransactionOrder principalTransactionOrder = new TransactionOrder();
			principalTransactionOrder.setSourceAccountBAN(sourceAccountBan);
			principalTransactionOrder.setPaymentId(payment.getId());
			principalTransactionOrder.setAmount(payment.getAmount());
			principalTransactionOrder.setDestinationAccountBAN(service.getProviderAccountIBAN());
			principalTransactionOrder.setPurpose("Service Payment");
			principalTransactionOrder.setType(TransactionType.PRINCIPAL_AMOUNT);

			em.persist(principalTransactionOrder);

			if (payment.getClCommission().compareTo(BigDecimal.ZERO) > 0) {
				TransactionOrder clientCommissionTransactionOrder = new TransactionOrder();
				clientCommissionTransactionOrder.setSourceAccountBAN(sourceAccountBan);
				clientCommissionTransactionOrder.setPaymentId(payment.getId());
				clientCommissionTransactionOrder.setAmount(payment.getClCommission());
				clientCommissionTransactionOrder.setDestinationAccountBAN(service.getProviderAccountIBAN());
				clientCommissionTransactionOrder.setPurpose("Commission Payment");
				clientCommissionTransactionOrder.setType(TransactionType.CLIENT_COMMISSION_AMOUNT);
				em.persist(clientCommissionTransactionOrder);
			}
		}
	}
}
