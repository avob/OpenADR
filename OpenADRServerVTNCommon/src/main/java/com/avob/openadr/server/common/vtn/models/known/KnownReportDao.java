package com.avob.openadr.server.common.vtn.models.known;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KnownReportDao
		extends CrudRepository<KnownReport, KnownReportId>, JpaSpecificationExecutor<KnownReport> {

	@Query("select distinct r.knownReportId.reportName from KnownReport r")
	List<String> findReportName();

	@Query("select distinct r.knownReportId.reportType from KnownReport r where r.knownReportId.reportName = :reportName")
	List<String> findReportType(@Param("reportName") String reportName);

}
