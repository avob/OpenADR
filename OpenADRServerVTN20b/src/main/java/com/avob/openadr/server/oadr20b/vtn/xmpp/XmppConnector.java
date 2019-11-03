package com.avob.openadr.server.oadr20b.vtn.xmpp;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiService;

@Service
public class XmppConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppConnector.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	private OadrXmppClient20b xmppUplinkClient;

	private OadrXmppClient20b xmppEiReportClient;
	private OadrXmppClient20b xmppEiRegisterPartyClient;
	private OadrXmppClient20b xmppEiOptClient;
	private OadrXmppClient20b xmppEiEventClient;

	private XMPPTCPConnection getXmppConnection(String domain, Oadr20bVTNEiService service) throws OadrXmppException {
		String resource = (service != null) ? service.getServiceName() : "uplink";
		SSLContext sslContext = vtnConfig.getSslContext();
		String host = vtnConfig.getXmppHost();
		int port = vtnConfig.getXmppPort();

		XMPPTCPConnectionConfiguration anonymousConnection = OadrXmppClient20b.anonymousConnection(host, port, domain,
				resource, sslContext);
		return new XMPPTCPConnection(anonymousConnection);
	}

	private OadrXmppClient20b getXmppClient(String domain, Oadr20bVTNEiService service, OadrXmppClient20b uplinkClient)
			throws OadrXmppException {
		String resource = (service != null) ? service.getServiceName() : "uplink";
		XmppListener xmppListener = new XmppListener(service, uplinkClient);
		XMPPTCPConnection xmppConnection = getXmppConnection(domain, service);
		String jid = vtnConfig.getVtnId() + "@" + domain + "/" + resource;
		return new OadrXmppClient20b(jid, xmppConnection, domain, xmppListener);
	}

	private OadrXmppClient20b getXmppUplinkClient(String domain) throws OadrXmppException {
		XMPPTCPConnection xmppConnection = getXmppConnection(domain, null);
		String jid = vtnConfig.getVtnId() + "@" + domain + "/uplink";
		return new OadrXmppClient20b(jid, xmppConnection, domain, null);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws OadrXmppException {

		if (vtnConfig.getXmppHost() != null && vtnConfig.getXmppPort() != null) {

			String domain = (vtnConfig.getXmppDomain() != null) ? vtnConfig.getXmppDomain() : vtnConfig.getXmppHost();

			setXmppUplinkClient(getXmppUplinkClient(domain));

			LOGGER.info("Xmpp VTN uplink client successfully initialized");

			setXmppEiReportClient(getXmppClient(domain, oadr20bVTNEiReportService, getXmppUplinkClient()));
			setXmppEiRegisterPartyClient(
					getXmppClient(domain, oadr20bVTNEiRegisterPartyService, getXmppUplinkClient()));
			setXmppEiOptClient(getXmppClient(domain, oadr20bVTNEiOptService, getXmppUplinkClient()));
			setXmppEiEventClient(getXmppClient(domain, oadr20bVTNEiEventService, getXmppUplinkClient()));

			LOGGER.info("Xmpp VTN connectors successfully initialized");

		}

	}

	public OadrXmppClient20b getXmppEiReportClient() {
		return xmppEiReportClient;
	}

	public void setXmppEiReportClient(OadrXmppClient20b xmppEiReportClient) {
		this.xmppEiReportClient = xmppEiReportClient;
	}

	public OadrXmppClient20b getXmppEiRegisterPartyClient() {
		return xmppEiRegisterPartyClient;
	}

	public void setXmppEiRegisterPartyClient(OadrXmppClient20b xmppEiRegisterPartyClient) {
		this.xmppEiRegisterPartyClient = xmppEiRegisterPartyClient;
	}

	public OadrXmppClient20b getXmppEiOptClient() {
		return xmppEiOptClient;
	}

	public void setXmppEiOptClient(OadrXmppClient20b xmppEiOptClient) {
		this.xmppEiOptClient = xmppEiOptClient;
	}

	public OadrXmppClient20b getXmppEiEventClient() {
		return xmppEiEventClient;
	}

	public void setXmppEiEventClient(OadrXmppClient20b xmppEiEventClient) {
		this.xmppEiEventClient = xmppEiEventClient;
	}

	public OadrXmppClient20b getXmppUplinkClient() {
		return xmppUplinkClient;
	}

	public void setXmppUplinkClient(OadrXmppClient20b xmppUplinkClient) {
		this.xmppUplinkClient = xmppUplinkClient;
	}

}
