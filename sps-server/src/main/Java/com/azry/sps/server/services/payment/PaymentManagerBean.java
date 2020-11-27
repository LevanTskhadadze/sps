package com.azry.sps.server.services.payment;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.payment.Payment;
import com.azry.sps.common.model.payment.PaymentStatus;
import com.azry.sps.common.model.payment.PaymentStatusLog;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Stateless
public class PaymentManagerBean implements PaymentManager {

	@PersistenceContext
	EntityManager em;


	@Override
	public ListResult<Payment> getPayments(int offset, int limit, Map<String, Serializable> params, List<PaymentStatus> statuses) {
		StringBuilder sql = new StringBuilder();
		String queryPrefix = "SELECT p FROM Payment p WHERE 1 = 1 ";
		String countPrefix = "SELECT COUNT(p.id) FROM Payment p WHERE 1 = 1 ";
		if (params.get("id") != null && !params.get("id").equals("")) {
			sql.append("AND p.id = :id ");
		}

		if (params.get("agentPaymentId") != null && !params.get("agentPaymentId").equals("")) {
			sql.append("AND p.agentPaymentId = :agentPaymentId ");
		}

		if (params.get("startTime") != null && !params.get("startTime").equals("")) {
			sql.append("AND p.createTime >= TRY_CONVERT(datetime, :startTime) ");
		}

		if (params.get("endTime") != null && !params.get("endTime").equals("")) {
			sql.append("AND p.createTime <= TRY_CONVERT(datetime, :endTime) ");
		}

		if (params.get("serviceId") != null && !params.get("serviceId").equals("")) {
			sql.append("AND p.serviceId = :serviceId ");
		}

		if (params.get("channel") != null && !params.get("channelId").equals("")) {
			sql.append("AND p.channelId = :channelId ");
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


		for(Map.Entry<String, Serializable> entry : params.entrySet()) {
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
}
