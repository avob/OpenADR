package com.avob.openadr.server.oadr20b.vtn.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bCreateReportBuilder;
import com.avob.openadr.model.oadr20b.builders.eireport.Oadr20bReportRequestTypeBuilder;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.PayloadBaseType;
import com.avob.openadr.model.oadr20b.ei.PayloadFloatType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportSpecifierType;
import com.avob.openadr.model.oadr20b.ei.SpecifierPayloadType;
import com.avob.openadr.model.oadr20b.emix.ItemBaseType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.BaseUnitType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyType;
import com.avob.openadr.model.oadr20b.oadr.CurrentType;
import com.avob.openadr.model.oadr20b.oadr.FrequencyType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrGBItemBase;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportPayloadType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.oadr.PulseCountType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.ThermType;
import com.avob.openadr.model.oadr20b.power.EnergyApparentType;
import com.avob.openadr.model.oadr20b.power.EnergyReactiveType;
import com.avob.openadr.model.oadr20b.power.EnergyRealType;
import com.avob.openadr.model.oadr20b.power.PowerApparentType;
import com.avob.openadr.model.oadr20b.power.PowerReactiveType;
import com.avob.openadr.model.oadr20b.power.PowerRealType;
import com.avob.openadr.model.oadr20b.power.VoltageType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.strm.Intervals;
import com.avob.openadr.model.oadr20b.strm.StreamPayloadBaseType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.model.oadr20b.xcal.Properties;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.common.vtn.exception.OadrVTNInitializationException;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.exception.OadrElementNotFoundException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCancelReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bCreatedReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bRegisterReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eireport.Oadr20bUpdateReportApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportData;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.SelfReportRequest;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
public class Oadr20bVTNEiReportService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";

	protected static final String METADATA_REPORT_RID = "METADATA";

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
	protected OtherReportDataService otherReportDataService;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	protected SelfReportCapabilityService selfReportCapabilityService;

	@Resource
	protected SelfReportCapabilityDescriptionService selfReportCapabilityDescriptionService;

	@Resource
	protected SelfReportRequestService selfReportRequestService;

	@Resource
	protected VenDistributeService venDistributeService;

	@Resource
	private VtnConfig vtnConfig;

	public Oadr20bVTNEiReportService() {
		try {
			jaxbContext = Oadr20bJAXBContext.getInstance();
		} catch (JAXBException e) {
			throw new OadrVTNInitializationException(e);
		}
	}

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
	public String oadrRegisterReport(OadrRegisterReportType payload, boolean signed)
			throws Oadr20bRegisterReportApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bMarshalException {

		String requestID = payload.getRequestID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bRegisterReportApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID).build());
		}

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
		for (OadrReportType oadrReportType : payload.getOadrReport()) {

			boolean created = false;
			XMLGregorianCalendar createdDateTime = oadrReportType.getCreatedDateTime();
			String reportId = oadrReportType.getEiReportID();
			String reportName = oadrReportType.getReportName();
			String reportRequestID = oadrReportType.getReportRequestID();
			String reportSpecifierID = oadrReportType.getReportSpecifierID();
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
			otherReportCapability.setReportRequestId(reportRequestID);

			if (duration != null) {
				otherReportCapability.setDuration(duration.getDuration());
			}
			if (dtstart != null) {
				otherReportCapability.setStart(Oadr20bFactory.xmlCalendarToTimestamp(dtstart.getDateTime()));
			}
			if (createdDateTime != null) {
				otherReportCapability.setCreatedDatetime(Oadr20bFactory.xmlCalendarToTimestamp(createdDateTime));
			}
			capabilities.add(otherReportCapability);
			List<OtherReportCapabilityDescription> capabilityDescription = new ArrayList<OtherReportCapabilityDescription>();
			for (OadrReportDescriptionType oadrReportDescriptionType : oadrReportType.getOadrReportDescription()) {
				String rid = oadrReportDescriptionType.getRID();
				OtherReportCapabilityDescription description = null;
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
				String itemDescription = null;
				String itemUnits = null;
				SiScaleCodeType siScaleCode = null;
				if (oadrReportDescriptionType.getItemBase() != null) {
					ItemBaseType value = oadrReportDescriptionType.getItemBase().getValue();
					Class<? extends ItemBaseType> declaredType = oadrReportDescriptionType.getItemBase()
							.getDeclaredType();

					if (declaredType.equals(VoltageType.class)) {
						VoltageType el = (VoltageType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(EnergyApparentType.class)) {
						EnergyApparentType el = (EnergyApparentType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(EnergyReactiveType.class)) {
						EnergyReactiveType el = (EnergyReactiveType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(EnergyRealType.class)) {
						EnergyRealType el = (EnergyRealType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(PowerApparentType.class)) {
						PowerApparentType el = (PowerApparentType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(PowerReactiveType.class)) {
						PowerReactiveType el = (PowerReactiveType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(PowerRealType.class)) {
						PowerRealType el = (PowerRealType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(BaseUnitType.class)) {
						BaseUnitType el = (BaseUnitType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(CurrentType.class)) {
						CurrentType el = (CurrentType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(CurrencyType.class)) {
						CurrencyType el = (CurrencyType) value;
						itemDescription = el.getItemDescription().value();
						itemUnits = el.getItemUnits().value();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(FrequencyType.class)) {
						FrequencyType el = (FrequencyType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(ThermType.class)) {
						ThermType el = (ThermType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(TemperatureType.class)) {
						TemperatureType el = (TemperatureType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits().value();
						siScaleCode = el.getSiScaleCode();

					} else if (declaredType.equals(PulseCountType.class)) {
						PulseCountType el = (PulseCountType) value;
						itemDescription = el.getItemDescription();
						itemUnits = el.getItemUnits();
						siScaleCode = SiScaleCodeType.NONE;

					} else if (declaredType.equals(OadrGBItemBase.class)) {
						itemDescription = "OadrGBItemBase";
						itemUnits = "OadrGBItemBase";
						siScaleCode = SiScaleCodeType.NONE;
					}
				}

				String marketContext = oadrReportDescriptionType.getMarketContext();
				description.setRid(rid);
				description.setReportType(ReportEnumeratedType.fromValue(reportType));
				description.setReadingType(ReadingTypeEnumeratedType.fromValue(readingType));
				description.setItemDescription(itemDescription);
				description.setItemUnits(itemUnits);
				description.setSiScaleCode(siScaleCode);
				description.setOadrMaxPeriod(oadrMaxPeriod);
				description.setOadrMinPeriod(oadrMinPeriod);
				description.setOadrOnChange(oadrOnChange);

				if (marketContext != null) {
					VenMarketContext findOneByName = venMarketContextService.findOneByName(marketContext);
					description.setVenMarketContext(findOneByName);
				}
				JAXBElement<OadrReportDescriptionType> createOadrReportDescription = Oadr20bFactory
						.createOadrReportDescription(oadrReportDescriptionType);
				String marshal;
				try {
					marshal = jaxbContext.marshal(createOadrReportDescription);
					description.setPayload(marshal);
				} catch (Oadr20bMarshalException e) {
					LOGGER.warn("Can't marshall report description payload", e);
				}
				descriptions.add(description);
				capabilityDescription.add(description);

			}
			if (!created) {
				this.distributeSubscriptionOadrCreatedReportPayload(ven, otherReportCapability, capabilityDescription);
			}

		}

		otherReportCapabilityService.save(capabilities);
		otherReportCapabilityDescriptionService.save(descriptions);

		Collection<OtherReportCapabilityDescription> toDeleteDesc = currentVenCapabilityDescriptionMap.values();
		toDeleteDesc.removeAll(descriptions);
		if (!toDeleteDesc.isEmpty()) {
			otherReportRequestService.deleteByOtherReportCapabilityDescriptionIn(toDeleteDesc);
			otherReportCapabilityDescriptionService.delete(toDeleteDesc);

		}

		Collection<OtherReportCapability> toDeleteCap = currentVenCapabilityMap.values();
		toDeleteCap.removeAll(capabilities);
		if (!toDeleteCap.isEmpty()) {
			otherReportCapabilityService.delete(toDeleteCap);
		}

		OadrRegisteredReportType response = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder(requestID, responseCode, venID).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

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
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Cannot distribute OadrRegisterReportType after self METADATA payload generation", e);
		}
	}

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
							selfReportCapability.getReportRequestId(), selfReportCapability.getReportName(),
							System.currentTimeMillis())
					.withDuration(selfReportCapability.getDuration()).addReportDescription(oadrReportDescriptionType)
					.build();
			reports.add(oadrReportType);
		}

		OadrRegisterReportType oadrRegisterReportType = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder("", null, "").addOadrReport(reports).build();

		try {
			venDistributeService.distribute(ven, oadrRegisterReportType);
		} catch (Oadr20bMarshalException e) {
			LOGGER.error("Cannot distribute OadrRegisterReportType after self METADATA payload generation", e);
		}
	}

	public String oadrCreateReport(OadrCreateReportType payload, boolean signed)
			throws Oadr20bCreateReportApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {
		String requestID = payload.getRequestID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCreateReportApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiReportBuilders.newOadr20bCreatedReportBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID).build());
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
					String readingType = specifierPayloadType.getReadingType();

					SelfReportCapabilityDescription selfReportCapabilityDescription = selfReportCapabilityDescriptionService
							.findBySelfReportCapabilityAndRid(selfReportCapability, rid);

					SelfReportRequest selfReportRequest = new SelfReportRequest(ven, selfReportCapability,
							selfReportCapabilityDescription);
					selfReportRequest.setGranularity(granularity.getDuration());
					selfReportRequest.setReportBackDuration(reportBackDuration.getDuration());
					selfReportRequest.setReportRequestId(reportRequestID);
					selfReportRequest.setStart(start);
					selfReportRequest.setEnd(end);
					selfReportRequest.setReadingType(ReadingTypeEnumeratedType.fromValue(readingType));

					selfReportRequests.add(selfReportRequest);
				}
			}

		}
		selfReportRequestService.save(selfReportRequests);

		List<SelfReportRequest> findByTarget = selfReportRequestService.findByTarget(ven);
		List<String> pendingReportRequestId = findByTarget.stream().map(SelfReportRequest::getReportRequestId)
				.collect(Collectors.toList());

		OadrCreatedReportType response = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(requestID, responseCode, venID)
				.addPendingReportRequestId(pendingReportRequestId).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrCreatedReport(OadrCreatedReportType payload, boolean signed)
			throws Oadr20bCreatedReportApplicationLayerException, Oadr20bXMLSignatureException,
			Oadr20bMarshalException {

		EiResponseType eiResponse = payload.getEiResponse();
		String requestID = eiResponse.getRequestID();
		String venID = payload.getVenID();

		Ven ven = venService.findOneByUsername(venID);

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCreatedReportApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(xmlSignatureRequiredButAbsent, venID).build());
		}

		List<OtherReportRequest> otherReportRequests = otherReportRequestService.findBySourceAndReportRequestIdIn(ven,
				payload.getOadrPendingReports().getReportRequestID());

		List<OtherReportRequest> collect = otherReportRequests.stream()
				.filter(otherReportRequest -> !otherReportRequest.isAcked()).collect(Collectors.toList());
		for (OtherReportRequest report : collect) {
			report.setAcked(true);
		}

		otherReportRequestService.save(collect);

		OadrResponseType response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(requestID, HttpStatus.OK_200, venID).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public String oadrCancelReport(OadrCancelReportType payload, boolean signed)
			throws Oadr20bCancelReportApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {
		String requestID = payload.getRequestID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);
		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bCancelReportApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiReportBuilders.newOadr20bCanceledReportBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID).build());
		}

		List<SelfReportRequest> otherReportRequests = selfReportRequestService.findByTargetAndReportRequestId(ven,
				payload.getReportRequestID());

		selfReportRequestService.delete(otherReportRequests);

		List<SelfReportRequest> pendings = selfReportRequestService.findByTarget(ven);

		List<String> pendingReportIds = pendings.stream().map(SelfReportRequest::getReportRequestId)
				.collect(Collectors.toList());

		int responseCode = HttpStatus.OK_200;
		OadrCanceledReportType response = Oadr20bEiReportBuilders
				.newOadr20bCanceledReportBuilder(requestID, responseCode, venID)
				.addPendingReportRequestId(pendingReportIds).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public void otherOadrCancelReport(OadrCancelReportType payload)
			throws Oadr20bCancelReportApplicationLayerException {
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		List<OtherReportRequest> otherReportRequests = otherReportRequestService.findBySourceAndReportRequestIdIn(ven,
				payload.getReportRequestID());

		if (otherReportRequests != null && !otherReportRequests.isEmpty()) {
			otherReportRequestService.delete(otherReportRequests);
		}

	}

	public String oadrUpdateReport(OadrUpdateReportType payload, boolean signed)
			throws Oadr20bUpdateReportApplicationLayerException, Oadr20bXMLSignatureException, Oadr20bMarshalException {

		String requestID = payload.getRequestID();
		String venID = payload.getVenID();
		Ven ven = venService.findOneByUsername(venID);

		Long now = System.currentTimeMillis();

		if (ven.getXmlSignature() != null && ven.getXmlSignature() && !signed) {
			EiResponseType xmlSignatureRequiredButAbsent = Oadr20bResponseBuilders
					.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestID, venID).build();
			throw new Oadr20bUpdateReportApplicationLayerException(
					xmlSignatureRequiredButAbsent.getResponseDescription(),
					Oadr20bEiReportBuilders.newOadr20bUpdatedReportBuilder(requestID,
							Integer.valueOf(xmlSignatureRequiredButAbsent.getResponseCode()), venID).build());
		}

		int responseCode = HttpStatus.OK_200;

		List<OtherReportData> list = Lists.newArrayList();
		for (OadrReportType oadrReportType : payload.getOadrReport()) {
			String reportRequestID = oadrReportType.getReportRequestID();
			String reportSpecifierID = oadrReportType.getReportSpecifierID();
			Intervals intervals = oadrReportType.getIntervals();

			List<OtherReportRequest> findByReportRequestId = otherReportRequestService
					.findByReportRequestId(reportRequestID);

			Map<String, OtherReportRequest> requests = Maps.newHashMap();
			for (OtherReportRequest r : findByReportRequestId) {
				requests.put(r.getOtherReportCapabilityDescription().getRid(), r);
			}

			List<OtherReportRequest> toUpdate = Lists.newArrayList();
			for (IntervalType intervalType : intervals.getInterval()) {

				Dtstart intervalDtstart = intervalType.getDtstart();
				Long start = Oadr20bFactory.xmlCalendarToTimestamp(intervalDtstart.getDateTime());
				DurationPropType intervalDuration = intervalType.getDuration();
				String durationInterval = intervalDuration.getDuration();
				for (JAXBElement<? extends StreamPayloadBaseType> jaxbElement : intervalType.getStreamPayloadBase()) {
					if (jaxbElement.getDeclaredType().equals(OadrReportPayloadType.class)) {

						OadrReportPayloadType reportPayload = (OadrReportPayloadType) jaxbElement.getValue();
						String rid = reportPayload.getRID();

						Long confidence = reportPayload.getConfidence();
						Float accuracy = reportPayload.getAccuracy();

						String lastUpdateValue = null;

						OtherReportData otherReportData = new OtherReportData();
						otherReportData.setStart(start);
						otherReportData.setDuration(durationInterval);
						otherReportData.setReportSpecifierId(reportSpecifierID);
						otherReportData.setRid(rid);
						otherReportData.setConfidence(confidence);
						otherReportData.setAccuracy(accuracy);

						JAXBElement<? extends PayloadBaseType> payloadBase = reportPayload.getPayloadBase();
						if (payloadBase.getDeclaredType().equals(PayloadFloatType.class)) {

							PayloadFloatType payloadFloat = (PayloadFloatType) payloadBase.getValue();
							float value = payloadFloat.getValue();
							otherReportData.setValue(value);
							lastUpdateValue = String.valueOf(value);
							list.add(otherReportData);

							if (requests.containsKey(rid)) {
								OtherReportRequest otherReportRequest = requests.get(rid);
								if (otherReportRequest.getLastUpdateDatetime() == null
										|| otherReportRequest.getLastUpdateDatetime() < start) {
									otherReportRequest.setLastUpdateDatetime(start);
									otherReportRequest.setLastUpdateValue(lastUpdateValue);
								}

								toUpdate.add(otherReportRequest);
							}

						} else if (payloadBase.getDeclaredType().equals(OadrPayloadResourceStatusType.class)) {
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

							list.add(otherReportData);
						}

					}
				}
			}

			if (vtnConfig.getSaveVenData()) {
				otherReportDataService.save(list);
			}

			otherReportRequestService.save(toUpdate);
		}

		ven.setLastUpdateDatetime(now);

		venService.save(ven);

		OadrUpdatedReportType response = Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder(requestID, responseCode, venID).build();

		if (signed) {
			return xmlSignatureService.sign(response);
		} else {
			return jaxbContext.marshalRoot(response);
		}
	}

	public void distributeSubscriptionOadrCreatedReportPayload(Ven ven, OtherReportCapability reportCapability,
			List<OtherReportCapabilityDescription> descriptions) throws Oadr20bMarshalException {

		boolean send = false;
		String requestId = "";
		Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, ven.getUsername());

		for (OtherReportCapabilityDescription description : descriptions) {

			if (description.getId() != null) {
				OtherReportRequest otherReportRequest = otherReportRequestService
						.findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(ven, description,
								reportCapability.getReportRequestId());
				if (otherReportRequest != null) {

					String granularity = otherReportRequest.getGranularity();
					String reportBackDuration = otherReportRequest.getReportBackDuration();
					String reportRequestId = otherReportRequest.getReportRequestId();

					send = true;
					Oadr20bReportRequestTypeBuilder reportRequestBuilder = Oadr20bEiReportBuilders
							.newOadr20bReportRequestTypeBuilder(reportRequestId,
									reportCapability.getReportSpecifierId(), granularity, reportBackDuration);

					if (otherReportRequest.getStart() != null && otherReportRequest.getEnd() != null) {
						String duration = Oadr20bFactory
								.millisecondToXmlDuration(otherReportRequest.getEnd() - otherReportRequest.getStart());
						WsCalendarIntervalType createWsCalendarIntervalType = Oadr20bFactory
								.createWsCalendarIntervalType(otherReportRequest.getStart(), duration);
						reportRequestBuilder.withWsCalendarIntervalType(createWsCalendarIntervalType);
					}

					reportRequestBuilder.addSpecifierPayload(null, description.getReadingType(), description.getRid());

					newOadr20bCreateReportBuilder.addReportRequest(reportRequestBuilder.build());
				}
			}

		}
		if (send) {
			venDistributeService.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	public void distributeSubscriptionOadrCreatedReportPayload(Ven ven, List<OtherReportRequest> requests)
			throws Oadr20bMarshalException {

		boolean send = false;
		String requestId = "";
		Oadr20bCreateReportBuilder newOadr20bCreateReportBuilder = Oadr20bEiReportBuilders
				.newOadr20bCreateReportBuilder(requestId, ven.getUsername());

		for (OtherReportRequest request : requests) {

			OtherReportCapability otherReportCapability = request.getOtherReportCapability();
			OtherReportCapabilityDescription otherReportCapabilityDescription = request
					.getOtherReportCapabilityDescription();

			String granularity = request.getGranularity();
			String reportBackDuration = request.getReportBackDuration();
			String reportRequestId = request.getReportRequestId();

			send = true;
			Oadr20bReportRequestTypeBuilder reportRequestBuilder = Oadr20bEiReportBuilders
					.newOadr20bReportRequestTypeBuilder(reportRequestId, otherReportCapability.getReportSpecifierId(),
							granularity, reportBackDuration);

			if (request.getStart() != null && request.getEnd() != null) {
				String duration = Oadr20bFactory.millisecondToXmlDuration(request.getEnd() - request.getStart());
				WsCalendarIntervalType createWsCalendarIntervalType = Oadr20bFactory
						.createWsCalendarIntervalType(request.getStart(), duration);
				reportRequestBuilder.withWsCalendarIntervalType(createWsCalendarIntervalType);
			} else if (request.getStart() != null) {
				String duration = "P0D";
				WsCalendarIntervalType createWsCalendarIntervalType = Oadr20bFactory
						.createWsCalendarIntervalType(request.getStart(), duration);
				reportRequestBuilder.withWsCalendarIntervalType(createWsCalendarIntervalType);
			}

			reportRequestBuilder.addSpecifierPayload(null, otherReportCapabilityDescription.getReadingType(),
					otherReportCapabilityDescription.getRid());

			newOadr20bCreateReportBuilder.addReportRequest(reportRequestBuilder.build());

		}
		if (send) {
			venDistributeService.distribute(ven, newOadr20bCreateReportBuilder.build());
		}
	}

	/**
	 * @param ven
	 * @param reportCapability
	 * @param rids
	 * @param reportBackDuration
	 * @param granularity
	 * @throws Oadr20bMarshalException
	 */
	public void subscribe(Ven ven, OtherReportCapability reportCapability, List<String> rids, String reportBackDuration,
			String granularity, Long start, Long end) throws Oadr20bMarshalException {

		String reportRequestId = UUID.randomUUID().toString();

		List<OtherReportCapabilityDescription> descriptions = otherReportCapabilityDescriptionService
				.findByOtherReportCapability(reportCapability);

		if (descriptions == null || descriptions.isEmpty()) {
			throw new IllegalArgumentException("reportCapability is not associated with any description");
		}

		Long granularityMillisecond = null;
		Long reportBackDurationMillisecond = null;

		granularityMillisecond = Oadr20bFactory.xmlDurationToMillisecond(granularity);
		reportBackDurationMillisecond = Oadr20bFactory.xmlDurationToMillisecond(reportBackDuration);

		if (granularityMillisecond == null || reportBackDurationMillisecond == null) {
			throw new IllegalArgumentException(
					"reportBackDuration and granularity must be valid xmlduration lexical representation (PnYnMnDTnHnMnS)");
		}

		List<OtherReportRequest> otherReportRequests = new ArrayList<OtherReportRequest>();
		for (OtherReportCapabilityDescription description : descriptions) {

			if (rids == null || rids.contains(description.getRid())) {

				OtherReportRequest otherReportRequest = otherReportRequestService
						.findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(ven, description,
								reportRequestId);

				if (otherReportRequest == null) {
					otherReportRequest = new OtherReportRequest(ven, reportCapability, description, reportRequestId,
							granularity, reportBackDuration);
				}

				otherReportRequest.setGranularity(granularity);
				otherReportRequest.setReportBackDuration(reportBackDuration);
				otherReportRequest.setStart(start);
				otherReportRequest.setEnd(end);

				otherReportRequests.add(otherReportRequest);

			}

		}

		if (reportCapability.getReportName().equals(ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE)
				|| reportCapability.getReportName().equals(ReportNameEnumeratedType.METADATA_TELEMETRY_STATUS)
				|| reportCapability.getReportName().equals(ReportNameEnumeratedType.METADATA_HISTORY_USAGE)) {

			reportCapability.setReportRequestId(reportRequestId);

			otherReportCapabilityService.save(reportCapability);

			otherReportCapabilityDescriptionService.save(descriptions);
		}

		otherReportRequestService.save(otherReportRequests);

		distributeSubscriptionOadrCreatedReportPayload(ven, reportCapability, descriptions);

	}

	public void request(Ven ven, OtherReportCapability reportCapability, List<String> rids, Long start, Long end)
			throws Oadr20bMarshalException {

		String reportRequestId = UUID.randomUUID().toString();

		List<OtherReportCapabilityDescription> descriptions = otherReportCapabilityDescriptionService
				.findByOtherReportCapability(reportCapability);

		if (descriptions == null || descriptions.isEmpty()) {
			throw new IllegalArgumentException("reportCapability is not associated with any description");
		}

		List<OtherReportRequest> otherReportRequests = new ArrayList<OtherReportRequest>();
		for (OtherReportCapabilityDescription description : descriptions) {

			if (rids == null || rids.contains(description.getRid())) {

				OtherReportRequest otherReportRequest = new OtherReportRequest(ven, reportCapability, description,
						reportRequestId, "P0D", "P0D");

				otherReportRequest.setGranularity("P0D");
				otherReportRequest.setReportBackDuration("P0D");
				otherReportRequest.setStart(start);
				otherReportRequest.setEnd(end);
				otherReportRequest.setReportRequestId(reportRequestId);
				otherReportRequests.add(otherReportRequest);

			}

		}

		otherReportRequestService.save(otherReportRequests);

		List<OtherReportRequest> findBySourceAndReportSpecifierId = otherReportRequestService
				.findBySourceAndReportSpecifierId(ven, reportCapability.getReportSpecifierId());
		otherReportRequests.addAll(findBySourceAndReportSpecifierId);
		distributeSubscriptionOadrCreatedReportPayload(ven, otherReportRequests);

	}

	public void removeFromSubscription(Ven ven, OtherReportCapability reportCapability, List<String> rids)
			throws Oadr20bMarshalException, OadrElementNotFoundException {

		String reportRequestId = reportCapability.getReportRequestId();

		List<OtherReportCapabilityDescription> descriptions = otherReportCapabilityDescriptionService
				.findByOtherReportCapability(reportCapability);

		List<OtherReportRequest> otherReportRequests = new ArrayList<OtherReportRequest>();
		boolean stillSubscribed = false;
		for (OtherReportCapabilityDescription description : descriptions) {

			if (rids.contains(description.getRid())) {

				OtherReportRequest otherReportRequest = otherReportRequestService
						.findBySourceAndOtherReportCapabilityDescriptionAndReportRequestId(ven, description,
								reportRequestId);

				if (otherReportRequest != null) {
					otherReportRequests.add(otherReportRequest);
				}

			}

		}

		if (stillSubscribed) {
			otherReportRequestService.delete(otherReportRequests);

			otherReportCapabilityDescriptionService.save(descriptions);

			distributeSubscriptionOadrCreatedReportPayload(ven, reportCapability, descriptions);
		} else {
			this.unsubscribe(ven, reportRequestId);
		}

	}

	public void unsubscribe(Ven ven, String reportRequestID)
			throws OadrElementNotFoundException, Oadr20bMarshalException {

		Iterable<OtherReportCapability> findByPayloadReportRequestId2 = otherReportCapabilityService
				.findByReportRequestId(Arrays.asList(reportRequestID));
		for (OtherReportCapability cap : findByPayloadReportRequestId2) {
			cap.setReportRequestId("0");
		}

		otherReportCapabilityService.save(findByPayloadReportRequestId2);

		List<OtherReportRequest> findByReportRequestId = otherReportRequestService
				.findByReportRequestId(reportRequestID);
		otherReportRequestService.delete(findByReportRequestId);

		String requestId = "";
		OadrCancelReportType build = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(requestId, ven.getUsername(), false).addReportRequestId(reportRequestID)
				.build();

		venDistributeService.distribute(ven, build);
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrRegisterReportType oadrRegisterReport)
			throws Oadr20bRegisterReportApplicationLayerException {
		String venID = oadrRegisterReport.getVenID();
		String requestID = oadrRegisterReport.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bRegisterReportApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiReportBuilders
							.newOadr20bRegisteredReportBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrUpdateReportType oadrUpdateReport)
			throws Oadr20bUpdateReportApplicationLayerException {
		String venID = oadrUpdateReport.getVenID();
		String requestID = oadrUpdateReport.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bUpdateReportApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiReportBuilders
							.newOadr20bUpdatedReportBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCreatedReportType oadrCreatedReport)
			throws Oadr20bCreatedReportApplicationLayerException {
		String venID = oadrCreatedReport.getVenID();
		String requestID = oadrCreatedReport.getEiResponse().getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCreatedReportApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bResponseBuilders.newOadr20bResponseBuilder(mismatchCredentialsVenIdResponse, venID).build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCreateReportType oadrCreateReport)
			throws Oadr20bCreateReportApplicationLayerException {
		String venID = oadrCreateReport.getVenID();
		String requestID = oadrCreateReport.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCreateReportApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiReportBuilders
							.newOadr20bCreatedReportBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}
	}

	public void checkMatchUsernameWithRequestVenId(String username, OadrCancelReportType oadrCancelReport)
			throws Oadr20bCancelReportApplicationLayerException {
		String venID = oadrCancelReport.getVenID();
		String requestID = oadrCancelReport.getRequestID();
		if (!username.equals(venID)) {
			EiResponseType mismatchCredentialsVenIdResponse = Oadr20bResponseBuilders
					.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestID, username, venID).build();
			throw new Oadr20bCancelReportApplicationLayerException(
					mismatchCredentialsVenIdResponse.getResponseDescription(),
					Oadr20bEiReportBuilders
							.newOadr20bCanceledReportBuilder(requestID,
									Integer.valueOf(mismatchCredentialsVenIdResponse.getResponseCode()), venID)
							.build());
		}
	}

}
