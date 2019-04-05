package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "abstract_user")
public abstract class AbstractUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4987643291191994638L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "token_1")
	private String digestPassword;

	@Column(name = "token_2")
	private String basicPassword;

	private String commonName;

	private String authenticationType;

	public AbstractUser() {
	}

	public AbstractUser(String username, String password) {
		this.setUsername(username);
		this.setDigestPassword(password);
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getDigestPassword() {
		return digestPassword;
	}

	public String getBasicPassword() {
		return basicPassword;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDigestPassword(String password) {
		this.digestPassword = password;
	}

	public void setBasicPassword(String basicPassword) {
		this.basicPassword = basicPassword;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

}
