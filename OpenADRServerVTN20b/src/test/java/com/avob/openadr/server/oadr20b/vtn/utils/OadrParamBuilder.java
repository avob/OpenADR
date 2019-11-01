package com.avob.openadr.server.oadr20b.vtn.utils;

import org.springframework.util.LinkedMultiValueMap;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;

public class OadrParamBuilder {
	private static final String PARAM_REPORT_SPECIFIER_ID = "reportSpecifierId";
	private static final String PARAM_VEN_ID = "venID";
	private static final String PARAM_REPORT_NAME = "reportName";
	private static final String PARAM_REPORT_TYPE = "reportType";
	private static final String PARAM_READING_TYPE = "readingType";
	private static final String PARAM_REPORT_REQUEST_ID = "reportRequestId";
	private static final String PARAM_START = "start";
	private static final String PARAM_END = "end";
	private static final String PARAM_MARKETCONTEXT = "marketContext";

	private LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();

	public static OadrParamBuilder builder() {
		return new OadrParamBuilder();
	}

	public OadrParamBuilder addVenId(String venId) {
		params.add(PARAM_VEN_ID, venId);
		return this;
	}

	public OadrParamBuilder addReportSpecifierId(String reportSpecifierId) {
		params.add(PARAM_REPORT_SPECIFIER_ID, reportSpecifierId);
		return this;
	}

	public OadrParamBuilder addReportName(ReportNameEnumeratedType reportName) {
		params.add(PARAM_REPORT_NAME, reportName.toString());
		return this;
	}

	public OadrParamBuilder addReportType(ReportEnumeratedType reportType) {
		params.add(PARAM_REPORT_TYPE, reportType.toString());
		return this;
	}

	public OadrParamBuilder addReadingType(ReadingTypeEnumeratedType readingType) {
		params.add(PARAM_READING_TYPE, readingType.toString());
		return this;
	}

	public OadrParamBuilder addStart(Long datetime) {
		params.add(PARAM_START, String.valueOf(datetime));
		return this;
	}

	public OadrParamBuilder addEnd(Long datetime) {
		params.add(PARAM_END, String.valueOf(datetime));
		return this;
	}

	public OadrParamBuilder addMarketContext(String name) {
		params.add(PARAM_MARKETCONTEXT, name);
		return this;
	}

	public LinkedMultiValueMap<String, String> build() {
		return params;
	}

	public OadrParamBuilder addReportRequestId(String reportRequestId) {
		params.add(PARAM_REPORT_REQUEST_ID, reportRequestId);
		return this;
	}
}
