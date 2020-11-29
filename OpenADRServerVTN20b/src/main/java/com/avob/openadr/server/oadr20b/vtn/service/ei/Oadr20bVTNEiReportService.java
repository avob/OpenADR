package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

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
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCreateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bReportRequestTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.strm.Intervals;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.model.oadr20b.xcal.Properties;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.user.AbstractUser;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SamplingRate;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloat;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyToken;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatus;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifier;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.SelfReportRequest;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
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
	private Oadr20bAppNotificationPublisher oadr20bAppNotificationPublisher;

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

		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
//		String reportRequestID = payload.getReportRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
		}

		VenReportDto venReportDto = oadr20bDtoMapper.map(ven, VenReportDto.class);
		List<VenReportCapabilityDto> capabilitiesDto = new ArrayList<>();

		int responseCode = HttpStatus.OK_200;

		List<OtherReportCapability> currentVenCapability = otherReportCapabilityService.findBySource(ven);
		Map<String, OtherReportCapability> currentVenCapabilityMap = currentVenCapability.stream()
				.collect(Collectors.toMap(OtherReportCapability::getReportSpecifierId, cap -> cap));

		List<OtherReportCapabilityDescription> currentVenCapabilityDescription = otherReportCapabilityDescriptionService
				.findByOtherReportCapabilityIn(currentVenCapability);
		Map<String, OtherReportCapabilityDescription> currentVenCapabilityDescriptionMap = currentVenCapabilityDescription
				.stream().collect(Collectors.toMap(
						desc -> desc.getOtherReportCapability().getReportSpecifierId() + desc.getRid(), desc -> desc));

		List<OtherReportCapability> capabilities = Lists.newArrayList();
		List<OtherReportCapabilityDescription> descriptions = Lists.newArrayList();
		boolean hasMetadataReport = false;
		for (OadrReportType oadrReportType : payload.getOadrReport()) {

			boolean created = false;
			XMLGregorianCalendar createdDateTime = oadrReportType.getCreatedDateTime();
			String reportId = oadrReportType.getEiReportID();
			String reportName = oadrReportType.getReportName();
			String reportSpecifierID = oadrReportType.getReportSpecifierID();
			if (METADATA_REPORT_SPECIFIER_ID.equals(reportSpecifierID)) {
				hasMetadataReport = true;
			}
			DurationPropType duration = oadrReportType.getDuration();
			Dtstart dtstart = oadrReportType.getDtstart();

			OtherReportCapability otherReportCapability = currentVenCapabilityMap.get(reportSpecifierID);
			if (otherReportCapability == null) {
				otherReportCapability = new OtherReportCapability(ven);
				created = true;
			}

			otherReportCapability.setReportId(reportId);
			otherReportCapability.setReportName(ReportNameEnumeratedType.fromValue(reportName));
			otherReportCapability.setReportSpecifierId(reportSpecifierID);

			if (duration != null) {
				otherReportCapability.setDuration(duration.getDuration());
			}
			if (dtstart != null) {
				otherReportCapability.setStart(Oadr20bFactory.xmlCalendarToTimestamp(dtstart.getDateTime()));
			}
			if (createdDateTime != null) {
				otherReportCapability.setCreatedDatetime(Oadr20bFactory.xmlCalendarToTimestamp(createdDateTime));
			}

			VenReportCapabilityDto capabilityDto = oadr20bDtoMapper.map(otherReportCapability,
					VenReportCapabilityDto.class);
			List<ReportCapabilityDescriptionDto> descriptionDto = new ArrayList<>();
			capabilities.add(otherReportCapability);
			List<OtherReportCapabilityDescription> capabilityDescription = new ArrayList<>();
			for (OadrReportDescriptionType oadrReportDescriptionType : oadrReportType.getOadrReportDescription()) {
				String rid = oadrReportDescriptionType.getRID();
				OtherReportCapabilityDescription description = currentVenCapabilityDescriptionMap
						.get(otherReportCapability.getReportSpecifierId() + rid);
				if (!created) {
					currentVenCapabilityDescriptionMap.get(otherReportCapability.getReportSpecifierId() + rid);
				}
				if (description == null) {
					description = new OtherReportCapabilityDescription(otherReportCapability);
				}
				String reportType = oadrReportDescriptionType.getReportType();
				String readingType = oadrReportDescriptionType.getReadingType();
				OadrSamplingRateType oadrSamplingRate = oadrReportDescriptionType.getOadrSamplingRate();
				String oadrMaxPeriod = null;
				String oadrMinPeriod = null;
				boolean oadrOnChange = false;
				if (oadrSamplingRate != null) {
					oadrMaxPeriod = oadrSamplingRate.getOadrMaxPeriod();
					oadrMinPeriod = oadrSamplingRate.getOadrMinPeriod();
					oadrOnChange = oadrSamplingRate.isOadrOnChange();
				}

				if (oadrReportDescriptionType.getItemBase() != null) {

					ItemBase createItemBase = Oadr20bVTNEiServiceUtils
							.createItemBase(oadrReportDescriptionType.getItemBase());
					description.setItemBase(createItemBase);

				}

				String marketContext = oadrReportDescriptionType.getMarketContext();
				description.setRid(rid);
				description.setReportType(ReportEnumeratedType.fromValue(reportType));
				description.setReadingType(ReadingTypeEnumeratedType.fromValue(readingType));

				SamplingRate samplingRate = new SamplingRate();
				samplingRate.setOadrMaxPeriod(oadrMaxPeriod);
				samplingRate.setOadrMinPeriod(oadrMinPeriod);
				samplingRate.setOadrOnChange(oadrOnChange);

				description.setSamplingRate(samplingRate);

				if (marketContext != null) {
					VenMarketContext findOneByName = venMarketContextService.findOneByName(marketContext);
					description.setVenMarketContext(findOneByName);
				}

				if (oadrReportDescriptionType.getReportSubject() != null) {

					EiTargetType reportSubject = oadrReportDescriptionType.getReportSubject();

					List<Target> createTargetList = Oadr20bVTNEiServiceUtils.createTargetList(reportSubject);
					description.setEiSubject(createTargetList);

				}

				if (oadrReportDescriptionType.getReportDataSource() != null) {

					EiTargetType reportDataSource = oadrReportDescriptionType.getReportDataSource();

					List<Target> createTargetList = Oadr20bVTNEiServiceUtils.createTargetList(reportDataSource);
					description.setEiSubject(createTargetList);

				}

				descriptionDto.add(oadr20bDtoMapper.map(description, ReportCapabilityDescriptionDto.class));
				descriptions.add(description);
				capabilityDescription.add(description);

			}
			capabilityDto.setDescriptions(descriptionDto);
			capabilitiesDto.add(capabilityDto);
			if (!created) {
				try {
					this.distributeSubscriptionOadrCreatedReportPayload(ven);
				} catch (Oadr20bApplicationLayerException e) {
					LOGGER.error("Can't distribute report requests to venId: " + ven.getUsername(), e);
				}
			}

		}
		venReportDto.setCapabilities(capabilitiesDto);

		Collection<OtherReportCapabilityDescription> toDeleteDesc = currentVenCapabilityDescriptionMap.values();
		toDeleteDesc.removeAll(descriptions);
		if (!toDeleteDesc.isEmpty()) {
			otherReportRequestSpecifierDao.deleteByOtherReportCapabilityDescriptionIn(toDeleteDesc);

			for (OtherReportCapabilityDescription desc : toDeleteDesc) {
				otherReportCapabilityDescriptionService.delete(desc.getId());
			}

		}

		Collection<OtherReportCapability> toDeleteCap = currentVenCapabilityMap.values();
		toDeleteCap.removeAll(capabilities);
		if (!toDeleteCap.isEmpty()) {
			otherReportCapabilityService.delete(toDeleteCap);
		}

		if (hasMetadataReport) {
			otherReportRequestSpecifierDao.deleteByOtherReportCapabilityDescriptionOtherReportCapabilitySource(ven);
			otherReportRequestService.deleteByOtherReportCapabilitySource(ven);
		}

		oadr20bAppNotificationPublisher.notifyRegisterReport(venReportDto, venID);

		otherReportCapabilityService.save(capabilities);
		otherReportCapabilityDescriptionService.save(descriptions);

		return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID, responseCode, venID).build();

	}

	public Object oadrRegisteredReport(Ven ven, OadrRegisteredReportType payload) {
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
	 * @param ven
	 */
	public void distributeRequestMetadataOadrCreateReportPayload(Ven ven) {
		String requestId = "0";
		String reportRequestId = "0";
		String reportSpecifierId = Oadr20bVTNEiReportService.METADATA_REPORT_SPECIFIER_ID;
		String granularity = "P0D";
		String reportBackDuration = "P0D";
		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.DIRECT_READ, METADATA_REPORT_RID).build();
		OadrCreateReportType build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(requestId, ven.getUsername())
				.addReportRequest(oadrReportRequestType).build();

		try {
			venDistributeService.distribute(ven, build);
		} catch (Oadr20bException e) {
			LOGGER.error("Cannot distribute OadrRegisterReportType after self METADATA payload generation", e);
		}
	}

	/**
	 * @param ven
	 */
	public void distributeOadrRegisterReport(Ven ven) {
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
					LOGGER.error("Cannot unmarshall OadrReportDescriptionType during self METADATA payload generation",
							e);
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
			venDistributeService.distribute(ven, oadrRegisterReportType);
		} catch (Oadr20bException e) {
			LOGGER.error("Cannot distribute OadrRegisterReportType after self METADATA payload generation", e);
		}
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

				distributeOadrRegisterReport(ven);

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
	 * @throws Oadr20bCancelReportApplicationLayerException
	 */
	public void otherOadrCancelReport(OadrCancelReportType payload) {
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		List<OtherReportRequestSpecifier> findByRequestSourceAndRequestReportRequestIdIn = otherReportRequestSpecifierDao
				.findByRequestSourceAndRequestReportRequestIdIn(ven, payload.getReportRequestID());

		if (findByRequestSourceAndRequestReportRequestIdIn != null) {
			otherReportRequestSpecifierDao.deleteAll(findByRequestSourceAndRequestReportRequestIdIn);
		}

		List<OtherReportRequest> otherReportRequests = otherReportRequestService.findBySourceAndReportRequestIdIn(ven,
				payload.getReportRequestID());

		if (otherReportRequests != null && !otherReportRequests.isEmpty()) {
			otherReportRequestService.delete(otherReportRequests);
		}

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
		String venID = ven.getUsername();
		String requestID = payload.getRequestID();
		if (!payload.getVenID().equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, payload.getVenID(), venID);
			return Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID,
					Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID).build();
		}

		Long now = System.currentTimeMillis();

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

			OtherReportRequest findByReportRequestId = otherReportRequestService.findOneBySourceAndReportRequestId(ven,
					reportRequestId);

			List<OtherReportRequestSpecifier> findByRequest = otherReportRequestSpecifierDao
					.findByRequest(findByReportRequestId);
			Map<String, OtherReportRequestSpecifier> perRid = new HashMap<>();
			for (OtherReportRequestSpecifier otherReportRequestSpecifier : findByRequest) {
				perRid.put(otherReportRequestSpecifier.getOtherReportCapabilityDescription().getRid(),
						otherReportRequestSpecifier);
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

		ven.setLastUpdateDatetime(now);

		venService.save(ven);

		return Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID, responseCode, venID).build();

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
				List<OtherReportRequestSpecifier> findByRequest = otherReportRequestSpecifierDao.findByRequest(request);
				for (OtherReportRequestSpecifier otherReportRequestSpecifier : findByRequest) {

					OtherReportCapabilityDescription otherReportCapabilityDescription = otherReportRequestSpecifier
							.getOtherReportCapabilityDescription();

					JAXBElement<? extends ItemBaseType> createItemBase = null;
					if (otherReportCapabilityDescription.getItemBase() != null) {

						createItemBase = Oadr20bVTNEiServiceUtils
								.createItemBase(otherReportCapabilityDescription.getItemBase());

					}

					reportRequestBuilder.addSpecifierPayload(createItemBase,
							otherReportCapabilityDescription.getReadingType(),
							otherReportCapabilityDescription.getRid());

				}
				newOadr20bCreateReportBuilder.addReportRequest(reportRequestBuilder.build());
			}
		}

		if (send) {
			venDistributeService.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	public void distributeRequestOadrCreatedReportPayload(Ven ven, List<OtherReportRequest> request,
			List<OtherReportRequestSpecifier> specifiers) throws Oadr20bApplicationLayerException {

		boolean send = false;
		String requestId = "";
		Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, ven.getUsername());

		Map<String, Set<OtherReportRequestSpecifier>> groupByReportRequestIdSpecifiers = specifiers.stream()
				.collect(Collectors.groupingBy(spec -> spec.getRequest().getReportRequestId(), Collectors.toSet()));

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

					OtherReportCapabilityDescription otherReportCapabilityDescription = specifier
							.getOtherReportCapabilityDescription();
					reportRequestBuilder.addSpecifierPayload(null, otherReportCapabilityDescription.getReadingType(),
							otherReportCapabilityDescription.getRid());

				}

				OadrReportRequestType build = reportRequestBuilder.build();

				newOadr20bCreateReportBuilder.addReportRequest(build);

			}
		}

		if (send) {
			venDistributeService.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	/**
	 * @param requestor
	 * @param ven
	 * @param subscriptions
	 * @throws Oadr20bException
	 */
	public void subscribe(AbstractUser requestor, Ven ven,
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
					OtherReportRequestSpecifier otherReportRequestSpecifier = new OtherReportRequestSpecifier();
					otherReportRequestSpecifier.setArchived(entry.getValue());
					otherReportRequestSpecifier.setOtherReportCapabilityDescription(descriptions.get(entry.getKey()));
					otherReportRequestSpecifier.setRequest(otherReportRequest);
					specifiers.add(otherReportRequestSpecifier);
				}

			}

			requests.add(otherReportRequest);
		}

		otherReportRequestService.save(requests);
		otherReportRequestSpecifierDao.saveAll(specifiers);

		distributeSubscriptionOadrCreatedReportPayload(ven);

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
					OtherReportRequestSpecifier otherReportRequestSpecifier = new OtherReportRequestSpecifier();
					otherReportRequestSpecifier.setArchived(false);
					otherReportRequestSpecifier.setOtherReportCapabilityDescription(descriptions.get(rid));
					otherReportRequestSpecifier.setRequest(otherReportRequest);
					specifiers.add(otherReportRequestSpecifier);
				}

			}

			requests.add(otherReportRequest);
		}

		distributeRequestOadrCreatedReportPayload(ven, requests, specifiers);

	}

	public void unsubscribe(Ven ven, String reportRequestID)
			throws OadrElementNotFoundException, Oadr20bApplicationLayerException {

		OtherReportRequest findByReportRequestId = otherReportRequestService.findOneBySourceAndReportRequestId(ven,
				reportRequestID);
		otherReportRequestSpecifierDao.deleteByRequest(findByReportRequestId);
		otherReportRequestService.delete(findByReportRequestId.getId());

		String requestId = "";
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(requestId, ven.getUsername(), false).addReportRequestId(reportRequestID)
				.build();

		venDistributeService.distribute(ven, build);
	}

	@Override
	public Object request(Ven ven, Object payload) {

		if (payload instanceof OadrRegisterReportType) {

			LOGGER.info(ven.getUsername() + " - OadrRegisterReport");

			OadrRegisterReportType obj = (OadrRegisterReportType) payload;

			return this.oadrRegisterReport(ven, obj);

		} else if (payload instanceof OadrRegisteredReportType) {

			LOGGER.info(ven.getUsername() + " - OadrRegisteredReport");

			OadrRegisteredReportType obj = (OadrRegisteredReportType) payload;

			return this.oadrRegisteredReport(ven, obj);

		} else if (payload instanceof OadrUpdateReportType) {

			LOGGER.info(ven.getUsername() + " - OadrUpdateReport");

			OadrUpdateReportType obj = (OadrUpdateReportType) payload;

			return this.oadrUpdateReport(ven, obj);

		} else if (payload instanceof OadrCreatedReportType) {

			LOGGER.info(ven.getUsername() + " - OadrCreatedReport");

			OadrCreatedReportType obj = (OadrCreatedReportType) payload;

			return this.oadrCreatedReport(ven, obj);

		} else if (payload instanceof OadrCreateReportType) {

			LOGGER.info(ven.getUsername() + " - OadrCreateReport");

			OadrCreateReportType obj = (OadrCreateReportType) payload;

			return this.oadrCreateReport(ven, obj);

		} else if (payload instanceof OadrCancelReportType) {

			LOGGER.info(ven.getUsername() + " - OadrCancelReport	");

			OadrCancelReportType obj = (OadrCancelReportType) payload;

			return this.oadrCancelReport(ven, obj);

		} else if (payload instanceof OadrCanceledReportType) {

			LOGGER.info(ven.getUsername() + " - OadrCanceledReportType	");

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

}
