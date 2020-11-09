package com.avob.openadr.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import com.avob.openadr.security.exception.OadrSecurityException;

/**
 * Utility functions to deal with Oadr Public Key Infrastructure security
 * 
 * @author bzanni
 *
 */
public class OadrPKISecurity {

	private OadrPKISecurity() {
	}

	/**
	 * Parse PEM formatted private key
	 * 
	 * @param privateKeyFilePath
	 * @return
	 * @throws OadrSecurityException
	 */
	public static PrivateKey parsePrivateKey(String privateKeyFilePath) throws OadrSecurityException {
		try {
			return parsePrivateKey(new FileReader(privateKeyFilePath));
		} catch (FileNotFoundException e) {
			throw new OadrSecurityException(e);
		}
	}

	/**
	 * Parse PEM formatted private key
	 * 
	 * @param fileReader
	 * @return
	 * @throws OadrSecurityException
	 */
	public static PrivateKey parsePrivateKey(FileReader fileReader) throws OadrSecurityException {
		Object readObject;
		try {
			readObject = parsePem(fileReader);
			PrivateKeyInfo privateKeyInfo;
			if (readObject instanceof PEMKeyPair) {
				privateKeyInfo = ((PEMKeyPair) readObject).getPrivateKeyInfo();
			} else if (readObject instanceof PrivateKeyInfo) {
				privateKeyInfo = (PrivateKeyInfo) readObject;
			} else {
				throw new OadrSecurityException("private key file does not have good format");
			}
			return new JcaPEMKeyConverter().setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
					.getPrivateKey(privateKeyInfo);
		} catch (IOException e) {
			throw new OadrSecurityException(e);
		}
	}

	/**
	 * Parse PEM formatted x509 certificate
	 * 
	 * @param certificateFilePath
	 * @return
	 * @throws OadrSecurityException
	 */
	public static X509Certificate parseCertificate(String certificateFilePath) throws OadrSecurityException {
		try {
			return parseCertificate(new FileReader(certificateFilePath));
		} catch (FileNotFoundException e) {
			throw new OadrSecurityException(e);
		}
	}

