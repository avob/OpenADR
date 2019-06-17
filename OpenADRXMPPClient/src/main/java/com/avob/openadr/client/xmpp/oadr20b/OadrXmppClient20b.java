package com.avob.openadr.client.xmpp.oadr20b;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.AbstractNodeInformationProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXmppClient20b {

	private static final String OADR_NAMESPACE = "http://openadr.org/openadr2";

	private static final String OADR_SERVICES_NAMESPACE = OADR_NAMESPACE + "#services";

	private static final String XMPP_OADR_SUBDOMAIN = "xmpp";

	private XMPPTCPConnection connection;

	private Oadr20bJAXBContext jaxbContext;

	private DomainBareJid domainJid;

	public OadrXmppClient20b(String host, int port, SSLContext context)
			throws SmackException, IOException, XMPPException, JAXBException, InterruptedException {

		this.jaxbContext = Oadr20bJAXBContext.getInstance();

		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//				.setHostAddress(address)
				.setHost(host).setPort(port).setUsernameAndPassword("admin", "admin").setXmppDomain(host)
				.setCustomSSLContext(context).build();

		connection = new XMPPTCPConnection(config);

		domainJid = JidCreate.domainBareFrom(XMPP_OADR_SUBDOMAIN + "." + host);

		// Obtain the ServiceDiscoveryManager associated with my XMPP connection
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);

//					ServiceDiscoveryManager.setDefaultIdentity(new Identity("client", "oadr"));
		// Register that a new feature is supported by this XMPP entity
//					discoManager.ad
		discoManager.addFeature(OADR_NAMESPACE);
		discoManager.addFeature("http://jabber.org/protocol/disco#info");
		discoManager.addFeature(OADR_NAMESPACE + "#vtn");
		discoManager.setNodeInformationProvider("", new AbstractNodeInformationProvider() {
			@Override
			public List<String> getNodeFeatures() {
				return Arrays.asList(OADR_NAMESPACE);
			}

			@Override
			public List<DiscoverItems.Item> getNodeItems() {

				List<DiscoverItems.Item> answer = new ArrayList<DiscoverItems.Item>();

				return answer;
			}
		});

		connection.addAsyncStanzaListener(new StanzaListener() {
			public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException,
					SmackException.NotLoggedInException {
				IQ iq = (IQ) stanza;
			}
		}, StanzaTypeFilter.IQ);

		connection.addAsyncStanzaListener(new StanzaListener() {
			public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException,
					SmackException.NotLoggedInException {
				Presence presence = (Presence) stanza;
			}
		}, StanzaTypeFilter.PRESENCE);

		connection.addAsyncStanzaListener(new StanzaListener() {
			public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException,
					SmackException.NotLoggedInException {
				Message message = (Message) stanza;
				String body = message.getBody();

				try {
					Object unmarshal = jaxbContext.unmarshal(body);
				} catch (Oadr20bUnmarshalException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, StanzaTypeFilter.MESSAGE);

		connection.connect().login(); // Establishes a connection to the server
		if (connection.isConnected() && connection.isAuthenticated()) {

			Presence p = new Presence(Type.available);
			connection.sendStanza(p);

		} else {
			System.out.println("Connection refused by Xmpp server");
		}

	}

	public void discoverXmppOadrFeature() {

		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		DiscoverInfo discoverInfo;
		try {
			discoverInfo = discoManager.discoverInfo(domainJid, null);
			Iterator it = discoverInfo.getFeatures().iterator();
			System.out.println("features");
			while (it.hasNext()) {
				DiscoverInfo.Feature identity = (DiscoverInfo.Feature) it.next();
				System.out.println(identity.getVar());
			}

			it = discoverInfo.getIdentities().iterator();
			// Display the identities of the remote XMPP
			// entitySystem.out.println(identity.getName());z
			System.out.println("identitites");
			while (it.hasNext()) {
				DiscoverInfo.Identity identity = (DiscoverInfo.Identity) it.next();
				System.out.println(identity.getName());
				System.out.println(identity.getType());
				System.out.println(identity.getCategory());
			}

		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void discoverXmppOadrItems() {
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		DiscoverItems discoverItems;
		try {
			discoverItems = discoManager.discoverItems(domainJid, OADR_SERVICES_NAMESPACE);

			for (Item item : discoverItems.getItems()) {
				System.out.println(item.toXML());
			}
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException,
			UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, OadrSecurityException, JAXBException {

		String host = "vtn.oadr.com";
		int port = 5222;
		String path = "../cert/";
//		String key = path + "vtn.oadr.com-rsa.key";
//		String cert = path + "vtn.oadr.com-rsa.crt";
		String key = path + "admin.oadr.com.key";
		String cert = path + "admin.oadr.com.crt";
		String pass = "";
		Map<String, String> trustedCert = new HashMap<>();
		trustedCert.put("ca", path + "oadr.com.crt");
		KeyStore createKeyStore = OadrHttpSecurity.createKeyStore(key, cert, pass);
		KeyStore truststore = OadrHttpSecurity.createTrustStore(trustedCert);

		// init key manager factory
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(createKeyStore, "".toCharArray());

		// init trust manager factory
		TrustManagerFactory trustManagerFactory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(truststore);

		// SSL Context Factory
		SSLContext sslContext = SSLContext.getInstance("TLS");

		// init ssl context
		String seed = UUID.randomUUID().toString();

		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
				new SecureRandom(seed.getBytes()));

		OadrXmppClient20b client = new OadrXmppClient20b(host, port, sslContext);

	}
}
