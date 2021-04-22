package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;

@Service
public class VenRequestReportService {

	@Resource
	protected OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	protected OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	private OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	protected VenDistributeService venDistributeService;

	/**
	 * @param requestor
	 * @param ven
	 * @param subscriptions
	 * @throws Oadr20bException
	 */
	public void subscribe(AbstractUser requestor, Ven ven,
			List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions) throws Oadr20bApplicationLayerException {
		this.subscribe(requestor, null, ven, subscriptions);
	}

	public void subscribe(AbstractUser requestor, VenMarketContext venMarketContext, Ven ven,
			List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions) throws Oadr20bApplicationLayerException {

		List<OtherReportRequest> requests = new ArrayList<>();
		List<OtherReportRequestSpecifier> specifiers = new ArrayList<>();
		for (OtherReportRequestDtoCreateSubscriptionDto subscription : subscriptions) {
			Long granularityMillisecond = null;
			Long reportBackDurationMillisecond = null;

			granularityMillisecond = Oadr20bFactory.xmlDurationToMillisecond(subscription.getGranularity());
			reportBackDurationMillisecond = Oadr20bFactory
					.xmlDurationToMillisecond(subscription.getReportBackDuration());

			if (granularityMillisecond == null || reportBackDurationMillisecond == null) {
				throw new IllegalArgumentException(
						"reportBackDuration and granularity must be valid xmlduration lexical representation (PnYnMnDTnHnMnS)");
			}

			OtherReportCapability reportCapability = otherReportCapabilityService
					.findOneBySourceUsernameAndReportSpecifierId(ven.getUsername(),
							subscription.getReportSpecifierId());

			OtherReportRequest otherReportRequest = new OtherReportRequest();
			otherReportRequest.setGranularity(subscription.getGranularity());
			otherReportRequest.setReportBackDuration(subscription.getReportBackDuration());
			otherReportRequest.setSource(ven);
			otherReportRequest.setOtherReportCapability(reportCapability);
			otherReportRequest.setRequestor(requestor);
			otherReportRequest.setVenMarketContextRequestor(venMarketContext);
			otherReportRequest.setReportRequestId(subscription.getReportRequestId());

			List<OtherReportCapabilityDescription> findByOtherReportCapability = otherReportCapabilityDescriptionService
					.findByOtherReportCapability(reportCapability);
			Map<String, OtherReportCapabilityDescription> descriptions = findByOtherReportCapability.stream()
					.collect(Collectors.toMap(OtherReportCapabilityDescription::getRid, Function.identity()));
			Map<String, Boolean> rids = subscription.getRid();
			if (rids == null) {
				rids = findByOtherReportCapability.stream()
						.collect(Collectors.toMap(OtherReportCapabilityDescription::getRid, p -> true));
			}
			for (Entry<String, Boolean> entry : rids.entrySet()) {

				if (descriptions.containsKey(entry.getKey())) {
					OtherReportCapabilityDescription desc = descriptions.get(entry.getKey());
					OtherReportRequestSpecifier otherReportRequestSpecifier = new OtherReportRequestSpecifier();
					otherReportRequestSpecifier.setArchived(entry.getValue());
					otherReportRequestSpecifier.setRid(entry.getKey());
					otherReportRequestSpecifier.setVenId(ven.getUsername());
					otherReportRequestSpecifier.setReportSpecifierId(reportCapability.getReportSpecifierId());
					otherReportRequestSpecifier.setReportRequestId(otherReportRequest.getReportRequestId());
					otherReportRequestSpecifier.setReadingType(desc.getReadingType());
					otherReportRequestSpecifier.setItemBase(desc.getItemBase());
					specifiers.add(otherReportRequestSpecifier);
				}

			}

			requests.add(otherReportRequest);
		}

		otherReportRequestService.save(requests);
		otherReportRequestSpecifierDao.saveAll(specifiers);

		venDistributeService.distributeSubscriptionOadrCreatedReportPayload(ven);

	}

