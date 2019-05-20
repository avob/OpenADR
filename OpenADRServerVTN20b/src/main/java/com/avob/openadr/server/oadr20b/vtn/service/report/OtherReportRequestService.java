package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportRequestService extends GenericService<OtherReportRequest> {

	@Resource
	private OtherReportRequestDao otherReportRequestDao;

	public OtherReportRequest findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(Ven ven,
			OtherReportCapabilityDescription otherReportCapabilityDescription, String reportRequestId) {
		return otherReportRequestDao.findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(ven,
				otherReportCapabilityDescription, reportRequestId);
	}

	public List<OtherReportRequest> findByReportRequestId(String reportRequestId) {
		return otherReportRequestDao.findByReportRequestId(reportRequestId);
	}

	public List<OtherReportRequest> findBySourceAndReportRequestIdIn(Ven ven, List<String> reportRequestId) {
		return otherReportRequestDao.findBySourceAndReportRequestIdIn(ven, reportRequestId);
	}

	public List<OtherReportRequest> findBySource(Ven ven) {
		return otherReportRequestDao.findBySource(ven);
	}

	public List<OtherReportRequest> findBySourceAndReportSpecifierId(Ven ven, String reportSpecifierId) {
		return otherReportRequestDao.findBySourceAndOtherReportCapability_ReportSpecifierId(ven, reportSpecifierId);
	}

	public List<OtherReportRequest> findBySourceAndReportRequestId(Ven ven, String reportRequestId) {
		return otherReportRequestDao.findBySourceAndOtherReportCapability_ReportRequestId(ven, reportRequestId);
	}

	public List<OtherReportRequest> findBySourceAndReportSpecifierIdStartingWith(Ven ven, String reportSpecifierId) {
		return otherReportRequestDao.findBySourceAndOtherReportCapability_ReportSpecifierIdStartingWith(ven,
				reportSpecifierId);
	}

	public void deleteByOtherReportCapabilityDescriptionIn(Collection<OtherReportCapabilityDescription> descriptions) {
		otherReportRequestDao.deleteByOtherReportCapabilityDescriptionIn(descriptions);
	}
	
	public void deleteByOtherReportCapabilitySource(Ven source) {
		otherReportRequestDao.deleteByOtherReportCapabilitySource(source);
	}

	@Override
	public CrudRepository<OtherReportRequest, Long> getDao() {
		return otherReportRequestDao;
	}

}
