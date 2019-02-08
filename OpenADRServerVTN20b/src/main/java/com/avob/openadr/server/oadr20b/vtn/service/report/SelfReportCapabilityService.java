package com.avob.openadr.server.oadr20b.vtn.service.report;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class SelfReportCapabilityService extends GenericService<SelfReportCapability> {

    @Resource
    private SelfReportCapabilityDao selfReportCapabilityDao;

    public SelfReportCapability findByReportSpecifierId(String reportSpecifierId) {
        return selfReportCapabilityDao.findOneByReportSpecifierId(reportSpecifierId);
    }

    @Override
    public CrudRepository<SelfReportCapability, Long> getDao() {
        return selfReportCapabilityDao;
    }

}
