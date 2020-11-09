package com.avob.openadr.server.common.vtn.models.ven;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.user.AbstractUserCreateDto;

public class VenCreateDto extends AbstractUserCreateDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3250807214482052191L;

	private String oadrName;

	private String oadrProfil;

	private String transport;

	private String pushUrl;

	private Boolean httpPullModel;

	private Boolean reportOnly = false;

	private Boolean xmlSignature = false;
	
	private List<String> marketContexts;
	
	private List<String> groups;
	
	private List<String> resources;

	public VenCreateDto() {
	}

	public VenCreateDto(String username) {
		this.setUsername(username);
	}

	public String getOadrName() {
		return oadrName;
	}

	public void setOadrName(String name) {
		this.oadrName = name;
	}

	public String getOadrProfil() {
		return oadrProfil;
	}

	public void setOadrProfil(String oadrProfil) {
		this.oadrProfil = oadrProfil;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getPushUrl() {
		return pushUrl;
	}

	public void setPushUrl(String pushUrl) {
		this.pushUrl = pushUrl;
	}

	public Boolean getHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(Boolean httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

	public Boolean getReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(Boolean reportOnly) {
		this.reportOnly = reportOnly;
	}

	public Boolean getXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(Boolean xmlSignature) {
		this.xmlSignature = xmlSignature;
	}

	public List<String> getMarketContexts() {
		return marketContexts;
	}

	public void setMarketContexts(List<String> marketContexts) {
		this.marketContexts = marketContexts;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

}
