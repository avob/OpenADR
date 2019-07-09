package com.avob.openadr.client.xmpp.oadr20b;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.StanzaListener;

public class OadrXmppClient20bBuilder {

	private String venId;
	private String host;
	private Integer port;
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

	public OadrXmppClient20b build() throws OadrXmppException {
		if (this.password != null) {
			return new OadrXmppClient20b(this.venId, this.host, this.port, this.resource, this.sslContext,
					this.password, this.listener);
		} else {
			return new OadrXmppClient20b(this.venId, this.host, this.port, this.resource, this.sslContext,
					this.listener);
		}

	}

}
