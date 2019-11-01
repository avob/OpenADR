package com.avob.openadr.server.oadr20b.vtn.exception.eiregisterparty;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCancelPartyRegistrationTypeApplicationLayerException extends Oadr20bException
		implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1468896185130868972L;

	private final transient OadrCanceledPartyRegistrationType response;
	private final transient boolean signed;

	public Oadr20bCancelPartyRegistrationTypeApplicationLayerException(String message,
			OadrCanceledPartyRegistrationType response, boolean signed) {
		super(message);
		this.response = response;
		this.signed = signed;
	}

	@Override
	public OadrCanceledPartyRegistrationType getResponse() {
		return response;
	}

	@Override
	public boolean isSigned() {
		return signed;
	}

}
