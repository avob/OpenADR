package com.avob.openadr.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

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
	private static final int OADR_FINGER_LENGTH = 29;

	private OadrFingerprintSecurity() {
	}

	public static String getOadr20aFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrPKISecurity.parseCertificate(pemFilePath);
		return OadrFingerprintSecurity.getOadr20aFingerprint(cert);
	}

	public static String getOadr20aFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrFingerprintSecurity.format(OadrFingerprintSecurity.truncate(getFingerprint(cert, SHA1)));
	}

	public static String getOadr20bFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrPKISecurity.parseCertificate(pemFilePath);
		return OadrFingerprintSecurity.getOadr20bFingerprint(cert);
	}

	public static String getOadr20bFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrFingerprintSecurity.format(OadrFingerprintSecurity.truncate(getFingerprint(cert, SHA_256)));
	}

	private static String truncate(String fingerprint) throws OadrSecurityException {
		if (fingerprint.length() == OADR_FINGER_LENGTH) {
			return fingerprint;
		} else if (fingerprint.length() > OADR_FINGER_LENGTH) {
			return fingerprint.substring(fingerprint.length() - OADR_FINGER_LENGTH);
		} else {
			// whatever is appropriate in this case
			throw new OadrSecurityException("Oadr fingerprint can't be generated");
		}
	}

	private static String hexify(byte[] bytes) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		String delimiter = "";
		for (int i = 0; i < bytes.length; ++i) {
			buf.append(delimiter);
			delimiter = ":";
			buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
			buf.append(hexDigits[bytes[i] & 0x0f]);
		}
		return buf.toString();
	}

	private static String format(String fingerprint) {
		return fingerprint.replaceAll(":", "").toLowerCase();
	}

	private static String getFingerprint(X509Certificate cert, String shaVersion) throws OadrSecurityException {

		MessageDigest md;
		try {
			md = MessageDigest.getInstance(shaVersion);
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException("Unknown SHA version", e);
		}
		byte[] der;
		try {
			der = cert.getEncoded();
		} catch (CertificateEncodingException e) {
			throw new OadrSecurityException("Unknown certificate encoding (should be DER)", e);
		}
		md.update(der);
		byte[] digest = md.digest();
		return hexify(digest);
	}

}
