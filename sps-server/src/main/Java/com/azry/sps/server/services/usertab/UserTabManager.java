package com.azry.sps.server.services.usertab;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.users.SystemUser;

import javax.ejb.Local;
import java.util.Map;

@Local
public interface UserTabManager {
	ListResult<SystemUser> getUsers(int startingIndex, int numberToShow, Map<String, String> params);

	void changeActivation(long id);

	void remove(long id);

	SystemUser editRow(SystemUser user) throws SPSException;

}
