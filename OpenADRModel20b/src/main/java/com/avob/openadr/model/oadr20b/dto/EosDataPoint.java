package com.avob.openadr.model.oadr20b.dto;

public class EosDataPoint extends EosExportedObject {

	private ReportCapabilityDescriptionDto description;

	private String mupMrid;

	private String endDevicePath;

	private String id;

	private String type;

	private String path;

	public EosDataPoint() {
	}

	public EosDataPoint(String venId, String pathId, String serviceName, String index, ReportCapabilityDto report,
			ReportCapabilityDescriptionDto description, String mupMrid, String endDevicePath) {
		super(venId, pathId, serviceName, index, report);
		description.setPayload(null);
		this.setDescription(description);
		this.setMupMrid(mupMrid);
		this.setEndDevicePath(endDevicePath);

	}

	public ReportCapabilityDescriptionDto getDescription() {
		return description;
	}

	public void setDescription(ReportCapabilityDescriptionDto description) {
		this.description = description;
	}

	public String getMupMrid() {
		return mupMrid;
	}

	public void setMupMrid(String mupMrid) {
		this.mupMrid = mupMrid;
	}

	public String getEndDevicePath() {
		return endDevicePath;
	}

	public void setEndDevicePath(String endDevicePath) {
		this.endDevicePath = endDevicePath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
