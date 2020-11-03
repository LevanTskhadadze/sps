package com.azry.sps.server.services.systemparam;

import com.azry.sps.common.exceptions.SPSException;
import com.azry.sps.systemparameters.model.systemparam.SystemParameter;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface SystemParameterManager {
	List<SystemParameter> getSystemParameters(Map<String, String> params);

	void remove(long id);

	void editRow(long id, String code, String type, String value, String description) throws SPSException;

	SystemParameter getRow(long id);

	void addEntry(SystemParameter entity);
}
