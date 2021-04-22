package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.strm.Intervals;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.model.oadr20b.xcal.Properties;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloat;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyToken;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatus;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.SelfReportRequest;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenRequestReportService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bAppNotificationPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataFloatService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataKeyTokenService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataPayloadResourceStatusService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@Service
public class Oadr20bVTNEiReportService implements Oadr20bVTNEiService {

	private static final String EI_SERVICE_NAME = "EiReport";

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";

	protected static final String METADATA_REPORT_RID = "METADATA";

	@Resource
	protected Oadr20bJAXBContext jaxbContext;

	@Resource
	protected VenService venService;

	@Resource
	protected XmlSignatureService xmlSignatureService;

	@Resource
	protected VenMarketContextService venMarketContextService;

	@Resource
	protected OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	protected OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	protected OtherReportDataFloatService otherReportDataService;

	@Resource
	private OtherReportDataPayloadResourceStatusService otherReportDataPayloadResourceStatusService;

	@Resource
	private OtherReportDataKeyTokenService otherReportDataKeyTokenService;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	private OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	protected SelfReportCapabilityService selfReportCapabilityService;

	@Resource
	protected SelfReportCapabilityDescriptionService selfReportCapabilityDescriptionService;

	@Resource
	protected SelfReportRequestService selfReportRequestService;

	@Resource
	protected VenDistributeService venDistributeService;

	@Resource
	private VenRequestReportService venRequestReportService;

	@Resource
	protected VenResourceService venResourceService;

	@Resource
	private Oadr20bAppNotificationPublisher oadr20bAppNotificationPublisher;

	@Resource
	private Oadr20bVTNEiReportRegisterService oadr20bVTNEiReportRegisterService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

	private ObjectMapper jsonMapper = new ObjectMapper();

	/**
	 * Register reporting capabilities
	 * 
	 * from VTN/VEN to VEN/VTN
	 * 
	 * The source party sends its reporting capabilities to the target party. The
	 * source party's reporting capabilities are specified using a special
	 * well-known report profil called the METADATA report, which is exchanged using
	 * the same schema as any other report
	 * 
	 * @param report
	 * @return
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bMarshalException
	 */
	@Transactional(readOnly = false)
	public Object oadrRegisterReport(Ven ven, OadrRegisterReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrRegisterReport");

		return oadr20bVTNEiReportRegisterService.oadrRegisterReport(ven, payload);
	}

	public Object oadrRegisteredReport(Ven ven, OadrRegisteredReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrRegisteredReport");

		logApplicationError(ven, payload.getEiResponse());

		String venID = ven.getUsername();
//		String requestID = payload.getEiResponse().getRequestID();
//		if (!payload.getVenID().equals(venID)) {
//			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
//					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, venID = payload.getVenID(), venID);
//			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID,
//					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
//
//		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(payload.getEiResponse().getRequestID(), HttpStatus.OK_200, venID).build();
	}

	public Object oadrCanceledReport(Ven ven, OadrCanceledReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrCanceledReportType");

		logApplicationError(ven, payload.getEiResponse());

		String venID = ven.getUsername();
//		String requestID = payload.getEiResponse().getRequestID();
//		if (!payload.getVenID().equals(venID)) {
//			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
//					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, venID = payload.getVenID(), venID);
//			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID,
//					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
//
//		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(payload.getEiResponse().getRequestID(), HttpStatus.OK_200, venID).build();
	}

	public Object oadrUpdatedReport(Ven ven, OadrUpdatedReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrUpdatedReportType");

		logApplicationError(ven, payload.getEiResponse());

		String venID = ven.getUsername();
//		String requestID = payload.getEiResponse().getRequestID();
//		if (!payload.getVenID().equals(venID)) {
//			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
//					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, venID = payload.getVenID(), venID);
//			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID,
//					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
//
//		}

		return Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(payload.getEiResponse().getRequestID(), HttpStatus.OK_200, venID).build();
	}

