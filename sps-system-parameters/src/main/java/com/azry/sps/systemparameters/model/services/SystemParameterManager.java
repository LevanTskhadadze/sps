package com.azry.sps.systemparameters.model.services;

import com.azry.sps.systemparameters.model.SystemParameter;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface SystemParameterManager {
	List<SystemParameter> getSystemParameters(Map<String, String> params);

	void remove(long id);

	void editRow(SystemParameter systemParameter);

	SystemParameter getRow(long id);

	long addEntry(SystemParameter entity);
}
