package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCreateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bReportRequestTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.push.VenCommandDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiServiceUtils;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@Service
public class VenDistributeService {

	public static final String OADR20B_QUEUE = "queue.command.oadr20b";

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";

	protected static final String METADATA_REPORT_RID = "METADATA";

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	protected OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	private OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	protected SelfReportCapabilityService selfReportCapabilityService;

	@Resource
	protected SelfReportCapabilityDescriptionService selfReportCapabilityDescriptionService;

	@Resource
	protected SelfReportRequestService selfReportRequestService;

	@Autowired
	private JmsTemplate jmsTemplate;

	private ObjectMapper mapper = new ObjectMapper();

	private <T> void publish(Ven ven, String payload, Class<T> klass) throws JsonProcessingException {
		VenCommandDto<T> command = new VenCommandDto<T>(ven, payload, klass);
		this.send(mapper.writeValueAsString(command));

	}

	protected void send(String payload) {
		jmsTemplate.convertAndSend(OADR20B_QUEUE, payload);
	}

	public void distribute(Ven ven, Object payload) throws Oadr20bApplicationLayerException {
		try {
			String marshalRoot = jaxbContext.marshalRoot(payload);
			if (payload instanceof OadrDistributeEventType) {

				this.publish(ven, marshalRoot, OadrDistributeEventType.class);

			} else if (payload instanceof OadrCancelReportType) {

				this.publish(ven, marshalRoot, OadrCancelReportType.class);

			} else if (payload instanceof OadrCreateReportType) {

				this.publish(ven, marshalRoot, OadrCreateReportType.class);

			} else if (payload instanceof OadrRegisterReportType) {

				this.publish(ven, marshalRoot, OadrRegisterReportType.class);

			} else if (payload instanceof OadrUpdateReportType) {

				this.publish(ven, marshalRoot, OadrUpdateReportType.class);

			} else if (payload instanceof OadrCancelPartyRegistrationType) {

				this.publish(ven, marshalRoot, OadrCancelPartyRegistrationType.class);

			} else if (payload instanceof OadrRequestReregistrationType) {

				this.publish(ven, marshalRoot, OadrRequestReregistrationType.class);

			} else {
				throw new Oadr20bApplicationLayerException("Can't distribute an unknown payload type");
			}
		} catch (JsonProcessingException | Oadr20bMarshalException e) {
			throw new Oadr20bApplicationLayerException(e);
		}
	}

	public void distributeRequestOadrCreatedReportPayload(Ven ven, List<OtherReportRequest> request,
			List<OtherReportRequestSpecifier> specifiers) throws Oadr20bApplicationLayerException {

		boolean send = false;
		String requestId = "";
		Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, ven.getUsername());

		Map<String, Set<OtherReportRequestSpecifier>> groupByReportRequestIdSpecifiers = specifiers.stream()
				.collect(Collectors.groupingBy(spec -> spec.getReportRequestId(), Collectors.toSet()));

		Map<String, OtherReportRequest> collect = request.stream()
				.collect(Collectors.toMap(OtherReportRequest::getReportRequestId, r -> r));

		for (Entry<String, Set<OtherReportRequestSpecifier>> entry : groupByReportRequestIdSpecifiers.entrySet()) {

			if (collect.containsKey(entry.getKey())) {

				send = true;
				OtherReportRequest otherReportRequest = collect.get(entry.getKey());

				Oadr20bReportRequestTypeBuilder reportRequestBuilder = Oadr20bEiReportBuilders
						.newOadr20bReportRequestTypeBuilder(otherReportRequest.getReportRequestId(),
								otherReportRequest.getOtherReportCapability().getReportSpecifierId(),
								otherReportRequest.getGranularity(), otherReportRequest.getReportBackDuration());

				Long end = otherReportRequest.getEnd();
				if (end == null) {
					end = System.currentTimeMillis();
				}
				if (otherReportRequest.getStart() != null) {
					String duration = Oadr20bFactory.millisecondToXmlDuration(end - otherReportRequest.getStart());
					WsCalendarIntervalType createWsCalendarIntervalType = Oadr20bFactory
							.createWsCalendarIntervalType(otherReportRequest.getStart(), duration);
					reportRequestBuilder.withWsCalendarIntervalType(createWsCalendarIntervalType);
				}

				for (OtherReportRequestSpecifier specifier : entry.getValue()) {

					reportRequestBuilder.addSpecifierPayload(null, specifier.getReadingType(), specifier.getRid());

				}

				OadrReportRequestType build = reportRequestBuilder.build();

				newOadr20bCreateReportBuilder.addReportRequest(build);

			}
		}

