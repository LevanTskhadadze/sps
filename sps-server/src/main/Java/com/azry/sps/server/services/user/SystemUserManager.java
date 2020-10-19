package com.azry.sps.server.services.user;


import com.azry.sps.common.model.users.SystemUser;

import javax.ejb.Local;

@Local
public interface SystemUserManager {

	SystemUser authenticate(String username, String password);

	SystemUser loadAuthorisedUser();
}