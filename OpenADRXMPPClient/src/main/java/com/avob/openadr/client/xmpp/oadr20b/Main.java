package com.avob.openadr.client.xmpp.oadr20b;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import com.avob.openadr.client.xmpp.oadr20b.ven.OadrXmppVenClient20b;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.eiregisterparty.Oadr20bCreatePartyRegistrationBuilder;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class Main {
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, OadrSecurityException, UnrecoverableKeyException, KeyManagementException, OadrXmppException,
			NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException, Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException,
			Oadr20bMarshalException, JAXBException {

		String host = "vtn.oadr.com";
		int port = 5222;
		String path = "../cert/";
//String key = path + "vtn.oadr.com-rsa.key";
//String cert = path + "vtn.oadr.com-rsa.crt";
		String key = path + "ven1.oadr.com.key";
		String cert = path + "ven1.oadr.com.crt";
		String pass = "";
		Map<String, String> trustedCert = new HashMap<>();
		trustedCert.put("ca", path + "oadr.com.crt");
		KeyStore createKeyStore = OadrHttpSecurity.createKeyStore(key, cert, pass);
		KeyStore truststore = OadrHttpSecurity.createTrustStore(trustedCert);
		String oadr20bFingerprint = OadrHttpSecurity.getOadr20bFingerprint(cert);

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

		StanzaListener messageListener = new StanzaListener() {

			@Override
			public void processStanza(Stanza packet)
					throws NotConnectedException, InterruptedException, NotLoggedInException {
				Message message = (Message) packet;
				System.out.println(message.getBody());

			}

		};

		OadrXmppClient20b client = new OadrXmppClient20b(oadr20bFingerprint, host, port, "client", sslContext,
				messageListener);

		OadrXmppVenClient20b oadrXmppVenClient20b = new OadrXmppVenClient20b(client);

		String requestId = "0";
		Oadr20bCreatePartyRegistrationBuilder builder = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, oadr20bFingerprint,
						SchemaVersionEnumeratedType.OADR_20B.value())
				.withOadrHttpPullModel(false).withOadrTransportAddress(client.getClientJid().toString())
				.withOadrReportOnly(false).withOadrTransportName(OadrTransportType.XMPP).withOadrVenName("Xmpp VEN")
				.withOadrXmlSignature(false);

//		if (registrationId != null) {
//			builder.withRegistrationId(registrationId);
//		}

		OadrCreatePartyRegistrationType createPartyRegistration = builder.build();

		oadrXmppVenClient20b.oadrCreatePartyRegistration(createPartyRegistration);

		Thread.sleep(1000);

	}

}
