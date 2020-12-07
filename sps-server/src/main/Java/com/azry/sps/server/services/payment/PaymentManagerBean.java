package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class PaymentManagerBean implements PaymentManager {

	@PersistenceContext
	EntityManager em;


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
		return em.createQuery("SELECT p FROM Payment p WHERE p.status = :collected or p.status = :pending", Payment.class)
			.setParameter("collected", PaymentStatus.COLLECTED)
			.setParameter("pending", PaymentStatus.PENDING)
			.getResultList();
	}

	@Override
	public List<PaymentStatusLog> getChanges(long id) {
		TypedQuery<PaymentStatusLog> query =
			em.createQuery("SELECT p FROM PaymentStatusLog p WHERE p.paymentId = :id ORDER BY p.statusTime ASC ", PaymentStatusLog.class)
				.setParameter("id", id);

		return query.getResultList();
	}

	@Override
	public void addPayments(List<Payment> payments) {
		for (Payment payment : payments) {
			em.persist(payment);
		}
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
}
