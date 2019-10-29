package com.avob.openadr.security;

import java.io.File;

/**
 * x509 user credential model
 * 
 * @author bzanni
 *
 */
public class OadrUserX509Credential {

	private String fingerprint;
	private File caCertificateFile;
	private File certificateFile;
	private File privateKeyFile;
	private File fingerprintFile;

	public OadrUserX509Credential(String fingerprint, File caCertificateFile, File certificateFile, File privateKeyFile,
			File fingerprintFile) {
		this.fingerprint = fingerprint;
		this.caCertificateFile = caCertificateFile;
		this.certificateFile = certificateFile;
		this.privateKeyFile = privateKeyFile;
		this.fingerprintFile = fingerprintFile;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public File getCaCertificateFile() {
		return caCertificateFile;
	}

	public File getCertificateFile() {
		return certificateFile;
	}

	public File getPrivateKeyFile() {
		return privateKeyFile;
	}

	public File getFingerprintFile() {
		return fingerprintFile;
	}

}
