package com.avob.openadr.server.oadr20b.vtn.models;

import java.util.Set;
import java.util.stream.Collectors;

import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.models.venresource.VenResourceType;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;

public class VenResourceFactory {

	public static VenResource createVen(Ven ven, Set<VenResource> children) {
		return new VenResource(
				VenResourceType.VEN, ven.getId(), ven.getCommonName(), ven, children.stream()
						.map(VenResource::getReportDescriptionCount).collect(Collectors.summingLong(Long::longValue)),
				children, null);
	}

	public static VenResource createReport(Ven ven, OtherReportCapability capability, Set<VenResource> children) {
		return new VenResource(
				VenResourceType.REPORT, capability.getId(), capability.getReportSpecifierId(), ven, children.stream()
						.map(VenResource::getReportDescriptionCount).collect(Collectors.summingLong(Long::longValue)),
				children, null);
	}

	public static VenResource createEndDeviceAsset(Ven ven, String endDeviceAsset, Set<VenResource> children) {
		return new VenResource(VenResourceType.ENDDEVICEASSET, null, endDeviceAsset, ven, Long.valueOf(children.size()),
				children, null);
	}

	public static VenResource createResource(Ven ven, String resourceId, Set<VenResource> children) {
		return new VenResource(
				VenResourceType.RESOURCE, null, resourceId, ven, children.stream()
						.map(VenResource::getReportDescriptionCount).collect(Collectors.summingLong(Long::longValue)),
				children, null);
	}

	public static VenResource createReportDescription(Ven ven, OtherReportCapabilityDescription description) {
		return new VenResource(VenResourceType.REPORT_DESCRIPTION, description.getId(), description.getRid(), ven, 1L,
				null, null);
	}

}
