package com.avob.openadr.server.oadr20b.vtn.exception.eiopt;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCreateOptApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2260781873450824384L;

	private final transient OadrCreatedOptType response;
	private final transient boolean signed;

	public Oadr20bCreateOptApplicationLayerException(String message, OadrCreatedOptType response, boolean signed) {
		super(message);
		this.response = response;
		this.signed = signed;
	}

	@Override
	public OadrCreatedOptType getResponse() {
		return response;
	}

	@Override
	public boolean isSigned() {
		return signed;
	}

}
