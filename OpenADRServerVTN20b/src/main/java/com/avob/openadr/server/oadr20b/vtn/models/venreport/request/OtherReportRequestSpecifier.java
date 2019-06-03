package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

@Entity
@Table(name = "otherreportrequestspecifier")
public class OtherReportRequestSpecifier {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "request_id")
	private OtherReportRequest request;

	@ManyToOne
	@JoinColumn(name = "otherreportcapabilitydescription_id")
	private OtherReportCapabilityDescription otherReportCapabilityDescription;

	private Boolean archived;

	private Long lastUpdateDatetime;

	@Lob
	private String lastUpdateValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OtherReportRequest getRequest() {
		return request;
	}

	public void setRequest(OtherReportRequest request) {
		this.request = request;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public OtherReportCapabilityDescription getOtherReportCapabilityDescription() {
		return otherReportCapabilityDescription;
	}

	public void setOtherReportCapabilityDescription(OtherReportCapabilityDescription otherReportCapabilityDescription) {
		this.otherReportCapabilityDescription = otherReportCapabilityDescription;
	}

	public Long getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Long lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public String getLastUpdateValue() {
		return lastUpdateValue;
	}

	public void setLastUpdateValue(String lastUpdateValue) {
		this.lastUpdateValue = lastUpdateValue;
	}
}
