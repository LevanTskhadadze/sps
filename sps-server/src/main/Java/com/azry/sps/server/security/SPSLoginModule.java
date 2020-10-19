package com.azry.sps.server.security;

import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.server.services.user.SystemUserManager;
import com.azry.sps.server.services.user.SystemUserManagerBean;
import com.azry.sps.server.utils.EJBUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

import javax.security.auth.login.LoginException;
import java.security.acl.Group;

@Slf4j
public class SPSLoginModule extends UsernamePasswordLoginModule {

	private SystemUser user = null;

	@Override
	protected String getUsersPassword() throws LoginException {
		return null;
	}

	@Override
	protected boolean validatePassword(String inputPassword, String expectedPassword) {
		user = getUserManager().authenticate(getUsername(), inputPassword);
		if (user != null) {
			log.info("Authenticate username: " + user.getUsername());
			return true;
		} else {
			log.info("Couldn't authenticate user " + getUsername());
			return false;
		}
	}

	@Override
	protected Group[] getRoleSets() throws LoginException {
		Group group = new SimpleGroup("Roles");
		Group callerPrincipal = new SimpleGroup("CallerPrincipal");
		if (user != null) {
			try {
				if(!user.getGroups().isEmpty()) {
					for (UserGroup userGroup : user.getGroups()) {
						for (String permission : userGroup.getPermissions().split(",")) {
							group.addMember(super.createIdentity(permission));
						}
					}
					group.addMember(super.createIdentity("ANY_ROLE"));
					SPSPrincipal principal = (SPSPrincipal) getIdentity();
					principal.setName(getUsername());
					principal.setSystemUser(user);
					callerPrincipal.addMember(principal);
				}
			} catch (Exception ex) {
				log.error("Failed to create principal", ex);
				throw new LoginException("Failed to create principal");
			}
		}
		return new Group[]{group, callerPrincipal};
	}

	private SystemUserManager getUserManager() {
		return EJBUtils.getBean(SystemUserManagerBean.class);
	}
}