package com.avob.openadr.client.xmpp.oadr20b;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.ConnectionConfiguration;
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
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.sasl.core.SASLAnonymous;
import org.jivesoftware.smack.sasl.javax.SASLExternalMechanism;
import org.jivesoftware.smack.sasl.javax.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Feature;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;

public class OadrXmppClient20b {

	public static final String OADR_NAMESPACE = "http://openadr.org/openadr2";

	public static final String OADR_SERVICES_NAMESPACE = OADR_NAMESPACE + "#services";

	public static final String OADR_EVENT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiEvent";

	public static final String OADR_REPORT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiReport";

	public static final String OADR_OPT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiOpt";

	public static final String OADR_REGISTERPARTY_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiRegisterParty";

	public static final String XMPP_OADR_SUBDOMAIN = "xmpp";

	private XMPPTCPConnection connection;

	private DomainBareJid domainJid;

	private ChatManager chatManager;

	private Map<String, Jid> discoveredXmppOadrServices;

	public static XMPPTCPConnectionConfiguration anonymousConnection(String host, int port, String domain,
			String resource, SSLContext context) throws OadrXmppException {
		try {
			return XMPPTCPConnectionConfiguration.builder().setHost(host).setPort(port)
					.performSaslAnonymousAuthentication().setResource(resource).setXmppDomain(domain)
					.setCustomSSLContext(context).build();
		} catch (XmppStringprepException e) {
			throw new OadrXmppException(e);
		}
	}

	public static XMPPTCPConnectionConfiguration passwordConnection(String host, int port, String domain,
			String resource, SSLContext context, String username, String password) throws OadrXmppException {
		try {
			return XMPPTCPConnectionConfiguration.builder().setHost(host).setPort(port)
					.setUsernameAndPassword(username, password)
					.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false)
					.setResource(resource).setXmppDomain(domain).setCustomSSLContext(context).build();
		} catch (XmppStringprepException e) {
			throw new OadrXmppException(e);
		}
	}

	public OadrXmppClient20b(String userJid, XMPPTCPConnection connection, String domain,
			StanzaListener onMessageListener) throws OadrXmppException {
		this.connection = connection;

		try {
			SASLAnonymous mechanism = new SASLAnonymous();
			SASLExternalMechanism ext = new SASLExternalMechanism();
			SASLPlainMechanism plain = new SASLPlainMechanism();
			SASLAuthentication.registerSASLMechanism(mechanism);
			SASLAuthentication.registerSASLMechanism(ext);
			SASLAuthentication.registerSASLMechanism(plain);
			SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
			SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
			setDomainJid(JidCreate.domainBareFrom(XMPP_OADR_SUBDOMAIN + "." + domain));

			chatManager = ChatManager.getInstanceFor(connection);

			if (onMessageListener != null) {
				this.connection.addAsyncStanzaListener(onMessageListener, StanzaTypeFilter.MESSAGE);
			}

			this.connection.connect().login(); // Establishes a connection to the server
			if (this.connection.isConnected() && this.connection.isAuthenticated()) {

				

				Presence p = new Presence(Type.available);
				this.connection.sendStanza(p);

				boolean hasXmppOadrFeature = this.hasXmppOadrFeature();

				if (!hasXmppOadrFeature) {
					throw new OadrXmppException("Xmpp Server does not provide OpenADR feature");
				}

				discoveredXmppOadrServices = this.discoverXmppOadrServices();
								

			} else {
				throw new OadrXmppException("Connection refused by Xmpp server ");
			}

		} catch (XMPPException | SmackException | IOException e) {
			throw new OadrXmppException(e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new OadrXmppException(e);
		}
		
	}

	public void sendMessage(Jid jid, String payload)
			throws Oadr20bMarshalException, XmppStringprepException, NotConnectedException, InterruptedException {
		EntityBareJid entityBareFrom = JidCreate.entityBareFrom(jid);
		Chat chatWith = chatManager.chatWith(entityBareFrom);
		Message message = new Message();
		message.setFrom(this.connection.getUser());
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

	public String getClientJid() {
		return this.connection.getUser().asEntityFullJidIfPossible().toString();
	}
	
	public String getBareClientJid() {
		return this.connection.getUser().asBareJid().asUnescapedString();
	}


	public DomainBareJid getDomainJid() {
		return domainJid;
	}

	private void setDomainJid(DomainBareJid domainJid) {
		this.domainJid = domainJid;
	}

}
