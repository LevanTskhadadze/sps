package com.azry.sps.server.services.usergroup;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.users.Permissions;
import com.azry.sps.common.model.users.UserGroup;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class UserGroupManagerBean implements UserGroupManager {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserGroup> getUserGroups() {
		return em.createQuery("SELECT g FROM UserGroup g", UserGroup.class)
			.getResultList();
	}

	@Override
	public List<UserGroup> getFilteredUserGroups(String name, Permissions permission, Boolean isActive) {
		String sql = "FROM UserGroup g ";
		Map<String, String> params = new HashMap<>();


		if (isActive == null){
			sql += "WHERE 1 = 1 ";
		}

		else if (isActive) {
			sql += "WHERE g.active = TRUE ";
		}

		else {
			sql += "WHERE g.active = FALSE ";
		}

		if (name != null && !name.trim().isEmpty()){
			sql += "AND LOWER(g.name) like :name ";
			params.put("name", "%" + name.toLowerCase() + "%");
		}

		if (permission != null){
			sql += "AND LOWER(g.permissions) like :permission ";
			params.put("permission", "%" + permission.name().toLowerCase() + "%");
		}


		TypedQuery<UserGroup> query = em.createQuery(sql, UserGroup.class);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return query.getResultList();
	}

	@Override
	public UserGroup updateUserGroup(UserGroup userGroup) throws SPSException {
		try {
			return em.merge(userGroup);
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}

	@Override
	public void deleteUserGroup(Long id) throws SPSException {
		Long count = em.createQuery("SELECT COUNT(u) FROM SystemUser u JOIN u.groups g WHERE g.id = :id", Long.class)
			.setParameter("id", id)
		    .getSingleResult();
		if (count > 0) {
			throw new SPSException("userGroupHasUsersAssigned");
		}
		UserGroup userGroup = em.find(UserGroup.class, id);
		if (userGroup != null) {
			em.remove(userGroup);
		}
	}
}
