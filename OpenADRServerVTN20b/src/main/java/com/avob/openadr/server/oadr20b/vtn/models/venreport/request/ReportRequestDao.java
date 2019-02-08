package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReportRequestDao<T extends ReportRequest> extends CrudRepository<T, Long> {
    
    public List<T> findByReportRequestId(String reportRequestId);

}
