package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyToken;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportDataKeyTokenService extends GenericService<OtherReportDataKeyToken> {

	@Resource
	private OtherReportDataKeyTokenDao otherReportDataDao;

	public List<OtherReportDataKeyToken> findByReportSpecifierId(String venId, String reportSpecifierId) {
		return otherReportDataDao.findByVenIdAndReportSpecifierId(venId, reportSpecifierId);
	}

	public List<OtherReportDataKeyToken> findByReportSpecifierIdAndRid(String venId, String reportSpecifierId,
			String rid) {
		return otherReportDataDao.findByVenIdAndReportSpecifierIdAndRid(venId, reportSpecifierId, rid);
	}

	@Override
	public CrudRepository<OtherReportDataKeyToken, Long> getDao() {
		return otherReportDataDao;
	}
}
