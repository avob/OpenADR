package com.avob.openadr.server.common.vtn.models.demandresponseevent.dto;

import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventActivePeriodDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventBaselineDto;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventDescriptorDto;

public class DemandResponseEventDto extends DemandResponseEventContentDto {

	private boolean published = false;

	private DemandResponseEventDescriptorDto descriptor = new DemandResponseEventDescriptorDto();

	private DemandResponseEventActivePeriodDto activePeriod = new DemandResponseEventActivePeriodDto();

	private DemandResponseEventBaselineDto baseline;

	public DemandResponseEventDescriptorDto getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(DemandResponseEventDescriptorDto descriptor) {
		this.descriptor = descriptor;
	}

	public DemandResponseEventActivePeriodDto getActivePeriod() {
		return activePeriod;
	}

	public void setActivePeriod(DemandResponseEventActivePeriodDto activePeriod) {
		this.activePeriod = activePeriod;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public DemandResponseEventBaselineDto getBaseline() {
		return baseline;
	}

	public void setBaseline(DemandResponseEventBaselineDto baseline) {
		this.baseline = baseline;
	}
}