	/**
	 * @param requestor
	 * @param ven
	 * @param dto
	 * @throws Oadr20bException
	 */
	public void request(AbstractUser requestor, Ven ven, List<OtherReportRequestDtoCreateRequestDto> dto)
			throws Oadr20bApplicationLayerException {
		List<OtherReportRequest> requests = new ArrayList<>();
		List<OtherReportRequestSpecifier> specifiers = new ArrayList<>();
		for (OtherReportRequestDtoCreateRequestDto request : dto) {

			OtherReportCapability reportCapability = otherReportCapabilityService
					.findOneBySourceUsernameAndReportSpecifierId(ven.getUsername(), request.getReportSpecifierId());

			String reportRequestId = UUID.randomUUID().toString();
			OtherReportRequest otherReportRequest = new OtherReportRequest();
			otherReportRequest.setStart(request.getStart());
			otherReportRequest.setEnd(request.getEnd());
			otherReportRequest.setGranularity("P0D");
			otherReportRequest.setReportBackDuration("P0D");
			otherReportRequest.setSource(ven);
			otherReportRequest.setOtherReportCapability(reportCapability);
			otherReportRequest.setReportRequestId(reportRequestId);
			otherReportRequest.setRequestor(requestor);

			List<OtherReportCapabilityDescription> findByOtherReportCapability = otherReportCapabilityDescriptionService
					.findByOtherReportCapability(reportCapability);
			Map<String, OtherReportCapabilityDescription> descriptions = findByOtherReportCapability.stream()
					.collect(Collectors.toMap(OtherReportCapabilityDescription::getRid, Function.identity()));
			for (String rid : request.getRid()) {

				if (descriptions.containsKey(rid)) {
					OtherReportCapabilityDescription desc = descriptions.get(rid);
					OtherReportRequestSpecifier otherReportRequestSpecifier = new OtherReportRequestSpecifier();
					otherReportRequestSpecifier.setArchived(false);
					otherReportRequestSpecifier.setRid(rid);
					otherReportRequestSpecifier.setReportSpecifierId(reportCapability.getReportSpecifierId());
					otherReportRequestSpecifier.setReportRequestId(reportRequestId);
					otherReportRequestSpecifier.setVenId(ven.getUsername());
					otherReportRequestSpecifier.setReadingType(desc.getReadingType());
					otherReportRequestSpecifier.setItemBase(desc.getItemBase());
					specifiers.add(otherReportRequestSpecifier);
				}

			}

			requests.add(otherReportRequest);
		}

		venDistributeService.distributeRequestOadrCreatedReportPayload(ven, requests, specifiers);

	}

	public void unsubscribe(Ven ven, String reportRequestID)
			throws OadrElementNotFoundException, Oadr20bApplicationLayerException {

		OtherReportRequest findByReportRequestId = otherReportRequestService.findOneBySourceAndReportRequestId(ven,
				reportRequestID);
		unsubscribe(ven, findByReportRequestId);
	}

	public void unsubscribe(Ven ven, OtherReportRequest reportRequest)
			throws OadrElementNotFoundException, Oadr20bApplicationLayerException {

		otherReportRequestSpecifierDao.deleteByVenIdAndReportRequestId(ven.getUsername(),
				reportRequest.getReportRequestId());
		otherReportRequestService.delete(reportRequest.getId());

		String requestId = "";
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(requestId, ven.getUsername(), false)
				.addReportRequestId(reportRequest.getReportRequestId()).build();

		venDistributeService.distribute(ven, build);
	}

	public void unsubscribe(Ven ven, List<OtherReportRequest> reportRequests) throws Oadr20bApplicationLayerException {
		List<String> reportRequestIds = reportRequests.stream().map(OtherReportRequest::getReportRequestId)
				.collect(Collectors.toList());
		otherReportRequestSpecifierDao.deleteByVenIdAndReportRequestIdIn(ven.getUsername(), reportRequestIds);
		otherReportRequestService.delete(reportRequests);

		String requestId = "";
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(requestId, ven.getUsername(), false).addReportRequestId(reportRequestIds)
				.build();

		venDistributeService.distribute(ven, build);
	}

}
