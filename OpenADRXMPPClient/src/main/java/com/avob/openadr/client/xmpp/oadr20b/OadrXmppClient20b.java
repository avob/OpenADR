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
import org.jivesoftware.smack.packet.IQ;
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

	public static final String OADR_OPT_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiOpt";

	public static final String OADR_REGISTERPARTY_SERVICE_NAMESPACE = "http://openadr.org/OpenADR2/EiRegisterParty";

	public static final String XMPP_OADR_SUBDOMAIN = "xmpp";

	private XMPPTCPConnection connection;

//	private Oadr20bJAXBContext jaxbContext;

	private DomainBareJid domainJid;
	private EntityFullJid clientJid;
	private EntityFullJid connectionJid;

	private ChatManager chatManager;

	private Map<String, Jid> discoveredXmppOadrServices;

	private static XMPPTCPConnectionConfiguration anonymousConnection(String host, int port, String domain,
			String resource, SSLContext context) throws OadrXmppException {
		try {
			return XMPPTCPConnectionConfiguration.builder()
//				.setHostAddress(address)

					.setHost(host).setPort(port)
//				.allowEmptyOrNullUsernames()
//				.setUsernameAndPassword(resource,resource)
//				.setAuthzid(authzid)
//				.setAuthzid(authzid)
					.performSaslAnonymousAuthentication().setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
					.setCompressionEnabled(false)
//				.addEnabledSaslMechanism("ANONYMOUS")
//				.performSaslExternalAuthentication(context)
//				.setUsernameAndPassword("admin", "mouaiccool")
//				.addEnabledSaslMechanism("EXTERNAL")
//				.addEnabledSaslMechanism("ANONYMOUS")
					.setResource(resource).setXmppDomain(domain).setCustomSSLContext(context).build();
		} catch (XmppStringprepException e) {
			throw new OadrXmppException(e);
		}
	}

	private static XMPPTCPConnectionConfiguration passwordConnection(String host, int port, String domain,
			String resource, SSLContext context, String username, String password) throws OadrXmppException {
		try {
			return XMPPTCPConnectionConfiguration.builder()
//				.setHostAddress(address)
					.setHost(host).setPort(port)
//				.allowEmptyOrNullUsernames()
					.setUsernameAndPassword(username, password)
					.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled).setCompressionEnabled(false)

//				.setAuthzid(authzid)
//				.setAuthzid(authzid)
//					.performSaslAnonymousAuthentication()
//				.addEnabledSaslMechanism("ANONYMOUS")
//				.performSaslExternalAuthentication(context)
//				.setUsernameAndPassword("admin", "mouaiccool")
//					.addEnabledSaslMechanism("EXTERNAL")
//				.addEnabledSaslMechanism("ANONYMOUS"
//					.performSaslExternalAuthentication(context)
					.setResource(resource).setXmppDomain(domain).setCustomSSLContext(context).build();
		} catch (XmppStringprepException e) {
			throw new OadrXmppException(e);
		}
	}

	private OadrXmppClient20b(XMPPTCPConnectionConfiguration config, String venId, String host, int port, String domain,
			String resource, SSLContext context, StanzaListener onMessageListener) throws OadrXmppException {
		try {
			SASLAnonymous mechanism = new SASLAnonymous();
			SASLExternalMechanism ext = new SASLExternalMechanism();
			SASLPlainMechanism plain = new SASLPlainMechanism();
			SASLAuthentication.registerSASLMechanism(mechanism);
			SASLAuthentication.registerSASLMechanism(ext);
			SASLAuthentication.registerSASLMechanism(plain);
			SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
			SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
			EntityBareJid authzid = JidCreate.entityBareFrom(resource + "@" + host);

			connection = new XMPPTCPConnection(config);
			connection.setUseStreamManagement(false);
			connection.setUseStreamManagementResumption(false);
			connection.setReplyTimeout(20000);

			setDomainJid(JidCreate.domainBareFrom(XMPP_OADR_SUBDOMAIN + "." + domain));
			setClientJid(JidCreate.entityFullFrom(venId, XMPP_OADR_SUBDOMAIN + "." + domain, resource));

			chatManager = ChatManager.getInstanceFor(connection);

			if (onMessageListener != null) {
				connection.addAsyncStanzaListener(onMessageListener, StanzaTypeFilter.MESSAGE);
			}

			connection.connect().login(resource, resource); // Establishes a connection to the server
			if (connection.isConnected() && connection.isAuthenticated()) {

				EntityFullJid user = connection.getUser();

				this.setConnectionJid(user);

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

		} catch (XmppStringprepException e) {
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

	public OadrXmppClient20b(String venId, String host, int port, String domain, String resource, SSLContext context,
			StanzaListener onMessageListener) throws OadrXmppException {
		this(OadrXmppClient20b.anonymousConnection(host, port, domain, resource, context), venId, host, port, domain,
				resource, context, onMessageListener);

	}

	public OadrXmppClient20b(String venId, String host, int port, String domain, String resource, SSLContext context,
			String password, StanzaListener onMessageListener) throws OadrXmppException {
		this(OadrXmppClient20b.passwordConnection(host, port, domain, resource, context, venId, password), venId, host,
				port, domain, resource, context, onMessageListener);

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

	public EntityFullJid getConnectionJid() {
		return connectionJid;
	}

	public void setConnectionJid(EntityFullJid connectionJid) {
		this.connectionJid = connectionJid;
	}

}
