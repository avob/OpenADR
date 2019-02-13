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
	private VenConfig venSessionConfig;

	public VtnSessionConfiguration(Properties properties, VenConfig defaultVenSessionConfig) {
		setVenSessionConfig(defaultVenSessionConfig.clone());
		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			String keyStr = (String) e.getKey();
			String prop = (String) e.getValue();
			if (VTN_ID.equals(keyStr)) {
				this.setVtnId(prop);
			} else if (VTN_URL.equals(keyStr)) {
				this.setVtnUrl(prop);
			} else if (AUTHENTIFICATION_BASIC_USER.equals(keyStr)) {
				getVenSessionConfig().setBasicUsername(prop);
			} else if (AUTHENTIFICATION_BASIC_PASS.equals(keyStr)) {
				getVenSessionConfig().setBasicPassword(prop);
			} else if (AUTHENTIFICATION_DIGEST_USER.equals(keyStr)) {
				getVenSessionConfig().setDigestUsername(prop);
			} else if (AUTHENTIFICATION_DIGEST_PASS.equals(keyStr)) {
				getVenSessionConfig().setDigestPassword(prop);
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


	public boolean isDigestAuthenticationConfigured() {
		return getVenSessionConfig().getDigestUsername() != null && getVenSessionConfig().getDigestPassword() != null;
	}
	
	public boolean isBasicAuthenticationConfigured() {
		return getVenSessionConfig().getBasicUsername() != null && getVenSessionConfig().getBasicPassword() != null;
	}

	public VenConfig getVenSessionConfig() {
		return venSessionConfig;
	}

	public void setVenSessionConfig(VenConfig venSessionConfig) {
		this.venSessionConfig = venSessionConfig;
	}


}
