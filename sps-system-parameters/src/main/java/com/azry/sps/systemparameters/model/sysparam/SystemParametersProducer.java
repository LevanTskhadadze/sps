package com.azry.sps.systemparameters.model.sysparam;

import com.azry.sps.systemparameters.model.SystemParameter;
import com.azry.sps.systemparameters.model.SystemParameterType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Singleton(name = "sysParamProducer")
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class SystemParametersProducer {

	private static Logger log = LoggerFactory.getLogger(SystemParametersProducer.class);

	@PersistenceContext
	private EntityManager em;

	@Resource
	private TimerService timerService;

	private static ConcurrentHashMap<String, Parameter<?>> values = new ConcurrentHashMap<>();

	private static ConcurrentHashMap<String, Object> defaultValues = new ConcurrentHashMap<>();

	@Produces
	@SysParam(type = SystemParameterType.BOOLEAN)
	public synchronized Parameter<Boolean> getParamBoolean(InjectionPoint ip) {
		return getParamInternal(ip);
	}

	@Produces
	@SysParam(type = SystemParameterType.INTEGER)
	public synchronized Parameter<Integer> getParamInteger(InjectionPoint ip) {
		return getParamInternal(ip);
	}

	@Produces
	@SysParam(type = SystemParameterType.STRING)
	public synchronized Parameter<String> getParamString(InjectionPoint ip) {
		return getParamInternal(ip);
	}


	@SuppressWarnings("unchecked")
	private synchronized <T> Parameter<T> getParamInternal(InjectionPoint ip) {
		Parameter<T> param = null;
		SysParam sysParam = ip.getAnnotated().getAnnotation(SysParam.class);
		if (sysParam != null) {
			synchronized (SystemParametersProducer.class) {
				if (StringUtils.isNotEmpty(sysParam.code())) {
					param = (Parameter<T>) values.get(sysParam.code());
					if (param == null) {
						param = new Parameter<>(sysParam.code(), (T) getParameterValue(sysParam.type(), sysParam.defaultValue()));
						values.put(sysParam.code(), param);
					}
					if (StringUtils.isNotEmpty(sysParam.defaultValue())) {
						defaultValues.put(sysParam.code(), getParameterValue(sysParam.type(), sysParam.defaultValue()));
					}
				}
			}
		}
		return param;
	}

	@SuppressWarnings({"unchecked"})
	public synchronized <T> T getParam(String key, T defaultValue) {
		Parameter<?> param = null;
		if (key != null && !key.trim().isEmpty()) {
			param = values.get(key);
			if (param == null && defaultValue != null) {
				param = new Parameter<>(key, defaultValue);
				values.put(key, param);
			}
		}
		return param == null ? null : (T) param.getValue();
	}

	@PostConstruct
	public void startup() {
		reload();
		timerService.createIntervalTimer(5000, 60 * 1000, new TimerConfig(null, false));
	}

	@Timeout
	 @SuppressWarnings({"rawtypes", "unchecked"})
	public void reload() {
		synchronized (SystemParametersProducer.class) {
			log.debug("Loading system parameters");
			for (SystemParameter p : (List<SystemParameter>) em.createQuery("SELECT s FROM SystemParameter s").getResultList()) {
				Object value = getParameterValue(p.getType(), p.getValue());

				if (values.get(p.getCode()) == null) {
					values.put(p.getCode(), new Parameter(p.getCode(), value));
				} else {
					Parameter param = values.get(p.getCode());
					param.setValue(value);
				}
			}
		}
	}

	private Object getParameterValue(SystemParameterType type, String strValue) {
		if (StringUtils.isNotEmpty(strValue)) {
			switch (type) {
				case STRING:
					return strValue;
				case INTEGER:
					return Integer.parseInt(strValue);
				case BOOLEAN:
					return Boolean.parseBoolean(strValue);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public void remove(String key) {
		synchronized (SystemParametersProducer.class) {
			Parameter param = values.get(key);
			Object defaultParam = defaultValues.get(key);
			param.setValue(defaultParam);
		}
	}
}