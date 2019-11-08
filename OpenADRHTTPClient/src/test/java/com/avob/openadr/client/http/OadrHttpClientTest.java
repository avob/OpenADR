package com.avob.openadr.client.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import com.avob.openadr.security.OadrFingerprintSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpClientTest {

	private static final String rsaPrivateKeyPemFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_privkey.pem";
	private static final String rsaClientCertPemFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_cert.pem";
	private static final String rsaClientCertOadr20aFingerprint = "743b28b5904dd2d4ad8f";
	private static final String rsaClientCertOadr20bFingerprint = "8a7dcb9724ed35242aad";
	private static final String rsaTrustedRootCertificate = "src/test/resources/rsa/TEST_OpenADR_RSA_RCA0002_Cert.pem";
	private static final String rsaTrustedIntermediateCertificate = "src/test/resources/rsa/TEST_OpenADR_RSA_SPCA0002_Cert.pem";

	private static final String eccPrivateKeyPemFilePath = "src/test/resources/ecc/TEST_ECC_VTN_17011961812_privkey.pem";
	private static final String eccClientCertPemFilePath = "src/test/resources/ecc/TEST_ECC_VTN_17011961812_cert.pem";
	private static final String eccTrustedRootCertificate = "src/test/resources/ecc/TEST_OpenADR_ECC_Root_CA3_cert.pem";
	private static final String eccTrustedIntermediateCertificate = "src/test/resources/ecc/TEST_OpenADR_ECC_SHA256_VTN_Int_CA3_cert.pem";

	private static final String[] protocols = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };
	private static final String[] ciphers = new String[] { "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
			"TLS_RSA_WITH_AES_128_CBC_SHA" };
	private static final int TIMEOUT = 5000;

	@Test
	public void testInitBasicClient() throws OadrSecurityException {

		String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

		new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
				.withDefaultBasicAuthentication("http://localhost:8080", "", "")
				.withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

	}

	@Test
	public void testInitDigestClient() throws OadrSecurityException {

		String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

		new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
				.withDefaultDigestAuthentication("http://localhost:8080", "", "", "", "")
				.withTrustedCertificate(Arrays.asList(certs)).withHeader("Content-Type", "application/json")
				.enableHttp(false).withTimeout(TIMEOUT).build();

	}

	@Test
	public void testInitRSAClient() throws OadrSecurityException, URISyntaxException {

		String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

		new OadrHttpClientBuilder().withProtocol(protocols, ciphers)
				.withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath)
				.withTrustedCertificate(Arrays.asList(certs)).enableHttp(true).withTimeout(TIMEOUT).build();

	}

	@Test
	public void testRSAFingerprint() throws OadrSecurityException {
		String fingerprint = OadrFingerprintSecurity.getOadr20aFingerprint(rsaClientCertPemFilePath);
		assertEquals(rsaClientCertOadr20aFingerprint, fingerprint);

		fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(rsaClientCertPemFilePath);
		assertEquals(rsaClientCertOadr20bFingerprint, fingerprint);
	}

	@Test
	public void testInitECCClient() throws OadrSecurityException, ClientProtocolException, IOException,
			NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

		String[] certs = { eccTrustedRootCertificate, eccTrustedIntermediateCertificate };

		new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
				.withX509Authentication(eccPrivateKeyPemFilePath, eccClientCertPemFilePath)
				.withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

	}

	@Test
	public void testInvalidX509() {

		String[] certs = { eccTrustedRootCertificate, eccTrustedIntermediateCertificate };

		boolean exception = false;
		try {
			new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
					.withX509Authentication("", "").withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT)
					.build();
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

	}
}
