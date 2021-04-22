package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class OtherReportRequestSpecifierSpecification {
	static public Specification<OtherReportRequestSpecifier> hasReportRpecifierIdIn(List<String> reportSpecifierId) {
		return (ven, cq, cb) -> {
			return ven.get("reportSpecifierId").in(reportSpecifierId);
		};
	}

	static public Specification<OtherReportRequestSpecifier> hasReportRequestIdIn(List<String> reportRequestId) {
		return (ven, cq, cb) -> {
			return ven.get("reportRequestId").in(reportRequestId);
		};
	}

	static public Specification<OtherReportRequestSpecifier> hasRidIdIn(List<String> rid) {
		return (ven, cq, cb) -> {
			return ven.get("rid").in(rid);
		};
	}

	static public Specification<OtherReportRequestSpecifier> search(
			OtherReportRequestSpecifierSearchCriteria criteria) {

		Specification<OtherReportRequestSpecifier> spec = null;
		if (criteria.getReportRequestId() != null && !criteria.getReportRequestId().isEmpty()) {
			Specification<OtherReportRequestSpecifier> hasReportRequestIdIn = OtherReportRequestSpecifierSpecification
					.hasReportRequestIdIn(criteria.getReportRequestId());
			spec = hasReportRequestIdIn;
		}
		if (criteria.getReportSpecifierId() != null && !criteria.getReportSpecifierId().isEmpty()) {
			Specification<OtherReportRequestSpecifier> hasReportRpecifierIdIn = OtherReportRequestSpecifierSpecification
					.hasReportRpecifierIdIn(criteria.getReportSpecifierId());
			spec = (spec == null) ? hasReportRpecifierIdIn : spec.and(hasReportRpecifierIdIn);
		}
		if (criteria.getRid() != null && !criteria.getRid().isEmpty()) {
			Specification<OtherReportRequestSpecifier> hasRidIdIn = OtherReportRequestSpecifierSpecification
					.hasRidIdIn(criteria.getRid());
			spec = (spec == null) ? hasRidIdIn : spec.and(hasRidIdIn);
		}

		return spec;
	}
}
