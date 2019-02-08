package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescriptionDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class SelfReportCapabilityDescriptionService extends GenericService<SelfReportCapabilityDescription> {

    @Resource
    private SelfReportCapabilityDescriptionDao selfReportCapabilityDescriptionDao;

    public SelfReportCapabilityDescription findBySelfReportCapabilityAndRid(SelfReportCapability selfReportCapability,
            String rid) {
        return selfReportCapabilityDescriptionDao.findOneBySelfReportCapabilityAndRid(selfReportCapability, rid);
    }

    public List<SelfReportCapabilityDescription> findBySelfReportCapability(SelfReportCapability selfReportCapability) {
        return selfReportCapabilityDescriptionDao.findBySelfReportCapability(selfReportCapability);
    }

    @Override
    public CrudRepository<SelfReportCapabilityDescription, Long> getDao() {
        return selfReportCapabilityDescriptionDao;
    }

}
