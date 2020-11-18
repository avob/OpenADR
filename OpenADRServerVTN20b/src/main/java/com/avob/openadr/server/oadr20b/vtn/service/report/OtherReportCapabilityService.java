package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportCapabilityService extends GenericService<OtherReportCapability> {

	@Resource
	private OtherReportCapabilityDao otherReportCapabilityDao;

	@Transactional
	public List<OtherReportCapability> findByReportSpecifierIdStartingWith(String reportSpecifierId) {
		return otherReportCapabilityDao.findByReportSpecifierIdStartingWith(reportSpecifierId);
	}

	@Transactional
	public List<OtherReportCapability> findByReportSpecifierId(String reportSpecifierId) {
		return otherReportCapabilityDao.findByReportSpecifierId(reportSpecifierId);
	}

	@Transactional
	public List<OtherReportCapability> findBySource(Ven source) {
		return otherReportCapabilityDao.findBySource(source);
	}

	@Transactional
	public Page<OtherReportCapability> findBySource(Ven source, Integer page, Integer size) {
		return otherReportCapabilityDao.findBySource(source, PageRequest.of(page, size));
	}

	@Transactional
	public List<OtherReportCapability> findBySourceUsernameIn(List<String> username) {
		return otherReportCapabilityDao.findBySourceUsernameIn(username);
	}

	@Transactional
	public OtherReportCapability findOneBySourceUsernameAndReportSpecifierId(String venID, String reportSpecifierID) {
		return otherReportCapabilityDao.findOneBySourceUsernameAndReportSpecifierId(venID, reportSpecifierID);
	}

	@Transactional
	public List<OtherReportCapability> findBySourceUsernameInAndReportSpecifierId(List<String> venID,
			String reportSpecifierID) {
		return otherReportCapabilityDao.findBySourceUsernameInAndReportSpecifierId(venID, reportSpecifierID);
	}

	@Override
	public CrudRepository<OtherReportCapability, Long> getDao() {
		return otherReportCapabilityDao;
	}

}
