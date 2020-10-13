package services;

import com.azry.sps.systemparameters.model.systemparam.SystemParameter;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Stateless
public class SystemParameterManagerBean implements SystemParameterManager {
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<SystemParameter> getSystemParameters() {
		List<SystemParameter> result;

		result = em.createQuery("SELECT sp FROM SystemParameter sp", SystemParameter.class).getResultList();

		return result;
	}


}