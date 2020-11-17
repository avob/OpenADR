package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface OtherReportRequestDao extends ReportRequestDao<OtherReportRequest> {

	public List<OtherReportRequest> findBySourceAndReportRequestIdIn(Ven ven, List<String> reportRequestId);

	public List<OtherReportRequest> findBySourceAndOtherReportCapability_ReportSpecifierId(Ven ven,
			String reportSpecifierId);

	public List<OtherReportRequest> findBySource(Ven ven);

	public Page<OtherReportRequest> findBySource(Ven ven, Pageable page);

	@Query("select req from OtherReportRequest req left join req.otherReportCapability cap where req.source = :ven and cap.reportSpecifierId like :reportSpecifierId%")
	public List<OtherReportRequest> findBySourceAndOtherReportCapability_ReportSpecifierIdStartingWith(
			@Param("ven") Ven ven, @Param("reportSpecifierId") String reportSpecifierId);

	@Query("select req from OtherReportRequest req where req.source = :ven and req.reportRequestId = :reportRequestId")
	public List<OtherReportRequest> findBySourceAndOtherReportCapability_ReportRequestId(@Param("ven") Ven ven,
			@Param("reportRequestId") String reportRequestId);

	@Transactional(readOnly = false)
	public void deleteByOtherReportCapabilitySource(Ven source);

	@Transactional(readOnly = false)
	public void deleteByRequestorAndOtherReportCapabilitySourceUsername(AbstractUser requestor, String username);

	public OtherReportRequest findOneBySourceAndReportRequestId(Ven ven, String reportRequestId);

}
