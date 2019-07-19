package com.avob.openadr.server.oadr20b.vtn.exception.eireport;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bCancelReportApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1468896185130868972L;

	private final transient OadrCanceledReportType response;

	public Oadr20bCancelReportApplicationLayerException(String message, OadrCanceledReportType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrCanceledReportType getResponse() {
		return response;
	}

}
