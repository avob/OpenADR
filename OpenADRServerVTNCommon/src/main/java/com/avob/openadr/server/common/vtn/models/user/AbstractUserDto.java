package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;

public class AbstractUserDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6110211810702643054L;

	private String id;

	private String username;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
