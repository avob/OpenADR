package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
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
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.ReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataFloatService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataKeyTokenService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataPayloadResourceStatusService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.utils.Oadr20bTestUtils;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiReportControllerTest {

	private static final String REPORT_ENDPOINT = "/Report/";
	private static final String VEN_ENDPOINT = "/Ven/";
	private static final String VTN_ENDPOINT = "/Vtn/";

	@Resource
	private VenService venService;

	@Resource
	private VenGroupService venGroupService;

	@Resource
	private VenMarketContextService venMarketContextService;

	@Resource
	private VenResourceService venReourceService;

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
	private OadrMockMvc mockMvc;

	private Oadr20bJAXBContext jaxbContext;

	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void testVENSourceVTNTarget() throws Exception {

		// first poll supposed to be 'empty'
		String str = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build()),
				HttpStatus.OK_200, String.class);
		OadrPayload payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		assertNotNull(payload.getOadrSignedObject().getOadrResponse());
		assertEquals(String.valueOf(HttpStatus.OK_200),
				payload.getOadrSignedObject().getOadrResponse().getEiResponse().getResponseCode());

		// create VEN METADATA payload with one report capability containing one
		// description
		String requestId = "requestId";
		String reportRequestId = "0";
		String reportSpecifierId = "reportSpecifierId";
		String rid = "rid";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		long createdTimestamp = System.currentTimeMillis();
		ReportEnumeratedType reportType = ReportEnumeratedType.READING;
		ReadingTypeEnumeratedType readingType = ReadingTypeEnumeratedType.DIRECT_READ;
		String minPeriod = "PT15M";
		String maxPeriod = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportRequestId, reportName,
						createdTimestamp)
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
						.build())
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
						.withCustomUnitBase("mouaiccool", "mouaiccool", SiScaleCodeType.NONE)
						.withOadrSamplingRate(minPeriod, maxPeriod, false).build())
				.addReportDescription(Oadr20bEiReportBuilders
						.newOadr20bOadrReportDescriptionBuilder(rid + "15", reportType, readingType)
						.withCurrencyBase(CurrencyItemDescriptionType.CURRENCY_PER_K_WH,
								ISO3AlphaCurrencyCodeContentType.EUR, SiScaleCodeType.NONE)
						.build())
				.build();

		OadrRegisterReportType oadrRegisterReportType = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder(requestId, OadrDataBaseSetup.VEN, null).addOadrReport(report).build();

		// invalid mismatch payload venID and username auth session
		OadrRegisteredReportType oadrRegisteredReportType = mockMvc.postEiReportAndExpect(
				OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, xmlSignatureService.sign(oadrRegisterReportType),
				HttpStatus.OK_200, OadrRegisteredReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrRegisteredReportType.getEiResponse().getResponseCode());

		// sign and push this payload into EiReport controller
		str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrRegisterReportType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		oadrRegisteredReportType = payload.getOadrSignedObject().getOadrRegisteredReport();

		assertNotNull(oadrRegisteredReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRegisteredReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrRegisteredReportType.getVenID());
		assertNotNull(oadrRegisteredReportType.getOadrReportRequest());

		List<ReportCapabilityDto> reportcapabilityList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available",
				HttpStatus.OK_200, ReportCapabilityDto.class);
		assertEquals(1, reportcapabilityList.size());
		assertEquals(reportSpecifierId, reportcapabilityList.get(0).getReportSpecifierId());
		assertEquals(reportName, reportcapabilityList.get(0).getReportName());

		Long reportCapabilityPrivateId = reportcapabilityList.get(0).getId();

		// retreive previous capability description from ven controller
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("reportSpecifierId", reportSpecifierId);
		List<ReportCapabilityDescriptionDto> reportcapabilityDescriptionList = mockMvc
				.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
						VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available/description", HttpStatus.OK_200,
						ReportCapabilityDescriptionDto.class, params);

		assertEquals(15, reportcapabilityDescriptionList.size());
		assertEquals(rid, reportcapabilityDescriptionList.get(0).getRid());
		assertEquals(readingType, reportcapabilityDescriptionList.get(0).getReadingType());
		assertEquals(reportType, reportcapabilityDescriptionList.get(0).getReportType());

		Long reportCapabilityDescriptionPrivateId = reportcapabilityDescriptionList.get(0).getId();

		// update register report
		report = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportRequestId, reportName,
						createdTimestamp)
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

		oadrRegisterReportType = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder(requestId, OadrDataBaseSetup.VEN, null).addOadrReport(report).build();

		str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrRegisterReportType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		oadrRegisteredReportType = payload.getOadrSignedObject().getOadrRegisteredReport();

		assertNotNull(oadrRegisteredReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRegisteredReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrRegisteredReportType.getVenID());
		assertNotNull(oadrRegisteredReportType.getOadrReportRequest());

		params = new LinkedMultiValueMap<String, String>();
		params.add("reportSpecifierId", reportSpecifierId);
		reportcapabilityDescriptionList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available/description", HttpStatus.OK_200,
				ReportCapabilityDescriptionDto.class, params);

		assertEquals(1, reportcapabilityDescriptionList.size());
		assertEquals(rid, reportcapabilityDescriptionList.get(0).getRid());
		assertEquals(readingType, reportcapabilityDescriptionList.get(0).getReadingType());
		assertEquals(reportType, reportcapabilityDescriptionList.get(0).getReportType());

		reportCapabilityDescriptionPrivateId = reportcapabilityDescriptionList.get(0).getId();
		OtherReportCapability reportCapability = otherReportCapabilityService.findOne(reportCapabilityPrivateId);
		OtherReportCapabilityDescription reportCapabilityDescription = otherReportCapabilityDescriptionService
				.findOne(reportCapabilityDescriptionPrivateId);

		// search other report capability
		List<OtherReportCapabilityDto> restJsonControllerAndExpectList3 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available/search", HttpStatus.OK_200,
				OtherReportCapabilityDto.class, params);

		assertEquals(1, restJsonControllerAndExpectList3.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList3.get(0).getReportSpecifierId());

		// ReportController viewOtherReportCapability
		mockMvc.perform(MockMvcRequestBuilders.get(REPORT_ENDPOINT + "/available/search")
				.header("Content-Type", "application/json").with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.BAD_REQUEST_400));

		List<OtherReportCapabilityDto> restJsonControllerAndExpectList5 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				REPORT_ENDPOINT + "/available/search?venID=" + OadrDataBaseSetup.VEN, HttpStatus.OK_200,
				OtherReportCapabilityDto.class);
		assertEquals(1, restJsonControllerAndExpectList5.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList5.get(0).getReportSpecifierId());

		restJsonControllerAndExpectList5 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, REPORT_ENDPOINT + "/available/search?venID="
						+ OadrDataBaseSetup.VEN + "&reportSpecifierId=" + reportCapability.getReportSpecifierId(),
				HttpStatus.OK_200, OtherReportCapabilityDto.class);
		assertEquals(1, restJsonControllerAndExpectList5.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList5.get(0).getReportSpecifierId());

		restJsonControllerAndExpectList5 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, REPORT_ENDPOINT + "/available/search?venID=mouaiccool",
				HttpStatus.OK_200, OtherReportCapabilityDto.class);
		assertEquals(0, restJsonControllerAndExpectList5.size());

		restJsonControllerAndExpectList5 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				REPORT_ENDPOINT + "/available/search?reportSpecifierId=mouaiccool", HttpStatus.OK_200,
				OtherReportCapabilityDto.class, params);
		assertEquals(0, restJsonControllerAndExpectList5.size());

		// ReportController searchOtherReportCapabilityDescription
		List<OtherReportCapabilityDescriptionDto> restJsonControllerAndExpectList6 = mockMvc
				.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
						REPORT_ENDPOINT + "/available/description/search?reportSpecifierId="
								+ reportCapability.getReportSpecifierId(),
						HttpStatus.OK_200, OtherReportCapabilityDescriptionDto.class);
		assertEquals(1, restJsonControllerAndExpectList6.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList6.get(0).getReportSpecifierId());
		assertEquals(rid, restJsonControllerAndExpectList6.get(0).getRid());

		restJsonControllerAndExpectList6 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				REPORT_ENDPOINT + "/available/description/search?reportName=" + reportCapability.getReportName(),
				HttpStatus.OK_200, OtherReportCapabilityDescriptionDto.class);
		assertEquals(1, restJsonControllerAndExpectList6.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList6.get(0).getReportSpecifierId());
		assertEquals(rid, restJsonControllerAndExpectList6.get(0).getRid());

		restJsonControllerAndExpectList6 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				REPORT_ENDPOINT + "/available/description/search?reportType="
						+ reportCapabilityDescription.getReportType(),
				HttpStatus.OK_200, OtherReportCapabilityDescriptionDto.class);
		assertEquals(1, restJsonControllerAndExpectList6.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList6.get(0).getReportSpecifierId());
		assertEquals(rid, restJsonControllerAndExpectList6.get(0).getRid());

		restJsonControllerAndExpectList6 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				REPORT_ENDPOINT + "/available/description/search?readingType="
						+ reportCapabilityDescription.getReadingType(),
				HttpStatus.OK_200, OtherReportCapabilityDescriptionDto.class);
		assertEquals(1, restJsonControllerAndExpectList6.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList6.get(0).getReportSpecifierId());
		assertEquals(rid, restJsonControllerAndExpectList6.get(0).getRid());

		// subscribe
		List<OtherReportRequestDtoCreateSubscriptionDto> subscriptions = new ArrayList<>();
		OtherReportRequestDtoCreateSubscriptionDto subscription = new OtherReportRequestDtoCreateSubscriptionDto();
		subscription.setGranularity(minPeriod);
		subscription.setReportBackDuration(maxPeriod);
		subscription.setReportSpecifierId(reportCapability.getReportSpecifierId());
		Map<String, Boolean> ridMap = new HashMap<>();
		ridMap.put(reportCapabilityDescription.getRid(), true);
		subscription.setRid(ridMap);
		subscriptions.add(subscription);
		mockMvc.perform(MockMvcRequestBuilders
				.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available/description/subscribe")
				.header("Content-Type", "application/json").content(mapper.writeValueAsString(subscriptions))
				.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION)).andExpect(status().is(HttpStatus.OK_200));

		// test subscription has been stored
		List<OtherReportRequestDto> restJsonControllerAndExpectList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested",
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertNotNull(restJsonControllerAndExpectList);
		assertEquals(1, restJsonControllerAndExpectList.size());
		reportRequestId = restJsonControllerAndExpectList.get(0).getReportRequestId();

		restJsonControllerAndExpectList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested?reportSpecifierId=" + reportSpecifierId,
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertNotNull(restJsonControllerAndExpectList);
		assertEquals(1, restJsonControllerAndExpectList.size());
		reportRequestId = restJsonControllerAndExpectList.get(0).getReportRequestId();

		restJsonControllerAndExpectList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested?reportRequestId=" + reportRequestId,
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertNotNull(restJsonControllerAndExpectList);
		assertEquals(1, restJsonControllerAndExpectList.size());
		reportRequestId = restJsonControllerAndExpectList.get(0).getReportRequestId();

		OtherReportRequestSpecifierSearchCriteria criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/specifier")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(criteria))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		List<OtherReportRequestSpecifierDto> convertMvcResultToDtoList = Oadr20bTestUtils
				.convertMvcResultToDtoList(andReturn, OtherReportRequestSpecifierDto.class);
		assertNotNull(convertMvcResultToDtoList);
		assertEquals(1, convertMvcResultToDtoList.size());
		assertEquals(reportCapabilityDescription.getRid(), convertMvcResultToDtoList.get(0).getRid());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setRid(Lists.newArrayList(reportCapabilityDescription.getRid()));
		andReturn = mockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/specifier")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(criteria))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		convertMvcResultToDtoList = Oadr20bTestUtils.convertMvcResultToDtoList(andReturn,
				OtherReportRequestSpecifierDto.class);
		assertNotNull(convertMvcResultToDtoList);
		assertEquals(1, convertMvcResultToDtoList.size());
		assertEquals(reportCapabilityDescription.getRid(), convertMvcResultToDtoList.get(0).getRid());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportRequestId(Lists.newArrayList(reportRequestId));
		andReturn = mockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/specifier")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(criteria))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		convertMvcResultToDtoList = Oadr20bTestUtils.convertMvcResultToDtoList(andReturn,
				OtherReportRequestSpecifierDto.class);
		assertNotNull(convertMvcResultToDtoList);
		assertEquals(1, convertMvcResultToDtoList.size());
		assertEquals(reportCapabilityDescription.getRid(), convertMvcResultToDtoList.get(0).getRid());

		// search other report request
		List<OtherReportRequestDto> restJsonControllerAndExpectList4 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/search", HttpStatus.OK_200,
				OtherReportRequestDto.class, params);

		assertEquals(1, restJsonControllerAndExpectList4.size());
		assertEquals(reportSpecifierId, restJsonControllerAndExpectList4.get(0).getReportSpecifierId());

		// second poll supposed to contains CreateReport cause user has subscribe
		str = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build()),
				HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		assertNotNull(payload.getOadrSignedObject().getOadrCreateReport());
		assertEquals(1, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().size());
		assertEquals(reportCapability.getReportSpecifierId(), payload.getOadrSignedObject().getOadrCreateReport()
				.getOadrReportRequest().get(0).getReportSpecifier().getReportSpecifierID());
		assertEquals(minPeriod, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().get(0)
				.getReportSpecifier().getGranularity().getDuration());
		assertEquals(maxPeriod, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().get(0)
				.getReportSpecifier().getReportBackDuration().getDuration());
		assertEquals(1, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().get(0)
				.getReportSpecifier().getSpecifierPayload().size());
		assertEquals(reportCapabilityDescription.getRid(), payload.getOadrSignedObject().getOadrCreateReport()
				.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(0).getRID());

		// create VEN oadrCreatedReport
		OadrCreatedReportType oadrCreatedReportType = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(reportRequestId, HttpStatus.OK_200, OadrDataBaseSetup.VEN)
				.addPendingReportRequestId(reportRequestId).build();

		// invalid mismatch payload venID and username auth session
		OadrResponseType response = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrCreatedReportType), HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				response.getEiResponse().getResponseCode());

		// push this payload into EiReport controller
		str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrCreatedReportType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		response = payload.getOadrSignedObject().getOadrResponse();
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, response.getVenID());

		// test previous payload has successfully acked requestreport
		List<OtherReportRequestDto> reportRequestList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested",
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertEquals(1, reportRequestList.size());
		assertTrue(reportRequestList.get(0).isAcked());
		assertEquals(minPeriod, reportRequestList.get(0).getGranularity());
		assertEquals(maxPeriod, reportRequestList.get(0).getReportBackDuration());
		assertEquals(reportRequestId, reportRequestList.get(0).getReportRequestId());
		assertNull(reportRequestList.get(0).getRequestorUsername());

		// create VEN oadrUpdateReport float payload
		String intervalId = "intervalId";
		long start = System.currentTimeMillis();
		String xmlDuration = "P1D";
		Long confidence = 80L;
		Float accuracy = 1F;
		Float value = 3F;
		OadrReportType reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportRequestId, reportName,
						createdTimestamp)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, value).build())
				.build();
		OadrUpdateReportType oadrUpdateReportType = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportBuilder("", OadrDataBaseSetup.VEN).addReport(reportUpdate).build();

		// invalid mismatch payload venID and username auth session
		OadrUpdatedReportType oadrUpdatedReportType = mockMvc.postEiReportAndExpect(
				OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, xmlSignatureService.sign(oadrUpdateReportType),
				HttpStatus.OK_200, OadrUpdatedReportType.class);

		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrUpdatedReportType.getEiResponse().getResponseCode());

		// push this payload into EiReport controller
		oadrUpdatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);

		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrUpdatedReportType.getVenID());

		// test previous payload has successfully inserted report data in
		// database
		List<OtherReportDataFloatDto> reportDataList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/float/" + reportSpecifierId, HttpStatus.OK_200,
				OtherReportDataFloatDto.class);
		assertEquals(1, reportDataList.size());

		mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/float/fakeReportSpecifierId",
				HttpStatus.NOT_ACCEPTABLE_406, null);

		reportDataList = mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/float/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, OtherReportDataFloatDto.class);
		assertEquals(1, reportDataList.size());

		mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/float/" + reportSpecifierId + "/rid/fakeRid",
				HttpStatus.NOT_ACCEPTABLE_406, null);

		// xml date do not support millisecond therefore I floor timestamp to
		// suppress millisecond
		NumberFormat formatter = new DecimalFormat("#0.00");
		assertEquals(formatter.format(new Double(Math.floor(new Long(start) / 1000) * 1000)),
				formatter.format(new Double(reportDataList.get(0).getStart())));
		assertEquals(confidence, reportDataList.get(0).getConfidence());
		assertEquals(value, reportDataList.get(0).getValue());

		Long reportDataPrivateId = reportDataList.get(0).getId();

		// cleanup data
		otherReportDataService.delete(reportDataPrivateId);

		// create VEN oadrUpdateReport OadrPayloadResourceStatusType payload
		OadrLoadControlStateTypeType loadControlState = Oadr20bFactory.createOadrLoadControlStateTypeType(0f, 0f, 0f,
				0f);
		OadrLoadControlStateType createOadrLoadControlStateType = Oadr20bFactory
				.createOadrLoadControlStateType(loadControlState, null, null, null);

		boolean manualOverride = true;
		boolean online = true;
		OadrPayloadResourceStatusType createOadrPayloadResourceStatusType = Oadr20bFactory
				.createOadrPayloadResourceStatusType(createOadrLoadControlStateType, manualOverride, online);

		reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportRequestId, reportName,
						createdTimestamp)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, createOadrPayloadResourceStatusType).build())
				.build();
		oadrUpdateReportType = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", OadrDataBaseSetup.VEN)
				.addReport(reportUpdate).build();

		// push this payload into EiReport controller
		oadrUpdatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);

		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrUpdatedReportType.getVenID());

		// test previous payload has successfully inserted report data in
		// database
		List<OtherReportDataPayloadResourceStatusDto> reportDataResourceStatusList = mockMvc
				.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
						VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/resourcestatus/" + reportSpecifierId,
						HttpStatus.OK_200, OtherReportDataPayloadResourceStatusDto.class);
		assertEquals(1, reportDataList.size());

		reportDataResourceStatusList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN
						+ "/report/data/resourcestatus/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, OtherReportDataPayloadResourceStatusDto.class);
		assertEquals(1, reportDataList.size());

		assertEquals(new Double(Math.floor(new Long(start) / 1000) * 1000),
				new Double(reportDataResourceStatusList.get(0).getStart()));
		assertEquals(confidence, reportDataResourceStatusList.get(0).getConfidence());

		assertEquals(new Float(0), reportDataResourceStatusList.get(0).getOadrCapacityCurrent());
		assertEquals(new Float(0), reportDataResourceStatusList.get(0).getOadrCapacityNormal());
		assertEquals(new Float(0), reportDataResourceStatusList.get(0).getOadrCapacityMin());
		assertEquals(new Float(0), reportDataResourceStatusList.get(0).getOadrCapacityMax());

		reportDataPrivateId = reportDataResourceStatusList.get(0).getId();

		// create VEN oadrUpdateReport keytoken payload
		PayloadKeyTokenType tokens = new PayloadKeyTokenType();
		KeyTokenType token = new KeyTokenType();
		token.setKey("mouaiccool");
		token.setValue("mouaiccool");
		tokens.getTokens().add(token);

		reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportOadrReportBuilder(reportSpecifierId, reportRequestId, reportName,
						createdTimestamp)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, tokens).build())
				.build();
		oadrUpdateReportType = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder("", OadrDataBaseSetup.VEN)
				.addReport(reportUpdate).build();

		// push this payload into EiReport controller
		oadrUpdatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				oadrUpdateReportType, HttpStatus.OK_200, OadrUpdatedReportType.class);

		assertNotNull(oadrUpdatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrUpdatedReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrUpdatedReportType.getVenID());

		// test previous payload has successfully inserted report data in
		// database
		List<OtherReportDataKeyTokenDto> restJsonControllerAndExpectList2 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/keytoken/" + reportSpecifierId, HttpStatus.OK_200,
				OtherReportDataKeyTokenDto.class);
		assertEquals(1, restJsonControllerAndExpectList2.size());

		restJsonControllerAndExpectList2 = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/keytoken/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, OtherReportDataKeyTokenDto.class);
		assertEquals(1, restJsonControllerAndExpectList2.size());

		assertEquals(new Double(Math.floor(new Long(start) / 1000) * 1000),
				new Double(restJsonControllerAndExpectList2.get(0).getStart()));
		assertEquals(confidence, restJsonControllerAndExpectList2.get(0).getConfidence());
		assertEquals(1, restJsonControllerAndExpectList2.get(0).getTokens().size());
		assertEquals("mouaiccool", restJsonControllerAndExpectList2.get(0).getTokens().get(0).getKey());
		assertEquals("mouaiccool", restJsonControllerAndExpectList2.get(0).getTokens().get(0).getValue());

		reportDataPrivateId = restJsonControllerAndExpectList2.get(0).getId();

		otherReportDataKeyTokenService.delete(reportDataPrivateId);

		// cancel subscription
		String reportRequestIdToDelete = convertMvcResultToDtoList.get(0).getReportRequestId();
		params = new LinkedMultiValueMap<String, String>();
		params.add("reportRequestId", reportRequestIdToDelete);
		mockMvc.perform(MockMvcRequestBuilders
				.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/cancelSubscription")
				.header("Content-Type", "application/json").params(params)
				.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION)).andExpect(status().is(HttpStatus.OK_200));

		// test subscription has been deleted
		restJsonControllerAndExpectList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested",
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertNotNull(restJsonControllerAndExpectList);
		assertEquals(0, restJsonControllerAndExpectList.size());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		andReturn = mockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/specifier")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(criteria))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		convertMvcResultToDtoList = Oadr20bTestUtils.convertMvcResultToDtoList(andReturn,
				OtherReportRequestSpecifierDto.class);
		assertNotNull(convertMvcResultToDtoList);
		assertEquals(0, convertMvcResultToDtoList.size());

		// third poll supposed to contains CancelReport cause user has cancel
		// subscription
		str = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build()),
				HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		assertNotNull(payload.getOadrSignedObject().getOadrCancelReport());
		assertEquals(1, payload.getOadrSignedObject().getOadrCancelReport().getReportRequestID().size());
		assertEquals(reportRequestIdToDelete,
				payload.getOadrSignedObject().getOadrCancelReport().getReportRequestID().get(0));

		// requests
		List<OtherReportRequestDtoCreateRequestDto> requests = new ArrayList<>();
		OtherReportRequestDtoCreateRequestDto request = new OtherReportRequestDtoCreateRequestDto();
		request.setReportSpecifierId(reportCapability.getReportSpecifierId());
		ridMap = new HashMap<>();
		ridMap.put(reportCapabilityDescription.getRid(), true);
		request.setRid(new ArrayList<>(ridMap.keySet()));
		requests.add(request);
		mockMvc.perform(MockMvcRequestBuilders
				.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/available/description/request")
				.header("Content-Type", "application/json").content(mapper.writeValueAsString(requests))
				.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION)).andExpect(status().is(HttpStatus.OK_200));

		// test requests has NOT been stored
		// (subscriptions are stored in database, requests are not)
		restJsonControllerAndExpectList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested",
				HttpStatus.OK_200, OtherReportRequestDto.class);
		assertNotNull(restJsonControllerAndExpectList);
		assertEquals(0, restJsonControllerAndExpectList.size());

		criteria = new OtherReportRequestSpecifierSearchCriteria();
		criteria.setReportSpecifierId(Lists.newArrayList(reportCapability.getReportSpecifierId()));
		andReturn = mockMvc
				.perform(MockMvcRequestBuilders
						.post(VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested/specifier")
						.header("Content-Type", "application/json").content(mapper.writeValueAsString(criteria))
						.with(OadrDataBaseSetup.ADMIN_SECURITY_SESSION))
				.andExpect(status().is(HttpStatus.OK_200)).andReturn();
		convertMvcResultToDtoList = Oadr20bTestUtils.convertMvcResultToDtoList(andReturn,
				OtherReportRequestSpecifierDto.class);
		assertNotNull(convertMvcResultToDtoList);
		assertEquals(0, convertMvcResultToDtoList.size());

		// fourth poll supposed to contains CreatedReport cause user create request
		str = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build()),
				HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		assertNotNull(payload.getOadrSignedObject().getOadrCreateReport());
		assertEquals(1, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().size());
		assertEquals(reportCapability.getReportSpecifierId(), payload.getOadrSignedObject().getOadrCreateReport()
				.getOadrReportRequest().get(0).getReportSpecifier().getReportSpecifierID());
		assertEquals(1, payload.getOadrSignedObject().getOadrCreateReport().getOadrReportRequest().get(0)
				.getReportSpecifier().getSpecifierPayload().size());
		assertEquals(reportCapabilityDescription.getRid(), payload.getOadrSignedObject().getOadrCreateReport()
				.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(0).getRID());

		// cleanup data PayloadResourceStatus

