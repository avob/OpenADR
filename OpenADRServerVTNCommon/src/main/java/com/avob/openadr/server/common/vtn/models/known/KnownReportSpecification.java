package com.avob.openadr.server.common.vtn.models.known;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class KnownReportSpecification {

	private static final String FILTER_KNOWN_REPORT_ID = "knownReportId";
	private static final String FILTER_REPORT_NAME = "reportName";
	private static final String FILTER_REPORT_TYPE = "reportType";
	private static final Sort DEFAULT_SORT = Sort.by(Direction.ASC, FILTER_KNOWN_REPORT_ID + "." + FILTER_REPORT_NAME,
			FILTER_KNOWN_REPORT_ID + "." + FILTER_REPORT_TYPE);

	public static Specification<KnownReport> hasReportName(String reportName) {
		if (reportName == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_REPORT_ID).get(FILTER_REPORT_NAME), reportName);
	}

	public static Specification<KnownReport> hasReportType(String reportType) {
		if (reportType == null) {
			return null;
		}
		return (event, cq, cb) -> cb.equal(event.get(FILTER_KNOWN_REPORT_ID).get(FILTER_REPORT_TYPE), reportType);
	}

	public static Specification<KnownReport> search(KnownReportDto dto) {
		return Specification.where(KnownReportSpecification.hasReportName(dto.getReportName()))
				.and(KnownReportSpecification.hasReportType(dto.getReportType()));
	}

	public static Sort defaultSort() {
		return DEFAULT_SORT;
	}

}
