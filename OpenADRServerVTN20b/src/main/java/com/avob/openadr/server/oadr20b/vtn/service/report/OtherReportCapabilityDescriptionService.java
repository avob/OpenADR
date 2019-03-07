package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportCapabilityDescriptionService extends GenericService<OtherReportCapabilityDescription> {

	@Resource
	private OtherReportCapabilityDescriptionDao otherReportCapabilityDescriptionDao;

	public List<OtherReportCapabilityDescription> findByOtherReportCapabilityAndRidIn(
			OtherReportCapability otherReportCapability, List<String> rid) {
		return otherReportCapabilityDescriptionDao.findByOtherReportCapabilityAndRidIn(otherReportCapability, rid);
	}

	public OtherReportCapabilityDescription findByOtherReportCapabilityAndRid(
			OtherReportCapability otherReportCapability, String rid) {
		return otherReportCapabilityDescriptionDao.findOneByOtherReportCapabilityAndRid(otherReportCapability, rid);
	}

	public List<OtherReportCapabilityDescription> findByOtherReportCapability(
			OtherReportCapability otherReportCapability) {
		return otherReportCapabilityDescriptionDao.findByOtherReportCapability(otherReportCapability);
	}

	public List<OtherReportCapabilityDescription> findByOtherReportCapabilityIn(
			List<OtherReportCapability> otherReportCapabilities) {
		return otherReportCapabilityDescriptionDao.findByOtherReportCapabilityIn(otherReportCapabilities);
	}

	@Override
	public CrudRepository<OtherReportCapabilityDescription, Long> getDao() {
		return otherReportCapabilityDescriptionDao;
	}

}
