package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface SelfReportRequestDao extends ReportRequestDao<SelfReportRequest> {

    public List<SelfReportRequest> findByTargetAndReportRequestIdIn(Ven ven, List<String> reportRequestId);
    
    public List<SelfReportRequest> findByTarget(Ven ven);

}
