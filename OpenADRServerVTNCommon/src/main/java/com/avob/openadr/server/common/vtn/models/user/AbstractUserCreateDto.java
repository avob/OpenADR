package com.avob.openadr.server.common.vtn.models.user;

import java.io.Serializable;

public class AbstractUserCreateDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2859827657765158357L;

	private String username;

	private String password;

	private String authenticationType;

	private String needCertificateGeneration;

	private String commonName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getNeedCertificateGeneration() {
		return needCertificateGeneration;
	}

	public void setNeedCertificateGeneration(String needCertificateGeneration) {
		this.needCertificateGeneration = needCertificateGeneration;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

}
