package com.azry.sps.console.shared.dto.systemparameter;


import com.azry.sps.systemparameters.model.SystemParameter;
import com.azry.sps.systemparameters.model.SystemParameterType;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;


public class SystemParameterDto implements IsSerializable {
	private long id;

	private String code;

	private String value;

	private SystemParameterDtoType type;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SystemParameterDtoType getType() {
		return type;
	}

	public void setType(SystemParameterDtoType type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SystemParameterDto)) {
			return false;
		}
		return id == ((SystemParameterDto)obj).id;
	}

	@GwtIncompatible
	public static List<SystemParameterDto> toDTOs(List<SystemParameter> parameters) {
		List<SystemParameterDto> dtos = new ArrayList<>();

		for (SystemParameter parameter : parameters) {
			dtos.add(toDTO(parameter));
		}

		return dtos;
	}

	@GwtIncompatible
	private static SystemParameterDtoType convertTypeToDto(SystemParameterType value) {
		return SystemParameterDtoType.values()[value.ordinal()];
	}

	@GwtIncompatible
	private static SystemParameterType convertTypeToEntity(SystemParameterDtoType value) {
		return SystemParameterType.values()[value.ordinal()];
	}


	@GwtIncompatible
	public static SystemParameterDto toDTO(SystemParameter parameter) {
		SystemParameterDto dto = new SystemParameterDto();
		dto.setId(parameter.getId());
		dto.setCode(parameter.getCode());
		dto.setValue(parameter.getValue());
		dto.setDescription(parameter.getDescription());
		dto.setType(convertTypeToDto(parameter.getType()));
		return dto;
	}

	@GwtIncompatible
	public static SystemParameter toEntity(SystemParameterDto dto) {
		SystemParameter entity = new SystemParameter();
		entity.setId(dto.getId());
		entity.setCode(dto.getCode());
		entity.setValue(dto.getValue());
		entity.setDescription(dto.getDescription());
		entity.setType(convertTypeToEntity(dto.getType()));
		return entity;
	}

}
