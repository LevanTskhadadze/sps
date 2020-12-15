package com.azry.sps.server.services.user;


import com.azry.sps.common.ListResult;
import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.users.SystemUser;

import javax.ejb.Local;
import java.util.Map;

@Local
public interface SystemUserManager {

	SystemUser authenticate(String username, String password);

	SystemUser loadAuthorisedUser();

	ListResult<SystemUser> getUsers(int startingIndex, int numberToShow, Map<String, String> params);

	void changeActivation(long id, long version) throws SPSException;

	void remove(long id);

	SystemUser updateUser(SystemUser user) throws SPSException;

	SystemUser addUser(SystemUser user) throws SPSException;

}