package com.avob.openadr.server.common.vtn.models.user;

public class AbstractUserDto extends AbstractUserCreateDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6110211810702643054L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
