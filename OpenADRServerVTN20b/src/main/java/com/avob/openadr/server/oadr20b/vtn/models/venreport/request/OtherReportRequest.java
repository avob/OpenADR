package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

@Entity
@Table(name = "otherreportrequest")
public class OtherReportRequest extends ReportRequest {

	@ManyToOne
	@JoinColumn(name = "otherreportcapability_id")
	private OtherReportCapability otherReportCapability;

	@ManyToOne
	@JoinColumn(name = "otherreportcapabilitydescription_id")
	private OtherReportCapabilityDescription otherReportCapabilityDescription;

	@ManyToOne
	@JoinColumn(name = "ven_id")
	private Ven source;

	@ManyToOne
	@JoinColumn(name = "requestor_id")
	private AbstractUser requestor;

	public OtherReportRequest() {
	}

	public OtherReportRequest(Ven ven, OtherReportCapability otherReportCapability,
			OtherReportCapabilityDescription otherReportCapabilityDescription, String reportRequestId,
			String granularity, String reportBackDuration) {
		super();
		this.setSource(ven);
		this.setOtherReportCapability(otherReportCapability);
		this.setOtherReportCapabilityDescription(otherReportCapabilityDescription);
		this.setReportRequestId(reportRequestId);
		this.setGranularity(granularity);
		this.setReportBackDuration(reportBackDuration);

	}

	public OtherReportCapability getOtherReportCapability() {
		return otherReportCapability;
	}

	private void setOtherReportCapability(OtherReportCapability otherReportCapability) {
		this.otherReportCapability = otherReportCapability;
	}

	public OtherReportCapabilityDescription getOtherReportCapabilityDescription() {
		return otherReportCapabilityDescription;
	}

	private void setOtherReportCapabilityDescription(
			OtherReportCapabilityDescription otherReportCapabilityDescription) {
		this.otherReportCapabilityDescription = otherReportCapabilityDescription;
	}

	public Ven getSource() {
		return source;
	}

	private void setSource(Ven source) {
		this.source = source;
	}

}
