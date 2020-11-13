package com.azry.sps.common.model.service;

import com.azry.sps.common.model.Configurable;
import com.azry.sps.common.utils.XmlUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
public class ServiceEntity extends Configurable {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;

	private boolean active;

	@Column(length = Integer.MAX_VALUE)
	private String data;

	@Transient
	public Service getService(){
		Service service = XmlUtils.fromXML(data, Service.class);
		service.setId(getId());
		service.setName(getName());
		service.setActive(isActive());
		service.setCreateTime(getCreateTime());
		service.setLastUpdateTime(getLastUpdateTime());
		service.setVersion(getVersion());
		return service;
	}

}
