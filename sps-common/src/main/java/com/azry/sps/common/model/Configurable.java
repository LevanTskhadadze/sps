package com.azry.sps.common.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@MappedSuperclass
@Data
public class Configurable {

	@CreationTimestamp
	private Date createTime;

	@UpdateTimestamp
	private Date lastUpdateTime;

	@Version
	private long version;

}
