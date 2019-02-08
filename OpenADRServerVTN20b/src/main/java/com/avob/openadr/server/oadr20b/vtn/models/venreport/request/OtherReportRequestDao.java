package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

public interface OtherReportRequestDao extends ReportRequestDao<OtherReportRequest> {

	public List<OtherReportRequest> findBySourceAndReportRequestIdIn(Ven ven, List<String> reportRequestId);

	public OtherReportRequest findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(Ven ven,
			OtherReportCapabilityDescription otherReportCapabilityDescription, String reportRequestId);

	public List<OtherReportRequest> findOneBySourceAndOtherReportCapability_ReportSpecifierId(Ven ven,
			String reportSpecifierId);

	public List<OtherReportRequest> findBySource(Ven ven);

	@Query("select req from OtherReportRequest req left join req.otherReportCapability cap where req.source = :ven and cap.reportSpecifierId like :reportSpecifierId%")
	public List<OtherReportRequest> findBySourceAndOtherReportCapability_ReportSpecifierIdStartingWith(
			@Param("ven") Ven ven, @Param("reportSpecifierId") String reportSpecifierId);

	@Transactional
	public void deleteByOtherReportCapabilityDescriptionIn(Collection<OtherReportCapabilityDescription> descriptions);
}
