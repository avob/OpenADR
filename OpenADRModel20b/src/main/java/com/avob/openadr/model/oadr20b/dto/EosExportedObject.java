package com.avob.openadr.model.oadr20b.dto;

public abstract class EosExportedObject {

	private String venId;
	private String pathId;
	private String serviceName;
	private String index;
	private ReportCapabilityDto report;

	public EosExportedObject() {
	}

	public EosExportedObject(String venId, String pathId, String serviceName, String index,
			ReportCapabilityDto report) {
		this.venId = venId;
		this.pathId = pathId;
		this.serviceName = serviceName;
		this.index = index;
		this.report = report;
	}

	public String getVenId() {
		return venId;
	}

	public void setVenId(String venId) {
		this.venId = venId;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public ReportCapabilityDto getReport() {
		return report;
	}

	public void setReport(ReportCapabilityDto report) {
		this.report = report;
	}

}
