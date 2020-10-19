package com.azry.sps.console.server;

import com.azry.sps.console.server.converter.SystemParameterDtoConverter;
import com.azry.sps.console.shared.systemparameters.SystemParameterDto;
import com.azry.sps.console.shared.systemparameters.TabService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import services.SystemParameterManager;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet("/sps/servlet/getTab")
public class TabServiceImpl extends RemoteServiceServlet implements TabService {

	@Inject
	SystemParameterManager systemParameterManager;


	@Override
	public List<SystemParameterDto> getSystemParameterTab() {
		return SystemParameterDtoConverter.toDTOs(systemParameterManager.getSystemParameters());
	}
}
