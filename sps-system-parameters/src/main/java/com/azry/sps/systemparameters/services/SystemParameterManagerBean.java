package com.azry.sps.systemparameters.services;

import com.azry.sps.systemparameters.model.SystemParameter;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless
public class SystemParameterManagerBean implements SystemParameterManager {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<SystemParameter> getSystemParameters(Map<String, String> params) {
		if(params == null) params = new HashMap<>();

		StringBuilder str = new StringBuilder("SELECT sp FROM SystemParameter sp WHERE 1 = 1 ");
		Map<String, Object> values = new HashMap<>();

		if (params.containsKey("code") && params.get("code") != null && !params.get("code").equals("")) {
			values.put("code", params.get("code"));
			str.append(" AND code LIKE :code ");
		}
		if (params.containsKey("value") && params.get("value") != null && !params.get("value").equals("")) {
			values.put("value", params.get("value"));
			str.append(" AND value LIKE :value ");
		}



		TypedQuery<SystemParameter> query = em.createQuery(str.toString(), SystemParameter.class);

		for (Map.Entry<String, Object> entry : values.entrySet()) {
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}


		return query.getResultList();
	}

	@Override
	public void remove(long id) {
		em.createQuery("DELETE FROM SystemParameter sp WHERE sp.id = :id").setParameter("id", id).executeUpdate();
	}


	@Override
	public void editRow(SystemParameter systemParameter) {
		em.merge(systemParameter);
	}


	@Override
	public SystemParameter getRow(long id) {
		return em.find(SystemParameter.class, id);
	}

	@Override
	public long addEntry(SystemParameter entity) {
		em.persist(entity);

		return entity.getId();
	}

}