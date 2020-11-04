package com.avob.openadr.server.oadr20b.vtn.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.pyld.EiRequestEvent;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppListener;

public class OadrMockVen {

	private VenDto ven;
	private UserRequestPostProcessor authSession;
	private OadrMockEiHttpMvc mockService;
	private XmlSignatureService xmlSignatureService;
	private Oadr20bJAXBContext jaxbcontext = null;
	private OadrMockEiXmpp oadrMockEiXmpp;

	public OadrMockVen(VenDto ven, UserRequestPostProcessor authSession, OadrMockEiHttpMvc mockService,
			OadrMockEiXmpp oadrMockEiXmpp, XmlSignatureService xmlSignatureService) throws JAXBException {
		this.ven = ven;
		this.authSession = authSession;
		this.mockService = mockService;
		this.oadrMockEiXmpp = oadrMockEiXmpp;
		this.xmlSignatureService = xmlSignatureService;
		jaxbcontext = Oadr20bJAXBContext.getInstance();
	}

	public String getVenId() {
		return ven.getUsername();
	}

	public VenDto getVen() {
		return ven;
	}

	public <T> T httpCall(String endpoint, Object payload, int status, Class<T> klass) throws Exception {
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			String postEiRegisterPartyAndExpect = mockService.postEiAndExpect(endpoint, authSession, sign, status,
					String.class);
			OadrPayload unmarshal = jaxbcontext.unmarshal(postEiRegisterPartyAndExpect, OadrPayload.class);
			xmlSignatureService.validate(postEiRegisterPartyAndExpect, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			return mockService.postEiAndExpect(endpoint, authSession, payload, status, klass);
		}
	}

	public <T> T xmppCall(XmppListener listener, Object payload, Class<T> klass) throws Exception {
		Message msg = new Message();

		EntityFullJid entityFullFrom = JidCreate.entityFullFrom(ven.getPushUrl());
		msg.setFrom(entityFullFrom);
		if (ven.getXmlSignature()) {
			String sign = xmlSignatureService.sign(payload);
			msg.setBody(sign);

			listener.processStanza(msg);

			Optional<InvocationOnMock> popResponse = oadrMockEiXmpp.popResponse();
			if (!popResponse.isPresent()) {
				fail("VEN supposed to have some response from VTN");
				return null;
			}
			InvocationOnMock invocationOnMock = popResponse.get();
			Jid from = (Jid) invocationOnMock.getArgument(0);
			String body = (String) invocationOnMock.getArgument(1);
			// TODO bertrand: test how jid is manipulated between ven - xmpp - vtn
			assertNotNull(from);
			//assertEquals(entityFullFrom, from);
			OadrPayload unmarshal = jaxbcontext.unmarshal(body, OadrPayload.class);
			xmlSignatureService.validate(body, unmarshal);
			return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
		} else {
			String content = jaxbcontext.marshalRoot(payload);
			msg.setBody(content);
			listener.processStanza(msg);

			Optional<InvocationOnMock> popResponse = oadrMockEiXmpp.popResponse();
			if (!popResponse.isPresent()) {
				fail("VEN supposed to have some response from VTN");
				return null;
			}
			InvocationOnMock invocationOnMock = popResponse.get();
			Jid from = (Jid) invocationOnMock.getArgument(0);
			String body = (String) invocationOnMock.getArgument(1);
			// TODO bertrand: test how jid is manipulated between ven - xmpp - vtn
			assertNotNull(from);
			//assertEquals(entityFullFrom, from);
			Object unmarshal = jaxbcontext.unmarshal(body);
			if (!klass.equals(unmarshal.getClass())) {
				System.out.println(body);
			}
			assertEquals(klass, unmarshal.getClass());
			return klass.cast(unmarshal);
		}
	}

