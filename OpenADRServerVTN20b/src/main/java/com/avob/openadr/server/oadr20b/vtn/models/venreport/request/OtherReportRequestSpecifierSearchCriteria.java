package com.avob.openadr.server.oadr20b.vtn.models.venreport.request;

import java.io.Serializable;
import java.util.List;

public class OtherReportRequestSpecifierSearchCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8204672022336744279L;

	private List<String> reportSpecifierId;
	private List<String> reportRequestId;
	private List<String> rid;
	public List<String> getReportSpecifierId() {
		return reportSpecifierId;
	}
	public void setReportSpecifierId(List<String> reportSpecifierId) {
		this.reportSpecifierId = reportSpecifierId;
	}
	public List<String> getReportRequestId() {
		return reportRequestId;
	}
	public void setReportRequestId(List<String> reportRequestId) {
		this.reportRequestId = reportRequestId;
	}
	public List<String> getRid() {
		return rid;
	}
	public void setRid(List<String> rid) {
		this.rid = rid;
	}
}
