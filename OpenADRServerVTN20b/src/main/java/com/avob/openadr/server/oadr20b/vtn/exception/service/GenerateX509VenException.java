package com.avob.openadr.server.oadr20b.vtn.exception.service;

public class GenerateX509VenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -281717362559148037L;

	public GenerateX509VenException(Exception e, String msg) {
		super(msg, e);
	}

	public GenerateX509VenException(String msg) {
		super(msg);
	}

	public GenerateX509VenException(Exception e) {
		super(e);
	}

}