	public static X509Certificate parseCertificate(FileReader fileReader) throws OadrSecurityException {
		Object readObject;
		try {
			readObject = parsePem(fileReader);
			if (readObject instanceof X509CertificateHolder) {
				X509CertificateHolder certHolder = (X509CertificateHolder) readObject;
				return new JcaX509CertificateConverter()
						.setProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())
						.getCertificate(certHolder);
			} else {
				throw new OadrSecurityException("certificate file does not have good format");
			}
		} catch (CertificateException e) {
			throw new OadrSecurityException(e);
		}
	}

	/**
	 * Create passwordless java Keystore from PEM formatted key/cert
	 * 
	 * @param privateKeyFilePath
	 * @param clientCertificatefilePath
	 * @param password
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws OadrSecurityException
	 */
	public static KeyStore createKeyStore(String privateKeyFilePath, String clientCertificatefilePath, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			OadrSecurityException {
		PrivateKey parsePrivateKey = OadrPKISecurity.parsePrivateKey(privateKeyFilePath);
		X509Certificate parseCertificate = OadrPKISecurity.parseCertificate(clientCertificatefilePath);
		Certificate[] certs = { parseCertificate };
		// create keystore
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setKeyEntry("key", parsePrivateKey, password.toCharArray(), certs);
		return ks;
	}

	/**
	 * Create passwordless java KeyManagerFactory from PEM formatted key/cert
	 * 
	 * @param clientPrivateKeyPemFilePath
	 * @param clientCertificatePemFilePath
	 * @param password
	 * @return
	 * @throws OadrSecurityException
	 */
	public static KeyManagerFactory createKeyManagerFactory(String clientPrivateKeyPemFilePath,
			String clientCertificatePemFilePath, String password) throws OadrSecurityException {
		KeyManagerFactory kmf = null;
		try {
			KeyStore ks = OadrPKISecurity.createKeyStore(clientPrivateKeyPemFilePath, clientCertificatePemFilePath,
					password);

			kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, password.toCharArray());
			return kmf;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
				| UnrecoverableKeyException e) {
			throw new OadrSecurityException(e);
		}
	}

	/**
	 * Create java Truststore from PEM formatted x509 certificates
	 * 
	 * @param certificates
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws OadrSecurityException
	 */
	public static KeyStore createTrustStore(List<String> certificates) throws KeyStoreException,
	NoSuchAlgorithmException, CertificateException, IOException, OadrSecurityException {
		KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
		ts.load(null, null);
		String keyEntryPrefix = "trust_";
		int i = 1;
		for (String certPath : certificates) {
			ts.setCertificateEntry(keyEntryPrefix + i, OadrPKISecurity.parseCertificate(certPath));
			i++;
		}
		return ts;
	}

	/**
	 * Create java TrustManagerFactory from PEM formatted x509 certificates
	 * 
	 * @param trustedCertificateFilePath
	 * @return
	 * @throws OadrSecurityException
	 */
	public static TrustManagerFactory createTrustManagerFactory(List<String> trustedCertificateFilePath)
			throws OadrSecurityException {
		TrustManagerFactory tmf = null;
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
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new OadrSecurityException(e);
		}
		return tmf;
	}

	/**
	 * Create java SSLContext from PEM formatted private key and x509 certificates
	 * 
	 * @param clientPrivateKeyPemFilePath
	 * @param clientCertificatePemFilePath
	 * @param trustCertificates
	 * @return
	 * @throws OadrSecurityException
	 */
	public static SSLContext createSSLContext(String clientPrivateKeyPemFilePath, String clientCertificatePemFilePath,
			List<String> trustCertificates, String password) throws OadrSecurityException {
		KeyManagerFactory kmf;
		TrustManagerFactory tmf;
		SSLContext sslContext;
		try {

			if (clientPrivateKeyPemFilePath != null && clientCertificatePemFilePath != null) {
				kmf = OadrPKISecurity.createKeyManagerFactory(clientPrivateKeyPemFilePath, clientCertificatePemFilePath,
						password);
			} else {
				kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(null, null);
			}

			if (trustCertificates != null) {
				tmf = OadrPKISecurity.createTrustManagerFactory(trustCertificates);
			} else {
				tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(KeyStore.getInstance("JKS"));
			}

			// SSL Context Factory

			sslContext = SSLContext.getInstance("TLS");

			// init ssl context
			String seed = UUID.randomUUID().toString();

			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom(seed.getBytes()));

			return sslContext;

		} catch (NoSuchAlgorithmException | KeyManagementException | UnrecoverableKeyException | KeyStoreException e) {
			throw new OadrSecurityException(e);
		}
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

	/**
	 * Generate RSA java KeyPair
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair("RSA", 2048);
	}

	/**
	 * Generate ECC java KeyPair
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateEccKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair("DSA", 512);
	}

	/**
	 * Generate credentials using certified by provided CA key/cert
	 * 
	 * @param caKeyPair
	 * @param caCert
	 * @param commonName
	 * @param algo
	 * @return
	 * @throws OadrSecurityException
	 */
	public static OadrUserX509Credential generateCredentials(KeyPair caKeyPair, X509Certificate caCert,
			String commonName, OadrPKIAlgorithm algo) throws OadrSecurityException {
		try {
			long now = System.currentTimeMillis();
			String venCN = commonName;
			BigInteger serialNumber = BigInteger.valueOf(now);

			KeyPair venCred = null;
			String csrAlgo = "";

			if (OadrPKIAlgorithm.SHA256_DSA.equals(algo)) {
				venCred = OadrPKISecurity.generateEccKeyPair();
				csrAlgo = "SHA256withDSA";
			} else if (OadrPKIAlgorithm.SHA256_RSA.equals(algo)) {
				venCred = OadrPKISecurity.generateRsaKeyPair();
				csrAlgo = "SHA256withRSA";
			}

			String x509PrincipalName = "C=FR, ST=Paris, L=Paris, O=Avob, OU=Avob, CN=" + venCN;
			PKCS10CertificationRequest csr = OadrPKISecurity.generateCsr(venCred, x509PrincipalName, csrAlgo);
			X509Certificate crt = OadrPKISecurity.signCsr(csr, caKeyPair, caCert, serialNumber);

			String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(crt);

			File crtFile = writeToFile(venCN, "crt", OadrPKISecurity.writeCrtToString(crt));
			File caCrtFile = writeToFile("ca", "crt", OadrPKISecurity.writeCrtToString(caCert));
			File keyFile = writeToFile(venCN, "key", OadrPKISecurity.writeKeyToString(venCred));
			File fingerprintFile = writeToFile(venCN, "fingerprint", fingerprint);

			return new OadrUserX509Credential(fingerprint, caCrtFile, crtFile, keyFile, fingerprintFile);
		} catch (NoSuchAlgorithmException | OperatorCreationException | CertificateException | NoSuchProviderException
				| IOException e) {
			throw new OadrSecurityException(e);
		}

	}

	private static KeyPair generateKeyPair(String algo, int length) throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algo);
		keyGen.initialize(length);
		return keyGen.generateKeyPair();

	}

	private static X509Certificate signCsr(PKCS10CertificationRequest csr, KeyPair caKeyPair, X509Certificate caCert,
			BigInteger serialNumber)
					throws IOException, OperatorCreationException, CertificateException, NoSuchProviderException {

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
		AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
		X500Name x500name = new JcaX509CertificateHolder(caCert).getSubject();
		X509v3CertificateBuilder certificateGenerator = new X509v3CertificateBuilder(
				// These are the details of the CA
				x500name,
				// This should be a serial number that the CA keeps track of
				serialNumber,
				// Certificate validity start
				Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)),
				// Certificate validity end
				Date.from(LocalDateTime.now().plusDays(365).toInstant(ZoneOffset.UTC)),
				// Blanket grant the subject as requested in the CSR
				// A real CA would want to vet this.
				csr.getSubject(),
				// Public key of the certificate authority
				csr.getSubjectPublicKeyInfo());

		ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
				.build(PrivateKeyFactory.createKey(caKeyPair.getPrivate().getEncoded()));

		X509CertificateHolder holder = certificateGenerator.build(sigGen);
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
		return (X509Certificate) certificateFactory
				.generateCertificate(new ByteArrayInputStream(holder.toASN1Structure().getEncoded()));

	}

	private static String writeCrtToString(X509Certificate certificate)
			throws CertificateEncodingException, OadrSecurityException {
		return writePemToString("CERTIFICATE", certificate.getEncoded());
	}

	private static String writeKeyToString(KeyPair pair) throws OadrSecurityException {
		return writePemToString("PRIVATE KEY", pair.getPrivate().getEncoded());
	}

	private static String writePemToString(String type, byte[] content) throws OadrSecurityException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream))) {
			pemWriter.writeObject(new PemObject(type, content));
		} catch (IOException e) {
			throw new OadrSecurityException(e);
		}
		return new String(outputStream.toByteArray());
	}

	private static File writeToFile(String fileName, String fileExtension, String content) throws IOException {
		Path path = Files.createTempFile(fileName + "-", "." + fileExtension);
		File file = path.toFile();

		if (file.exists()) {
			boolean delete = Files.deleteIfExists(file.toPath());
			if (!delete) {
				throw new IOException("file can't be deleted");
			}
		}

		try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
			byte[] strToBytes = content.getBytes();
			outputStream.write(strToBytes);
		}

		return file;
	}

	private static PKCS10CertificationRequest generateCsr(KeyPair pair, String x509PrincipalName, String algo)
			throws OadrSecurityException {
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal(x509PrincipalName), pair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(algo);
		ContentSigner signer = null;
		try {
			signer = csBuilder.build(pair.getPrivate());
		} catch (OperatorCreationException e) {
			throw new OadrSecurityException(e);
		}
		return p10Builder.build(signer);
	}

	private static Object parsePem(FileReader fileReader) throws OadrSecurityException {
		try (PEMParser pemReader = new PEMParser(fileReader)) {
			return pemReader.readObject();
		} catch (IOException e) {
			throw new OadrSecurityException(e);
		}

	}

}
