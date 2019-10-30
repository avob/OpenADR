package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;

@Entity
@Table(name = "otherreportrequest")
public class OtherReportRequest extends ReportRequest {

	@ManyToOne
	@JoinColumn(name = "otherreportcapability_id")
	private OtherReportCapability otherReportCapability;

	@ManyToOne
	@JoinColumn(name = "ven_id")
	private Ven source;

	@ManyToOne
	@JoinColumn(name = "requestor_id")
	private AbstractUser requestor;

	public OtherReportCapability getOtherReportCapability() {
		return otherReportCapability;
	}

	public void setOtherReportCapability(OtherReportCapability otherReportCapability) {
		this.otherReportCapability = otherReportCapability;
	}

	public Ven getSource() {
		return source;
	}

	public void setSource(Ven source) {
		this.source = source;
	}

	public AbstractUser getRequestor() {
		return requestor;
	}

	public void setRequestor(AbstractUser requestor) {
		this.requestor = requestor;
	}

}
