package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface OtherReportCapabilityDao extends ReportCapabilityDao<OtherReportCapability> {
    
    public List<OtherReportCapability> findBySource(Ven source);
    
}
