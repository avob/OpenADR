package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

public class OtherReportRequestSpecifierSpecification {
	static public Specification<OtherReportRequestSpecifier> hasReportRpecifierIdIn(List<String> reportSpecifierId) {
		return (ven, cq, cb) -> {
			Join<OtherReportRequestSpecifier, OtherReportRequest> joinRequest = ven.join("request", JoinType.INNER);
			Join<OtherReportRequestSpecifier, OtherReportCapability> joinCapability = joinRequest
					.join("otherReportCapability", JoinType.INNER);

			return joinCapability.get("reportSpecifierId").in(reportSpecifierId);
//			return joinRequest.get("otherReportCapability.reportSpecifierId").in(reportSpecifierId);
		};
	}

	static public Specification<OtherReportRequestSpecifier> hasReportRequestIdIn(List<String> reportRequestId) {
		return (ven, cq, cb) -> {
			Join<OtherReportRequestSpecifier, OtherReportRequest> joinRequest = ven.join("request", JoinType.INNER);
			return joinRequest.get("reportRequestId").in(reportRequestId);
		};
	}

	static public Specification<OtherReportRequestSpecifier> hasRidIdIn(List<String> rid) {
		return (ven, cq, cb) -> {
			Join<OtherReportRequestSpecifier, OtherReportCapabilityDescription> joinRequest = ven
					.join("otherReportCapabilityDescription", JoinType.INNER);
			return joinRequest.get("rid").in(rid);
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
