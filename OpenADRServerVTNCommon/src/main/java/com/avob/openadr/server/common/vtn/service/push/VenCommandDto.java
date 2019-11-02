package com.avob.openadr.server.common.vtn.service.push;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public class VenCommandDto<T> {

	private String venUsername;

	private String venPushUrl;

	private String venTransport;

	private boolean xmlSignature;

	private String payload;

	private Class<T> payloadClass;

	public VenCommandDto() {
	}

	public VenCommandDto(Ven ven, String payload, Class<T> klass) {
		this.setVenUsername(ven.getUsername());
		this.setVenPushUrl(ven.getPushUrl());
		this.setVenTransport(ven.getTransport());
		this.setXmlSignature(ven.getXmlSignature());
		this.setPayload(payload);
		this.setPayloadClass(klass);
	}

	public String getVenPushUrl() {
		return venPushUrl;
	}

	public void setVenPushUrl(String venPushUrl) {
		this.venPushUrl = venPushUrl;
	}

	public boolean isXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getVenUsername() {
		return venUsername;
	}

	@Override
	public String toString() {
		return "VenCommandDto [venUsername=" + venUsername + ", venPushUrl=" + venPushUrl + ", xmlSignature="
				+ xmlSignature + ", payload=" + payload + "]";
	}

	public void setVenUsername(String venUsername) {
		this.venUsername = venUsername;
	}

	public Class<T> getPayloadClass() {
		return payloadClass;
	}

	public void setPayloadClass(Class<T> payloadClass) {
		this.payloadClass = payloadClass;
	}

	public String getVenTransport() {
		return venTransport;
	}

	public void setVenTransport(String venTransport) {
		this.venTransport = venTransport;
	}

}
