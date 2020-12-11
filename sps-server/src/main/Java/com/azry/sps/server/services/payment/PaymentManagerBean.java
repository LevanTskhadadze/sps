package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;
import com.azry.sps.server.services.transactionorder.TransactionOrderManager;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.sysparam.Parameter;
import com.azry.sps.systemparameters.sysparam.SysParam;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Slf4j
public class PaymentManagerBean implements PaymentManager {

	@PersistenceContext
	EntityManager em;

	@Inject
	TransactionOrderManager transactionOrderManager;

	@Inject
	@SysParam(type = SystemParameterType.STRING, code = "clientAccountIBAN")
	private Parameter<String> clientAccountIBAN;

	@Override
	public ListResult<Payment> getPayments(int offset, int limit, PaymentParams params, List<PaymentStatus> statuses) {
		StringBuilder sql = new StringBuilder();
		String queryPrefix = "SELECT p FROM Payment p WHERE 1 = 1 ";
		String countPrefix = "SELECT COUNT(p.id) FROM Payment p WHERE 1 = 1 ";
		Map<String, Object> parameters = new HashMap<>();

		if (params.getId() != null) {
			sql.append("AND p.id = :id ");
			parameters.put("id", params.getId());
		}

		if (params.getAgentPaymentId() != null && !params.getAgentPaymentId().equals("")) {
			sql.append("AND p.agentPaymentId = :agentPaymentId ");
			parameters.put("agentPaymentId", params.getAgentPaymentId());
		}

		if (params.getCreationStartTime() != null) {
			sql.append("AND p.createTime >= TRY_CONVERT(datetime, :startTime) ");
			parameters.put("startTime", params.getCreationStartTime());
		}

		if (params.getCreationEndTime() != null) {
			sql.append("AND p.createTime <= TRY_CONVERT(datetime, :endTime) ");
			parameters.put("endTime", params.getCreationEndTime());
		}

		if (params.getServiceId() != null) {
			sql.append("AND p.serviceId = :serviceId ");
			parameters.put("serviceId", params.getServiceId());
		}

		if (params.getChannelId() != null) {
			sql.append("AND p.channelId = :channelId ");
			parameters.put("channelId", params.getChannelId());
		}
		if (statuses != null && statuses.size() > 0) {
			sql.append("AND p.status IN (");
			String comma = "";
			for(int i = 0; i < statuses.size(); i ++) {
				String name = "status_" + i;
				sql.append(comma).append(" :").append(name);
				comma = ",";
			}
			sql.append(") ");
		}
		TypedQuery<Long> count = em.createQuery(countPrefix + sql.toString(), Long.class);
		sql.append("ORDER BY p.createTime DESC");
		TypedQuery<Payment> query = em.createQuery(queryPrefix + sql.toString(), Payment.class);


		for(Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
			count.setParameter(entry.getKey(), entry.getValue());
		}

		if (statuses != null) {
			for(int i = 0; i < statuses.size(); i ++) {
				String name = "status_" + i;
				query.setParameter(name, statuses.get(i));
				count.setParameter(name, statuses.get(i));
			}
		}

		query.setFirstResult(offset);
		query.setMaxResults(limit);

		return new ListResult<>(query.getResultList(), count.getSingleResult().intValue());
	}

	@Override
	public Payment getPayment(long id) {
		return em.find(Payment.class, id);
	}

	@Override
	public Payment getPaymentByAgentId(String agentPaymentId) {
		return em.createQuery("SELECT p FROM Payment p WHERE p.agentPaymentId = :paymentId", Payment.class)
			.setParameter("paymentId", agentPaymentId)
			.getSingleResult();
	}

	@Override
	public List<Payment> getCollectedAndPendingPayments() {
		return em.createQuery("SELECT p FROM Payment p WHERE p.status = :collected or p.status = :pending",
			Payment.class)
			.setParameter("collected", PaymentStatus.COLLECTED)
			.setParameter("pending", PaymentStatus.PENDING)
			.getResultList();
	}

	@Override
	public List<PaymentStatusLog> getChanges(long id) {
		TypedQuery<PaymentStatusLog> query =
			em.createQuery("SELECT p FROM PaymentStatusLog p WHERE p.paymentId = :id ORDER BY p.statusTime ASC ",
				PaymentStatusLog.class)
				.setParameter("id", id);

		return query.getResultList();
	}

	@Override
	public List<Payment> addPayments(List<Payment> payments, String sourceAccountIBAN) {
		List<Payment> paymentList = new ArrayList<>();
		for (Payment payment : payments) {
			try {
				paymentList.add(addPayment(payment, sourceAccountIBAN));
			} catch (Exception ex) {
				Payment paymentEntity = setPayment(payment, PaymentStatus.COLLECT_REJECTED,
					"Unexpected exception during payment creation.");
				paymentEntity = em.merge(paymentEntity);
				paymentList.add(paymentEntity);
				log.error("Unexpected exception during payment creation. Payment id: " + paymentEntity.getId(), ex);
			}
		}
		return paymentList;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Payment addPayment(Payment payment, String sourceAccountIBAN) {
		Payment paymentEntity;
		if (!payment.getClCommission().equals(BigDecimal.ZERO) && clientAccountIBAN.getValue() == null) {
			paymentEntity = setPayment(payment, PaymentStatus.COLLECT_REJECTED,
				"Client account is not configured for client Commission. " +
					"Please set client account in system parameters tab with key: \"clientAccountIBAN\".");
		} else {
			paymentEntity = setPayment(payment, PaymentStatus.CREATED, null);
			transactionOrderManager.addTransaction(sourceAccountIBAN, clientAccountIBAN.getValue(), paymentEntity);
		}
		return paymentEntity;
	}

	private Payment setPayment(Payment payment, PaymentStatus paymentStatus, String statusMessage) {
		PaymentStatusLog paymentStatusLog = new PaymentStatusLog();
		payment.setStatus(paymentStatus);
		paymentStatusLog.setStatus(paymentStatus);
		if (statusMessage != null) {
			payment.setStatusMessage(statusMessage);
			paymentStatusLog.setStatusMessage(statusMessage);
		}
		Payment paymentEntity = em.merge(payment);
		paymentStatusLog.setPaymentId(paymentEntity.getId());
		em.persist(paymentStatusLog);
		return paymentEntity;
	}

	@Override
	public Payment addPayment(Payment payment) {
		em.persist(payment);

		PaymentStatusLog statusLog = new PaymentStatusLog();
		statusLog.setStatusTime(new Date());
		statusLog.setStatus(payment.getStatus());
		statusLog.setPaymentId(payment.getId());
		em.persist(statusLog);

		return payment;
	}

	@Override
	public void updatePayment(Payment payment) {
		em.merge(payment);
	}

	@Override
	public void addPaymentStatusLog(PaymentStatusLog paymentStatusLog) {
		em.persist(paymentStatusLog);
	}

	@Override
	public void changePaymentStatus(long paymentId, PaymentStatus status) {
		Payment payment = em.find(Payment.class, paymentId);
		payment.setStatus(status);

		em.merge(payment);
	}
}
