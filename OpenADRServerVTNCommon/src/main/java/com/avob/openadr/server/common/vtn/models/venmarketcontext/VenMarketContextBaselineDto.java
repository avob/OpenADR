package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.demandresponseevent.dto.embedded.DemandResponseEventSignalIntervalDto;

public class VenMarketContextBaselineDto {

	private Long id;

	private List<DemandResponseEventSignalIntervalDto> intervals;

	private Long start;

	private String duration;

	private String baselineId;

	private List<String> resourceId;

	private String baselineName;

	private ItemBase itemBase;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<DemandResponseEventSignalIntervalDto> getIntervals() {
		return intervals;
	}

	public void setIntervals(List<DemandResponseEventSignalIntervalDto> intervals) {
		this.intervals = intervals;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getBaselineId() {
		return baselineId;
	}

	public void setBaselineId(String baselineId) {
		this.baselineId = baselineId;
	}

	public List<String> getResourceId() {
		return resourceId;
	}

	public void setResourceId(List<String> resourceId) {
		this.resourceId = resourceId;
	}

	public String getBaselineName() {
		return baselineName;
	}

	public void setBaselineName(String baselineName) {
		this.baselineName = baselineName;
	}

	public ItemBase getItemBase() {
		return itemBase;
	}

	public void setItemBase(ItemBase itemBase) {
		this.itemBase = itemBase;
	}

}
