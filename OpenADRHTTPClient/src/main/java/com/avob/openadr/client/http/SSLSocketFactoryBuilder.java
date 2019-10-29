package com.avob.openadr.client.http;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

/**
 * create SSLContextFactory from PEM formatted key/cert
 * 
 * @author bzanni
 *
 */
public class SSLSocketFactoryBuilder {

	private SSLSocketFactoryBuilder() {
	}

	/**
	 * create SSLContextFactory from PEM formatted key/cert. Fallback on default
	 * SSLContext if no key/cert given
	 * 
	 * @param clientPrivateKeyPemFilePath
	 * @param clientCertificatePemFilePath
	 * @param trustedCertificateFilePath
	 * @param protocols
	 * @param ciphers
	 * @return
	 * @throws OadrSecurityException
	 */
	public static SSLConnectionSocketFactory createSSLSocketFactory(String clientPrivateKeyPemFilePath,
			String clientCertificatePemFilePath, List<String> trustedCertificateFilePath, String[] protocols,
			String[] ciphers) throws OadrSecurityException {
		KeyManagerFactory kmf = null;
		TrustManagerFactory tmf = null;
		try {
			// if
			if (clientPrivateKeyPemFilePath != null && clientCertificatePemFilePath != null) {
				String password = UUID.randomUUID().toString();
				kmf = OadrPKISecurity.createKeyManagerFactory(clientPrivateKeyPemFilePath, clientCertificatePemFilePath,
						password);
			} else {
				kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(null, null);
			}

			if (trustedCertificateFilePath != null) {
				tmf = OadrPKISecurity.createTrustManagerFactory(trustedCertificateFilePath);
			} else {
				tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(KeyStore.getInstance("JKS"));
			}
		} catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
			throw new OadrSecurityException(e);
		}
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			return new SSLConnectionSocketFactory(sc, protocols, ciphers,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new OadrSecurityException(e);
		}

	}
}
