package com.avob.openadr.model.oadr20b.builders;

import java.util.Arrays;

import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCancelReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCanceledReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCreateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCreatedReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bOadrReportDescriptionBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bRegisterReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bRegisterReportOadrReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bRegisteredReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bReportRequestTypeBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bUpdateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bUpdateReportOadrReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bUpdatedReportBuilder;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SchemaVersionEnumeratedType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;

public class Oadr20bEiReportBuilders {

	private Oadr20bEiReportBuilders() {
	}

	public static Oadr20bCanceledReportBuilder newOadr20bCanceledReportBuilder(String requestId, int responseCode,
			String venId) {
		return new Oadr20bCanceledReportBuilder(requestId, responseCode, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCancelReportBuilder newOadr20bCancelReportBuilder(String requestId, String venId,
			boolean reportToFollow) {
		return new Oadr20bCancelReportBuilder(requestId, venId, reportToFollow)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCreatedReportBuilder newOadr20bCreatedReportBuilder(String requestId, int responseCode,
			String venId) {
		return new Oadr20bCreatedReportBuilder(requestId, responseCode, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder(String requestId, String venId) {
		return new Oadr20bCreateReportBuilder(requestId, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bRegisteredReportBuilder newOadr20bRegisteredReportBuilder(String requestId, int responseCode,
			String venId) {
		return new Oadr20bRegisteredReportBuilder(requestId, responseCode, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bRegisterReportBuilder newOadr20bRegisterReportBuilder(String requestId, String venId) {
		return new Oadr20bRegisterReportBuilder(requestId, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bUpdatedReportBuilder newOadr20bUpdatedReportBuilder(String requestId, int responseCode,
			String venId) {
		return new Oadr20bUpdatedReportBuilder(requestId, responseCode, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bUpdateReportBuilder newOadr20bUpdateReportBuilder(String requestId, String venId) {
		return new Oadr20bUpdateReportBuilder(requestId, venId)
				.withSchemaVersion(SchemaVersionEnumeratedType.OADR_20B.value());
	}

	public static Oadr20bReportRequestTypeBuilder newOadr20bReportRequestTypeBuilder(String reportRequestId,
			String reportSpecifierId, String granularity, String reportBackDuration) {
		return new Oadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration);
	}

	public static Oadr20bUpdateReportOadrReportBuilder newOadr20bUpdateReportOadrReportBuilder(String reportId, String reportSpecifierId,
			String reportrequestId, ReportNameEnumeratedType reportName,
			long createdTimestamp, Long startTimestamp, String duration) {
		return new Oadr20bUpdateReportOadrReportBuilder(reportId, reportSpecifierId, reportrequestId, reportName,
				createdTimestamp, startTimestamp, duration);
	}

	public static Oadr20bOadrReportDescriptionBuilder newOadr20bOadrReportDescriptionBuilder(String rid,
			ReportEnumeratedType reportType, ReadingTypeEnumeratedType readingType) {
		return new Oadr20bOadrReportDescriptionBuilder(rid, reportType, readingType);
	}

	public static Oadr20bRegisterReportOadrReportBuilder newOadr20bRegisterReportOadrReportBuilder(
			String reportSpecifierId,  ReportNameEnumeratedType reportName,
			long createdTimestamp) {
		return new Oadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportName,
				createdTimestamp);
	}
}
