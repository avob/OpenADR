package com.avob.openadr.server.oadr20b.vtn.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.EndDeviceAssertMridType;
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.models.ven.VenDto;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataFloatDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataKeyTokenDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.OtherReportDataPayloadResourceStatusDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateRequestDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestDtoCreateSubscriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDao;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequestSpecifierSearchCriteria;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bDemandResponseEventCreateListener;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushListener;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataFloatService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataKeyTokenService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataPayloadResourceStatusService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiXmpp;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpReportMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVtnMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockVen;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrParamBuilder;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReportScenarioTest {

	private static final String MOUAICCOOL = "mouaiccool";
	private static final String RID_ID = "rid";
	private static final String REPORT_REQUEST_ID = "reportRequestId";
	private static final String REPORT_SPECIFIER_ID = "reportSpecifierId";
	private static final String REQUEST_ID = "requestId";

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private DemandResponseEventService demandResponseEventService;

	@Resource
	private OtherReportCapabilityService otherReportCapabilityService;

	@Resource
	private OtherReportCapabilityDescriptionService otherReportCapabilityDescriptionService;

	@Resource
	private OtherReportRequestService otherReportRequestService;

	@Resource
	private OtherReportRequestSpecifierDao otherReportRequestSpecifierDao;

	@Resource
	private OtherReportDataFloatService otherReportDataService;

	@Resource
	private OtherReportDataPayloadResourceStatusService otherReportDataPayloadResourceStatusService;

	@Resource
	private OtherReportDataKeyTokenService otherReportDataKeyTokenService;

	@Resource
	private SelfReportCapabilityService selfReportCapabilityservice;

	@Resource
	private SelfReportCapabilityDescriptionService selfReportCapabilityDescriptionService;

	@Resource
	private SelfReportRequestService selfReportRequestService;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockEiXmpp oadrMockEiXmpp;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private OadrMockHttpReportMvc oadrMockHttpReportMvc;

	@Resource
	private OadrMockHttpVtnMvc oadrMockHttpVtnMvc;

	@Resource
	private JmsTemplate jmsTemplate;

	@Resource
	private Oadr20bPushListener oadr20bPushListener;

	@Resource
	private Oadr20bDemandResponseEventCreateListener oadr20bDemandResponseEventCreateListener;

	private Oadr20bJAXBContext jaxbContext;

	@PostConstruct
	public void init() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bPushListener.receiveCommand(invocation.getArgument(1));

			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(VenDistributeService.OADR20B_QUEUE), Mockito.any(String.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bDemandResponseEventCreateListener.receiveEvent(invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(DemandResponseEventPublisher.OADR20B_QUEUE),
				Mockito.any(String.class));
	}

	@Before
	public void setup() throws JAXBException {

	}

	@Test
	public void testVENSourceVTNTarget() throws Exception {
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_testVENSourceVTNTarget(mockVen);
		}
	}

	private void _testVENSourceVTNTarget(OadrMockVen mockVen) throws Exception {

		LinkedMultiValueMap<String, String> params;

		// OADR POLL CONTROLLER - poll supposed to be 'empty' - OadrResponseType for
		// HTTP VEN
		mockVen.pollForEmpty();

		// create VEN METADATA payload with one report capability containing one
		// description
		String rid = RID_ID;
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		long createdTimestamp = System.currentTimeMillis();
		ReportEnumeratedType reportType = ReportEnumeratedType.READING;
		ReadingTypeEnumeratedType readingType = ReadingTypeEnumeratedType.DIRECT_READ;
		String minPeriod = "PT15M";
		String maxPeriod = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(REPORT_SPECIFIER_ID, reportName, createdTimestamp)
				.withDuration("PT1H").withStart(System.currentTimeMillis())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid, reportType, readingType)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY, ISO3AlphaCurrencyCodeContentType.EUR,
								SiScaleCodeType.NONE)
						.withSubject(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
								.addEndDeviceAsset(Arrays.asList(Oadr20bFactory
										.createEndDeviceAssetType(EndDeviceAssertMridType.BASEBOARD_HEATER)))
								.build())

						.withDataSource(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId("res1").build())
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "2", reportType, readingType)
						.withVoltageBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.withMarketContext(OadrDataBaseSetup.MARKET_CONTEXT_NAME).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "3", reportType, readingType)
						.withEnergyApparentBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "4", reportType, readingType)
						.withEnergyReactiveBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "5", reportType, readingType)
						.withEnergyRealBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "6", reportType, readingType)
						.withPowerApparentBase(SiScaleCodeType.NONE, new BigDecimal(60), new BigDecimal(230), true)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "7", reportType, readingType)
						.withPowerReactiveBase(SiScaleCodeType.NONE, new BigDecimal(60), new BigDecimal(230), true)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "8", reportType, readingType)
						.withPowerRealBase(PowerRealUnitType.WATT, SiScaleCodeType.NONE, new BigDecimal(60),
								new BigDecimal(230), true)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "9", reportType, readingType)
						.withFrequencyBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "10", reportType, readingType)
						.withPulseCountBase(5.0F).withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "11", reportType, readingType)
						.withTemperatureBase(TemperatureUnitType.CELSIUS, SiScaleCodeType.NONE)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "12", reportType, readingType)
						.withThermBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "13", reportType, readingType)
						.withVoltageBase(SiScaleCodeType.NONE).withOadrSamplingRate(minPeriod, maxPeriod, false)
						.build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "14", reportType, readingType)
						.withCustomUnitBase(MOUAICCOOL, MOUAICCOOL, SiScaleCodeType.NONE)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "15", reportType, readingType)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY_PER_K_WH,
								ISO3AlphaCurrencyCodeContentType.EUR, SiScaleCodeType.NONE)
						.build())
				.build();

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		OadrRegisterReportType oadrRegisterReportType = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder(REQUEST_ID, "mouaiccool").addOadrReport(report).build();

		OadrRegisteredReportType oadrRegisteredReportType = mockVen.report(oadrRegisterReportType, HttpStatus.OK_200,
				OadrRegisteredReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrRegisteredReportType.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrRegisteredReportType
		oadrRegisterReportType = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(REQUEST_ID, mockVen.getVenId())
				.addOadrReport(report).build();
		oadrRegisteredReportType = mockVen.report(oadrRegisterReportType, HttpStatus.OK_200,
				OadrRegisteredReportType.class);

		assertNotNull(oadrRegisteredReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRegisteredReportType.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), oadrRegisteredReportType.getVenID());
		assertNotNull(oadrRegisteredReportType.getOadrReportRequest());

		// VEN CONTROLLER - get available report
		params = new LinkedMultiValueMap<>();
		List<ReportCapabilityDto> reportcapabilityList = oadrMockHttpVenMvc.getVenReportAvailable(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params, HttpStatus.OK_200);
		assertEquals(1, reportcapabilityList.size());
		assertEquals(REPORT_SPECIFIER_ID, reportcapabilityList.get(0).getReportSpecifierId());
		assertEquals(reportName, reportcapabilityList.get(0).getReportName());

		// VEN CONTROLLER - get available report description by reportSpecifierId
		params = OadrParamBuilder.builder().addReportSpecifierId(REPORT_SPECIFIER_ID).build();
		List<ReportCapabilityDescriptionDto> reportcapabilityDescriptionList = oadrMockHttpVenMvc
				.getVenReportAvailableDescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params,
						HttpStatus.OK_200);

		assertEquals(15, reportcapabilityDescriptionList.size());
		assertEquals(rid, reportcapabilityDescriptionList.get(0).getRid());
		assertEquals(readingType, reportcapabilityDescriptionList.get(0).getReadingType());
		assertEquals(reportType, reportcapabilityDescriptionList.get(0).getReportType());

		// update register report
		report = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(REPORT_SPECIFIER_ID, reportName, createdTimestamp)
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid, reportType, readingType)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY, ISO3AlphaCurrencyCodeContentType.EUR,
								SiScaleCodeType.NONE)
						.withSubject(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
								.addEndDeviceAsset(Arrays.asList(Oadr20bFactory
										.createEndDeviceAssetType(EndDeviceAssertMridType.BASEBOARD_HEATER)))
								.build())

						.withDataSource(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addResourceId("res1").build())
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())

				.build();
		oadrRegisterReportType = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(REQUEST_ID, mockVen.getVenId())
				.addOadrReport(report).build();

		// EI REPORT CONTROLLER - send OadrRegisteredReportType
		oadrRegisteredReportType = mockVen.report(oadrRegisterReportType, HttpStatus.OK_200,
				OadrRegisteredReportType.class);

		assertNotNull(oadrRegisteredReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRegisteredReportType.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), oadrRegisteredReportType.getVenID());
		assertNotNull(oadrRegisteredReportType.getOadrReportRequest());

		// VEN CONTROLLER - get available by reportSpecifierId
		reportcapabilityList = oadrMockHttpVenMvc.getVenReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertEquals(1, reportcapabilityList.size());
		assertEquals(REPORT_SPECIFIER_ID, reportcapabilityList.get(0).getReportSpecifierId());
		assertEquals(reportName, reportcapabilityList.get(0).getReportName());

		// VEN CONTROLLER - get available description by reportSpecifierId
		params = OadrParamBuilder.builder().addReportSpecifierId(REPORT_SPECIFIER_ID).build();
		reportcapabilityDescriptionList = oadrMockHttpVenMvc.getVenReportAvailableDescription(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params, HttpStatus.OK_200);

		assertEquals(1, reportcapabilityDescriptionList.size());
		assertEquals(rid, reportcapabilityDescriptionList.get(0).getRid());
		assertEquals(readingType, reportcapabilityDescriptionList.get(0).getReadingType());
		assertEquals(reportType, reportcapabilityDescriptionList.get(0).getReportType());

		Long reportCapabilityPrivateId = reportcapabilityList.get(0).getId();
		Long reportCapabilityDescriptionPrivateId = reportcapabilityDescriptionList.get(0).getId();
		OtherReportCapability reportCapability = otherReportCapabilityService.findOne(reportCapabilityPrivateId);
		OtherReportCapabilityDescription reportCapabilityDescription = otherReportCapabilityDescriptionService
				.findOne(reportCapabilityDescriptionPrivateId);

		// VEN CONTROLLER - search ven other report capability by reportSpecifierId
		List<OtherReportCapabilityDto> venReportCapability = oadrMockHttpVenMvc.searchVenReportAvailable(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), HttpStatus.OK_200);
		assertEquals(1, venReportCapability.size());
		assertEquals(REPORT_SPECIFIER_ID, venReportCapability.get(0).getReportSpecifierId());

		// REPORT CONTROLLER - search other report capability by venId/reportSpecifierId
		params = OadrParamBuilder.builder().addVenId(mockVen.getVenId()).build();
		List<OtherReportCapabilityDto> searchReportAvailable = oadrMockHttpReportMvc
				.searchReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailable.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailable.get(0).getReportSpecifierId());

		params = OadrParamBuilder.builder().addVenId(mockVen.getVenId()).build();
		searchReportAvailable = oadrMockHttpReportMvc.searchReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailable.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailable.get(0).getReportSpecifierId());

		params = OadrParamBuilder.builder().addVenId("mouaiccool").build();
		searchReportAvailable = oadrMockHttpReportMvc.searchReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				params, HttpStatus.OK_200);
		assertEquals(0, searchReportAvailable.size());

		params = OadrParamBuilder.builder().addReportSpecifierId(REPORT_SPECIFIER_ID).build();
		searchReportAvailable = oadrMockHttpReportMvc.searchReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailable.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailable.get(0).getReportSpecifierId());

		params = OadrParamBuilder.builder().addReportSpecifierId("mouaiccool").build();
		searchReportAvailable = oadrMockHttpReportMvc.searchReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				params, HttpStatus.OK_200);
		assertEquals(0, searchReportAvailable.size());

		// REPORT CONTROLLER - search other report capability description by venId
		params = OadrParamBuilder.builder().addReportSpecifierId(REPORT_SPECIFIER_ID).build();
		List<OtherReportCapabilityDescriptionDto> searchReportAvailabledescription = oadrMockHttpReportMvc
				.searchReportAvailabledescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailabledescription.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailabledescription.get(0).getReportSpecifierId());
		assertEquals(rid, searchReportAvailabledescription.get(0).getRid());

		params = OadrParamBuilder.builder().addReportName(reportCapability.getReportName()).build();
		searchReportAvailabledescription = oadrMockHttpReportMvc
				.searchReportAvailabledescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailabledescription.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailabledescription.get(0).getReportSpecifierId());
		assertEquals(rid, searchReportAvailabledescription.get(0).getRid());

		params = OadrParamBuilder.builder().addReportType(reportCapabilityDescription.getReportType()).build();
		searchReportAvailabledescription = oadrMockHttpReportMvc
				.searchReportAvailabledescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailabledescription.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailabledescription.get(0).getReportSpecifierId());
		assertEquals(rid, searchReportAvailabledescription.get(0).getRid());

		params = OadrParamBuilder.builder().addReadingType(reportCapabilityDescription.getReadingType()).build();
		searchReportAvailabledescription = oadrMockHttpReportMvc
				.searchReportAvailabledescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, params, HttpStatus.OK_200);
		assertEquals(1, searchReportAvailabledescription.size());
		assertEquals(REPORT_SPECIFIER_ID, searchReportAvailabledescription.get(0).getReportSpecifierId());
		assertEquals(rid, searchReportAvailabledescription.get(0).getRid());

		// VEN CONTROLLER - subscribe
		List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions = new ArrayList<>();
		OtherReportRequestDtoCreateSubscriptionDto subscription = new OtherReportRequestDtoCreateSubscriptionDto();
		subscription.setReportRequestId(REPORT_REQUEST_ID);
		subscription.setGranularity(minPeriod);
		subscription.setReportBackDuration(maxPeriod);
		subscription.setReportSpecifierId(reportCapability.getReportSpecifierId());
		Map<String, Boolean> ridMap = new HashMap<>();
		ridMap.put(reportCapabilityDescription.getRid(), true);
		subscription.setRid(ridMap);
		subscriptions.add(subscription);
		oadrMockHttpVenMvc.subscribe(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), subscriptions,
				HttpStatus.OK_200);

		// VEN CONTROLLER - get ven requested report
		params = OadrParamBuilder.builder().build();
		List<OtherReportRequestDto> venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(1, venReportRequested.size());

		params = OadrParamBuilder.builder().build();
		venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(1, venReportRequested.size());

		params = OadrParamBuilder.builder().addReportSpecifierId(REPORT_SPECIFIER_ID).build();
		venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(1, venReportRequested.size());

		params = OadrParamBuilder.builder().addReportRequestId(REPORT_REQUEST_ID).build();
		venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(1, venReportRequested.size());

		// VEN CONTROLLER - search ven requested report specifier
		OtherReportRequestSpecifierSearchCriteria criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		List<OtherReportRequestSpecifierDto> searchVenReportRequestedSpecifier = oadrMockHttpVenMvc
				.searchVenReportRequestedSpecifier(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
						criteria, HttpStatus.OK_200);
		assertNotNull(searchVenReportRequestedSpecifier);
		assertEquals(1, searchVenReportRequestedSpecifier.size());
		assertEquals(reportCapabilityDescription.getRid(), searchVenReportRequestedSpecifier.get(0).getRid());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setRid(Lists.newArrayList(reportCapabilityDescription.getRid()));
		searchVenReportRequestedSpecifier = oadrMockHttpVenMvc.searchVenReportRequestedSpecifier(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), criteria, HttpStatus.OK_200);
		assertNotNull(searchVenReportRequestedSpecifier);
		assertEquals(1, searchVenReportRequestedSpecifier.size());
		assertEquals(reportCapabilityDescription.getRid(), searchVenReportRequestedSpecifier.get(0).getRid());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportRequestId(Lists.newArrayList(REPORT_REQUEST_ID));
		searchVenReportRequestedSpecifier = oadrMockHttpVenMvc.searchVenReportRequestedSpecifier(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), criteria, HttpStatus.OK_200);
		assertNotNull(searchVenReportRequestedSpecifier);
		assertEquals(1, searchVenReportRequestedSpecifier.size());
		assertEquals(reportCapabilityDescription.getRid(), searchVenReportRequestedSpecifier.get(0).getRid());

		// VEN CONTROLLER - search other report request
		params = OadrParamBuilder.builder().build();
		List<OtherReportRequestDto> searchVenReportRequested = oadrMockHttpVenMvc.searchVenReportRequested(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params, HttpStatus.OK_200);
		assertEquals(1, searchVenReportRequested.size());
		assertEquals(REPORT_SPECIFIER_ID, searchVenReportRequested.get(0).getReportSpecifierId());

		// OADR POLL CONTROLLER - second poll supposed to contains CreateReport cause
		// user has subscribe
		OadrCreateReportType secondPoll = mockVen.poll(HttpStatus.OK_200, OadrCreateReportType.class);
		assertNotNull(secondPoll);
		assertEquals(1, secondPoll.getOadrReportRequest().size());
		assertEquals(reportCapability.getReportSpecifierId(),
				secondPoll.getOadrReportRequest().get(0).getReportSpecifier().getReportSpecifierID());
		assertEquals(minPeriod,
				secondPoll.getOadrReportRequest().get(0).getReportSpecifier().getGranularity().getDuration());
		assertEquals(maxPeriod,
				secondPoll.getOadrReportRequest().get(0).getReportSpecifier().getReportBackDuration().getDuration());
		assertEquals(1, secondPoll.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().size());
		assertEquals(reportCapabilityDescription.getRid(),
				secondPoll.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(0).getRID());

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		OadrCreatedReportType oadrCreatedReportType = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder("requestId", HttpStatus.OK_200, "mouaiccool")
				.addPendingReportRequestId(REPORT_REQUEST_ID).build();
		OadrResponseType response = mockVen.report(oadrCreatedReportType, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				response.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrCreatedReportType
		oadrCreatedReportType = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder("requestId", HttpStatus.OK_200, mockVen.getVenId())
				.addPendingReportRequestId(REPORT_REQUEST_ID).build();
		response = mockVen.report(oadrCreatedReportType, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), response.getVenID());

		// VEN CONTROLLER - test previous payload has successfully acked requestreport
		params = OadrParamBuilder.builder().build();
		List<OtherReportRequestDto> reportRequestList = oadrMockHttpVenMvc.getVenReportRequested(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), params, HttpStatus.OK_200);
		assertEquals(1, reportRequestList.size());
		assertTrue(reportRequestList.get(0).isAcked());
		assertEquals(minPeriod, reportRequestList.get(0).getGranularity());
		assertEquals(maxPeriod, reportRequestList.get(0).getReportBackDuration());
		assertEquals(REPORT_REQUEST_ID, reportRequestList.get(0).getReportRequestId());
		assertNull(reportRequestList.get(0).getRequestorUsername());

		// create VEN oadrUpdateReport float payload
		String intervalId = "intervalId";
		long start = System.currentTimeMillis();
		String xmlDuration = "P1D";
		Long confidence = 80L;
		Float accuracy = 1F;
		Float value = 3F;
		String reportId = "reportId";
		OadrReportType reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName,
						createdTimestamp, start, xmlDuration)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, value).build())
				.build();

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		OadrUpdateReportType oadrUpdateReportType = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportBuilder("", "mouaiccool").addReport(reportUpdate).build();
		OadrUpdatedReportType oadrUpdatedReportType = mockVen.report(oadrUpdateReportType, HttpStatus.OK_200,
				OadrUpdatedReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrUpdatedReportType.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrUpdateReportType Float data
		oadrUpdateReportType = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", mockVen.getVenId())
				.addReport(reportUpdate).build();
		oadrUpdatedReportType = mockVen.report(oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);
		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), oadrUpdatedReportType.getVenID());

		// VEN CONTROLLER - test previous payload has successfully inserted report data
		// in
		// database
		List<OtherReportDataFloatDto> reportDataList = oadrMockHttpVenMvc.getVenReportRequestedFloatData(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), REPORT_SPECIFIER_ID, HttpStatus.OK_200);
		assertEquals(1, reportDataList.size());

		oadrMockHttpVenMvc.getVenReportRequestedFloatData(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				"fakeReportSpecifierId", HttpStatus.NOT_ACCEPTABLE_406);

		reportDataList = oadrMockHttpVenMvc.getVenReportRequestedSpecifierFloatData(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), REPORT_SPECIFIER_ID, rid,
				HttpStatus.OK_200);
		assertEquals(1, reportDataList.size());
		assertTrue(reportDataList.get(0).getStart().equals(start));
		assertEquals(confidence, reportDataList.get(0).getConfidence());
		assertEquals(value, reportDataList.get(0).getValue());

		oadrMockHttpVenMvc.getVenReportRequestedSpecifierFloatData(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), REPORT_SPECIFIER_ID, "fakeRid", HttpStatus.NOT_ACCEPTABLE_406);

		Long reportDataPrivateId = reportDataList.get(0).getId();

		// cleanup data
		otherReportDataService.delete(reportDataPrivateId);

		// create VEN oadrUpdateReport OadrPayloadResourceStatusType payload
		OadrLoadControlStateTypeType loadControlState = Oadr20bFactory.createOadrLoadControlStateTypeType(0f, 0f, 0f,
				0f);
		OadrLoadControlStateTypeType levelOffset = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateTypeType percentOffset = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateTypeType setPoint = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateType createOadrLoadControlStateType = Oadr20bFactory
				.createOadrLoadControlStateType(loadControlState, levelOffset, percentOffset, setPoint);

		boolean manualOverride = true;
		boolean online = true;
		OadrPayloadResourceStatusType createOadrPayloadResourceStatusType = Oadr20bFactory
				.createOadrPayloadResourceStatusType(createOadrLoadControlStateType, manualOverride, online);

		
		
		reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName,
						createdTimestamp, start, xmlDuration)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, createOadrPayloadResourceStatusType).build())
				.build();
		oadrUpdateReportType = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", mockVen.getVenId())
				.addReport(reportUpdate).build();

		// EI REPORT CONTROLLER - send OadrUpdateReportType
		// OadrPayloadResourceStatusType data
		oadrUpdatedReportType = mockVen.report(oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);

		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), oadrUpdatedReportType.getVenID());

		// VEN CONTROLLER - test previous payload has successfully inserted report data
		// in database
		List<OtherReportDataPayloadResourceStatusDto> reportDataResourceStatusList = oadrMockHttpVenMvc
				.getVenReportRequestedResourceStatusData(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
						REPORT_SPECIFIER_ID, HttpStatus.OK_200);
		assertEquals(1, reportDataResourceStatusList.size());

		reportDataResourceStatusList = oadrMockHttpVenMvc.getVenReportRequestedSpecifierResourceStatusData(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), REPORT_SPECIFIER_ID, rid,
				HttpStatus.OK_200);
		assertEquals(1, reportDataResourceStatusList.size());

		assertTrue(reportDataResourceStatusList.get(0).getStart().equals(start));
		assertEquals(confidence, reportDataResourceStatusList.get(0).getConfidence());
		assertTrue(reportDataResourceStatusList.get(0).getOadrCapacityCurrent().equals(0F));
		assertTrue(reportDataResourceStatusList.get(0).getOadrCapacityNormal().equals(0F));
		assertTrue(reportDataResourceStatusList.get(0).getOadrCapacityMin().equals(0F));
		assertTrue(reportDataResourceStatusList.get(0).getOadrCapacityMax().equals(0F));

		// create VEN oadrUpdateReport keytoken payload
		PayloadKeyTokenType tokens = new PayloadKeyTokenType();
		KeyTokenType token = new KeyTokenType();
		token.setKey(MOUAICCOOL);
		token.setValue(MOUAICCOOL);
		tokens.getTokens().add(token);

		reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName,
						createdTimestamp, start, xmlDuration)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, tokens).build())
				.build();
		oadrUpdateReportType = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", mockVen.getVenId())
				.addReport(reportUpdate).build();

		// EI REPORT CONTROLLER - send OadrUpdateReportType
		// PayloadKeyTokenType data
		oadrUpdatedReportType = mockVen.report(oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);
		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(mockVen.getVenId(), oadrUpdatedReportType.getVenID());

		// VEN CONTROLLER - test previous payload has successfully inserted report data
		// in database
		List<OtherReportDataKeyTokenDto> venReportRequestedKeyTokenData = oadrMockHttpVenMvc
				.getVenReportRequestedKeyTokenData(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
						REPORT_SPECIFIER_ID, HttpStatus.OK_200);
		assertEquals(1, venReportRequestedKeyTokenData.size());

		venReportRequestedKeyTokenData = oadrMockHttpVenMvc.getVenReportRequestedSpecifierKeyTokenData(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), REPORT_SPECIFIER_ID, rid,
				HttpStatus.OK_200);
		assertEquals(1, venReportRequestedKeyTokenData.size());

		assertTrue(venReportRequestedKeyTokenData.get(0).getStart().equals(start));
		assertEquals(confidence, venReportRequestedKeyTokenData.get(0).getConfidence());
		assertEquals(1, venReportRequestedKeyTokenData.get(0).getTokens().size());
		assertEquals(MOUAICCOOL, venReportRequestedKeyTokenData.get(0).getTokens().get(0).getKey());
		assertEquals(MOUAICCOOL, venReportRequestedKeyTokenData.get(0).getTokens().get(0).getValue());

		reportDataPrivateId = venReportRequestedKeyTokenData.get(0).getId();
		otherReportDataKeyTokenService.delete(reportDataPrivateId);

		// VEN CONTROLLER - cancel subscription
		String reportRequestIdToDelete = searchVenReportRequestedSpecifier.get(0).getReportRequestId();
		oadrMockHttpVenMvc.cancelSubscription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(),
				reportRequestIdToDelete, HttpStatus.OK_200);

		// VEN CONTROLLER - test other report request has been deleted
		params = OadrParamBuilder.builder().build();

		venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(0, venReportRequested.size());

		// VEN CONTROLLER - test other report request specifier has been deleted
		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		searchVenReportRequestedSpecifier = oadrMockHttpVenMvc.searchVenReportRequestedSpecifier(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), criteria, HttpStatus.OK_200);
		assertNotNull(searchVenReportRequestedSpecifier);
		assertEquals(0, searchVenReportRequestedSpecifier.size());

		// OADR POLL CONTROLLER - third poll supposed to contains CancelReport cause
		// user has cancel
		// subscription
		OadrCancelReportType thirdPoll = mockVen.poll(HttpStatus.OK_200, OadrCancelReportType.class);
		assertNotNull(thirdPoll);
		assertEquals(1, thirdPoll.getReportRequestID().size());
		assertEquals(reportRequestIdToDelete, thirdPoll.getReportRequestID().get(0));

		// VEN CONTROLLER - requests
		List<OtherReportRequestDtoCreateRequestDto> requests = new ArrayList<>();
		OtherReportRequestDtoCreateRequestDto request = new OtherReportRequestDtoCreateRequestDto();
		request.setReportSpecifierId(reportCapability.getReportSpecifierId());
		ridMap = new HashMap<>();
		ridMap.put(reportCapabilityDescription.getRid(), true);
		request.setRid(new ArrayList<>(ridMap.keySet()));
		requests.add(request);
		oadrMockHttpVenMvc.request(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), requests,
				HttpStatus.OK_200);

		// VEN CONTROLLER - test requests has NOT been stored
		// (subscriptions are stored in database, requests are not)
		params = OadrParamBuilder.builder().build();
		venReportRequested = oadrMockHttpVenMvc.getVenReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				mockVen.getVenId(), params, HttpStatus.OK_200);
		assertNotNull(venReportRequested);
		assertEquals(0, venReportRequested.size());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		searchVenReportRequestedSpecifier = oadrMockHttpVenMvc.searchVenReportRequestedSpecifier(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, mockVen.getVenId(), criteria, HttpStatus.OK_200);
		assertNotNull(searchVenReportRequestedSpecifier);
		assertEquals(0, searchVenReportRequestedSpecifier.size());

		// OADR POLL CONTROLLER - fourth poll supposed to contains CreatedReport cause
		// user create request
		OadrCreateReportType fourthPoll = mockVen.poll(HttpStatus.OK_200, OadrCreateReportType.class);
		assertNotNull(fourthPoll);
		assertEquals(1, fourthPoll.getOadrReportRequest().size());
		assertEquals(reportCapability.getReportSpecifierId(),
				fourthPoll.getOadrReportRequest().get(0).getReportSpecifier().getReportSpecifierID());
		assertEquals(1, fourthPoll.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().size());
		assertEquals(reportCapabilityDescription.getRid(),
				fourthPoll.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(0).getRID());

		// cleanup data PayloadResourceStatus
		otherReportCapabilityDescriptionService.delete(reportCapabilityDescriptionPrivateId);
		otherReportCapabilityService.delete(reportCapabilityPrivateId);

	}

	private OadrCreateReportType createMetadataRequestPayload(String venId) {
		WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
				"PT1H");

		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(REPORT_REQUEST_ID, "METADATA", "PT1H", "PT1H")
				.withWsCalendarIntervalType(calendar)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.X_NOT_APPLICABLE, "").build();

		return Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(REQUEST_ID, venId)
				.addReportRequest(oadrReportRequestType).build();
	}

	@Test
	public void testVENTargetVTNSource() throws Exception {
		for (Entry<String, UserRequestPostProcessor> entry : OadrDataBaseSetup.getTestVen().entrySet()) {
			VenDto ven = oadrMockHttpVenMvc.getVen(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, entry.getKey(),
					HttpStatus.OK_200);
			OadrMockVen mockVen = new OadrMockVen(ven, entry.getValue(), oadrMockEiHttpMvc, oadrMockEiXmpp,
					xmlSignatureService);
			_testVENTargetVTNSource(mockVen);
		}
	}

	private void _testVENTargetVTNSource(OadrMockVen mockVen) throws Oadr20bMarshalException, Exception {

		if (OadrTransportType.SIMPLE_HTTP.value().equals(mockVen.getVen().getTransport())
				&& mockVen.getVen().getHttpPullModel()) {
			// OADR POLL CONTROLLER - invalid mismatch payload venID and username auth
			// session
			OadrResponseType postOadrPollAndExpect = mockVen.poll(
					Oadr20bPollBuilders.newOadr20bPollBuilder("mouaiccool").build(), HttpStatus.OK_200,
					OadrResponseType.class);
			assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
					postOadrPollAndExpect.getEiResponse().getResponseCode());

		}

		// OADR POLL CONTROLLER - first poll supposed to be 'empty'
		mockVen.pollForEmpty();

		// create oadrCreateReport payload requesting for METADATA payload
		OadrCreateReportType oadrCreateReportType = createMetadataRequestPayload(mockVen.getVenId());

		// EI REPORT CONTROLLER - send OadrCreateReportType
		OadrCreatedReportType oadrCreatedReportType = mockVen.report(oadrCreateReportType, HttpStatus.OK_200,
				OadrCreatedReportType.class);
		assertNotNull(oadrCreatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());
		assertTrue(oadrCreatedReportType.getOadrPendingReports().getReportRequestID().isEmpty());

		// OADR POLL CONTROLLER - second poll VEN shall retrieve previously request
		// OadrRegisterReportType
		// this payload shall be empty as no self report cpaability has been
		// configured on VTN
		OadrRegisterReportType secondPoll = mockVen.poll(HttpStatus.OK_200, OadrRegisterReportType.class);
		assertEquals(0, secondPoll.getOadrReport().size());

		OadrRegisteredReportType registeredPayload = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder(secondPoll.getRequestID(), HttpStatus.OK_200, mockVen.getVenId())
				.build();
		// EI REPORT CONTROLLER - send OadrRegisteredReport
		OadrResponseType report = mockVen.report(registeredPayload, HttpStatus.OK_200, OadrResponseType.class);
		assertNotNull(report);
		assertEquals(String.valueOf(HttpStatus.OK_200), report.getEiResponse().getResponseCode());

		// create available (self) report on VTN
		String duration = "P1D";
		ReportNameEnumeratedType metadataTelemetryUsage = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		SelfReportCapability selfReportCapability = new SelfReportCapability();
		selfReportCapability.setReportName(metadataTelemetryUsage);
		selfReportCapability.setReportSpecifierId(REPORT_REQUEST_ID);
		selfReportCapability.setDuration(duration);

		selfReportCapabilityservice.save(selfReportCapability);

		String rid = RID_ID;
		ReadingTypeEnumeratedType readingType = ReadingTypeEnumeratedType.DIRECT_READ;
		ReportEnumeratedType reportType = ReportEnumeratedType.READING;
		OadrReportDescriptionType oadrReportDescriptionType = Oadr20bEiReportBuilders
				.newOadr20bOadrReportDescriptionBuilder(rid, reportType, readingType)
				.withTemperatureBase(TemperatureUnitType.CELSIUS, SiScaleCodeType.NONE).build();
		SelfReportCapabilityDescription selfReportCapabilityDescription = new SelfReportCapabilityDescription();
		selfReportCapabilityDescription.setSelfReportCapability(selfReportCapability);
		selfReportCapabilityDescription.setReadingType(readingType);
		selfReportCapabilityDescription.setReportType(reportType);
		selfReportCapabilityDescription.setRid(rid);
		selfReportCapabilityDescription
				.setPayload(jaxbContext.marshal(Oadr20bFactory.createOadrReportDescription(oadrReportDescriptionType)));

		selfReportCapabilityDescriptionService.save(selfReportCapabilityDescription);

		// VTN CONTROLLER - test previous objects are saved inj bdd
		List<ReportCapabilityDto> vtnReportAvailable = oadrMockHttpVtnMvc
				.getVtnReportAvailable(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, HttpStatus.OK_200);
		assertEquals(1, vtnReportAvailable.size());
		Long selfCapPrivateId = vtnReportAvailable.get(0).getId();

		List<ReportCapabilityDescriptionDto> vtnReportAvailableDescription = oadrMockHttpVtnMvc
				.getVtnReportAvailableDescription(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, REPORT_REQUEST_ID,
						HttpStatus.OK_200);
		assertEquals(1, vtnReportAvailableDescription.size());
		Long selfCapDescriptionPrivateId = vtnReportAvailableDescription.get(0).getId();

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		oadrCreateReportType = createMetadataRequestPayload("mouaiccool");
		oadrCreatedReportType = mockVen.report(oadrCreateReportType, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedReportType.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrCreateReportType
		oadrCreateReportType = createMetadataRequestPayload(mockVen.getVenId());
		oadrCreatedReportType = mockVen.report(oadrCreateReportType, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertNotNull(oadrCreatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());
		assertTrue(oadrCreatedReportType.getOadrPendingReports().getReportRequestID().isEmpty());

		// OADR POLL CONTROLLER - third poll VEN shall retrieve previously request
		// OadrRegisterReportType
		// this payload shall be empty as no self report cpaability has been
		// configured on VTN
		OadrRegisterReportType thirdPoll = mockVen.poll(HttpStatus.OK_200, OadrRegisterReportType.class);
		assertEquals(1, thirdPoll.getOadrReport().size());

		OadrReportType oadrReportType = thirdPoll.getOadrReport().get(0);
		assertEquals(REPORT_REQUEST_ID, oadrReportType.getReportSpecifierID());
		assertEquals(metadataTelemetryUsage.value(), oadrReportType.getReportName());
		assertEquals("0", oadrReportType.getReportRequestID());
		assertEquals(duration, oadrReportType.getDuration().getDuration());
		assertEquals(1, oadrReportType.getOadrReportDescription().size());
		OadrReportDescriptionType description = oadrReportType.getOadrReportDescription().get(0);

		assertEquals(rid, description.getRID());
		assertEquals(readingType.value(), description.getReadingType());
		assertEquals(reportType.value(), description.getReportType());
		assertEquals(TemperatureType.class, description.getItemBase().getDeclaredType());
		TemperatureType temperature = (TemperatureType) description.getItemBase().getValue();
		assertEquals(TemperatureUnitType.CELSIUS, temperature.getItemUnits());
		assertEquals(SiScaleCodeType.NONE, temperature.getSiScaleCode());

		// create oadrCreateReport payload
		String reportRequestId = REPORT_REQUEST_ID;

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		OadrCreateReportType build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder("", "mouaiccool")
				.addReportRequest(Oadr20bEiReportBuilders
						.newOadr20bReportRequestTypeBuilder(reportRequestId, REPORT_REQUEST_ID, "P0D", "P0D")
						.addSpecifierPayload(Oadr20bFactory.createTemperature(temperature), readingType, rid).build())
				.build();

		oadrCreatedReportType = mockVen.report(build, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedReportType.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrCreateReportType
		build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder("", mockVen.getVenId())
				.addReportRequest(Oadr20bEiReportBuilders
						.newOadr20bReportRequestTypeBuilder(reportRequestId, REPORT_REQUEST_ID, "P0D", "P0D")
						.addSpecifierPayload(Oadr20bFactory.createTemperature(temperature), readingType, rid).build())
				.build();
		oadrCreatedReportType = mockVen.report(build, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());

		// request Vtn controller and check previously created report has been
		// saved in selfReportRequest database
		List<OtherReportRequestDto> vtnReportRequested = oadrMockHttpVtnMvc
				.getVtnReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION, HttpStatus.OK_200);
		assertEquals(1, vtnReportRequested.size());

		// EI REPORT CONTROLLER - invalid mismatch payload venID and username auth
		// session
		OadrCancelReportType oadrCancelReportType = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder("", "mouaiccool", true).addReportRequestId(reportRequestId).build();

		OadrCanceledReportType oadrCanceledReportType = mockVen.report(oadrCancelReportType, HttpStatus.OK_200,
				OadrCanceledReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCanceledReportType.getEiResponse().getResponseCode());

		// EI REPORT CONTROLLER - send OadrCancelReportType
		oadrCancelReportType = Oadr20bEiReportBuilders.newOadr20bCancelReportBuilder("", mockVen.getVenId(), true)
				.addReportRequestId(reportRequestId).build();
		oadrCanceledReportType = mockVen.report(oadrCancelReportType, HttpStatus.OK_200, OadrCanceledReportType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCanceledReportType.getEiResponse().getResponseCode());

		// VTN CONTROLLER - request Vtn controller and check previously created report
		// has been removed from selfReportRequest database
		vtnReportRequested = oadrMockHttpVtnMvc.getVtnReportRequested(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				HttpStatus.OK_200);
		assertEquals(0, vtnReportRequested.size());

		selfReportCapabilityDescriptionService.delete(selfCapDescriptionPrivateId);
		selfReportCapabilityservice.delete(selfCapPrivateId);

	}
}
