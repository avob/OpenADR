package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloat;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportDataFloatService extends GenericService<OtherReportDataFloat> {

	@Resource
	private OtherReportDataFloatDao otherReportDataDao;

	public List<OtherReportDataFloat> findByReportSpecifierId(String reportSpecifierId) {
		return otherReportDataDao.findByReportSpecifierId(reportSpecifierId);
	}

	public List<OtherReportDataFloat> findByReportSpecifierIdAndRid(String reportSpecifierId, String rid) {
		return otherReportDataDao.findByReportSpecifierIdAndRid(reportSpecifierId, rid);
	}

	@Override
	public CrudRepository<OtherReportDataFloat, Long> getDao() {
		return otherReportDataDao;
	}
}
