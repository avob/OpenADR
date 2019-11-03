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
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppConnector;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppListener;

@Service
public class OadrMockEiXmpp {

	@Resource
	private XmppConnector xmppConnector;

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
				response.add(invocation);
				return null;
			}).when(mockUplinkClient).sendMessage(Mockito.any(Jid.class), Mockito.any(String.class));
		} catch (XmppStringprepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Oadr20bMarshalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xmppEiRegisterPartyListener = new XmppListener(oadr20bVTNEiRegisterPartyService, mockUplinkClient);
		xmppEiReportListener = new XmppListener(oadr20bVTNEiReportService, mockUplinkClient);
		xmppEiEventListener = new XmppListener(oadr20bVTNEiEventService, mockUplinkClient);
		xmppEiOptListener = new XmppListener(oadr20bVTNEiOptService, mockUplinkClient);
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