package com.azry.sps.server.services.usergroup;

import com.azry.sps.common.model.users.Permissions;
import com.azry.sps.common.model.users.UserGroup;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
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
	public List<UserGroup> getUserGroups(String name, Permissions permission, Boolean isActive) {

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
	public UserGroup updateUserGroup(UserGroup userGroup) {
		return em.merge(userGroup);
	}

	@Override
	public void deleteUserGroup(Long id) {
		UserGroup userGroup = em.find(UserGroup.class, id);
		if (userGroup != null) {
			em.remove(userGroup);
		}
	}
}
