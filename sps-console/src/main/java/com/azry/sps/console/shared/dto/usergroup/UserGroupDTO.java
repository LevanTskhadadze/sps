package com.azry.sps.console.shared.dto.usergroup;

import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.console.client.utils.StringUtil;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserGroupDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private String permissions;

	private boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	@GwtIncompatible
	public static UserGroupDTO toDTO(UserGroup userGroup) {
		if (userGroup != null) {
			UserGroupDTO dto = new UserGroupDTO();
			dto.setId(userGroup.getId());
			dto.setName(userGroup.getName());
			dto.setPermissions(userGroup.getPermissions());
			dto.setActive(userGroup.isActive());
			dto.setCreateTime(userGroup.getCreateTime());
			dto.setLastUpdateTime(userGroup.getLastUpdateTime());
			dto.setVersion(userGroup.getVersion());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<UserGroupDTO> toDTOs(List<UserGroup> userGroups) {
		if (userGroups != null) {
			List<UserGroupDTO> dtos = new ArrayList<>();

			for (UserGroup serviceGroup : userGroups) {
				dtos.add(toDTO(serviceGroup));

			}
			return dtos;
		}
		return null;
	}

	@GwtIncompatible
	public UserGroup fromDTO() {
		UserGroup userGroup = new UserGroup();
		userGroup.setId(this.getId());
		userGroup.setName(this.getName());
		userGroup.setPermissions(this.getPermissions());
		userGroup.setActive(this.isActive());
		userGroup.setCreateTime(this.getCreateTime());
		userGroup.setLastUpdateTime(this.getLastUpdateTime());
		userGroup.setVersion(this.getVersion());

		return userGroup;
	}

	@GwtIncompatible
	public static List<UserGroup> fromDTOs(List<UserGroupDTO> dtos) {
		List<UserGroup> userGroups = new ArrayList<>();
		for (UserGroupDTO dto : dtos) {
			userGroups.add(dto.fromDTO());
		}
		return userGroups;
	}

	@Override
	public String toString() {
		if (!permissions.isEmpty()) {
			List<String> permList = Arrays.asList(permissions.split(","));
			return StringUtil.joinMes(permList, ", ");
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserGroupDTO)) return false;
		return getId() == ((UserGroupDTO) obj).getId();
	}
}
