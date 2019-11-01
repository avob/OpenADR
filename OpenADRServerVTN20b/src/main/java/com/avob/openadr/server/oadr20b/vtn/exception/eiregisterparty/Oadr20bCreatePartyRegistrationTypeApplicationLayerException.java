package com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCreatePartyRegistrationTypeApplicationLayerException extends Oadr20bException
		implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1435569206550937794L;

	private final transient OadrCreatedPartyRegistrationType response;
	private final transient boolean signed;

	public Oadr20bCreatePartyRegistrationTypeApplicationLayerException(String message,
			OadrCreatedPartyRegistrationType response, boolean signed) {
		super(message);
		this.response = response;
		this.signed = signed;
	}

	@Override
	public OadrCreatedPartyRegistrationType getResponse() {
		return response;
	}

	@Override
	public boolean isSigned() {
		return signed;
	}

}
