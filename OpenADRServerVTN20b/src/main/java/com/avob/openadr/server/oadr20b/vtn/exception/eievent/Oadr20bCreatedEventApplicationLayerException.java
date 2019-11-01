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
	private final transient boolean signed;
	public Oadr20bCreatedEventApplicationLayerException(String message, OadrResponseType response, boolean signed) {
		super(message);
		this.response = response;
		this.signed = signed;
	}

	@Override
	public OadrResponseType getResponse() {
		return response;
	}
	
	@Override
	public boolean isSigned() {
		return signed;
	}

}
