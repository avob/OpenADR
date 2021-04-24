package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.TargetTypeEnum;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContextReport;
import com.avob.openadr.server.common.vtn.models.venresource.VenResource;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.oadr20b.vtn.models.VenResourceFactory;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;

@Service
public class VenResourceCreateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(VenResourceCreateService.class);

	@Resource
	protected VenRequestReportService venRequestReportService;

	@Resource
	protected VenResourceService venResourceService;

	@Resource
	protected OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	protected OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	protected OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	public void createResourceTree(Ven ven) throws Oadr20bApplicationLayerException {

		List<OtherReportCapability> capabilities = otherReportCapabilityService.findBySource(ven);
		Map<String, List<String>> capabilityToDesc = new HashMap<>();
		List<OtherReportCapabilityDescription> descriptions = new ArrayList<>();
		for (OtherReportCapability report : capabilities) {
			List<String> rids = new ArrayList<>();
			List<OtherReportCapabilityDescription> findByOtherReportCapability = otherReportCapabilityDescriptionService
					.findByOtherReportCapability(report);
			for (Iterator<OtherReportCapabilityDescription> iterator = findByOtherReportCapability.iterator(); iterator
					.hasNext();) {
				OtherReportCapabilityDescription otherReportCapabilityDescription = iterator.next();
				descriptions.add(otherReportCapabilityDescription);
				rids.add(otherReportCapabilityDescription.getRid());
			}
			capabilityToDesc.put(report.getReportSpecifierId(), rids);
		}
		this.createResourceTree(ven, capabilities, descriptions, capabilityToDesc);

	}

	public void createResourceTree(Ven ven, Iterable<OtherReportCapability> capabilities,
			Iterable<OtherReportCapabilityDescription> descriptions, Map<String, List<String>> capabilityToDesc)
			throws Oadr20bApplicationLayerException {

		Map<String, VenMarketContextReport> venMarketContextReports = ven.getVenMarketContexts().stream()
				.map(VenMarketContext::getReports).flatMap(Collection::stream)
				.collect(Collectors.toMap(this::getReportKey, Function.identity()));

		Map<String, OtherReportCapability> capMap = StreamSupport.stream(capabilities.spliterator(), false)
				.collect(Collectors.toMap(OtherReportCapability::getReportSpecifierId, cap -> cap));

		Map<String, OtherReportCapabilityDescription> deskMap = StreamSupport.stream(descriptions.spliterator(), false)
				.collect(Collectors.toMap(
						desc -> desc.getOtherReportCapability().getReportSpecifierId() + desc.getRid(), desc -> desc));
		List<VenResource> venChildren = new ArrayList<>();

		List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions = new ArrayList<>();

		for (Entry<String, List<String>> entry : capabilityToDesc.entrySet()) {

			OtherReportCapability cap = capMap.get(entry.getKey());
			Map<String, List<String>> resourceIdToEndDeviceAssetMap = new HashMap<>();
			Map<String, List<VenResource>> endDeviceAssetMap = new HashMap<>();
			List<OtherReportCapabilityDescription> toSubscribe = new ArrayList<>();

			for (String rid : entry.getValue()) {

				OtherReportCapabilityDescription desc = deskMap.get(entry.getKey() + rid);

				String endDeviceAsset = "NotSpecified";
				String resourceId = "NotSpecified";
				if (desc.getEiSubject() != null) {

					Set<Target> createTargetList = desc.getEiSubject();

					List<Target> collect = createTargetList.stream()
							.filter(t -> TargetTypeEnum.ENDDEVICE_ASSET.equals(t.getTargetType()))
							.collect(Collectors.toList());

					if (collect.size() != collect.size() || createTargetList.size() > 1) {
						LOGGER.warn(String.format(
								"reportSpecifierID: %s, rID: %s reportSubject is malformed: should contains at most 'endDeviceAsset' target",
								cap.getReportSpecifierId(), rid));
					} else if (!collect.isEmpty()) {
						endDeviceAsset = collect.get(0).getTargetId();
					}

				}

				if (desc.getEiDatasource() != null) {

					Set<Target> createTargetList = desc.getEiDatasource();

					List<Target> collect = createTargetList.stream()
							.filter(t -> TargetTypeEnum.RESOURCE.equals(t.getTargetType()))
							.collect(Collectors.toList());

					if (collect.size() > 1) {
						LOGGER.warn(String.format(
								"reportSpecifierID: %s, rID: %s reportDatasource is malformed: should contains at most one 'resourceId' target",
								cap.getReportSpecifierId(), rid));
					} else if (!collect.isEmpty()) {
						resourceId = collect.get(0).getTargetId();
					}

				}

				

				String key = getReportKey(cap, desc);
				if (venMarketContextReports.containsKey(key)) {
					toSubscribe.add(desc);
					
					VenResource createReportDescription = VenResourceFactory.createReportDescription(ven, desc);

					List<String> endDeviceAssetListId = resourceIdToEndDeviceAssetMap.get(resourceId);
					if (endDeviceAssetListId == null) {
						endDeviceAssetListId = new ArrayList<>();
					}
					if (!endDeviceAssetListId.contains(endDeviceAsset)) {
						endDeviceAssetListId.add(endDeviceAsset);
					}
					resourceIdToEndDeviceAssetMap.put(resourceId, endDeviceAssetListId);

					List<VenResource> endDeviceAssetList = endDeviceAssetMap.get(resourceId + endDeviceAsset);
					if (endDeviceAssetList == null) {
						endDeviceAssetList = new ArrayList<>();
					}
					endDeviceAssetList.add(createReportDescription);
					endDeviceAssetMap.put(resourceId + endDeviceAsset, endDeviceAssetList);
				}

			}

			subscriptions.addAll(
					this.autoSubscribeStrategy(toSubscribe, UUID.randomUUID().toString(), cap.getReportSpecifierId()));

			List<VenResource> reportChildren = new ArrayList<>();
			Set<Entry<String, List<String>>> entrySet = resourceIdToEndDeviceAssetMap.entrySet();
			for (Entry<String, List<String>> resourceIdEntry : entrySet) {

				String resourceId = resourceIdEntry.getKey();
				List<String> associatedEndDeviceAsset = resourceIdEntry.getValue();
				List<VenResource> resourceChildren = new ArrayList<>();
				if (associatedEndDeviceAsset != null) {
					for (String endDeviceAsset : associatedEndDeviceAsset) {

						List<VenResource> reportDescriptionResource = endDeviceAssetMap
								.get(resourceId + endDeviceAsset);
						if (reportDescriptionResource != null) {
							VenResource createEndDeviceAsset = VenResourceFactory.createEndDeviceAsset(ven,
									endDeviceAsset, new HashSet<>(reportDescriptionResource));
							resourceChildren.add(createEndDeviceAsset);
						}

					}
					if (!resourceChildren.isEmpty()) {
						VenResource createResource = VenResourceFactory.createResource(ven, resourceId,
								new HashSet<>(resourceChildren));
						reportChildren.add(createResource);
					}
				}

			}

			VenResource createReport = VenResourceFactory.createReport(ven, cap, new HashSet<>(reportChildren));
			venChildren.add(createReport);
		}
		venResourceService.deleteByVen(ven);
		VenResource createVen = VenResourceFactory.createVen(ven, new HashSet<>(venChildren));
		venResourceService.save(createVen);

		List<OtherReportRequest> findByRequestorIsNullAndSource = otherReportRequestService
				.findByRequestorIsNullAndSource(ven);

		if(!findByRequestorIsNullAndSource.isEmpty()) {
			venRequestReportService.unsubscribe(ven, findByRequestorIsNullAndSource);
		}
		if (!subscriptions.isEmpty()) {
			venRequestReportService.subscribe(null, ven, subscriptions);
		}

	}

	private String getReportKey(VenMarketContextReport report) {
		String string = new StringBuilder().append("METADATA_").append(report.getReportName())
				.append(report.getReportType()).append(report.getReadingType()).toString();
		return string;
	}

	private String getReportKey(OtherReportCapability oadrReportType,
			OtherReportCapabilityDescription oadrReportDescriptionType) {
		String string = new StringBuilder().append(oadrReportType.getReportName())
				.append(oadrReportDescriptionType.getReportType().value())
				.append(oadrReportDescriptionType.getReadingType().value()).toString();
		return string;
	}

	private List<OtherReportRequestDtoCreateSubscriptionDto> autoSubscribeStrategy(
			List<OtherReportCapabilityDescription> toSubscribe, String reportRequestId, String reportSpecifierId) {
		List<OtherReportRequestDtoCreateSubscriptionDto> res = new ArrayList<>();

		Map<String, List<OtherReportCapabilityDescription>> groupByMinPeriod = toSubscribe.stream().filter(d -> {
			return !d.getSamplingRate().isOadrOnChange();
		}).collect(Collectors.groupingBy(d -> {
			return d.getSamplingRate().getOadrMinPeriod();
		}));

		for (Entry<String, List<OtherReportCapabilityDescription>> entry : groupByMinPeriod.entrySet()) {
			String minPeriod = entry.getKey();
			Map<String, Boolean> rids = entry.getValue().stream().map(d -> {
				return d.getRid();
			}).collect(Collectors.toMap(Function.identity(), d -> {
				return true;
			}));
			OtherReportRequestDtoCreateSubscriptionDto otherReportRequestDtoCreateSubscriptionDto = new OtherReportRequestDtoCreateSubscriptionDto();
			otherReportRequestDtoCreateSubscriptionDto.setGranularity(minPeriod);
			otherReportRequestDtoCreateSubscriptionDto.setReportBackDuration("PT1M");
			otherReportRequestDtoCreateSubscriptionDto.setReportRequestId(reportRequestId);
			otherReportRequestDtoCreateSubscriptionDto.setReportSpecifierId(reportSpecifierId);
			otherReportRequestDtoCreateSubscriptionDto.setRid(rids);
			res.add(otherReportRequestDtoCreateSubscriptionDto);
		}
		return res;
	}

}
