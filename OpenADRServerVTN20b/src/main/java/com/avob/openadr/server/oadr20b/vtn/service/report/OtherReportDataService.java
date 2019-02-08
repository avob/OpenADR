package com.avob.openadr.server.oadr20b.vtn.service.report;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportData;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataDao;
import com.avob.openadr.server.oadr20b.vtn.service.GenericService;

@Service
public class OtherReportDataService extends GenericService<OtherReportData> {

    @Resource
    private OtherReportDataDao otherReportDataDao;

    public List<OtherReportData> findByReportSpecifierId(String reportSpecifierId) {
        return otherReportDataDao.findByReportSpecifierId(reportSpecifierId);
    }

    public List<OtherReportData> findByReportSpecifierIdAndRid(String reportSpecifierId, String rid) {
        return otherReportDataDao.findByReportSpecifierIdAndRid(reportSpecifierId, rid);
    }

    @Override
    public CrudRepository<OtherReportData, Long> getDao() {
        return otherReportDataDao;
    }
}
