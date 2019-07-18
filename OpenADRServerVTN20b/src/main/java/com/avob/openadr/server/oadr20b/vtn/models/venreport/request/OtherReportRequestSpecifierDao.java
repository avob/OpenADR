package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

public interface OtherReportRequestSpecifierDao extends PagingAndSortingRepository<OtherReportRequestSpecifier, Long>,
		JpaSpecificationExecutor<OtherReportRequestSpecifier> {

	public List<OtherReportRequestSpecifier> findByRequest(OtherReportRequest request);

	public List<OtherReportRequestSpecifier> findByRequestReportRequestId(String reportRequestId);

	public List<OtherReportRequestSpecifier> findByRequestSource(Ven source);

	public List<OtherReportRequestSpecifier> findByRequestSourceAndRequestReportRequestIdIn(Ven source,
			List<String> reportRequestId);

	public List<OtherReportRequestSpecifier> findByRequestSourceAndOtherReportCapabilityDescriptionRidIn(Ven source,
			List<String> rid);

	public List<OtherReportRequestSpecifier> findByRequestSourceAndRequestReportRequestIdInAndOtherReportCapabilityDescriptionRidIn(
			Ven source, List<String> reportRequestId, List<String> rid);

	@Transactional(readOnly = false)
	public void deleteByOtherReportCapabilityDescriptionIn(Collection<OtherReportCapabilityDescription> descs);

	@Transactional(readOnly = false)
	public void deleteByRequestReportRequestId(String reportRequestId);

	@Transactional(readOnly = false)
	public void deleteByOtherReportCapabilityDescriptionRidInAndOtherReportCapabilityDescriptionOtherReportCapability(
			Collection<String> rids, OtherReportCapability otherReportCapability);

	@Transactional(readOnly = false)
	public void deleteByRequest(OtherReportRequest request);

	public Long countByOtherReportCapabilityDescriptionOtherReportCapability(
			OtherReportCapability otherReportCapability);
}
