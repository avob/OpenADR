package com.avob.openadr.server.oadr20b.ven.service;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.xmlsignature.OadrXMLSignatureHandler;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class XmlSignatureService {

	private Map<String, PrivateKey> cachedKey = new HashMap<>();
	private Map<String, X509Certificate> cachedCert = new HashMap<>();

	private X509Certificate loadCert(String path) throws OadrSecurityException {
		X509Certificate cert = null;
		if (!cachedCert.containsKey(path)) {
			cert = OadrPKISecurity.parseCertificate(path);
			cachedCert.put(path, cert);
		} else {
			cert = cachedCert.get(path);
		}
		return cert;
	}

	private PrivateKey loadKey(String path) throws OadrSecurityException {
		PrivateKey cert = null;
		if (!cachedKey.containsKey(path)) {
			cert = OadrPKISecurity.parsePrivateKey(path);
			cachedKey.put(path, cert);
		} else {
			cert = cachedKey.get(path);
		}
		return cert;
	}

	public String sign(Object object, VtnSessionConfiguration multiConfig)
			throws Oadr20bXMLSignatureException, OadrSecurityException {
		PrivateKey loadKey = loadKey(multiConfig.getVenPrivateKeyPath());
		X509Certificate loadCert = loadCert(multiConfig.getVenCertificatePath());
		String nonce = UUID.randomUUID().toString();
		Long createdtimestamp = System.currentTimeMillis();
		return OadrXMLSignatureHandler.sign(object, loadKey, loadCert, nonce, createdtimestamp);
	}

	public void validate(String raw, OadrPayload payload, VtnSessionConfiguration multiConfig)
			throws Oadr20bXMLSignatureValidationException {
		long nowDate = System.currentTimeMillis();
		OadrXMLSignatureHandler.validate(raw, payload, nowDate,
				multiConfig.getReplayProtectAcceptedDelaySecond() * 1000L);
	}

}
