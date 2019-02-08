package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;

@Entity
@Table(name = "selfreportrequest")
public class SelfReportRequest extends ReportRequest {

    @ManyToOne
    @JoinColumn(name = "selfreportcapability_id")
    private SelfReportCapability selfReportCapability;

    @ManyToOne
    @JoinColumn(name = "selfreportcapabilitydescription_id")
    private SelfReportCapabilityDescription selfReportCapabilityDescription;

    @ManyToOne
    @JoinColumn(name = "ven_id")
    private Ven target;

    public SelfReportRequest() {
    }

    public SelfReportRequest(Ven ven, SelfReportCapability selfReportCapability,
            SelfReportCapabilityDescription selfReportCapabilityDescription) {
        this.setTarget(ven);
        this.setSelfReportCapability(selfReportCapability);
        this.setSelfReportCapabilityDescription(selfReportCapabilityDescription);
    }

    public SelfReportCapability getSelfReportCapability() {
        return selfReportCapability;
    }

    private void setSelfReportCapability(SelfReportCapability selfReportCapability) {
        this.selfReportCapability = selfReportCapability;
    }

    public SelfReportCapabilityDescription getSelfReportCapabilityDescription() {
        return selfReportCapabilityDescription;
    }

    private void setSelfReportCapabilityDescription(SelfReportCapabilityDescription selfReportCapabilityDescription) {
        this.selfReportCapabilityDescription = selfReportCapabilityDescription;
    }

    public Ven getTarget() {
        return target;
    }

    private void setTarget(Ven target) {
        this.target = target;
    }

}
