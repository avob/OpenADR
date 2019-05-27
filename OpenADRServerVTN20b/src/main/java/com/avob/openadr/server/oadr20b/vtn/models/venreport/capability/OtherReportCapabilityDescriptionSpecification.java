package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;

public class OtherReportCapabilityDescriptionSpecification {

	static public Specification<OtherReportCapabilityDescription> hasReportspecifierId(String reportSpecifierId) {
		return (ven, cq, cb) -> {
			Join<OtherReportCapabilityDescription, OtherReportCapability> join = ven.join("otherReportCapability",
					JoinType.INNER);
			return cb.equal(join.get("reportSpecifierId"), reportSpecifierId);
		};
	}

	static public Specification<OtherReportCapabilityDescription> hasReportName(ReportNameEnumeratedType reportName) {
		return (ven, cq, cb) -> {
			Join<OtherReportCapabilityDescription, OtherReportCapability> join = ven.join("otherReportCapability",
					JoinType.INNER);
			return cb.equal(join.get("reportName"), reportName);
		};
	}

	static public Specification<OtherReportCapabilityDescription> hasReadingType(
			ReadingTypeEnumeratedType readingType) {
		return (event, cq, cb) -> cb.equal(event.get("readingType"), readingType);
	}

	static public Specification<OtherReportCapabilityDescription> hasReportType(ReportEnumeratedType reportType) {
		return (event, cq, cb) -> cb.equal(event.get("reportType"), reportType);
	}

}
