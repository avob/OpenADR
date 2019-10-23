package com.avob.openadr.model.oadr20b.exception;

public class Oadr20bXMLSignatureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6972794693172498180L;

	public Oadr20bXMLSignatureException(Exception e) {
		super(e);
	}

	public Oadr20bXMLSignatureException(String msg) {
		super(msg);
	}

}
