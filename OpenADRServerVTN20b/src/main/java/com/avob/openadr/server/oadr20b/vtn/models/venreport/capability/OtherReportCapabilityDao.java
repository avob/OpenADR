package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface OtherReportCapabilityDao extends ReportCapabilityDao<OtherReportCapability> {

	public List<OtherReportCapability> findBySource(Ven source);

	public Page<OtherReportCapability> findBySource(Ven source, Pageable page);

	@Query(value = "select report from OtherReportCapability report where report.source.username in :username")
	public List<OtherReportCapability> findBySourceUsernameIn(List<String> username);

	public OtherReportCapability findOneBySourceUsernameAndReportSpecifierId(String venID, String reportSpecifierId);

	public List<OtherReportCapability> findBySourceUsernameInAndReportSpecifierId(List<String> username,
			String reportSpecifierId);
}
