package com.azry.sps.console.shared.dto.services;

import com.azry.sps.common.model.Configurable;
import com.google.gwt.user.client.rpc.IsSerializable;
import lombok.Data;

@Data
public class ServiceEntityDto extends Configurable implements IsSerializable {

	private long id;

	private String name;

	private boolean active;

	private String data;
}
