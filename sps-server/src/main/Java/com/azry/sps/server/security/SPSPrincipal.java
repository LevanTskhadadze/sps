package com.azry.sps.server.security;



import com.azry.sps.common.model.users.SystemUser;

import java.security.Principal;

public class SPSPrincipal implements Principal {

	private String name;

	private SystemUser systemUser;

	public SPSPrincipal() {}

	public SPSPrincipal(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Principal) {
			Principal other = (Principal)obj;
			return name == null ? other.getName() == null : name.equals(other.getName());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return name == null ? 0 : name.hashCode();
	}
}