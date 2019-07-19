package com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bResponsePartyReregistrationApplicationLayerException extends Oadr20bException
		implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3493089269293585027L;
	private final transient OadrResponseType response;

	public Oadr20bResponsePartyReregistrationApplicationLayerException(String message, OadrResponseType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrResponseType getResponse() {
		return response;
	}

}
