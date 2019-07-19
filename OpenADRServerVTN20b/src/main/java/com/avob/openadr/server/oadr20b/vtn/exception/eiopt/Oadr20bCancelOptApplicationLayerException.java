package com.avob.openadr.server.oadr20b.vtn.exception.eiopt;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCancelOptApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3946966387409535342L;

	private final transient OadrCanceledOptType response;

	public Oadr20bCancelOptApplicationLayerException(String message, OadrCanceledOptType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrCanceledOptType getResponse() {
		return response;
	}

}
