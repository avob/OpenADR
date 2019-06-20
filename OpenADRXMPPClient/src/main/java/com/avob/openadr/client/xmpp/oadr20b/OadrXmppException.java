package com.avob.openadr.client.xmpp.oadr20b;

public class OadrXmppException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6784508245989785955L;

	public OadrXmppException(String message) {
		super(message);
	}

	public OadrXmppException(Exception e) {
		super(e);
	}

}
