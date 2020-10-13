package com.azry.sps.console.server.converter;

import com.azry.sps.systemparameters.model.systemparam.SystemParameter;
import com.azry.sps.systemparameters.model.systemparam.SystemParameterType;
import com.azry.sps.console.shared.systemparameters.SystemParameterDto;
import com.azry.sps.console.shared.systemparameters.SystemParameterDtoType;

import java.util.ArrayList;
import java.util.List;

public class SystemParameterDtoConverter {
	public static List<SystemParameterDto> toDTOs(List<SystemParameter> parameters) {
		List<SystemParameterDto> dtos = new ArrayList<>();

		for (SystemParameter parameter : parameters) {
			dtos.add(toDTO(parameter));
		}

		return dtos;
	}

	private static SystemParameterDtoType convertEnum(SystemParameterType value) {
		return SystemParameterDtoType.values()[value.ordinal()];
	}


	public static SystemParameterDto toDTO(SystemParameter parameter) {
		SystemParameterDto dto = new SystemParameterDto();
		dto.setId(parameter.getId());
		dto.setCode(parameter.getCode());
		dto.setValue(parameter.getValue());
		dto.setType(convertEnum(parameter.getType()));
		return dto;
	}
}
