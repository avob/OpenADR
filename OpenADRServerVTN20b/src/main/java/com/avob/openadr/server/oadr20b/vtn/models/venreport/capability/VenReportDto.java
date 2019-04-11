package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

public class VenReportDto {
	private Long id;
	private String username;
	private List<VenReportCapabilityDto> capabilities;

	public List<VenReportCapabilityDto> getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(List<VenReportCapabilityDto> capabilities) {
		this.capabilities = capabilities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
