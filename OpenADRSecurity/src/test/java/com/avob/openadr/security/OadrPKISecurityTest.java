package com.avob.openadr.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.junit.Test;

import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrPKISecurityTest {

	@Test
	public void parseCertificateTest() throws OadrSecurityException {

		// valid rsa
		X509Certificate parseCertificate = OadrPKISecurity.parseCertificate(TestUtils.TEST_CRT);
		assertNotNull(parseCertificate);

		// valid ecc
		parseCertificate = OadrPKISecurity.parseCertificate(TestUtils.TEST_ECC_CRT);
		assertNotNull(parseCertificate);

		// invalid path raise exception
		boolean exception = false;
		try {
			OadrPKISecurity.parseCertificate("mouaiccool");
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

		// invalid pem raise exception
		exception = false;
		try {
			OadrPKISecurity.parseCertificate(TestUtils.TEST_KEY);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void parsePrivateKeyTest() throws OadrSecurityException {
		// valid rsa
		PrivateKey parsePrivateKey = OadrPKISecurity.parsePrivateKey(TestUtils.TEST_KEY);
		assertNotNull(parsePrivateKey);

		// valid ecc
		parsePrivateKey = OadrPKISecurity.parsePrivateKey(TestUtils.TEST_ECC_KEY);
		assertNotNull(parsePrivateKey);

		// invalid path raise exception
		boolean exception = false;
		try {
			OadrPKISecurity.parsePrivateKey("mouaiccool");
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

		// invalid pem raise exception
		exception = false;
		try {
			OadrPKISecurity.parsePrivateKey(TestUtils.TEST_CRT);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void createKeyStoreTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		String password = UUID.randomUUID().toString();
		KeyStore createKeyStore = OadrPKISecurity.createKeyStore(TestUtils.TEST_KEY, TestUtils.TEST_CRT, password);
		assertNotNull(createKeyStore);

		// invalid pem
		boolean exception = false;
		try {
			createKeyStore = OadrPKISecurity.createKeyStore(TestUtils.TEST_CRT, TestUtils.TEST_KEY, password);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void createKeyManagerFactoryTest() throws OadrSecurityException {
		String password = UUID.randomUUID().toString();
		KeyManagerFactory createKeyManagerFactory = OadrPKISecurity.createKeyManagerFactory(TestUtils.TEST_KEY,
				TestUtils.TEST_CRT, password);
		assertNotNull(createKeyManagerFactory);

		// invalid pem
		boolean exception = false;
		try {
			createKeyManagerFactory = OadrPKISecurity.createKeyManagerFactory(TestUtils.TEST_CRT, TestUtils.TEST_KEY,
					password);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void createTrustStoreTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		List<String> certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_CRT);
		KeyStore createTrustStore = OadrPKISecurity.createTrustStore(certificates);
		assertNotNull(createTrustStore);
		createTrustStore = OadrPKISecurity.createTrustStore(new ArrayList<>());
		assertNotNull(createTrustStore);

		// invalid pem
		certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_KEY);
		boolean exception = false;
		try {
			createTrustStore = OadrPKISecurity.createTrustStore(certificates);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void createTrustManagerFactoryTest() throws OadrSecurityException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		List<String> certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_CRT);
		TrustManagerFactory createTrustManagerFactory = OadrPKISecurity.createTrustManagerFactory(certificates);
		assertNotNull(createTrustManagerFactory);
		createTrustManagerFactory = OadrPKISecurity.createTrustManagerFactory(new ArrayList<>());
		assertNotNull(createTrustManagerFactory);

		// invalid pem
		certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_KEY);
		boolean exception = false;
		try {
			createTrustManagerFactory = OadrPKISecurity.createTrustManagerFactory(certificates);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void createSSLContextTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		String password = UUID.randomUUID().toString();
		List<String> certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_CRT);
		SSLContext createSSLContext = OadrPKISecurity.createSSLContext(TestUtils.TEST_KEY, TestUtils.TEST_CRT,
				certificates, password);
		assertNotNull(createSSLContext);
		createSSLContext = OadrPKISecurity.createSSLContext(TestUtils.TEST_KEY, TestUtils.TEST_CRT,
				new ArrayList<>(), password);
		assertNotNull(createSSLContext);

		// invalid pem
		boolean exception = false;
		try {
			createSSLContext = OadrPKISecurity.createSSLContext(TestUtils.TEST_CRT, TestUtils.TEST_KEY, certificates,
					password);
		} catch (OadrSecurityException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void createDefaultSSLContextTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		String password = UUID.randomUUID().toString();
		List<String> certificates = new ArrayList<>();
		certificates.add(TestUtils.TEST_CRT);
		SSLContext createSSLContext = OadrPKISecurity.createSSLContext(null, null, null, password);
		assertNotNull(createSSLContext);

	}

	@Test
	public void generateRsaKeyPairTest() throws NoSuchAlgorithmException {
		OadrPKISecurity.generateRsaKeyPair();

	}

	@Test
	public void generateEccKeyPairTest() throws NoSuchAlgorithmException {
		OadrPKISecurity.generateEccKeyPair();
	}

	@Test
	public void generateCredentialsTest()
			throws NoSuchAlgorithmException, IOException, CertificateException, NoSuchProviderException,
			OadrSecurityException, InvalidKeyException, SignatureException, OperatorCreationException {
		KeyPair ca = OadrPKISecurity.generateRsaKeyPair();
		SubjectPublicKeyInfo subPubKeyInfo = SubjectPublicKeyInfo.getInstance(ca.getPublic().getEncoded());
		AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
		AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		BigInteger sn = new BigInteger(64, new SecureRandom());
		X509v3CertificateBuilder certificateGenerator = new X509v3CertificateBuilder(
				// These are the details of the CA
				new X500Name("cn=TestCa"),
				// This should be a serial number that the CA keeps track of
				sn,
				// Certificate validity start
				Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)),
				// Certificate validity end
				Date.from(LocalDateTime.now().plusDays(365).toInstant(ZoneOffset.UTC)),
				// Blanket grant the subject as requested in the CSR
				// A real CA would want to vet this.
				new X500Name("cn=TestSubject"),
				// Public key of the certificate authority
				subPubKeyInfo);

		ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
				.build(PrivateKeyFactory.createKey(ca.getPrivate().getEncoded()));

		X509CertificateHolder holder = certificateGenerator.build(sigGen);
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
		X509Certificate caCert = (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(holder.toASN1Structure().getEncoded()));

		String commonName = "test.oadr.com";

		// TEST RSA
		OadrUserX509Credential generateCredentials = OadrPKISecurity.generateCredentials(ca, caCert, commonName,
				OadrPKIAlgorithm.SHA256_RSA);
		testGeneratedCredential(generateCredentials, "SHA256withRSA");

		// TEST DSA
		generateCredentials = OadrPKISecurity.generateCredentials(ca, caCert, commonName, OadrPKIAlgorithm.SHA256_DSA);
		testGeneratedCredential(generateCredentials, "SHA256withDSA");

	}

	@Test
	public void md5HexTest() {
		String md5Hex = OadrPKISecurity.md5Hex("mouaiccool");
		assertEquals("f2d3fcdce97f021064356321534e2fda", md5Hex);
	}

	private void testGeneratedCredential(OadrUserX509Credential generateCredentials, String sigAlgo)
			throws OadrSecurityException, FileNotFoundException, NoSuchAlgorithmException, InvalidKeyException,
			SignatureException {
		assertNotNull(generateCredentials);
		assertNotNull(generateCredentials.getCaCertificateFile());
		assertNotNull(generateCredentials.getCertificateFile());
		assertNotNull(generateCredentials.getFingerprint());
		assertNotNull(generateCredentials.getFingerprintFile());
		assertNotNull(generateCredentials.getPrivateKeyFile());
		String readFile = TestUtils.readFile(generateCredentials.getFingerprintFile());
		assertEquals(readFile, generateCredentials.getFingerprint());

		X509Certificate cert = OadrPKISecurity
				.parseCertificate(new FileReader(generateCredentials.getCertificateFile()));
		PrivateKey key = OadrPKISecurity.parsePrivateKey(new FileReader(generateCredentials.getPrivateKeyFile()));

		// create a challenge
		byte[] challenge = new byte[10000];
		ThreadLocalRandom.current().nextBytes(challenge);

		// sign using the private key
		Signature sig = Signature.getInstance(sigAlgo);
		sig.initSign(key);
		sig.update(challenge);
		byte[] signature = sig.sign();

		// verify signature using the public key
		sig.initVerify(cert);
		sig.update(challenge);

		boolean keyPairMatches = sig.verify(signature);
		assertTrue(keyPairMatches);
	}

}
