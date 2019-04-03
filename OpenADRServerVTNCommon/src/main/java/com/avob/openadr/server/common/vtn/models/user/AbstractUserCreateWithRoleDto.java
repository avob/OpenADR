package com.avob.openadr.server.common.vtn.models.user;

import java.util.List;

public class AbstractUserCreateWithRoleDto  extends AbstractUserCreateDto{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1134614176729481009L;
	private List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