	/**
	 * @param payload
	 * @param signed
	 * @return
	 * @throws Oadr20bCreateReportApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bMarshalException
	 */
	public Object oadrCreateReport(Ven ven, OadrCreateReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrCreateReport");

		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiReportBuilders.newOadr20bCreatedReportBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
		}

		int responseCode = HttpStatus.OK_200;
		List<SelfReportRequest> selfReportRequests = Lists.newArrayList();
		for (OadrReportRequestType oadrReportRequestType : payload.getOadrReportRequest()) {
			String reportRequestID = oadrReportRequestType.getReportRequestID();

			ReportSpecifierType reportSpecifier = oadrReportRequestType.getReportSpecifier();
			DurationPropType granularity = reportSpecifier.getGranularity();
			String reportSpecifierID = reportSpecifier.getReportSpecifierID();
			// if reportBackDuration == 0, one shot report, else indicate the
			// frequency of report sending
			DurationPropType reportBackDuration = reportSpecifier.getReportBackDuration();
			WsCalendarIntervalType reportInterval = reportSpecifier.getReportInterval();
			Long start = null;
			Long end = null;
			if (reportInterval != null) {
				Properties properties = reportInterval.getProperties();
				start = Oadr20bFactory.xmlCalendarToTimestamp(properties.getDtstart().getDateTime());
				String duration = properties.getDuration().getDuration();
				end = Oadr20bFactory.addXMLDurationToTimestamp(start, duration);
			}

			// if reportSpecifierId is METADATA, no report request is actually
			// created
			// but requestor ask for oadrRegisterReport payload
			if (METADATA_REPORT_SPECIFIER_ID.equals(reportSpecifierID)) {

				try {
					venDistributeService.distributeOadrRegisterReport(ven);
				} catch (Oadr20bApplicationLayerException e) {
					LOGGER.error("Can't distribute self register report to venId: " + ven.getUsername(), e);
				}

			} else {
				SelfReportCapability selfReportCapability = selfReportCapabilityService
						.findByReportSpecifierId(reportSpecifierID);

				for (SpecifierPayloadType specifierPayloadType : reportSpecifier.getSpecifierPayload()) {
					String rid = specifierPayloadType.getRID();

					SelfReportCapabilityDescription selfReportCapabilityDescription = selfReportCapabilityDescriptionService
							.findBySelfReportCapabilityAndRid(selfReportCapability, rid);

					SelfReportRequest selfReportRequest = new SelfReportRequest(ven, selfReportCapability,
							selfReportCapabilityDescription);
					selfReportRequest.setGranularity(granularity.getDuration());
					selfReportRequest.setReportBackDuration(reportBackDuration.getDuration());
					selfReportRequest.setReportRequestId(reportRequestID);
					selfReportRequest.setStart(start);
					selfReportRequest.setEnd(end);

					selfReportRequests.add(selfReportRequest);
				}
			}

		}
		selfReportRequestService.save(selfReportRequests);

		List<SelfReportRequest> findByTarget = selfReportRequestService.findByTarget(ven);
		List<String> pendingReportRequestId = findByTarget.stream().map(SelfReportRequest::getReportRequestId)
				.collect(Collectors.toList());

