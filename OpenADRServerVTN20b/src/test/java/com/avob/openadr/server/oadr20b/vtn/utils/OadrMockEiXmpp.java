package com.avob.openadr.server.oadr20b.vtn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jxmpp.jid.Jid;
import org.jxmpp.stringprep.XmppStringprepException;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNPayloadService;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppConnector;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppListener;

@Service
public class OadrMockEiXmpp {

	private static final Logger LOGGER = LoggerFactory.getLogger(OadrMockEiXmpp.class);

	@Resource
	private XmppConnector xmppConnector;

	@Resource
	private Oadr20bVTNPayloadService oadr20bVTNPayloadService;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	private XmppListener xmppEiRegisterPartyListener;
	private XmppListener xmppEiReportListener;
	private XmppListener xmppEiEventListener;
	private XmppListener xmppEiOptListener;

	private List<InvocationOnMock> response = new ArrayList<>();

	private Oadr20bJAXBContext jaxbContext;

	public Object unmarshal(String payload) throws Oadr20bUnmarshalException {
		return jaxbContext.unmarshal(payload);
	}

	public <T extends JAXBElement<?>> String marshal(T payload) throws Oadr20bMarshalException {
		return jaxbContext.marshal(payload);
	}

	@PostConstruct
	public void init() throws JAXBException {

		jaxbContext = Oadr20bJAXBContext.getInstance();

		OadrXmppClient20b mockUplinkClient = Mockito.mock(OadrXmppClient20b.class);
		xmppConnector.setXmppUplinkClient(mockUplinkClient);

		try {
			Mockito.doAnswer((Answer<?>) invocation -> {
				response.add(0, invocation);
//				response.add(invocation);
				return null;
			}).when(mockUplinkClient).sendMessage(Mockito.any(Jid.class), Mockito.any(String.class));
		} catch (Oadr20bMarshalException | NotConnectedException | XmppStringprepException e) {
			LOGGER.error("", e);
		} catch (InterruptedException e) {
			LOGGER.error("", e);
			Thread.currentThread().interrupt();
		}

		xmppEiRegisterPartyListener = new XmppListener(oadr20bVTNEiRegisterPartyService, mockUplinkClient,
				oadr20bVTNPayloadService);
		xmppEiReportListener = new XmppListener(oadr20bVTNEiReportService, mockUplinkClient, oadr20bVTNPayloadService);
		xmppEiEventListener = new XmppListener(oadr20bVTNEiEventService, mockUplinkClient, oadr20bVTNPayloadService);
		xmppEiOptListener = new XmppListener(oadr20bVTNEiOptService, mockUplinkClient, oadr20bVTNPayloadService);
	}

	public Optional<InvocationOnMock> popResponse() {
		if (response.isEmpty()) {
			return Optional.empty();
		}
		Optional<InvocationOnMock> of = Optional.of(response.get(0));
		response.remove(0);
		return of;
	}

	public XmppListener getXmppEiRegisterPartyListener() {
		return xmppEiRegisterPartyListener;
	}

	public XmppListener getXmppEiReportListener() {
		return xmppEiReportListener;
	}

	public XmppListener getXmppEiEventListener() {
		return xmppEiEventListener;
	}

	public XmppListener getXmppEiOptListener() {
		return xmppEiOptListener;
	}

}
