package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "oadr_user")
public class OadrUser extends AbstractUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5248787555143594631L;
	private String email;
//
//	@ElementCollection
//	@LazyCollection(LazyCollectionOption.FALSE)
//	private List<String> roles;

	public OadrUser() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
//
//	public List<String> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(List<String> roles) {
//		this.roles = roles;
//	}

}
