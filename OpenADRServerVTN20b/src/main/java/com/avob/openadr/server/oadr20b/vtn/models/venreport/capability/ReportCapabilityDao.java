package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReportCapabilityDao<T extends ReportCapability> extends CrudRepository<T, Long> {

	public List<T> findByReportSpecifierId(String reportSpecifierId);

	public List<T> findByReportSpecifierIdStartingWith(String reportSpecifierId);

	public List<T> findByReportRequestIdIn(List<String> reportRequestId);
}
