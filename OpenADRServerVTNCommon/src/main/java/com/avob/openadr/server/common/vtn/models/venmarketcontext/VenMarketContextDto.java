package com.avob.openadr.server.common.vtn.models.venmarketcontext;

import java.util.List;
import java.util.Set;

import com.avob.openadr.server.common.vtn.models.TargetDto;

public class VenMarketContextDto {

	private Long id;

	private String name;

	private String description;

	private String color;

	private Long createdTimestamp;

	private Long lastUpdateTimestamp;

	private VenMarketContextDescriptorDto descriptor;

	private VenMarketContextActivePeriodDto activePeriod;

	private VenMarketContextBaselineDto baseline;

	private Set<VenMarketContextSignalDto> signals;

	private List<TargetDto> targets;

	private Set<VenMarketContextReportDto> reports;

	private VenMarketContextDemandResponseEventScheduleStrategyDto demandResponseEventScheduleStrategy;

	private VenMarketContextReportSubscriptionStrategyDto reportSubscriptionStrategy;

	public VenMarketContextDto() {
	}

	public VenMarketContextDto(String name) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Long getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Long lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public VenMarketContextBaselineDto getBaseline() {
		return baseline;
	}

	public void setBaseline(VenMarketContextBaselineDto baseline) {
		this.baseline = baseline;
	}

	public Set<VenMarketContextSignalDto> getSignals() {
		return signals;
	}

	public void setSignals(Set<VenMarketContextSignalDto> signals) {
		this.signals = signals;
	}

	public List<TargetDto> getTargets() {
		return targets;
	}

	public void setTargets(List<TargetDto> targets) {
		this.targets = targets;
	}

	public VenMarketContextDescriptorDto getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(VenMarketContextDescriptorDto descriptor) {
		this.descriptor = descriptor;
	}

	public VenMarketContextActivePeriodDto getActivePeriod() {
		return activePeriod;
	}

	public void setActivePeriod(VenMarketContextActivePeriodDto activePeriod) {
		this.activePeriod = activePeriod;
	}

	public Set<VenMarketContextReportDto> getReports() {
		return reports;
	}

	public void setReports(Set<VenMarketContextReportDto> reports) {
		this.reports = reports;
	}

	public VenMarketContextReportSubscriptionStrategyDto getReportSubscriptionStrategy() {
		return reportSubscriptionStrategy;
	}

	public void setReportSubscriptionStrategy(
			VenMarketContextReportSubscriptionStrategyDto reportSubscriptionStrategy) {
		this.reportSubscriptionStrategy = reportSubscriptionStrategy;
	}

	public VenMarketContextDemandResponseEventScheduleStrategyDto getDemandResponseEventScheduleStrategy() {
		return demandResponseEventScheduleStrategy;
	}

	public void setDemandResponseEventScheduleStrategy(
			VenMarketContextDemandResponseEventScheduleStrategyDto demandResponseEventScheduleStrategy) {
		this.demandResponseEventScheduleStrategy = demandResponseEventScheduleStrategy;
	}
}
