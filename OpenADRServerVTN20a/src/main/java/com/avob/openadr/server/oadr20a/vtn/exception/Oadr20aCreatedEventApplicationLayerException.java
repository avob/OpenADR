package com.avob.openadr.server.oadr20a.vtn.exception;

import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

public class Oadr20aCreatedEventApplicationLayerException extends Oadr20aException implements Oadr20aGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6648518668115777460L;

	private final transient OadrResponse response;

	public Oadr20aCreatedEventApplicationLayerException(String message, OadrResponse response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrResponse getResponse() {
		return response;
	}

}
