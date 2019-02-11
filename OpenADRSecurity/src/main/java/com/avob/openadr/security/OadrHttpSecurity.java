package com.avob.openadr.security;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.util.encoders.Hex;

import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpSecurity {

	private static final int OADR_FINGER_LENGTH = 29;

	private OadrHttpSecurity() {
	}

	public static PrivateKey parsePrivateKey(String privateKeyFilePath) throws OadrSecurityException {
		Object readObject;
		try {
			readObject = parsePem(privateKeyFilePath);
			if (readObject instanceof PrivateKeyInfo) {
				PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) readObject;
				return new JcaPEMKeyConverter().setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
						.getPrivateKey(privateKeyInfo);
			} else if (readObject instanceof PKCS8EncryptedPrivateKeyInfo) {
				throw new OadrSecurityException(
						"PKCS8 Encrypted PrivateKeys are not supported. Only unencrypted private keys");
			} else {
				throw new OadrSecurityException("private key file does not have good format: " + privateKeyFilePath);
			}
		} catch (IOException e) {
			throw new OadrSecurityException("private key file cannot be read", e);
		}
	}

	public static X509Certificate parseCertificate(String certificateFilePath) throws OadrSecurityException {
		Object readObject;
		try {
			readObject = parsePem(certificateFilePath);
			if (readObject instanceof X509CertificateHolder) {
				X509CertificateHolder certHolder = (X509CertificateHolder) readObject;
				return new JcaX509CertificateConverter()
						.setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
						.getCertificate(certHolder);
			} else {
				throw new OadrSecurityException("certificate file does not have good format: " + certificateFilePath);
			}
		} catch (IOException e) {
			throw new OadrSecurityException("certificate file cannot be read: " + certificateFilePath, e);
		} catch (CertificateException e) {
			throw new OadrSecurityException(
					"certificate holder cannot be read convert to X509 certificate: " + certificateFilePath, e);
		}
	}

	private static Object parsePem(String filePath) throws IOException {
		PEMParser pemReader = null;
		FileReader fileReader;
		try {
			fileReader = new FileReader(filePath);
			pemReader = new PEMParser(fileReader);
			return pemReader.readObject();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (pemReader != null) {
				try {
					pemReader.close();
				} catch (IOException e) {
					throw e;
				}
			}
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

	public static String getFingerprint(X509Certificate cert, String shaVersion) throws OadrSecurityException {

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

	public static String getFingerprint(String pemFilePath, String shaVersion) throws OadrSecurityException {
		if (pemFilePath == null || shaVersion == null) {
			throw new OadrSecurityException("Invalid fingerprint generation input");
		}
		X509Certificate parseCertificate = OadrHttpSecurity.parseCertificate(pemFilePath);
		return getFingerprint(parseCertificate, shaVersion);
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

	public static String getOadr20aFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrHttpSecurity.parseCertificate(pemFilePath);
		return OadrHttpSecurity.getOadr20aFingerprint(cert);
	}

	public static String getOadr20aFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrHttpSecurity.truncate(getFingerprint(cert, "SHA1"));
	}

	public static String getOadr20bFingerprint(String pemFilePath) throws OadrSecurityException {
		X509Certificate cert = OadrHttpSecurity.parseCertificate(pemFilePath);
		return OadrHttpSecurity.getOadr20bFingerprint(cert);
	}

	public static String getOadr20bFingerprint(X509Certificate cert) throws OadrSecurityException {
		return OadrHttpSecurity.truncate(getFingerprint(cert, "SHA-256"));
	}

	public static KeyStore createKeyStore(String privateKeyFilePath, String clientCertificatefilePath, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			OadrSecurityException {
		PrivateKey parsePrivateKey = OadrHttpSecurity.parsePrivateKey(privateKeyFilePath);
		X509Certificate parseCertificate = OadrHttpSecurity.parseCertificate(clientCertificatefilePath);
		Certificate[] certs = { parseCertificate };
		// create keystore
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setKeyEntry("oadrRsaPrivateKey", parsePrivateKey, password.toCharArray(), certs);
		return ks;
	}

	public static KeyStore createTrustStore(Map<String, String> certificates) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, OadrSecurityException {

		KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
		ts.load(null, null);

		Iterator<Entry<String, String>> iterator = certificates.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			ts.setCertificateEntry(next.getKey(), OadrHttpSecurity.parseCertificate(next.getValue()));
		}
		return ts;
	}

	public static String md5Hex(String data) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!", e);
		}

		return new String(Hex.encode(digest.digest(data.getBytes())));
	}

	public static void main(String[] args) throws OadrSecurityException {
		String oadr20bFingerprint = OadrHttpSecurity
				.getOadr20bFingerprint("/home/bzanni/Downloads/oadr/TEST_RSA_VTN_180411105205_cert.pem");

		System.out.println(oadr20bFingerprint);
	}

}
