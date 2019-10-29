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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
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
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
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

	/**
	 * Parse PEM formatted private key
	 * 
	 * @param privateKeyFilePath
	 * @return
	 * @throws OadrSecurityException
	 */
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

	/**
	 * Parse PEM formatted x509 certificate
	 * 
	 * @param certificateFilePath
	 * @return
	 * @throws OadrSecurityException
	 */
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

	/**
	 * Create java Keystore from PEM formatted key/cert
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
	public static KeyStore createTrustStore(Map<String, String> certificates) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, OadrSecurityException {

		KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
		ts.load(null, null);

		Iterator<Entry<String, String>> iterator = certificates.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			ts.setCertificateEntry(next.getKey(), OadrPKISecurity.parseCertificate(next.getValue()));
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
			String commonName, String algo) throws OadrSecurityException {
		try {
			long now = System.currentTimeMillis();
			String venCN = commonName;
			BigInteger serialNumber = BigInteger.valueOf(now);

			KeyPair venCred = null;

			if ("SHA256withDSA".equals(algo)) {

				venCred = OadrPKISecurity.generateEccKeyPair();

			} else if ("SHA256withRSA".equals(algo)) {
				venCred = OadrPKISecurity.generateRsaKeyPair();
			} else {
				throw new OadrSecurityException("Can't generate credentials using algo: " + algo);
			}

			String x509PrincipalName = "C=FR, ST=Paris, L=Paris, O=Avob, OU=Avob, CN=" + venCN;
			PKCS10CertificationRequest csr = OadrPKISecurity.generateCsr(venCred, x509PrincipalName, algo);
			X509Certificate crt = OadrPKISecurity.signCsr(csr, caKeyPair, caCert, serialNumber);

			String fingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(crt);

			File crtFile = writeToFile(venCN, "crt", OadrPKISecurity.writeCrtToString(crt));
			File caCrtFile = writeToFile("ca", "crt", OadrPKISecurity.writeCrtToString(caCert));
			File keyFile = writeToFile(venCN, "key", OadrPKISecurity.writeKeyToString(venCred));
			File fingerprintFile = writeToFile(venCN, "fingerprint", fingerprint);

			return new OadrUserX509Credential(fingerprint, caCrtFile, crtFile, keyFile, fingerprintFile);
		} catch (NoSuchAlgorithmException e) {
			throw new OadrSecurityException(e);
		} catch (OperatorCreationException e) {
			throw new OadrSecurityException(e);
		} catch (CertificateException e) {
			throw new OadrSecurityException(e);
		} catch (NoSuchProviderException e) {
			throw new OadrSecurityException(e);
		} catch (IOException e) {
			throw new OadrSecurityException(e);
		} catch (OadrSecurityException e) {
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
			throws IOException, CertificateEncodingException {
		return writePemToString("CERTIFICATE", certificate.getEncoded());
	}

	private static String writeKeyToString(KeyPair pair) throws IOException, CertificateEncodingException {
		return writePemToString("PRIVATE KEY", pair.getPrivate().getEncoded());
	}

	private static String writePemToString(String type, byte[] content) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (PemWriter pemWriter = new PemWriter(new OutputStreamWriter(outputStream))) {
			pemWriter.writeObject(new PemObject(type, content));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return new String(outputStream.toByteArray());
	}

	private static File writeToFile(String fileName, String fileExtension, String content) throws IOException {
		Path path = Files.createTempFile(fileName + "-", "." + fileExtension);
		File file = path.toFile();

		if (file.exists()) {
			boolean delete = file.delete();
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

	private static PKCS10CertificationRequest generateCsr(KeyPair pair, String x509PrincipalName, String algo) {
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				new X500Principal(x509PrincipalName), pair.getPublic());
		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(algo);
		ContentSigner signer = null;
		try {
			signer = csBuilder.build(pair.getPrivate());
		} catch (OperatorCreationException e) {
			throw new RuntimeException(e);
		}
		return p10Builder.build(signer);
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
				pemReader.close();
			}
		}

	}

}
