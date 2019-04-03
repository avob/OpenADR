package com.avob.openadr.server.common.vtn.models.user;

import java.util.List;

public class AbstractUserWithRoleDto extends AbstractUserDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -882304923821373940L;
	private List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
