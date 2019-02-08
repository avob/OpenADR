package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author bzanni
 *
 */
@Entity
@Table(name = "selfreportcapability")
public class SelfReportCapability extends ReportCapability {

    @OneToMany(mappedBy = "selfReportCapability", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SelfReportCapabilityDescription> selfReportCapabilityDescription;

    public Set<SelfReportCapabilityDescription> getSelfReportCapabilityDescription() {
        return selfReportCapabilityDescription;
    }

    public void setSelfReportCapabilityDescription(
            Set<SelfReportCapabilityDescription> selfReportCapabilityDescription) {
        this.selfReportCapabilityDescription = selfReportCapabilityDescription;
    }
}
