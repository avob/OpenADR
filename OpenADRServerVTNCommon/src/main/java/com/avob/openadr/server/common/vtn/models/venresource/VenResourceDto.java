package com.avob.openadr.server.common.vtn.models.venresource;

public class VenResourceDto {

	private long id;

	private String name;

	private VenResourceType type;

	private Long venResourceId;

	private String venResourceLabel;

	private VenResourceShortDto parent;

	private Long reportDescriptionCount;

	public VenResourceDto() {
	}

	public VenResourceDto(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VenResourceType getType() {
		return type;
	}

	public void setType(VenResourceType type) {
		this.type = type;
	}

	public Long getVenResourceId() {
		return venResourceId;
	}

	public void setVenResourceId(Long venResourceId) {
		this.venResourceId = venResourceId;
	}

	public String getVenResourceLabel() {
		return venResourceLabel;
	}

	public void setVenResourceLabel(String venResourceLabel) {
		this.venResourceLabel = venResourceLabel;
	}

	public VenResourceShortDto getParent() {
		return parent;
	}

	public void setParent(VenResourceShortDto parent) {
		this.parent = parent;
	}

	public Long getReportDescriptionCount() {
		return reportDescriptionCount;
	}

	public void setReportDescriptionCount(Long reportDescriptionCount) {
		this.reportDescriptionCount = reportDescriptionCount;
	}

}
