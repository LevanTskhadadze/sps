package com.azry.sps.console.server;

import com.azry.sps.common.ListResult;
import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.azry.sps.console.shared.usertab.UserTabService;
import com.azry.sps.server.services.user.SystemUserManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.Map;

@WebServlet("/sps/servlet/userTab")
public class UserTabServiceImpl extends RemoteServiceServlet implements UserTabService {

	@Inject
	SystemUserManager userTabManager;

	@Override
	public PagingLoadResult<SystemUserDTO> getUsers(int startingIndex, int numberToDisplay, Map<String, String> params) {
		ListResult<SystemUser> result = userTabManager.getUsers(startingIndex, numberToDisplay, params);
		List<SystemUserDTO> res = SystemUserDTO.toDTOs(result.getResultList(), true);
		return new PagingLoadResultBean<>(
			res,
			result.getResultCount(),
			startingIndex
			);
	}

	@Override
	public void changeActivation(long id, long version) throws SPSConsoleException {
		try {
			userTabManager.changeActivation(id, version);
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public SystemUserDTO editParameter(SystemUserDTO dto) throws SPSConsoleException {
		try {
			return SystemUserDTO.toDTO(userTabManager.editRow(SystemUserDTO.toEntity(dto)), true);
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public SystemUserDTO addParameter(SystemUserDTO dto) throws SPSConsoleException {
		try {
			return SystemUserDTO.toDTO(userTabManager.editRow(SystemUserDTO.toEntity(dto)), true);
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void removeParameter(long id) {
		userTabManager.remove(id);
	}
}
