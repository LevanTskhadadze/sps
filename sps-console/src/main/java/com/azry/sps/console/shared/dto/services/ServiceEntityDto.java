package com.azry.sps.console.shared.dto.services;

import com.azry.sps.common.model.Configurable;
import lombok.Data;

@Data
public class ServiceEntityDto extends Configurable {

	private long id;

	private String name;

	private boolean active;

	private String data;
}
