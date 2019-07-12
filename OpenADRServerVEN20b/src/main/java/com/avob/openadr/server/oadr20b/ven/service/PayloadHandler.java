package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class PayloadHandler {

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	public String payloadToString(VtnSessionConfiguration multiConfig, Object payload, boolean signed)
			throws Oadr20bXMLSignatureException, Oadr20bMarshalException, OadrSecurityException {
		if (signed) {
			return xmlSignatureService.sign(payload, multiConfig);
		} else {
			return jaxbContext.marshalRoot(payload);
		}
	}

	public Object stringToObject(String payload) throws Oadr20bUnmarshalException {
		return jaxbContext.unmarshal(payload);
	}

	public void validate(VtnSessionConfiguration multiConfig, String raw, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException {
		xmlSignatureService.validate(raw, oadrPayload, multiConfig);
	}

}
