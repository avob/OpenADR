package com.avob.openadr.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
		X509Certificate parseCertificate = OadrPKISecurity.parseCertificate("src/test/resources/cert/test.crt");
		assertNotNull(parseCertificate);
	}

	@Test
	public void parsePrivateKeyTest() throws OadrSecurityException {
		PrivateKey parsePrivateKey = OadrPKISecurity.parsePrivateKey("src/test/resources/cert/test.key");
		assertNotNull(parsePrivateKey);
	}

	@Test
	public void createKeyStoreTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		String password = "mysuperstrongpassword";
		KeyStore createKeyStore = OadrPKISecurity.createKeyStore("src/test/resources/cert/test.key",
				"src/test/resources/cert/test.crt", password);
		assertNotNull(createKeyStore);
	}

	@Test
	public void createTrustStoreTest() throws OadrSecurityException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		Map<String, String> certificates = new HashMap<>();
		certificates.put("test", "src/test/resources/cert/test.crt");
		KeyStore createTrustStore = OadrPKISecurity.createTrustStore(certificates);
		assertNotNull(createTrustStore);
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
	public void generateCredentialsTest() throws NoSuchAlgorithmException, OperatorCreationException, IOException,
			CertificateException, NoSuchProviderException, OadrSecurityException {
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

		String algo = "SHA256withRSA";
		String commonName = "test.oadr.com";
		OadrUserX509Credential generateCredentials = OadrPKISecurity.generateCredentials(ca, caCert, commonName, algo);
		assertNotNull(generateCredentials);
		assertNotNull(generateCredentials.getCaCertificateFile());
		assertNotNull(generateCredentials.getCertificateFile());
		assertNotNull(generateCredentials.getFingerprint());
		assertNotNull(generateCredentials.getFingerprintFile());
		assertNotNull(generateCredentials.getPrivateKeyFile());
		try (BufferedReader reader = new BufferedReader(new FileReader(generateCredentials.getFingerprintFile()))) {
			String currentLine = reader.readLine();
			assertEquals(currentLine, generateCredentials.getFingerprint());
		} catch (IOException ex) {
			throw new OadrSecurityException("Can't read generated fingerprint file");
		}

	}

}
