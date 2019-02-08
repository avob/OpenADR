package com.avob.openadr.server.oadr20b.vtn.models.venreport.data;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReportDataDao<T extends ReportData> extends CrudRepository<T, Long> {

    public List<T> findByReportSpecifierId(String reportSpecifierId);

    public List<T> findByReportSpecifierIdAndRid(String reportSpecifierId, String rid);
}
