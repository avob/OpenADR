package com.avob.openadr.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.bouncycastle.util.encoders.Hex;

import com.avob.openadr.security.exception.OadrSecurityException;

/**
 * Utility functions to get Oadr 2.0a / 2.0b fingerprint from X509Certificate
 * 
 * @author bzanni
 *
 */
public class OadrFingerprintSecurity {

	private static final String SHA_256 = "SHA-256";
	private static final String SHA1 = "SHA1";
	private static final int OADR_FINGER_LENGTH = 20;

	private OadrFingerprintSecurity() {
	}

	public static String getOadr20aFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrPKISecurity.parseCertificate(pemFilePath);
		return OadrFingerprintSecurity.getOadr20aFingerprint(cert);
	}

	public static String getOadr20aFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrFingerprintSecurity.truncate(getFingerprint(cert, SHA1));
	}

	public static String getOadr20bFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrPKISecurity.parseCertificate(pemFilePath);
		return OadrFingerprintSecurity.getOadr20bFingerprint(cert);
	}

	public static String getOadr20bFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrFingerprintSecurity.truncate(getFingerprint(cert, SHA_256));
	}

	private static String truncate(String fingerprint) throws OadrSecurityException {
		if (fingerprint.length() >= OADR_FINGER_LENGTH) {
			return fingerprint.substring(fingerprint.length() - OADR_FINGER_LENGTH);
		} else {
			throw new OadrSecurityException("Oadr fingerprint can't be generated");
		}
	}

	private static String getFingerprint(X509Certificate cert, String shaVersion) throws OadrSecurityException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(shaVersion);
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException(e);
		}
		byte[] der;
		try {
			der = cert.getEncoded();
		} catch (CertificateEncodingException e) {
			throw new OadrSecurityException(e);
		}
		md.update(der);
		byte[] digest = md.digest();
		return new String(Hex.encode(digest));
	}

}
