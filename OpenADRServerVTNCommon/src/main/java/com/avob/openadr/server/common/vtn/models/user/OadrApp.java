package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "oadr_app")
public class OadrApp extends AbstractUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2869155520774908886L;

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> roles;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
