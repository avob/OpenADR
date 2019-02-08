package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ReportCapabilityDescriptionDao<T extends ReportCapabilityDescription> extends CrudRepository<T, Long> {

	public T findByRid(String rid);

	public Iterable<T> findByReportRequestIdIn(List<String> reportRequestId);
}
