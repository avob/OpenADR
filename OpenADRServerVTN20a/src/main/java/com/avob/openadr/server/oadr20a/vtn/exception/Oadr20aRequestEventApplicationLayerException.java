package com.avob.openadr.server.oadr20a.vtn.exception;

import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;

public class Oadr20aRequestEventApplicationLayerException extends Oadr20aException implements Oadr20aGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6679798213075312741L;

	private final transient OadrDistributeEvent response;

	public Oadr20aRequestEventApplicationLayerException(String message, OadrDistributeEvent response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrDistributeEvent getResponse() {
		return response;
	}

}
