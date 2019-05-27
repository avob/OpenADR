package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;

@Entity
@Table(name = "selfreportcapabilitydescription")
public class SelfReportCapabilityDescription extends ReportCapabilityDescription {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "selfreportcapability_id")
	private SelfReportCapability selfReportCapability;

	@ManyToOne
	@JoinColumn(name = "marketcontext_id")
	private VenMarketContext venMarketContext;

	@Lob
    private String payload;

	public SelfReportCapability getSelfReportCapability() {
		return selfReportCapability;
	}

	public void setSelfReportCapability(SelfReportCapability selfReportCapability) {
		this.selfReportCapability = selfReportCapability;
	}

	public VenMarketContext getVenMarketContext() {
		return venMarketContext;
	}

	public void setVenMarketContext(VenMarketContext venMarketContext) {
		this.venMarketContext = venMarketContext;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
