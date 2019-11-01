package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBException;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

public class OadrMockVen {

	private Ven ven;
	private UserRequestPostProcessor authSession;
	private OadrMockEiHttpMvc mockService;
	private XmlSignatureService xmlSignatureService;
	private Oadr20bJAXBContext jaxbcontext = null;

	public OadrMockVen(Ven ven, UserRequestPostProcessor authSession, OadrMockEiHttpMvc mockService,
			XmlSignatureService xmlSignatureService) throws JAXBException {
		this.ven = ven;
		this.authSession = authSession;
		this.mockService = mockService;
		this.xmlSignatureService = xmlSignatureService;
		jaxbcontext = Oadr20bJAXBContext.getInstance();
	}

	public <T> T register(Object payload, int status, Class<T> klass) throws Exception {
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postEiRegisterPartyAndExpect(authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			return mockService.postEiRegisterPartyAndExpect(authSession, payload, status, klass);
		}
	}

	public <T> T report(Object payload, int status, Class<T> klass) throws Exception {
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postEiReportAndExpect(authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			return mockService.postEiReportAndExpect(authSession, payload, status, klass);
		}
	}

	public <T> T event(Object payload, int status, Class<T> klass) throws Exception {
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postEiEventAndExpect(authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			return mockService.postEiEventAndExpect(authSession, payload, status, klass);
		}
	}

	public <T> T opt(Object payload, int status, Class<T> klass) throws Exception {
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postEiOptAndExpect(authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			return mockService.postEiOptAndExpect(authSession, payload, status, klass);
		}
	}

	public <T> T poll(int status, Class<T> klass) throws Exception {
		Object payload = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postOadrPollAndExpect(authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			T signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
			assertNotNull(signedObjectFromOadrPayload);
			return signedObjectFromOadrPayload;
		} else {
			T postOadrPollAndExpect = mockService.postOadrPollAndExpect(authSession, payload, status, klass);
			assertNotNull(postOadrPollAndExpect);
			return postOadrPollAndExpect;
		}
	}

}
