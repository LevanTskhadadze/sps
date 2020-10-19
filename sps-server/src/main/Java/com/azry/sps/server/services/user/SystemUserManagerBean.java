package com.azry.sps.server.services.user;


import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.server.security.SPSPrincipal;
import com.azry.sps.server.utils.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
@Slf4j
public class SystemUserManagerBean implements SystemUserManager {

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ctx;

	@Override
	public SystemUser authenticate(String username, String inputPassword) {
		SystemUser selectedUser = findSystemUser(username);
		if (selectedUser != null && !passwordsMatch(inputPassword, selectedUser)) {
			selectedUser = null;
		}
		return selectedUser;
	}

	@Override
	public SystemUser loadAuthorisedUser() {
		return getAuthorisedUserFromSessionContext();
	}

	private SystemUser findSystemUser(String username) {
		try {
			return em.createQuery("select u from SystemUser u join fetch u.groups g where u.username = :username", SystemUser.class)
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException no) {
			return null;
		}
	}

	private boolean passwordsMatch(String inputPassword, SystemUser selectedUser) {
		return selectedUser.getPassword().equals(EncryptionUtils.encodeSHA1(inputPassword));
	}


	private SystemUser getAuthorisedUserFromSessionContext() {
		SPSPrincipal principal;
		if (ctx.getCallerPrincipal() instanceof SPSPrincipal) {
			principal = (SPSPrincipal) ctx.getCallerPrincipal();
			return principal.getSystemUser();
		}
		return null;
	}
}