	public <T> T register(Object payload, int status, Class<T> klass) throws Exception {
		String endpoint = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_REGISTER_PARTY_SERVICE;
		if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport())) {
			return httpCall(endpoint, payload, status, klass);
		} else if (OadrTransportType.XMPP.value().equals(ven.getTransport())) {
			return xmppCall(oadrMockEiXmpp.getXmppEiRegisterPartyListener(), payload, klass);
		}
		fail("venId: " + ven.getUsername() + " has unknown transport: " + ven.getTransport());
		return null;

	}

	public <T> T report(Object payload, int status, Class<T> klass) throws Exception {
		String endpoint = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_REPORT_SERVICE;
		if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport())) {
			return httpCall(endpoint, payload, status, klass);
		} else if (OadrTransportType.XMPP.value().equals(ven.getTransport())) {
			return xmppCall(oadrMockEiXmpp.getXmppEiReportListener(), payload, klass);
		}
		fail("venId: " + ven.getUsername() + " has unknown transport: " + ven.getTransport());
		return null;
	}

	public <T> T event(Object payload, int status, Class<T> klass) throws Exception {
		String endpoint = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE;
		if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport())) {
			return httpCall(endpoint, payload, status, klass);
		} else if (OadrTransportType.XMPP.value().equals(ven.getTransport())) {
			return xmppCall(oadrMockEiXmpp.getXmppEiEventListener(), payload, klass);
		}
		fail("venId: " + ven.getUsername() + " has unknown transport: " + ven.getTransport());
		return null;
	}

	public <T> T requestEvent(Class<T> klass) throws Exception {
		OadrRequestEventType oadrRequestEventType = new OadrRequestEventType();
		EiRequestEvent eiRequestEvent = new EiRequestEvent();
		eiRequestEvent.setRequestID("0");
		eiRequestEvent.setVenID(this.getVenId());
		oadrRequestEventType.setEiRequestEvent(eiRequestEvent);
		return this.event(oadrRequestEventType, HttpStatus.OK_200, klass);
	}

	public <T> T opt(Object payload, int status, Class<T> klass) throws Exception {
		String endpoint = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_OPT_SERVICE;
		if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport())) {
			return httpCall(endpoint, payload, status, klass);
		} else if (OadrTransportType.XMPP.value().equals(ven.getTransport())) {
			return xmppCall(oadrMockEiXmpp.getXmppEiOptListener(), payload, klass);
		}
		fail("venId: " + ven.getUsername() + " has unknown transport: " + ven.getTransport());
		return null;
	}

	public <T> T poll(Object payload, int status, Class<T> klass) throws Exception {
		String endpoint = Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.OADR_POLL_SERVICE;
		if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport()) && ven.getHttpPullModel()) {
			return httpCall(endpoint, payload, status, klass);
		} else if (OadrTransportType.SIMPLE_HTTP.value().equals(ven.getTransport()) && !ven.getHttpPullModel()) {
			Optional<InvocationOnMock> popResponse = mockService.popResponse();
			if (klass == null && popResponse.isPresent()) {
				fail("VEN is not supposed to have some response from VTN");
				return null;
			} else if (klass == null && !popResponse.isPresent()) {
				return null;
			}
			if (!popResponse.isPresent()) {
				fail("VEN supposed to have some response from VTN");
				return null;
			}
			InvocationOnMock invocationOnMock = popResponse.get();
			String from = (String) invocationOnMock.getArgument(0);
			assertEquals(this.getVen().getPushUrl(), from);
			assertEquals(klass, invocationOnMock.getArgument(1).getClass());
			return klass.cast(invocationOnMock.getArgument(1));
		} else if (OadrTransportType.XMPP.value().equals(ven.getTransport())) {
			Optional<InvocationOnMock> popResponse = oadrMockEiXmpp.popResponse();
			if (klass == null && popResponse.isPresent()) {
				fail("VEN is not supposed to have some response from VTN");
				return null;
			} else if (klass == null && !popResponse.isPresent()) {
				return null;
			}
			if (!popResponse.isPresent()) {
				fail("VEN supposed to have some response from VTN");
				return null;
			}
			InvocationOnMock invocationOnMock = popResponse.get();
			Jid from = (Jid) invocationOnMock.getArgument(0);
			String body = (String) invocationOnMock.getArgument(1);
			assertEquals(JidCreate.entityFullFrom(this.getVen().getPushUrl()), from);
			if (ven.getXmlSignature()) {
				OadrPayload unmarshal = jaxbcontext.unmarshal(body, OadrPayload.class);
				xmlSignatureService.validate(body, unmarshal);
				return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, klass);
			} else {
				Object unmarshal = jaxbcontext.unmarshal(body);
				assertEquals(klass, unmarshal.getClass());
				return klass.cast(unmarshal);
			}

		}
		fail("venId: " + ven.getUsername() + " has unknown transport: " + ven.getTransport());
		return null;
	}

	public <T> T poll(int status, Class<T> klass) throws Exception {
		Object payload = Oadr20bPollBuilders.newOadr20bPollBuilder(ven.getUsername()).build();
		return this.poll(payload, status, klass);
	}

	public OadrDistributeEventType pollForValidOadrDistributeEvent() throws Exception {
		OadrDistributeEventType poll = this.poll(HttpStatus.OK_200, OadrDistributeEventType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), poll.getEiResponse().getResponseCode());
		return poll;
	}

	public void pollForEmpty() throws Exception {
		if (OadrTransportType.SIMPLE_HTTP.value().equals(this.getVen().getTransport()) && ven.getHttpPullModel()) {
			OadrResponseType poll = this.poll(HttpStatus.OK_200, OadrResponseType.class);
			assertEquals(String.valueOf(HttpStatus.OK_200), poll.getEiResponse().getResponseCode());
		} else if (OadrTransportType.SIMPLE_HTTP.value().equals(this.getVen().getTransport())
				&& !ven.getHttpPullModel()) {
			this.poll(HttpStatus.OK_200, null);
		} else if (OadrTransportType.XMPP.value().equals(this.getVen().getTransport())) {
			this.poll(HttpStatus.OK_200, null);
		}
	}

}
