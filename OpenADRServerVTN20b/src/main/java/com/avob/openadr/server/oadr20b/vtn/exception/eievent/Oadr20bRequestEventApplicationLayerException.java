package com.avob.openadr.server.oadr20b.vtn.exception.eievent;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bRequestEventApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5176285555685520621L;

	private final transient OadrDistributeEventType response;

	public Oadr20bRequestEventApplicationLayerException(String message, OadrDistributeEventType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrDistributeEventType getResponse() {
		return response;
	}

}
