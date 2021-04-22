package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OtherReportRequestSpecifierDao extends PagingAndSortingRepository<OtherReportRequestSpecifier, Long>,
		JpaSpecificationExecutor<OtherReportRequestSpecifier> {

	public List<OtherReportRequestSpecifier> findByVenIdAndReportRequestId(String venId, String reportRequestId);

	@Transactional(readOnly = false)
	public void deleteByVenIdAndReportSpecifierIdAndRid(String venId, String reportSpecifierId, String rid);

	@Transactional(readOnly = false)
	public void deleteByVenIdAndReportRequestId(String venId, String reportRequestId);

	@Transactional(readOnly = false)
	public void deleteByVenIdAndReportRequestIdIn(String venId, List<String> reportRequestIds);

	@Transactional(readOnly = false)
	public void deleteByVenId(String venId);
}
