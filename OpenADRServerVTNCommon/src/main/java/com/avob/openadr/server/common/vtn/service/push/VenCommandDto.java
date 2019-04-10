package com.avob.openadr.server.common.vtn.service.push;

public class VenCommandDto<T> {

	private String venUsername;

	private String venPushUrl;

	private boolean xmlSignature;

	private String payload;

	private Class<T> payloadClass;

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

}
