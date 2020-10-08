package com.azry.sps.common;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Data
public class Configurable {

	private Date createTime;

	private Date lastUpdateTime;

	private long version;
}
