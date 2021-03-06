package com.azry.sps.server.services.user;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.server.security.SPSPrincipal;
import com.azry.sps.server.utils.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		if (selectedUser == null) {
			return null;
		}

		if (!passwordsMatch(inputPassword, selectedUser) || !selectedUser.isActive()) {
			selectedUser = null;
		}


		for (UserGroup group : selectedUser.getGroups()) {
			if (group.getPermissions().isEmpty()) {
				continue;
			}
			return selectedUser;
		}
		return null;
	}

	@Override
	public SystemUser loadAuthorisedUser() {
		return getAuthorisedUserFromSessionContext();
	}


	@Override
	public ListResult<SystemUser> getUsers(int startingIndex, int numberToShow, Map<String, String> params) {
		if (params == null) params = new HashMap<>();
		boolean join = false;
		if (params.containsKey("groups") && params.get("groups") != null && !params.get("groups").equals("")) {
			join = true;
		}
		String queryPrefix = "SELECT su FROM SystemUser su LEFT JOIN FETCH su.groups UG ";
		String countPrefix = "SELECT COUNT(su.id) FROM SystemUser su " + (join ? "INNER JOIN su.groups UG " : "");
		StringBuilder str = new StringBuilder("WHERE 1 = 1");
		Map<String, Object> values = new HashMap<>();

		if (params.containsKey("username") && params.get("username") != null && !params.get("username").equals("")) {
			values.put("username", params.get("username"));
			str.append(" AND su.username LIKE :username ");
		}

		if (params.containsKey("name") && params.get("name") != null && !params.get("name").equals("")) {
			values.put("name", params.get("name"));
			str.append(" AND su.name LIKE :name ");
		}

		if (params.containsKey("email") && params.get("email") != null && !params.get("email").equals("")) {
			values.put("email", params.get("email"));
			str.append(" AND su.email LIKE :email ");
		}
		if (params.containsKey("groups") && params.get("groups") != null && !params.get("groups").equals("")) {
			values.put("groups", params.get("groups"));
			str.append(" AND UG.name LIKE :groups ");
		}

		if (params.containsKey("active") && params.get("active") != null && !params.get("active").equals("")) {
			values.put("active", params.get("active"));
			str.append(" AND su.active = :active ");
		}


		String finalQuery = queryPrefix + str.toString() + " ORDER BY su.lastUpdateTime DESC";
		TypedQuery<SystemUser> query = em.createQuery(finalQuery, SystemUser.class);
		Query count = em.createQuery(countPrefix + str.toString());
		query.setFirstResult(startingIndex);
		query.setMaxResults(numberToShow);

		for (Map.Entry<String, Object> entry : values.entrySet()) {
			if (entry.getKey().equals("groups")) {
				query.setParameter(entry.getKey(), entry.getValue());
				count.setParameter(entry.getKey(), entry.getValue());
				continue;
			}
			if (entry.getKey().equals("active")) {
				query.setParameter(entry.getKey(), entry.getValue().equals("1"));
				count.setParameter(entry.getKey(), entry.getValue().equals("1"));
				continue;
			}
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
			count.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}


		List<SystemUser> res = query.getResultList();

		return new ListResult<>(res, (int)((long)count.getSingleResult()));
	}

	@Override
	public void changeActivation(long id, long version) throws SPSException {

		SystemUser user = em.find(SystemUser.class, id);
		if (user.getVersion() != version) throw new SPSException("optimisticLockException");
		user.setActive(!user.isActive());
		em.persist(user);

	}

	@Override
	public void remove(long id) {
		SystemUser user = em.find(SystemUser.class, id);
		if (user != null) {
			em.remove(user);
		}
	}



	@Override
	public SystemUser addUser(SystemUser user) throws SPSException {
		try {

			Long count = em.createQuery("SELECT count (u.id) FROM SystemUser u where u.username = :name", Long.class)
				.setParameter("name", user.getUsername())
				.getSingleResult();

			if (count > 0) {
				throw new SPSException("usernameAlreadyExists");
			}

			SystemUser newEntity = em.merge(user);
			newEntity.setVersion(newEntity.getVersion() + 1);
			return newEntity;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
	}


	@Override
	public SystemUser updateUser(SystemUser user) throws SPSException {
		try {
			SystemUser presentUser = em.find(SystemUser.class, user.getId());
			if (user.getPassword() == null || user.getPassword().equals("")) {
				user.setPassword(presentUser.getPassword());
			}

			SystemUser newEntity = em.merge(user);
			newEntity.setVersion(newEntity.getVersion() + 1);
			return newEntity;
		}
		catch (OptimisticLockException ex) {
			throw new SPSException("optimisticLockException", ex);
		}
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