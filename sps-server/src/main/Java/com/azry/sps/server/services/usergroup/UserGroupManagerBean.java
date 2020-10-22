package com.azry.sps.server.services.usergroup;

import com.azry.sps.common.model.users.UserGroup;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class UserGroupManagerBean implements UserGroupManager {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<UserGroup> getAllUserGroups() {
		return em.createQuery("SELECT ug FROM UserGroup ug", UserGroup.class).getResultList();
	}
}
