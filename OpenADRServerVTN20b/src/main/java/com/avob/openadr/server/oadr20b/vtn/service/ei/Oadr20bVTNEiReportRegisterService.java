package com.avob.openadr.server.oadr20b.vtn.service.ei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiResponseType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrSamplingRateType;
import com.avob.openadr.model.oadr20b.xcal.Dtstart;
import com.avob.openadr.model.oadr20b.xcal.DurationPropType;
import com.avob.openadr.server.common.vtn.models.ItemBase;
import com.avob.openadr.server.common.vtn.models.Target;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.models.venmarketcontext.VenMarketContext;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SamplingRate;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.VenReportDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.VenRequestReportService;
import com.avob.openadr.server.oadr20b.vtn.service.VenResourceCreateService;
import com.avob.openadr.server.oadr20b.vtn.service.dtomapper.Oadr20bDtoMapper;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bAppNotificationPublisher;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.google.common.collect.Lists;

@Service
public class Oadr20bVTNEiReportRegisterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";

	protected static final String METADATA_REPORT_RID = "METADATA";

	@Resource
	protected VenMarketContextService venMarketContextService;

	@Resource
	protected OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	protected OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	protected OtherReportRequestService otherReportRequestService;

	@Resource
	protected OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	protected VenDistributeService venDistributeService;

	@Resource
	protected VenRequestReportService venRequestReportService;

	@Resource
	protected VenResourceService venResourceService;

	@Resource
	private Oadr20bAppNotificationPublisher oadr20bAppNotificationPublisher;

	@Resource
	private VenResourceCreateService venResourceCreateService;

	@Resource
	private Oadr20bDtoMapper oadr20bDtoMapper;

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
		Map<String, List<String>> capToDesc = new HashMap<>();
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
			List<String> rids = new ArrayList<>();
			for (OadrReportDescriptionType oadrReportDescriptionType : oadrReportType.getOadrReportDescription()) {

				String rid = oadrReportDescriptionType.getRID();
				rids.add(rid);
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

					description.setEiSubject(new HashSet<>(createTargetList));

				}

				if (oadrReportDescriptionType.getReportDataSource() != null) {

					EiTargetType reportDataSource = oadrReportDescriptionType.getReportDataSource();

					List<Target> createTargetList = Oadr20bVTNEiServiceUtils.createTargetList(reportDataSource);
					description.setEiDatasource(new HashSet<>(createTargetList));

				}

				descriptionDto.add(oadr20bDtoMapper.map(description, ReportCapabilityDescriptionDto.class));
				descriptions.add(description);
				capabilityDescription.add(description);

			}
			capToDesc.put(capabilityDto.getReportSpecifierId(), rids);
			capabilityDto.setDescriptions(descriptionDto);
			capabilitiesDto.add(capabilityDto);
			if (!created) {
				try {
					venDistributeService.distributeSubscriptionOadrCreatedReportPayload(ven);
				} catch (Oadr20bApplicationLayerException e) {
					LOGGER.error("Can't distribute report requests to venId: " + ven.getUsername(), e);
				}
			}

		}
		venReportDto.setCapabilities(capabilitiesDto);

		Collection<OtherReportCapabilityDescription> toDeleteDesc = currentVenCapabilityDescriptionMap.values();
		toDeleteDesc.removeAll(descriptions);
		if (!toDeleteDesc.isEmpty()) {

			for (OtherReportCapabilityDescription desc : toDeleteDesc) {
				otherReportRequestSpecifierDao.deleteByVenIdAndReportSpecifierIdAndRid(ven.getUsername(),
						desc.getOtherReportCapability().getReportSpecifierId(), desc.getRid());
				otherReportCapabilityDescriptionService.delete(desc.getId());
			}

		}

		Collection<OtherReportCapability> toDeleteCap = currentVenCapabilityMap.values();
		toDeleteCap.removeAll(capabilities);
		if (!toDeleteCap.isEmpty()) {
			otherReportCapabilityService.delete(toDeleteCap);
		}

		if (hasMetadataReport) {
			otherReportRequestSpecifierDao.deleteByVenId(ven.getUsername());
			otherReportRequestService.deleteByOtherReportCapabilitySource(ven);
		}

		Iterable<OtherReportCapability> savedCapabilities = otherReportCapabilityService.save(capabilities);
		Iterable<OtherReportCapabilityDescription> savedDescriptions = otherReportCapabilityDescriptionService
				.save(descriptions);

		oadr20bAppNotificationPublisher.notifyRegisterReport(venReportDto, venID);

		try {
			venResourceCreateService.createResourceTree(ven, savedCapabilities, savedDescriptions, capToDesc);

		} catch (Oadr20bApplicationLayerException e) {
			LOGGER.error("Can't create subscriptions after registerReport", e);
		}

		return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID, responseCode, venID).build();

	}

}
