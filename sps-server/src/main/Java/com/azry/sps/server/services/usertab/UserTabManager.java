package com.azry.sps.server.services.usertab;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.systemparameters.model.systemparam.SystemParameter;
import org.hibernate.StatelessSession;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface UserTabManager {
	ListResult<SystemUser> getUsers(int startingIndex, int numberToShow, Map<String, String> params);

	void changeActivation(long id);

	void remove(long id);

	SystemUser editRow(SystemUser user);

}
