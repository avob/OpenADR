package com.avob.openadr.server.oadr20b.vtn.controller;

public class GenerateX509VenDto {
	private String venName;
	private String venCN;
	private String type;

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public String getvenCN() {
		return venCN;
	}

	public void setVenCN(String venCN) {
		this.venCN = venCN;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
