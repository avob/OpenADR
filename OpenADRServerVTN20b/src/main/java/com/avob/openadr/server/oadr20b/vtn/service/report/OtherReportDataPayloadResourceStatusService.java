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

	public List<OtherReportDataPayloadResourceStatus> findByReportSpecifierId(String venId, String reportSpecifierId) {
		return otherReportDataDao.findByVenIdAndReportSpecifierId(venId, reportSpecifierId);
	}

	public List<OtherReportDataPayloadResourceStatus> findByReportSpecifierIdAndRid(String venId,
			String reportSpecifierId, String rid) {
		return otherReportDataDao.findByVenIdAndReportSpecifierIdAndRid(venId, reportSpecifierId, rid);
	}

	@Override
	public CrudRepository<OtherReportDataPayloadResourceStatus, Long> getDao() {
		return otherReportDataDao;
	}

}
