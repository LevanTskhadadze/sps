package services;

import com.azry.sps.systemparameters.model.systemparam.SystemParameter;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SystemParameterManager {
	List<SystemParameter> getSystemParameters();
}
