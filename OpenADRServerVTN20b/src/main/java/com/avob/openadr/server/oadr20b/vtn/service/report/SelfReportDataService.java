package com.avob.openadr.server.oadr20b.vtn.service.report;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.SelfReportData;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.SelfReportDataDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class SelfReportDataService extends GenericService<SelfReportData> {

    @Resource
    private SelfReportDataDao selfReportDataDao;

    @Override
    public CrudRepository<SelfReportData, Long> getDao() {
        return selfReportDataDao;
    }

}
