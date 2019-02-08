package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

@Entity
@Table(name = "otherreportcapability")
public class OtherReportCapability extends ReportCapability {

    @OneToMany(mappedBy = "otherReportCapability", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OtherReportCapabilityDescription> otherReportCapabilityDescription;

    @ManyToOne
    @JoinColumn(name = "ven_id")
    private Ven source;

    public OtherReportCapability() {
    }

    public OtherReportCapability(Ven ven) {
        this.source = ven;
    }

    public Ven getSource() {
        return source;
    }

    public void setSource(Ven source) {
        this.source = source;
    }

    public Set<OtherReportCapabilityDescription> getOtherReportCapabilityDescription() {
        return otherReportCapabilityDescription;
    }

    public void setOtherReportCapabilityDescription(
            Set<OtherReportCapabilityDescription> otherReportCapabilityDescription) {
        this.otherReportCapabilityDescription = otherReportCapabilityDescription;
    }

}
