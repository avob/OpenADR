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
	private final transient boolean signed;

	public Oadr20bRequestEventApplicationLayerException(String message, OadrDistributeEventType response,
			boolean signed) {
		super(message);
		this.response = response;
		this.signed = signed;
	}

	@Override
	public OadrDistributeEventType getResponse() {
		return response;
	}

	@Override
	public boolean isSigned() {
		return signed;
	}

}
