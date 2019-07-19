package com.avob.openadr.server.oadr20b.vtn.exception.eievent;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCreatedEventApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6388593598870112862L;

	private final transient OadrResponseType response;

	public Oadr20bCreatedEventApplicationLayerException(String message, OadrResponseType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrResponseType getResponse() {
		return response;
	}

}
