package com.avob.openadr.model.oadr20b.builders.eireport;

import java.util.Arrays;
import java.util.List;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;

public class Oadr20bUpdateReportOadrReportBuilder {

	private OadrReportType oadrReportType;

	public Oadr20bUpdateReportOadrReportBuilder(String reportId, String reportSpecifierId, String reportrequestId,
			ReportNameEnumeratedType reportName, long createdTimestamp, Long startTimestamp, String duration) {
		oadrReportType = Oadr20bFactory.createOadrUpdateReportOadrReportType(reportId, reportSpecifierId,
				reportrequestId, reportName, createdTimestamp, startTimestamp, duration);
	}

	public Oadr20bUpdateReportOadrReportBuilder addInterval(List<IntervalType> interval) {
		oadrReportType.getIntervals().getInterval().addAll(interval);
		return this;
	}

	public Oadr20bUpdateReportOadrReportBuilder addInterval(IntervalType interval) {
		this.addInterval(Arrays.asList(interval));
		return this;
	}

	public OadrReportType build() {
		return oadrReportType;
	}

}
