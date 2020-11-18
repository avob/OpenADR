package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;

public class Oadr20bRegisterReportOadrReportBuilder {

	private OadrReportType report;

	public Oadr20bRegisterReportOadrReportBuilder(String reportSpecifierId, 
			ReportNameEnumeratedType reportName, long createdTimestamp) {
		report = Oadr20bFactory.createOadrRegisterReportOadrReportType(reportSpecifierId, reportName,
				createdTimestamp);
	}

	public Oadr20bRegisterReportOadrReportBuilder withStart(Long start) {
		report.setDtstart(Oadr20bFactory.createDtstart(start));
		return this;
	}

	public Oadr20bRegisterReportOadrReportBuilder withDuration(String duration) {
		report.setDuration(Oadr20bFactory.createDurationPropType(duration));
		return this;
	}

	public Oadr20bRegisterReportOadrReportBuilder addReportDescription(
			List<OadrReportDescriptionType> oadrReportDescriptionType) {
		report.getOadrReportDescription().addAll(oadrReportDescriptionType);
		return this;
	}

	public Oadr20bRegisterReportOadrReportBuilder addReportDescription(
			OadrReportDescriptionType oadrReportDescriptionType) {
		this.addReportDescription(Arrays.asList(oadrReportDescriptionType));
		return this;
	}

	public Oadr20bRegisterReportOadrReportBuilder addInterval(List<IntervalType> intervals) {
		if (report.getIntervals() == null) {
			report.setIntervals(Oadr20bFactory.createIntervals());
		}
		report.getIntervals().getInterval().addAll(intervals);
		return this;
	}

	public Oadr20bRegisterReportOadrReportBuilder addInterval(IntervalType interval) {
		this.addInterval(Arrays.asList(interval));
		return this;
	}

	public OadrReportType build() {
		return report;
	}

}
