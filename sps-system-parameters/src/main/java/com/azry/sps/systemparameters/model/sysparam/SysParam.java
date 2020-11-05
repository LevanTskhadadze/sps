package com.azry.sps.systemparameters.model.sysparam;

import com.azry.sps.systemparameters.model.SystemParameterType;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface SysParam {

	/**
	 * რა ტიპის სისტემური პარამეტრია
	 */
	SystemParameterType type();

	/**
	 * სისტემური პარამეტრის კოდი
	 */
	@Nonbinding String code() default "";

	/**
	 * სისტემური პარამეტრის დეფაულტ მნიშვნელობა
	 */
	@Nonbinding String defaultValue() default "";

}