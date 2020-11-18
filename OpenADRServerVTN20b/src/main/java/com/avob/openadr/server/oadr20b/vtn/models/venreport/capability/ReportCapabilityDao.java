package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ReportCapabilityDao<T extends ReportCapability> extends PagingAndSortingRepository<T, Long> {

	public List<T> findByReportSpecifierId(String reportSpecifierId);

	public List<T> findByReportSpecifierIdStartingWith(String reportSpecifierId);

}
