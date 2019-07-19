package com.avob.openadr.server.oadr20b.vtn.exception.eireport;

import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.server.oadr20b.vtn.exception.Oadr20bGenericException;

public class Oadr20bUpdateReportApplicationLayerException extends Oadr20bException implements Oadr20bGenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1468896185130868972L;

	private final transient OadrUpdatedReportType response;

	public Oadr20bUpdateReportApplicationLayerException(String message, OadrUpdatedReportType response) {
		super(message);
		this.response = response;
	}

	@Override
	public OadrUpdatedReportType getResponse() {
		return response;
	}
}