		if (send) {
			this.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	public void distributeSubscriptionOadrCreatedReportPayload(Ven ven) throws Oadr20bApplicationLayerException {

		boolean send = false;
		String requestId = "";
		Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, ven.getUsername());

		List<OtherReportRequest> findBySource = otherReportRequestService.findBySource(ven);
		if (findBySource != null) {
			for (OtherReportRequest request : findBySource) {
				send = true;
				OtherReportCapability otherReportCapability = request.getOtherReportCapability();
				Oadr20bReportRequestTypeBuilder reportRequestBuilder = Oadr20bEiReportBuilders
						.newOadr20bReportRequestTypeBuilder(request.getReportRequestId(),
								otherReportCapability.getReportSpecifierId(), request.getGranularity(),
								request.getReportBackDuration());
				if (request.getStart() != null && request.getEnd() != null) {
					String duration = Oadr20bFactory.millisecondToXmlDuration(request.getEnd() - request.getStart());
					WsCalendarIntervalType createWsCalendarIntervalType = Oadr20bFactory
							.createWsCalendarIntervalType(request.getStart(), duration);
					reportRequestBuilder.withWsCalendarIntervalType(createWsCalendarIntervalType);
				}
				List<OtherReportRequestSpecifier> findByRequest = otherReportRequestSpecifierDao
						.findByVenIdAndReportRequestId(ven.getUsername(), request.getReportRequestId());
				for (OtherReportRequestSpecifier otherReportRequestSpecifier : findByRequest) {

					JAXBElement<? extends ItemBaseType> createItemBase = null;
					if (otherReportRequestSpecifier.getItemBase() != null) {

						createItemBase = Oadr20bVTNEiServiceUtils
								.createItemBase(otherReportRequestSpecifier.getItemBase());

					}

					reportRequestBuilder.addSpecifierPayload(createItemBase,
							otherReportRequestSpecifier.getReadingType(), otherReportRequestSpecifier.getRid());

				}
				newOadr20bCreateReportBuilder.addReportRequest(reportRequestBuilder.build());
			}
		}

		if (send) {
			this.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	/**
	 * @param ven
	 * @throws Oadr20bApplicationLayerException
	 */
	public void distributeOadrRegisterReport(Ven ven) throws Oadr20bApplicationLayerException {
		List<OadrReportType> reports = Lists.newArrayList();
		for (SelfReportCapability selfReportCapability : selfReportCapabilityService.findAll()) {

			List<SelfReportCapabilityDescription> findBySelfReportCapability = selfReportCapabilityDescriptionService
					.findBySelfReportCapability(selfReportCapability);

			List<OadrReportDescriptionType> oadrReportDescriptionType = Lists.newArrayList();
			for (SelfReportCapabilityDescription description : findBySelfReportCapability) {
				String descriptionPayload = description.getPayload();
				try {
					OadrReportDescriptionType unmarshal = jaxbContext.unmarshal(descriptionPayload,
							OadrReportDescriptionType.class);
					oadrReportDescriptionType.add(unmarshal);
				} catch (Oadr20bUnmarshalException e) {
					throw new Oadr20bApplicationLayerException(e);
				}
			}

			OadrReportType oadrReportType = Oadr20bEiReportBuilders
					.newOadr20bRegisterReportOadrReportBuilder(selfReportCapability.getReportSpecifierId(),
							selfReportCapability.getReportName(), System.currentTimeMillis())
					.withDuration(selfReportCapability.getDuration()).addReportDescription(oadrReportDescriptionType)
					.build();
			reports.add(oadrReportType);
		}

		OadrRegisterReportType oadrRegisterReportType = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder("", null).addOadrReport(reports).build();

		try {
			this.distribute(ven, oadrRegisterReportType);
		} catch (Oadr20bException e) {
			throw new Oadr20bApplicationLayerException(e);
		}
	}

	/**
	 * @param ven
	 * @throws Oadr20bApplicationLayerException
	 */
	public void distributeRequestMetadataOadrCreateReportPayload(Ven ven) throws Oadr20bApplicationLayerException {
		String requestId = "0";
		String reportRequestId = "0";
		String reportSpecifierId = METADATA_REPORT_SPECIFIER_ID;
		String granularity = "P0D";
		String reportBackDuration = "P0D";
		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, METADATA_REPORT_RID).build();
		OadrCreateReportType build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(requestId, ven.getUsername())
				.addReportRequest(oadrReportRequestType).build();

		this.distribute(ven, build);

	}

}
