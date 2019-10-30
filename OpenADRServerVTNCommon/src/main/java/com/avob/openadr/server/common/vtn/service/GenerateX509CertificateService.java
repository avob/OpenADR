package com.avob.openadr.server.common.vtn.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.avob.openadr.security.OadrPKIAlgorithm;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.OadrUserX509Credential;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.exception.GenerateX509VenException;
import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.user.AbstractUserCreateDto;
import com.google.common.collect.Maps;

/**
 * Service to generate VEN/User/App PKI credentials if valid CA key/cert
 * provided
 * 
 * @author bzanni
 *
 */
@ConditionalOnProperty(value = VtnConfig.CA_KEY_CONF)
@Service
public class GenerateX509CertificateService {

	@Resource
	private VtnConfig vtnConfig;

	private KeyPair caKeyPair = null;
	private X509Certificate caCert = null;

	@PostConstruct
	public void init() throws OadrSecurityException {
		PrivateKey caKey = OadrPKISecurity.parsePrivateKey(vtnConfig.getCaKey());
		caCert = OadrPKISecurity.parseCertificate(vtnConfig.getCaCert());
		caKeyPair = new KeyPair(caCert.getPublicKey(), caKey);
	}

	public File generateCredentials(AbstractUserCreateDto dto, AbstractUser abstractUser)
			throws GenerateX509VenException {
		long now = System.currentTimeMillis();
		String commonName = dto.getCommonName();
		try {
			OadrPKIAlgorithm algo = null;
			if (dto.getNeedCertificateGeneration().equalsIgnoreCase("ecc")) {
				algo = OadrPKIAlgorithm.SHA256_DSA;
			} else if (dto.getNeedCertificateGeneration().equalsIgnoreCase("rsa")) {
				algo = OadrPKIAlgorithm.SHA256_RSA;
			}
			OadrUserX509Credential generateCredentials = OadrPKISecurity.generateCredentials(caKeyPair, caCert,
					commonName, algo);

			abstractUser.setUsername(generateCredentials.getFingerprint());

			HashMap<String, File> fileMap = Maps.newHashMap();
			fileMap.put(commonName + ".crt", generateCredentials.getCertificateFile());
			fileMap.put("ca.crt", generateCredentials.getCaCertificateFile());
			fileMap.put(commonName + ".key", generateCredentials.getPrivateKeyFile());
			fileMap.put(commonName + ".fingerprint", generateCredentials.getFingerprintFile());
			String archiveName = now + "-" + commonName + "-credentials.tar.gz";
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

		} catch (OadrSecurityException | IOException e) {
			throw new GenerateX509VenException(e);
		}

	}

}
