package com.avob.openadr.client.xmpp.oadr20b;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class OadrXmppClient20bBuilder {

	private String venId;
	private String host;
	private Integer port;
	private String domain;
	private SSLContext sslContext;
	private String password;
	private String resource;
	private StanzaListener listener;

	public OadrXmppClient20bBuilder withVenID(String venId) {
		this.venId = venId;
		return this;
	}

	public OadrXmppClient20bBuilder withHostAndPort(String host, int port) {
		this.host = host;
		this.port = port;
		return this;
	}

	public OadrXmppClient20bBuilder withSSLContext(SSLContext context) {
		this.sslContext = context;
		return this;
	}

	public OadrXmppClient20bBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public OadrXmppClient20bBuilder withResource(String resource) {
		this.resource = resource;
		return this;
	}

	public OadrXmppClient20bBuilder withListener(StanzaListener listener) {
		this.listener = listener;
		return this;
	}

	public OadrXmppClient20bBuilder withDomain(String domain) {
		this.domain = domain;
		return this;
	}

	public OadrXmppClient20b build() throws OadrXmppException {
		String domain = this.domain;
		if (domain == null) {
			domain = host;
		}
		XMPPTCPConnection xmpptcpConnection = null;
		if (this.password != null) {
			XMPPTCPConnectionConfiguration anonymousConnection = OadrXmppClient20b.passwordConnection(domain, port,
					domain, domain, sslContext, venId, this.password);
			xmpptcpConnection = new XMPPTCPConnection(anonymousConnection);
		} else {
			XMPPTCPConnectionConfiguration anonymousConnection = OadrXmppClient20b.anonymousConnection(host, port,
					domain, resource, sslContext);
			xmpptcpConnection = new XMPPTCPConnection(anonymousConnection);
		}

		String jid = this.venId + "@" + domain;
		if(this.resource != null) {
			jid += "/"+resource;
		}
		return new OadrXmppClient20b(jid, xmpptcpConnection, domain, this.listener);

	}

}
