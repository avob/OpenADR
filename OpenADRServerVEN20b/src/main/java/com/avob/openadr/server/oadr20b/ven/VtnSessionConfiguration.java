package com.avob.openadr.server.oadr20b.ven;

import java.util.Map;
import java.util.Properties;

public class VtnSessionConfiguration {

	private static final String VTN_ID = "oadr.vtnid";
	private static final String VTN_URL = "oadr.vtnUrl";

	private static final String AUTHENTIFICATION_BASIC_USER = "oadr.security.authentication.basic.username";
	private static final String AUTHENTIFICATION_BASIC_PASS = "oadr.security.authentication.basic.password";
	private static final String AUTHENTIFICATION_DIGEST_USER = "oadr.security.authentication.digest.username";
	private static final String AUTHENTIFICATION_DIGEST_PASS = "oadr.security.authentication.digest.password";
	private String vtnId;
	private String vtnUrl;
	private String basicUser;
	private String basicPass;
	private String digestUser;
	private String digestPass;

	public VtnSessionConfiguration(Properties properties) {

		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			String keyStr = (String) e.getKey();
			String prop = (String) e.getValue();
			if (VTN_ID.equals(keyStr)) {
				this.setVtnId(prop);
			} else if (VTN_URL.equals(keyStr)) {
				this.setVtnUrl(prop);
			} else if (AUTHENTIFICATION_BASIC_USER.equals(keyStr)) {
				this.setBasicUser(prop);
			} else if (AUTHENTIFICATION_BASIC_PASS.equals(keyStr)) {
				this.setBasicPass(prop);
			} else if (AUTHENTIFICATION_DIGEST_USER.equals(keyStr)) {
				this.setDigestUser(prop);
			} else if (AUTHENTIFICATION_DIGEST_PASS.equals(keyStr)) {
				this.setDigestPass(prop);
			}
		}

	}

	public String getVtnId() {
		return vtnId;
	}

	public void setVtnId(String vtnId) {
		this.vtnId = vtnId;
	}

	public String getVtnUrl() {
		return vtnUrl;
	}

	public void setVtnUrl(String vtnUrl) {
		this.vtnUrl = vtnUrl;
	}

	public String getBasicUser() {
		return basicUser;
	}

	public void setBasicUser(String basicUser) {
		this.basicUser = basicUser;
	}

	public String getBasicPass() {
		return basicPass;
	}

	public void setBasicPass(String basicPass) {
		this.basicPass = basicPass;
	}

	public String getDigestUser() {
		return digestUser;
	}

	public void setDigestUser(String digestUser) {
		this.digestUser = digestUser;
	}

	public String getDigestPass() {
		return digestPass;
	}

	public void setDigestPass(String digestPass) {
		this.digestPass = digestPass;
	}

	public boolean isBasicAuthenticationConfigured() {
		return basicUser != null && basicPass != null;
	}

	public boolean isDigestAuthenticationConfigured() {
		return digestUser != null && digestPass != null;
	}

	@Override
	public String toString() {
		return "VtnSessionConfiguration [vtnId=" + vtnId + ", vtnUrl=" + vtnUrl + ", basicUser=" + basicUser
				+ ", basicPass=" + basicPass + ", digestUser=" + digestUser + ", digestPass=" + digestPass + "]";
	}

}
