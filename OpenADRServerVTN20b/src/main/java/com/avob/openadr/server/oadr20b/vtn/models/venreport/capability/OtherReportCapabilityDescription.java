package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

@Entity
@Table(name = "otherreportcapabilitydescription")
public class OtherReportCapabilityDescription extends ReportCapabilityDescription {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "otherreportcapability_id")
    private OtherReportCapability otherReportCapability;

    @ManyToOne
    @JoinColumn(name = "marketcontext_id")
    private VenMarketContext venMarketContext;

    public OtherReportCapabilityDescription() {
    }

    public OtherReportCapabilityDescription(OtherReportCapability otherReportCapability) {
        this.otherReportCapability = otherReportCapability;
    }

    public OtherReportCapability getOtherReportCapability() {
        return otherReportCapability;
    }

    public void setOtherReportCapability(OtherReportCapability otherReportCapability) {
        this.otherReportCapability = otherReportCapability;
    }

    public VenMarketContext getVenMarketContext() {
        return venMarketContext;
    }

    public void setVenMarketContext(VenMarketContext venMarketContext) {
        this.venMarketContext = venMarketContext;
    }

}
