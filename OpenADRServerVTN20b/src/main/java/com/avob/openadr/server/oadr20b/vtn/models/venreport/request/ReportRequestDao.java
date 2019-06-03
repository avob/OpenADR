package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface ReportRequestDao<T extends ReportRequest> extends PagingAndSortingRepository<T, Long> {

	public T findOneByReportRequestId(String reportRequestId);

}
