package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

public interface SelfReportCapabilityDescriptionDao
        extends ReportCapabilityDescriptionDao<SelfReportCapabilityDescription> {

    public SelfReportCapabilityDescription findOneBySelfReportCapabilityAndRid(
            SelfReportCapability selfReportCapability, String rid);

    public  List<SelfReportCapabilityDescription> findBySelfReportCapability(SelfReportCapability selfReportCapability);

}
