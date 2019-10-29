package com.avob.openadr.client.http;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class SSLSocketFactoryBuilder {

	public static SSLConnectionSocketFactory createSSLSocketFactory(String clientPrivateKeyPemFilePath,
			String clientCertificatePemFilePath, List<String> trustedCertificateFilePath, String[] protocols,
			String[] ciphers) throws OadrSecurityException {
		KeyManagerFactory kmf = null;
		TrustManagerFactory tmf = null;
		try {

			if (clientPrivateKeyPemFilePath != null && clientCertificatePemFilePath != null) {
				kmf = createKeyManagerFactory(clientPrivateKeyPemFilePath, clientCertificatePemFilePath);
			} else {

				kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(null, null);
			}

			if (trustedCertificateFilePath != null) {
				tmf = createTrustManagerFactory(trustedCertificateFilePath);
			} else {
				tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(KeyStore.getInstance("JKS"));
			}
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException(e);
		} catch (UnrecoverableKeyException e) {
			throw new OadrSecurityException(e);
		} catch (KeyStoreException e) {
			throw new OadrSecurityException(e);
		}

		String exceptionMsg = "SSLConnectionSocketFactory can't be initialized";
		if (kmf != null && tmf != null) {
			SSLContext sc;
			try {
				sc = SSLContext.getInstance("TLS");
				sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

				return new SSLConnectionSocketFactory(sc, protocols, ciphers,
						SSLConnectionSocketFactory.getDefaultHostnameVerifier());

			} catch (NoSuchAlgorithmException e) {
				throw new OadrSecurityException(exceptionMsg, e);
			} catch (KeyManagementException e) {
				throw new OadrSecurityException(exceptionMsg, e);
			}
		} else {
			throw new OadrSecurityException(exceptionMsg);
		}

	}

	private static KeyManagerFactory createKeyManagerFactory(String clientPrivateKeyPemFilePath,
			String clientCertificatePemFilePath) throws OadrSecurityException {
		KeyManagerFactory kmf = null;

		String exceptionMsg = "private key/client certificate can't be inserted into keystore";
		try {

			PrivateKey parsePrivateKey = OadrPKISecurity.parsePrivateKey(clientPrivateKeyPemFilePath);
			X509Certificate parseCertificate = OadrPKISecurity.parseCertificate(clientCertificatePemFilePath);
			Certificate[] certs = { parseCertificate };
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			ks.setKeyEntry("mykey", parsePrivateKey, new char[0], certs);
			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, new char[0]);
		} catch (KeyStoreException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (CertificateException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (IOException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (UnrecoverableKeyException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		}
		return kmf;
	}

	private static TrustManagerFactory createTrustManagerFactory(List<String> trustedCertificateFilePath)
			throws OadrSecurityException {
		TrustManagerFactory tmf = null;
		String exceptionMsg = "certificates can't be inserted into truststore";
		try {
			KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
			ts.load(null, null);
			for (String certPath : trustedCertificateFilePath) {
				X509Certificate cert = OadrPKISecurity.parseCertificate(certPath);
				File file = new File(certPath);
				ts.setCertificateEntry(file.getName(), cert);
			}
			tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(ts);
		} catch (KeyStoreException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (CertificateException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		} catch (IOException e) {
			throw new OadrSecurityException(exceptionMsg, e);
		}
		return tmf;
	}
}
