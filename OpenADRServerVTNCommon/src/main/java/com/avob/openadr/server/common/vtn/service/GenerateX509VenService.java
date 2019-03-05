package com.avob.openadr.server.common.vtn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.ven.VenCreateDto;
import com.google.common.collect.Maps;

@ConditionalOnProperty(value = VtnConfig.CA_KEY_CONF)
@Service
public class GenerateX509VenService {

	@Resource
	private VtnConfig vtnConfig;

	private KeyPair caKeyPair = null;
	private X509Certificate caCert = null;

	private File writeToFile(String fileName, String fileExtension, String content) throws IOException {
		Path path = Files.createTempFile(fileName + "-", "." + fileExtension);
		File file = path.toFile();

		if (file.exists()) {
			file.delete();
		}
		FileOutputStream outputStream = new FileOutputStream(file, true);
		byte[] strToBytes = content.getBytes();
		outputStream.write(strToBytes);

		outputStream.close();

		return file;
	}

	private KeyPair loadCaKeyPair() throws OadrSecurityException {
		if (caKeyPair == null) {
			PrivateKey caKey = OadrHttpSecurity.parsePrivateKey(vtnConfig.getCaKey());
			caCert = OadrHttpSecurity.parseCertificate(vtnConfig.getCaCert());
			caKeyPair = new KeyPair(caCert.getPublicKey(), caKey);
		}
		return caKeyPair;
	}

	public File generateCredentials(VenCreateDto dto, Ven ven) throws GenerateX509VenException {
		try {

			long now = System.currentTimeMillis();
			String venCN = dto.getCommonName();
			BigInteger serialNumber = BigInteger.valueOf(now);

			String algoCsr = null;
			KeyPair venCred = null;
			if (dto.getNeedCertificateGeneration().toLowerCase().equals("ecc")) {
				algoCsr = "SHA256withDSA";
				venCred = OadrHttpSecurity.generateEccKeyPair();
			} else if (dto.getNeedCertificateGeneration().toLowerCase().equals("rsa")) {
				algoCsr = "SHA256withRSA";
				venCred = OadrHttpSecurity.generateRsaKeyPair();
			}
			KeyPair loadCaKeyPair = loadCaKeyPair();
			PKCS10CertificationRequest csr = OadrHttpSecurity.generateCsr(venCred, venCN, algoCsr);
			X509Certificate crt = OadrHttpSecurity.signCsr(csr, loadCaKeyPair, caCert, serialNumber);

			String fingerprint = OadrHttpSecurity.getOadr20bFingerprint(crt);
			ven.setUsername(fingerprint);

			File crtFile = writeToFile(venCN, "crt", OadrHttpSecurity.writeCrtToString(crt));
			File caCrtFile = writeToFile("ca", "crt", OadrHttpSecurity.writeCrtToString(caCert));
			File keyPairFile = writeToFile(venCN, "key", OadrHttpSecurity.writeKeyToString(venCred));
			File fingerprintFile = writeToFile(venCN, "fingerprint", fingerprint);

			HashMap<String, File> fileMap = Maps.newHashMap();
			fileMap.put(venCN + ".crt", crtFile);
			fileMap.put("ca.crt", caCrtFile);
			fileMap.put(venCN + ".key", keyPairFile);
			fileMap.put(venCN + ".fingerprint", fingerprintFile);
			String archiveName = now + "-" + venCN + "-credentials.tar.gz";
			Path path = Files.createTempFile(archiveName, "");
			File outFile = path.toFile();
			FileOutputStream out = new FileOutputStream(outFile);
			ArchiveOutputStream o = new TarArchiveOutputStream(out);
			for (Entry<String, File> fileEntry : fileMap.entrySet()) {

				ArchiveEntry entry = o.createArchiveEntry(fileEntry.getValue(), fileEntry.getKey());
				o.putArchiveEntry(entry);
				if (fileEntry.getValue().isFile()) {
					try (InputStream i = new FileInputStream(fileEntry.getValue().getAbsolutePath())) {
						IOUtils.copy(i, o);
					}
				}
				o.closeArchiveEntry();
			}
			o.finish();
			out.close();

			return outFile;

		} catch (NoSuchAlgorithmException e) {
			throw new GenerateX509VenException(e);
		} catch (OadrSecurityException e) {
			throw new GenerateX509VenException(e);
		} catch (OperatorCreationException e) {
			throw new GenerateX509VenException(e);
		} catch (CertificateException e) {
			throw new GenerateX509VenException(e);
		} catch (NoSuchProviderException e) {
			throw new GenerateX509VenException(e);
		} catch (IOException e) {
			throw new GenerateX509VenException(e);
		}
	}
}
