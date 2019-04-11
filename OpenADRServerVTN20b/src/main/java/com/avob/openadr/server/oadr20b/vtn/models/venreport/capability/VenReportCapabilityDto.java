package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

public class VenReportCapabilityDto extends ReportCapabilityDto {
	private List<ReportCapabilityDescriptionDto> descriptions;

	public List<ReportCapabilityDescriptionDto> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<ReportCapabilityDescriptionDto> descriptions) {
		this.descriptions = descriptions;
	}

}
