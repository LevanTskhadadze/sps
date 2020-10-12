package com.azry.sps.server.services.user;


import com.azry.sps.common.model.users.SystemUser;

import javax.ejb.Local;
import java.util.Set;

@Local
public interface SystemUserManager {

	SystemUser authenticate(String username, String password);

	SystemUser loadAuthorisedUser();

	Set<String> getPermissions(String username);
}