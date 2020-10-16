package com.azry.sps.console.server.helper;

import com.azry.sps.systemparameters.model.systemparam.SystemParameter;
import com.azry.sps.systemparameters.model.systemparam.SystemParameterType;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDtoType;

import java.util.ArrayList;
import java.util.List;

public class SystemParameterDtoHelper {
	public static List<SystemParameterDto> toDTOs(List<SystemParameter> parameters) {
		List<SystemParameterDto> dtos = new ArrayList<>();

		for (SystemParameter parameter : parameters) {
			dtos.add(toDTO(parameter));
		}

		return dtos;
	}

	private static SystemParameterDtoType convertEnumToDto(SystemParameterType value) {
		return SystemParameterDtoType.values()[value.ordinal()];
	}

	private static SystemParameterType convertEnumToEntity(SystemParameterDtoType value) {
		return SystemParameterType.values()[value.ordinal()];
	}


	public static SystemParameterDto toDTO(SystemParameter parameter) {
		SystemParameterDto dto = new SystemParameterDto();
		dto.setId(parameter.getId());
		dto.setCode(parameter.getCode());
		dto.setValue(parameter.getValue());
		dto.setDescription(parameter.getDescription());
		dto.setType(convertEnumToDto(parameter.getType()));
		return dto;
	}

	public static SystemParameter toEntity(SystemParameterDto dto) {
		SystemParameter entity = new SystemParameter();
		entity.setId(dto.getId());
		entity.setCode(dto.getCode());
		entity.setValue(dto.getValue());
		entity.setDescription(dto.getDescription());
		entity.setType(convertEnumToEntity(dto.getType()));
		return entity;
	}
}