//		otherReportRequestSpecifierDao.deleteByRequestReportRequestId(reportRequestId);

//		otherReportRequestService.delete(reportRequestPrivateId);
		otherReportCapabilityDescriptionService.delete(reportCapabilityDescriptionPrivateId);
		otherReportCapabilityService.delete(reportCapabilityPrivateId);

	}

	private OadrCreateReportType createMetadataRequestPayload() {
		String requestId = "requestId";
		String reportRequestId = "reportRequestId";
		String reportSpecifierId = "METADATA";
		WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
				"PT1H");

		OadrReportRequestType oadrReportRequestType = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, "PT1H", "PT1H")
				.withWsCalendarIntervalType(calendar)
				.addSpecifierPayload(null, ReadingTypeEnumeratedType.X_NOT_APPLICABLE, "").build();

		return Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(requestId, OadrDataBaseSetup.VEN)
				.addReportRequest(oadrReportRequestType).build();
	}

	@Test
	public void testVENTargetVTNSource() throws Oadr20bMarshalException, Exception {

		// VEN poll VTN usr oadrPoll service
		// as there is nothing to distribute to VEN, VTN shall respond with an
		// OadrResponseType
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
//		OadrPayload payload = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
//				xmlSignatureService.sign(poll), HttpStatus.OK_200, OadrPayload.class);
		String str = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(poll), HttpStatus.OK_200, String.class);
		OadrPayload payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		OadrResponseType oadrResponse = payload.getOadrSignedObject().getOadrResponse();
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponse.getEiResponse().getResponseCode());

		// create oadrCreateReport payload requesting for METADATA payload
		OadrCreateReportType oadrCreateReportType = createMetadataRequestPayload();

		// push this payload into EiReport controller
		OadrCreatedReportType oadrCreatedReportType = mockMvc.postEiReportAndExpect(
				OadrDataBaseSetup.VEN_SECURITY_SESSION, oadrCreateReportType, HttpStatus.OK_200,
				OadrCreatedReportType.class);

		assertNotNull(oadrCreatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());
		assertTrue(oadrCreatedReportType.getOadrPendingReports().getReportRequestID().isEmpty());

		// invalid mismatch payload venID and username auth session
		OadrResponseType postOadrPollAndExpect = mockMvc.postOadrPollAndExpect(
				OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				postOadrPollAndExpect.getEiResponse().getResponseCode());

		// VEN poll VTN usr oadrPoll service
		// VEN shall retrieve previously request OadrRegisterReportType
		// this payload shall be empty as no self report cpaability has been
		// configured on VTN
		OadrRegisterReportType oadrRegisterReportType = mockMvc.postOadrPollAndExpect(
				OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200, OadrRegisterReportType.class);

		// TODO bzanni: when VTN sent oadrRegister payload to VEN, does it has
		// to fill venID field ?
		// if he does, does it send targeted venId ? Does it send it's own vtnId
		// ?
		// assertEquals(VEN, oadrRegisterReportType.getVenID());
		assertEquals(0, oadrRegisterReportType.getOadrReport().size());

		// create available (self) report on VTN
		String selfPayloadReportRequestId = "0";
		String selfReportSpecifierId = "selfReportSpecifierId";
		String duration = "P1D";
		ReportNameEnumeratedType metadataTelemetryUsage = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		SelfReportCapability selfReportCapability = new SelfReportCapability();
		selfReportCapability.setReportRequestId(selfPayloadReportRequestId);
		selfReportCapability.setReportName(metadataTelemetryUsage);
		selfReportCapability.setReportSpecifierId(selfReportSpecifierId);
		selfReportCapability.setDuration(duration);

		selfReportCapabilityservice.save(selfReportCapability);

		String rid = "rid";
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

		// test previous objects are saved inj bdd
		List<ReportCapabilityDto> selfCap = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VTN_ENDPOINT + "/report/available", HttpStatus.OK_200,
				ReportCapabilityDto.class);
		assertEquals(1, selfCap.size());
		Long selfCapPrivateId = selfCap.get(0).getId();

		List<ReportCapabilityDescriptionDto> selfCapDescription = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VTN_ENDPOINT + "/report/available/" + selfReportSpecifierId,
				HttpStatus.OK_200, ReportCapabilityDescriptionDto.class);
		assertEquals(1, selfCapDescription.size());
		Long selfCapDescriptionPrivateId = selfCapDescription.get(0).getId();

		// create oadrCreateReport payload requesting for METADATA payload
		oadrCreateReportType = createMetadataRequestPayload();

		// invalid mismatch payload venID and username auth session
		oadrCreatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION,
				oadrCreateReportType, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedReportType.getEiResponse().getResponseCode());

		// push this payload into EiReport controller
		oadrCreatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				oadrCreateReportType, HttpStatus.OK_200, OadrCreatedReportType.class);
		assertNotNull(oadrCreatedReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());
		assertTrue(oadrCreatedReportType.getOadrPendingReports().getReportRequestID().isEmpty());

		// VEN poll VTN usr oadrPoll service
		// VEN shall retrieve previously request OadrRegisterReportType
		// this payload shall be empty as no self report cpaability has been
		// configured on VTN
		oadrRegisterReportType = mockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrRegisterReportType.class);
		assertEquals(1, oadrRegisterReportType.getOadrReport().size());

		OadrReportType oadrReportType = oadrRegisterReportType.getOadrReport().get(0);
		assertEquals(selfReportSpecifierId, oadrReportType.getReportSpecifierID());
		assertEquals(metadataTelemetryUsage.value(), oadrReportType.getReportName());
		assertEquals(selfPayloadReportRequestId, oadrReportType.getReportRequestID());
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
		String reportRequestId = "reportRequestId";
		OadrCreateReportType build = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder("", OadrDataBaseSetup.VEN)
				.addReportRequest(Oadr20bEiReportBuilders
						.newOadr20bReportRequestTypeBuilder(reportRequestId, selfReportSpecifierId, "P0D", "P0D")
						.addSpecifierPayload(Oadr20bFactory.createTemperature(temperature), readingType, rid).build())
				.build();

		// invalid mismatch payload venID and username auth session
		oadrCreatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, build,
				HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCreatedReportType.getEiResponse().getResponseCode());

		oadrCreatedReportType = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, build,
				HttpStatus.OK_200, OadrCreatedReportType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCreatedReportType.getEiResponse().getResponseCode());

		// request Vtn controller and check previously created report has been
		// saved in selfReportRequest database
		List<ReportRequestDto> fromRestJsonController = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VTN_ENDPOINT + "/report/requested", HttpStatus.OK_200,
				ReportRequestDto.class);
		assertEquals(1, fromRestJsonController.size());

		// create oadrCancelReport payload
		OadrCancelReportType oadrCancelReportType = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder("", OadrDataBaseSetup.VEN, true).addReportRequestId(reportRequestId)
				.build();

		// invalid mismatch payload venID and username auth session
		OadrCanceledReportType oadrCanceledReportType = mockMvc.postEiReportAndExpect(
				OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, oadrCancelReportType, HttpStatus.OK_200,
				OadrCanceledReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrCanceledReportType.getEiResponse().getResponseCode());

		// push cancel to ei report
//		payload = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
//				xmlSignatureService.sign(oadrCancelReportType), HttpStatus.OK_200, OadrPayload.class);
		str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrCancelReportType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);

		xmlSignatureService.validate(str, payload);
		oadrCanceledReportType = payload.getOadrSignedObject().getOadrCanceledReport();
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrCanceledReportType.getEiResponse().getResponseCode());

		// request Vtn controller and check previously created report has been
		// removed from selfReportRequest database
		fromRestJsonController = mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VTN_ENDPOINT + "/report/requested", HttpStatus.OK_200, ReportRequestDto.class);
		assertEquals(0, fromRestJsonController.size());

		selfReportCapabilityDescriptionService.delete(selfCapDescriptionPrivateId);
		selfReportCapabilityservice.delete(selfCapPrivateId);

	}
}
