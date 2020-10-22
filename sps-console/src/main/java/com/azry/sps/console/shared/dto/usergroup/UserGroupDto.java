package com.azry.sps.console.shared.dto.usergroup;

import com.azry.sps.common.model.users.UserGroup;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;


import java.util.ArrayList;
import java.util.List;

public class UserGroupDto implements IsSerializable {

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

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserGroupDto)) return false;
		return getId() == ((UserGroupDto) obj).getId();
	}

	@GwtIncompatible
	public static UserGroupDto toDto(UserGroup entity){
		UserGroupDto dto = new UserGroupDto();
		dto.setActive(entity.isActive());
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setPermissions(entity.getPermissions());

		return dto;
	}

	@GwtIncompatible
	public static UserGroup toEntity(UserGroupDto userGroupDto){
		UserGroup entity = new UserGroup();
		entity.setActive(userGroupDto.isActive());
		entity.setId(userGroupDto.getId());
		entity.setName(userGroupDto.getName());
		entity.setPermissions(userGroupDto.getPermissions());
		return entity;
	}

	@GwtIncompatible
	public static List<UserGroupDto> toDtos(List<UserGroup> entities){
		List<UserGroupDto> dtos = new ArrayList<>();

		for(UserGroup entity : entities){
			dtos.add(toDto(entity));
		}

		return dtos;
	}

	@GwtIncompatible
	public static List<UserGroup> toEntities(List<UserGroupDto> dtos){
		List<UserGroup> entities = new ArrayList<>();

		for(UserGroupDto dto : dtos){
			entities.add(toEntity(dto));
		}

		return entities;
	}
}
