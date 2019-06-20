package com.avob.openadr.client.xmpp.oadr20b;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.sasl.core.SASLAnonymous;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Feature;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;

public class OadrXmppClient20b {

	public static final String OADR_NAMESPACE = "http://openadr.org/openadr2";

	public static final String OADR_SERVICES_NAMESPACE = OADR_NAMESPACE + "#services";

	public static final String OADR_EVENT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiEvent";

	public static final String OADR_REPORT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiReport";

	public static final String OADR_REGISTERPARTY_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiRegisterParty";

	public static final String XMPP_OADR_SUBDOMAIN = "xmpp";

	private XMPPTCPConnection connection;

//	private Oadr20bJAXBContext jaxbContext;

	private DomainBareJid domainJid;
	private EntityFullJid clientJid;

	private ChatManager chatManager;

	private Map<String, Jid> discoveredXmppOadrServices;

	public OadrXmppClient20b(String fingerprint, String host, int port, String resource, SSLContext context,
			StanzaListener onMessageListener) throws OadrXmppException {

		try {
//			this.jaxbContext = Oadr20bJAXBContext.getInstance();
			SASLAnonymous mechanism = new SASLAnonymous();
			SASLAuthentication.registerSASLMechanism(mechanism);
			EntityBareJid authzid = JidCreate.entityBareFrom(resource + "@" + host);
			XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//					.setHostAddress(address)
					.setHost(host).setPort(port)
//					.allowEmptyOrNullUsernames()
//					.setUsernameAndPassword(resource,resource)
//					.setAuthzid(authzid)
//					.setAuthzid(authzid)
					.performSaslAnonymousAuthentication()
//					.addEnabledSaslMechanism("ANONYMOUS")
//					.performSaslExternalAuthentication(context)
//					.setUsernameAndPassword("admin", "mouaiccool")
//					.addEnabledSaslMechanism("EXTERNAL")
//					.addEnabledSaslMechanism("ANONYMOUS")
					.setResource(resource).setXmppDomain(host).setCustomSSLContext(context).build();

			connection = new XMPPTCPConnection(config);

			setDomainJid(JidCreate.domainBareFrom(XMPP_OADR_SUBDOMAIN + "." + host));
			setClientJid(JidCreate.entityFullFrom(fingerprint, XMPP_OADR_SUBDOMAIN + "." + host, resource));

			chatManager = ChatManager.getInstanceFor(connection);

			// Obtain the ServiceDiscoveryManager associated with my XMPP connection
//			ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);

//						ServiceDiscoveryManager.setDefaultIdentity(new Identity("client", "oadr"));
			// Register that a new feature is supported by this XMPP entity
//						discoManager.ad
//
//			connection.addAsyncStanzaListener(new StanzaListener() {
//				public void processStanza(Stanza stanza) throws SmackException.NotConnectedException,
//						InterruptedException, SmackException.NotLoggedInException {
//					IQ iq = (IQ) stanza;
//				}
//			}, StanzaTypeFilter.IQ);
//
//			connection.addAsyncStanzaListener(new StanzaListener() {
//				public void processStanza(Stanza stanza) throws SmackException.NotConnectedException,
//						InterruptedException, SmackException.NotLoggedInException {
//					Presence presence = (Presence) stanza;
//				}
//			}, StanzaTypeFilter.PRESENCE);
//
//			connection.addAsyncStanzaListener(new StanzaListener() {
//				public void processStanza(Stanza stanza) throws SmackException.NotConnectedException,
//						InterruptedException, SmackException.NotLoggedInException {
//					Message message = (Message) stanza;
//					String body = message.getBody();
//
//					try {
//						Object unmarshal = jaxbContext.unmarshal(body);
//					} catch (Oadr20bUnmarshalException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}, StanzaTypeFilter.MESSAGE);

			if (onMessageListener != null) {
				connection.addAsyncStanzaListener(onMessageListener, StanzaTypeFilter.MESSAGE);
			}
//			

			connection.connect().login(resource, resource); // Establishes a connection to the server
			if (connection.isConnected() && connection.isAuthenticated()) {

				IQ request = new Ping(authzid);
				connection.sendIqRequestAsync(request);

				Presence p = new Presence(Type.available);
				connection.sendStanza(p);

				boolean hasXmppOadrFeature = this.hasXmppOadrFeature();

				if (!hasXmppOadrFeature) {
					throw new OadrXmppException("Xmpp Server does not provide OpenADR feature");
				}

				discoveredXmppOadrServices = this.discoverXmppOadrServices();

			} else {
				throw new OadrXmppException("Connection refused by Xmpp server ");
			}

		}
//		catch (JAXBException e) {
//			throw new OadrXmppException(e);
//		} 
		catch (XmppStringprepException e) {
			throw new OadrXmppException(e);
		} catch (XMPPException e) {
			throw new OadrXmppException(e);
		} catch (SmackException e) {
			throw new OadrXmppException(e);
		} catch (IOException e) {
			throw new OadrXmppException(e);
		} catch (InterruptedException e) {
			throw new OadrXmppException(e);
		}

	}

	public void sendMessage(Jid jid, String payload)
			throws Oadr20bMarshalException, XmppStringprepException, NotConnectedException, InterruptedException {
		EntityBareJid entityBareFrom = JidCreate.entityBareFrom(jid);
		Chat chatWith = chatManager.chatWith(entityBareFrom);
		Message message = new Message();
		message.setFrom(getClientJid());
		message.setTo(jid);
		message.setBody(payload);
		chatWith.send(message);
	}

	public boolean hasXmppOadrFeature()
			throws NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException {
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		DiscoverInfo discoverInfo;
		discoverInfo = discoManager.discoverInfo(getDomainJid(), null);
		Iterator<Feature> it = discoverInfo.getFeatures().iterator();
		while (it.hasNext()) {
			DiscoverInfo.Feature identity = (DiscoverInfo.Feature) it.next();
			if (OADR_NAMESPACE.equals(identity.getVar())) {
				return true;
			}
		}
		return false;

	}

	public Map<String, Jid> discoverXmppOadrServices()
			throws NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException {
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		Map<String, Jid> discoveredServiceJid = new HashMap<>();
		DiscoverItems discoverItems = discoManager.discoverItems(getDomainJid(), OADR_SERVICES_NAMESPACE);

		for (Item item : discoverItems.getItems()) {
			discoveredServiceJid.put(item.getNode(), item.getEntityID());
		}

		return discoveredServiceJid;
	}

	public Map<String, Jid> getDiscoveredXmppOadrServices() {
		return discoveredXmppOadrServices;
	}

	public EntityFullJid getClientJid() {
		return clientJid;
	}

	private void setClientJid(EntityFullJid clientJid) {
		this.clientJid = clientJid;
	}

	public DomainBareJid getDomainJid() {
		return domainJid;
	}

	private void setDomainJid(DomainBareJid domainJid) {
		this.domainJid = domainJid;
	}

}
