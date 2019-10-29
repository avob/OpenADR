package com.avob.openadr.server.oadr20b.vtn.service;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.xmlsignature.OadrXMLSignatureHandler;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.common.vtn.VtnConfig;

@Service
public class XmlSignatureService {

	@Resource
	private VtnConfig vtnConfig;

	private PrivateKey parsePrivateKey;

	private X509Certificate parseCertificate;

	@PostConstruct
	public void init() throws OadrSecurityException {
		parsePrivateKey = OadrPKISecurity.parsePrivateKey(vtnConfig.getKey());
		parseCertificate = OadrPKISecurity.parseCertificate(vtnConfig.getCert());
	}

	public String sign(Object object) throws Oadr20bXMLSignatureException {
		String nonce = UUID.randomUUID().toString();
		Long createdtimestamp = System.currentTimeMillis();
		return OadrXMLSignatureHandler.sign(object, parsePrivateKey, parseCertificate, nonce, createdtimestamp);
	}

	public void validate(String raw, OadrPayload payload) throws Oadr20bXMLSignatureValidationException {
		long nowDate = System.currentTimeMillis();
		OadrXMLSignatureHandler.validate(raw, payload, nowDate,
				vtnConfig.getReplayProtectAcceptedDelaySecond() * 1000L);
	}
}
