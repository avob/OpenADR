package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatus;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportDataPayloadResourceStatusService extends GenericService<OtherReportDataPayloadResourceStatus> {

	@Resource
	private OtherReportDataPayloadResourceStatusDao otherReportDataDao;

	public List<OtherReportDataPayloadResourceStatus> findByReportSpecifierId(String reportSpecifierId) {
		return otherReportDataDao.findByReportSpecifierId(reportSpecifierId);
	}

	public List<OtherReportDataPayloadResourceStatus> findByReportSpecifierIdAndRid(String reportSpecifierId,
			String rid) {
		return otherReportDataDao.findByReportSpecifierIdAndRid(reportSpecifierId, rid);
	}

	@Override
	public CrudRepository<OtherReportDataPayloadResourceStatus, Long> getDao() {
		return otherReportDataDao;
	}

}