		return Oadr20bEiReportBuilders.newOadr20bCreatedReportBuilder(requestID, responseCode, venID)
				.addPendingReportRequestId(pendingReportRequestId).build();

	}

	/**
	 * @param payload
	 * @param signed
	 * @return
	 * @throws Oadr20bCreatedReportApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bMarshalException
	 */
	public Object oadrCreatedReport(Ven ven, OadrCreatedReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrCreatedReport");

		logApplicationError(ven, payload.getEiResponse());

		String venID = ven.getUsername();
		EiResponseType eiResponse = payload.getEiResponse();
		String requestID = eiResponse.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build();
		}

		List<OtherReportRequest> otherReportRequests = otherReportRequestService.findBySourceAndReportRequestIdIn(ven,
				payload.getOadrPendingReports().getReportRequestID());

		List<OtherReportRequest> collect = otherReportRequests.stream()
				.filter(otherReportRequest -> !otherReportRequest.isAcked()).collect(Collectors.toList());
		for (OtherReportRequest report : collect) {
			report.setAcked(true);
		}

		otherReportRequestService.save(collect);

		return Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();

	}

	/**
	 * @param payload
	 * @param signed
	 * @return
	 * @throws Oadr20bCancelReportApplicationLayerException
	 * @throws Oadr20bMarshalException
	 * @throws Oadr20bXMLSignatureException
	 */
	public Object oadrCancelReport(Ven ven, OadrCancelReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrCancelReport");

		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiReportBuilders.newOadr20bCanceledReportBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
		}

		List<SelfReportRequest> otherReportRequests = selfReportRequestService.findByTargetAndReportRequestId(ven,
				payload.getReportRequestID());

		selfReportRequestService.delete(otherReportRequests);

		List<SelfReportRequest> pendings = selfReportRequestService.findByTarget(ven);

		List<String> pendingReportIds = pendings.stream().map(SelfReportRequest::getReportRequestId)
				.collect(Collectors.toList());

		int responseCode = HttpStatus.OK_200;
		return Oadr20bEiReportBuilders.newOadr20bCanceledReportBuilder(requestID, responseCode, venID)
				.addPendingReportRequestId(pendingReportIds).build();

	}

	/**
	 * @param payload
	 * @param signed
	 * @return
	 * @throws Oadr20bUpdateReportApplicationLayerException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bMarshalException
	 */
	public Object oadrUpdateReport(Ven ven, OadrUpdateReportType payload) {

		LOGGER.info(ven.getUsername() + " - OadrUpdateReport");

		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
		}

		int responseCode = HttpStatus.OK_200;

		List<OtherReportDataFloat> listPayloadFloat = Lists.newArrayList();
		List<OtherReportDataFloat> listPayloadFloatToSave = Lists.newArrayList();
		List<OtherReportDataPayloadResourceStatus> listPayloadResourceStatus = Lists.newArrayList();
		List<OtherReportDataPayloadResourceStatus> listPayloadResourceStatusToSave = Lists.newArrayList();
		List<OtherReportDataKeyToken> listPayloadKeyToken = Lists.newArrayList();
		List<OtherReportDataKeyToken> listPayloadKeyTokenToSave = Lists.newArrayList();
		for (OadrReportType oadrReportType : payload.getOadrReport()) {
			String reportRequestId = oadrReportType.getReportRequestID();
			String reportSpecifierID = oadrReportType.getReportSpecifierID();
			Intervals intervals = oadrReportType.getIntervals();

			List<OtherReportRequestSpecifier> findByRequest = otherReportRequestSpecifierDao
					.findByVenIdAndReportRequestId(ven.getUsername(), reportRequestId);

			Map<String, OtherReportRequestSpecifier> perRid = new HashMap<>();
			for (OtherReportRequestSpecifier otherReportRequestSpecifier : findByRequest) {
				perRid.put(otherReportRequestSpecifier.getRid(), otherReportRequestSpecifier);
			}
			List<OtherReportRequestSpecifier> toUpdate = Lists.newArrayList();
			for (IntervalType intervalType : intervals.getInterval()) {

				Dtstart intervalDtstart = intervalType.getDtstart();
				Long start = null;
				if (intervalDtstart != null) {
					start = Oadr20bFactory.xmlCalendarToTimestamp(intervalDtstart.getDateTime());
				}
				DurationPropType intervalDuration = intervalType.getDuration();
				String durationInterval = null;
				if (intervalDuration != null) {
					durationInterval = intervalDuration.getDuration();
				}
				for (JAXBElement<? extends StreamPayloadBaseType> jaxbElement : intervalType.getStreamPayloadBase()) {
					if (jaxbElement.getDeclaredType().equals(OadrReportPayloadType.class)) {

						OadrReportPayloadType reportPayload = (OadrReportPayloadType) jaxbElement.getValue();
						String rid = reportPayload.getRID();

						Long confidence = reportPayload.getConfidence();
						Float accuracy = reportPayload.getAccuracy();

						String lastUpdateValue = null;

						JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
						if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {
							OtherReportDataFloat otherReportData = new OtherReportDataFloat();
							otherReportData.setStart(start);
							otherReportData.setDuration(durationInterval);
							otherReportData.setVenId(venID);
							otherReportData.setReportSpecifierId(reportSpecifierID);
							otherReportData.setReportRequestId(reportRequestId);
							otherReportData.setRid(rid);
							otherReportData.setConfidence(confidence);
							otherReportData.setAccuracy(accuracy);
							PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();
							float value = payloadFloat.getValue();
							otherReportData.setValue(value);
							lastUpdateValue = String.valueOf(value);
							listPayloadFloat.add(otherReportData);

							if (perRid.containsKey(rid)) {
								OtherReportRequestSpecifier otherReportRequestSpecifier = perRid.get(rid);
								if (otherReportRequestSpecifier != null) {
									if (otherReportRequestSpecifier.getLastUpdateDatetime() == null
											|| otherReportRequestSpecifier.getLastUpdateDatetime() < start) {
										otherReportRequestSpecifier.setLastUpdateDatetime(start);
										otherReportRequestSpecifier.setLastUpdateValue(lastUpdateValue);
									}

									if (otherReportRequestSpecifier.getArchived() != null
											&& otherReportRequestSpecifier.getArchived()) {
										listPayloadFloatToSave.add(otherReportData);
									}

									perRid.put(rid, otherReportRequestSpecifier);
									toUpdate.add(otherReportRequestSpecifier);
								}
							}

						} else if (payloadBase.getDeclaredType().equals(PayloadKeyTokenType.class)) {

							OtherReportDataKeyToken otherReportData = new OtherReportDataKeyToken();
							otherReportData.setStart(start);
							otherReportData.setDuration(durationInterval);
							otherReportData.setVenId(venID);
							otherReportData.setReportSpecifierId(reportSpecifierID);
							otherReportData.setReportRequestId(reportRequestId);
							otherReportData.setRid(rid);
							otherReportData.setConfidence(confidence);
							otherReportData.setAccuracy(accuracy);
							PayloadKeyTokenType payloadKeyToken = (PayloadKeyTokenType) payloadBase.getValue();
							List<KeyTokenType> tokens = payloadKeyToken.getTokens();
							otherReportData.setTokens(tokens);
							listPayloadKeyToken.add(otherReportData);
							try {
								lastUpdateValue = jsonMapper.writeValueAsString(tokens);
								if (perRid.containsKey(rid)) {
									OtherReportRequestSpecifier otherReportRequestSpecifier = perRid.get(rid);
									if (otherReportRequestSpecifier != null) {
										if (otherReportRequestSpecifier.getLastUpdateDatetime() == null
												|| otherReportRequestSpecifier.getLastUpdateDatetime() < start) {
											otherReportRequestSpecifier.setLastUpdateDatetime(start);
											otherReportRequestSpecifier.setLastUpdateValue(lastUpdateValue);
										}

										if (otherReportRequestSpecifier.getArchived() != null
												&& otherReportRequestSpecifier.getArchived()) {
											listPayloadKeyTokenToSave.add(otherReportData);
										}

										perRid.put(rid, otherReportRequestSpecifier);
										toUpdate.add(otherReportRequestSpecifier);
									}
								}

							} catch (JsonProcessingException e) {
								LOGGER.error("Can't marshall token to string", e);
							}

						} else if (payloadBase.getDeclaredType().equals(OadrPayloadResourceStatusType.class)) {

							OtherReportDataPayloadResourceStatus otherReportData = new OtherReportDataPayloadResourceStatus();
							otherReportData.setStart(start);
							otherReportData.setDuration(durationInterval);
							otherReportData.setVenId(venID);
							otherReportData.setReportSpecifierId(reportSpecifierID);
							otherReportData.setReportRequestId(reportRequestId);
							otherReportData.setRid(rid);
							otherReportData.setConfidence(confidence);
							otherReportData.setAccuracy(accuracy);
							OadrPayloadResourceStatusType payloadResourceStatus = (OadrPayloadResourceStatusType) payloadBase
									.getValue();

							OadrLoadControlStateTypeType oadrCapacity = payloadResourceStatus.getOadrLoadControlState()
									.getOadrCapacity();
							if (oadrCapacity != null) {
								otherReportData.setOadrCapacityCurrent(oadrCapacity.getOadrCurrent());
								otherReportData.setOadrCapacityMin(oadrCapacity.getOadrMin());
								otherReportData.setOadrCapacityMax(oadrCapacity.getOadrMax());
								otherReportData.setOadrCapacityNormal(oadrCapacity.getOadrNormal());
							}

							OadrLoadControlStateTypeType oadrLevelOffset = payloadResourceStatus
									.getOadrLoadControlState().getOadrLevelOffset();
							if (oadrLevelOffset != null) {
								otherReportData.setOadrLevelOffsetCurrent(oadrLevelOffset.getOadrCurrent());
								otherReportData.setOadrLevelOffsetMin(oadrLevelOffset.getOadrMin());
								otherReportData.setOadrLevelOffsetMax(oadrLevelOffset.getOadrMax());
								otherReportData.setOadrLevelOffsetNormal(oadrLevelOffset.getOadrNormal());
							}

							OadrLoadControlStateTypeType oadrPercentOffset = payloadResourceStatus
									.getOadrLoadControlState().getOadrPercentOffset();
							if (oadrPercentOffset != null) {
								otherReportData.setOadrPercentOffsetCurrent(oadrPercentOffset.getOadrCurrent());
								otherReportData.setOadrPercentOffsetMin(oadrPercentOffset.getOadrMin());
								otherReportData.setOadrPercentOffsetMax(oadrPercentOffset.getOadrMax());
								otherReportData.setOadrPercentOffsetNormal(oadrPercentOffset.getOadrNormal());
							}

							OadrLoadControlStateTypeType oadrSetPoint = payloadResourceStatus.getOadrLoadControlState()
									.getOadrSetPoint();
							if (oadrSetPoint != null) {
								otherReportData.setOadrSetPointCurrent(oadrSetPoint.getOadrCurrent());
								otherReportData.setOadrSetPointMin(oadrSetPoint.getOadrMin());
								otherReportData.setOadrSetPointMax(oadrSetPoint.getOadrMax());
								otherReportData.setOadrSetPointNormal(oadrSetPoint.getOadrNormal());
							}

							listPayloadResourceStatus.add(otherReportData);

							if (perRid.containsKey(rid)) {
								OtherReportRequestSpecifier otherReportRequestSpecifier = perRid.get(rid);
								if (otherReportRequestSpecifier != null) {
									if (otherReportRequestSpecifier.getLastUpdateDatetime() == null
											|| otherReportRequestSpecifier.getLastUpdateDatetime() < start) {
										otherReportRequestSpecifier.setLastUpdateDatetime(start);
										otherReportRequestSpecifier.setLastUpdateValue(lastUpdateValue);
									}

									if (otherReportRequestSpecifier.getArchived() != null
											&& otherReportRequestSpecifier.getArchived()) {
										listPayloadResourceStatusToSave.add(otherReportData);
									}

									perRid.put(rid, otherReportRequestSpecifier);
									toUpdate.add(otherReportRequestSpecifier);
								}
							}
						}

					}
				}
			}

			if (!listPayloadFloat.isEmpty()) {

				oadr20bAppNotificationPublisher.notifyUpdateReportFloat(
						oadr20bDtoMapper.mapList(listPayloadFloat, OtherReportDataFloatDto.class), venID);
			}
			if (!listPayloadResourceStatus.isEmpty()) {

				oadr20bAppNotificationPublisher.notifyUpdateReportResourceStatus(oadr20bDtoMapper
						.mapList(listPayloadResourceStatus, OtherReportDataPayloadResourceStatusDto.class), venID);
			}
			if (!listPayloadKeyToken.isEmpty()) {

				oadr20bAppNotificationPublisher.notifyUpdateReportKeyToken(
						oadr20bDtoMapper.mapList(listPayloadKeyToken, OtherReportDataKeyTokenDto.class), venID);
			}

			if (!listPayloadFloatToSave.isEmpty()) {
				otherReportDataService.save(listPayloadFloat);

			}
			if (!listPayloadResourceStatusToSave.isEmpty()) {
				otherReportDataPayloadResourceStatusService.save(listPayloadResourceStatus);

			}
			if (!listPayloadKeyTokenToSave.isEmpty()) {
				otherReportDataKeyTokenService.save(listPayloadKeyToken);

			}

			otherReportRequestSpecifierDao.saveAll(toUpdate);

		}

		venService.updateLastUpdateDatetime(ven);

		return Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID, responseCode, venID).build();

	}

	@Override
	public Object request(Ven ven, Object payload) {

		if (payload instanceof OadrRegisterReportType) {

			OadrRegisterReportType obj = (OadrRegisterReportType) payload;

			return this.oadrRegisterReport(ven, obj);

		} else if (payload instanceof OadrRegisteredReportType) {

			OadrRegisteredReportType obj = (OadrRegisteredReportType) payload;

			return this.oadrRegisteredReport(ven, obj);

		} else if (payload instanceof OadrUpdateReportType) {

			OadrUpdateReportType obj = (OadrUpdateReportType) payload;

			return this.oadrUpdateReport(ven, obj);

		} else if (payload instanceof OadrUpdatedReportType) {

			OadrUpdatedReportType obj = (OadrUpdatedReportType) payload;

			return this.oadrUpdatedReport(ven, obj);

		} else if (payload instanceof OadrCreatedReportType) {

			OadrCreatedReportType obj = (OadrCreatedReportType) payload;

			return this.oadrCreatedReport(ven, obj);

		} else if (payload instanceof OadrCreateReportType) {

			OadrCreateReportType obj = (OadrCreateReportType) payload;

			return this.oadrCreateReport(ven, obj);

		} else if (payload instanceof OadrCancelReportType) {

			OadrCancelReportType obj = (OadrCancelReportType) payload;

			return this.oadrCancelReport(ven, obj);

		} else if (payload instanceof OadrCanceledReportType) {

			OadrCanceledReportType obj = (OadrCanceledReportType) payload;

			return this.oadrCanceledReport(ven, obj);

		} else {
			return Oadr20bResponseBuilders
					.newOadr20bResponseBuilder("0", Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453,
							ven.getUsername())
					.withDescription("Unknown payload type for service: " + this.getServiceName()).build();
		}

	}

	@Override
	public String getServiceName() {
		return EI_SERVICE_NAME;
	}

	private void logApplicationError(Ven ven, EiResponseType response) {
		if (!response.getResponseCode().equals(String.valueOf(HttpStatus.OK_200))) {
			LOGGER.error(String.format("requestId: '%s' rejected by ven: '%s' [code: '%s', description: '%s']",
					ven.getUsername(), response.getRequestID(), response.getResponseCode(),
					response.getResponseDescription()));
		}
	}

}
