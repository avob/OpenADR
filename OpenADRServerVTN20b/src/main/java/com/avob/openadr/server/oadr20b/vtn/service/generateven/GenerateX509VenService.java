package com.avob.openadr.server.oadr20b.vtn.service.generateven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

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
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.controller.GenerateX509VenDto;
import com.avob.openadr.server.oadr20b.vtn.exception.service.GenerateX509VenException;
import com.google.common.collect.Lists;

@ConditionalOnProperty(value = VtnConfig.CA_KEY_CONF)
@Service
public class GenerateX509VenService {

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private VenService venService;

	private KeyPair caKeyPair = null;
	private X509Certificate caCert = null;

	private File writeToFile(String fileName, String content) throws IOException {
		File file = new File(fileName);
		if(file.exists()) {
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
			caCert = OadrHttpSecurity.parseCertificate(vtnConfig.getCertKey());
			caKeyPair = new KeyPair(caCert.getPublicKey(), caKey);
		}
		return caKeyPair;
	}

	public File generateCredentials(GenerateX509VenDto dto) throws GenerateX509VenException {
		try {

			long now = System.currentTimeMillis();
			String venCN = dto.getvenCN();
			String venName = dto.getVenName();
			BigInteger serialNumber = BigInteger.valueOf(now);

			String algo = null;
			KeyPair venCred = null;
			if (dto.getType().toLowerCase().equals("ecc")) {
				algo = "SHA256withDSA";
				venCred = OadrHttpSecurity.generateEccKeyPair();
			} else if (dto.getType().toLowerCase().equals("rsa")) {
				algo = "SHA256withRSA";
				venCred = OadrHttpSecurity.generateRsaKeyPair();
			}
			KeyPair loadCaKeyPair = loadCaKeyPair();
			PKCS10CertificationRequest csr = OadrHttpSecurity.generateCsr(venCred, venCN, algo);
			X509Certificate crt = OadrHttpSecurity.signCsr(csr, loadCaKeyPair, caCert, serialNumber);
			String fingerprint = OadrHttpSecurity.getOadr20bFingerprint(crt);

			File csrFile = writeToFile(venName + ".csr", OadrHttpSecurity.writeCsrToString(csr));
			File crtFile = writeToFile(venName + ".crt", OadrHttpSecurity.writeCrtToString(crt));
			File caCrtFile = writeToFile("ca.crt", OadrHttpSecurity.writeCrtToString(caCert));
			File keyPairFile = writeToFile(venName + ".key", OadrHttpSecurity.writeKeyToString(venCred));
			File fingerprintFile = writeToFile(venName + ".fingerprint", fingerprint);

			Collection<File> filesToArchive = Lists.newArrayList(csrFile, crtFile, keyPairFile, fingerprintFile, caCrtFile);
			String archiveName = now + "-" + venName + "-credentials.tar.gz";
			File outFile = new File(archiveName);
			FileOutputStream out = new FileOutputStream(outFile);
			ArchiveOutputStream o = new TarArchiveOutputStream(out);

			for (File f : filesToArchive) {
				// maybe skip directories for formats like AR that don't store directories
				ArchiveEntry entry = o.createArchiveEntry(f, f.getName());
				// potentially add more flags to entry
				o.putArchiveEntry(entry);
				if (f.isFile()) {
					try (InputStream i = new FileInputStream(f.getAbsolutePath())) {
						IOUtils.copy(i, o);
					}
				}
				o.closeArchiveEntry();
			}
			o.finish();
			out.close();

			Ven prepare = venService.prepare(fingerprint);
			venService.save(prepare);

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
