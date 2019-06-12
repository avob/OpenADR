package com.avob.openadr.client.xmpp;

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

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.disco.AbstractNodeInformationProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXmppClient {

	private static final String OADR_NAMESPACE = "http://openadr.org/openadr2";

	public OadrXmppClient() throws SmackException, IOException, XMPPException, InterruptedException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, OadrSecurityException, UnrecoverableKeyException,
			KeyManagementException {

		String path = "../cert/";
		String key = path + "ven1.oadr.com.key";
		String cert = path + "ven1.oadr.com.crt";
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

		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//				.setHostAddress(address)
				.setHost("192.168.33.2").setPort(5222).setUsernameAndPassword("admin", "admin")
				.setXmppDomain("vtn.oadr.com").setCustomSSLContext(sslContext).build();

		XMPPTCPConnection connection = new XMPPTCPConnection(config);
		// Obtain the ServiceDiscoveryManager associated with my XMPP connection
		ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
		// Register that a new feature is supported by this XMPP entity
//		discoManager.ad
		discoManager.addFeature(OADR_NAMESPACE);
//		discoManager.addFeature("http://jabber.org/protocol/disco#info");
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
				// handle stanza
				System.out.println("IN: "+stanza.toXML(null));
			}
		}, StanzaTypeFilter.IQ);
		connection.addStanzaSendingListener(new StanzaListener() {
			public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException,
					SmackException.NotLoggedInException {
				// handle stanza
				System.out.println("OUT: " +stanza.toXML(null));
			}
		}, StanzaTypeFilter.IQ);
		connection.connect().login(); // Establishes a connection to the server
		ChatManager chatManager = ChatManager.getInstanceFor(connection);
		IncomingChatMessageListener listener = new IncomingChatMessageListener() {

			@Override
			public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
				System.out.println(from.getLocalpart() + " - " + message.getBody());
			}

		};
		chatManager.addIncomingListener(listener);
//		EntityBareJid jid = JidCreate.entityBareFrom("admin@vtn.oadr.com");
//		Chat chat = chatManager.chatWith(jid);
//		System.out.println(chat.getXmppAddressOfChatPartner().getLocalpart());
//		chat.send("mouaiccool");

//		discoManager.

		connection.sendStanza(new Presence(Presence.Type.available));

		// Get the items of a given XMPP entity
		// This example gets the items associated with online catalog service
		Thread.sleep(5000);

//		DiscoverItems discoverItems = discoManager.discoverItems(JidCreate.entityBareFrom("admin@vtn.oadr.com"));

		DiscoverInfo discoverInfo = discoManager.discoverInfo(null);
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
		Thread.sleep(1000);
	}

	public static void main(String[] args) throws SmackException, IOException, XMPPException, InterruptedException,
			UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, OadrSecurityException {
		OadrXmppClient client = new OadrXmppClient();

	}
}
