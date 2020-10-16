package services;

import com.azry.sps.systemparameters.model.systemparam.SystemParameter;
import com.azry.sps.systemparameters.model.systemparam.SystemParameterType;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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



		Query query = em.createQuery(str.toString(), SystemParameter.class);

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
	public void editRow(long id, String code, String type, String value, String description) {
		em.createQuery("UPDATE SystemParameter sp SET sp.type = :type, sp.code = :code, sp.value = :value, sp.description = :desc WHERE sp.id = :id")
			.setParameter("id", id)
			.setParameter("type", toEnum(type))
			.setParameter("code", code)
			.setParameter("value", value)
			.setParameter("desc", description)
			.executeUpdate();
	}


	@Override
	public SystemParameter getRow(long id) {
		return em.createQuery("SELECT sp FROM SystemParameter sp WHERE sp.id = :id", SystemParameter.class)
			.setParameter("id", id)
			.getResultList().get(0);
	}

	@Override
	public void addEntry(SystemParameter entity) {
		em.persist(entity);
	}

	private SystemParameterType toEnum(String str) {
		switch (str) {
			case "bool":
				return SystemParameterType.BOOLEAN;
			case "integer":
				return SystemParameterType.INTEGER;
			case "string":
				return SystemParameterType.STRING;
		}
		return null;
	}
}