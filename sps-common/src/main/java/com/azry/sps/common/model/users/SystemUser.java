package com.azry.sps.common.model.users;

import com.azry.sps.common.model.Configurable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class SystemUser extends Configurable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String username;

	@Column(length = 50)
	private String password;

	@Column(length = 50)
	private String name;

	@Column(length = 50)
	private String email;

	@ManyToMany
	private List<UserGroup> groups;

	private boolean active;

}
