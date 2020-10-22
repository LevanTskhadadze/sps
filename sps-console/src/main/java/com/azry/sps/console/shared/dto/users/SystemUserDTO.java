package com.azry.sps.console.shared.dto.users;


import com.azry.sps.common.model.users.SystemUser;
import com.azry.sps.common.model.users.UserGroup;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDto;
import com.azry.sps.server.utils.EncryptionUtils;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

public class SystemUserDTO extends ConfigurableDTO {


	private long id;

	private String username;

	private String password;

	private String name;

	private String email;

	private List<UserGroupDto> groups;

	private boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<UserGroupDto> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroupDto> groups) {
		this.groups = groups;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@GwtIncompatible
	public static SystemUserDTO toDTO(SystemUser systemUser) {
		if (systemUser == null) {
			return null;
		}
		SystemUserDTO dto = new SystemUserDTO();
		dto.setUsername(systemUser.getUsername());
		dto.setId(systemUser.getId());
		dto.setActive(systemUser.isActive());
		dto.setEmail(systemUser.getEmail());
		try {
			dto.setGroups(UserGroupDto.toDtos(systemUser.getGroups()));
		} catch (Exception ex){
			dto.setGroups(new ArrayList<UserGroupDto>());
			ex.printStackTrace();
		}
		dto.setName(systemUser.getName());
		dto.setPassword(systemUser.getPassword());
		dto.setVersion(systemUser.getVersion());
		dto.setCreateTime(systemUser.getCreateTime());
		dto.setLastUpdateTime(systemUser.getLastUpdateTime());
		return dto;
	}

	@GwtIncompatible
	public static SystemUser toEntity(SystemUserDTO dto) {
		if (dto == null) {
			return null;
		}
		SystemUser systemUser = new SystemUser();
		systemUser.setUsername(dto.getUsername());
		systemUser.setId(dto.getId());
		systemUser.setActive(dto.isActive());
		systemUser.setEmail(dto.getEmail());
		try {
			systemUser.setGroups(UserGroupDto.toEntities(dto.getGroups()));
		} catch (Exception ex){
			systemUser.setGroups(new ArrayList<UserGroup>());
			ex.printStackTrace();
		}
		systemUser.setName(dto.getName());
		systemUser.setPassword(EncryptionUtils.encodeSHA1(dto.getPassword()));
		systemUser.setVersion(dto.getVersion());
		systemUser.setCreateTime(dto.getCreateTime());
		systemUser.setLastUpdateTime(dto.getLastUpdateTime());
		return systemUser;
	}

	@GwtIncompatible
	public static List<SystemUserDTO> toDTOs(List<SystemUser> systemUsers) {
		if (systemUsers == null) {
			return null;
		}

		List<SystemUserDTO> dtos = new ArrayList<>();

		for(SystemUser user : systemUsers){
			dtos.add(toDTO(user));
		}

		return dtos;
	}

	@GwtIncompatible
	public static List<SystemUser> toEntities(List<SystemUserDTO> userDTOs) {
		if (userDTOs == null) {
			return null;
		}
		List<SystemUser> entities = new ArrayList<>();

		for(SystemUserDTO dto : userDTOs){
			entities.add(toEntity(dto));
		}

		return entities;
	}
}