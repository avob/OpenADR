package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import java.util.List;

public interface OtherReportCapabilityDescriptionDao
		extends ReportCapabilityDescriptionDao<OtherReportCapabilityDescription> {

	public List<OtherReportCapabilityDescription> findByOtherReportCapabilityAndRidIn(
			OtherReportCapability otherReportCapability, List<String> rid);

	public OtherReportCapabilityDescription findOneByOtherReportCapabilityAndRid(
			OtherReportCapability otherReportCapability, String rid);

	public List<OtherReportCapabilityDescription> findByOtherReportCapability(
			OtherReportCapability otherReportCapability);

	public List<OtherReportCapabilityDescription> findByOtherReportCapabilityIn(
			List<OtherReportCapability> otherReportCapabilities);

}
