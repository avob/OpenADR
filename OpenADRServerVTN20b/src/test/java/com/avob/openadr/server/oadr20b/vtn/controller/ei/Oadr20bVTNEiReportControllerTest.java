package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.EndDeviceAssertMridType;
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
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.DemandResponseEventService;
import com.avob.openadr.server.common.vtn.service.VenGroupService;
import com.avob.openadr.server.common.vtn.service.VenMarketContextService;
import com.avob.openadr.server.common.vtn.service.VenResourceService;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.OtherReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDescriptionDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapability;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.SelfReportCapabilityDescription;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.data.ReportDataDto;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.OtherReportRequest;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.request.ReportRequestDto;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportDataService;
import com.avob.openadr.server.oadr20b.vtn.service.report.OtherReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityDescriptionService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportCapabilityService;
import com.avob.openadr.server.oadr20b.vtn.service.report.SelfReportRequestService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles( "test")
public class Oadr20bVTNEiReportControllerTest {

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
	private OtherReportDataService otherReportDataService;

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

	@Before
	public void setup() throws Exception {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void testVENSourceVTNTarget() throws Exception {

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
//		OadrPayload payload = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
//				xmlSignatureService.sign(oadrRegisterReportType), HttpStatus.OK_200, OadrPayload.class);
		String str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrRegisterReportType), HttpStatus.OK_200, String.class);
		OadrPayload payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);

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

		assertEquals(1, reportcapabilityDescriptionList.size());
		assertEquals(rid, reportcapabilityDescriptionList.get(0).getRid());
		assertEquals(readingType, reportcapabilityDescriptionList.get(0).getReadingType());
		assertEquals(reportType, reportcapabilityDescriptionList.get(0).getReportType());

		Long reportCapabilityDescriptionPrivateId = reportcapabilityDescriptionList.get(0).getId();

		// here we assume that VTN has successfully sent report create payload
		// to VEN, either using oadrRegisteredReport or OadrCreateReport
		// by doing so, It is suppose to have created a non acked
		// OtherReportRequest. Here we simulate this step by creating this
		// object
		Ven ven = venService.findOneByUsername(OadrDataBaseSetup.VEN);
		reportRequestId = "reportRequestId";
		String reportBackDuration = "P1D";
		OtherReportCapability reportCapability = otherReportCapabilityService.findOne(reportCapabilityPrivateId);
		OtherReportCapabilityDescription reportCapabilityDescription = otherReportCapabilityDescriptionService
				.findOne(reportCapabilityDescriptionPrivateId);
		String granularity = "PT1H";
		OtherReportRequest otherReportRequest = new OtherReportRequest(ven, reportCapability,
				reportCapabilityDescription, reportRequestId, granularity, reportBackDuration);
		otherReportRequest.setGranularity(minPeriod);
		otherReportRequest.setReportRequestId(reportRequestId);
		otherReportRequest.setReadingType(readingType);
		otherReportRequest.setReportBackDuration(reportBackDuration);
		otherReportRequestService.save(otherReportRequest);

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
//		payload = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
//				xmlSignatureService.sign(oadrCreatedReportType), HttpStatus.OK_200, OadrPayload.class);
		str = mockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrCreatedReportType), HttpStatus.OK_200, String.class);
		payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		response = payload.getOadrSignedObject().getOadrResponse();
		assertNotNull(response);
		assertEquals(String.valueOf(HttpStatus.OK_200), response.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, response.getVenID());

		// test previous payload has successfully acked requestreport
		List<ReportRequestDto> reportRequestList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/requested",
				HttpStatus.OK_200, ReportRequestDto.class);
		assertEquals(1, reportRequestList.size());
		assertTrue(reportRequestList.get(0).isAcked());
		assertEquals(minPeriod, reportRequestList.get(0).getGranularity());
		assertEquals(reportBackDuration, reportRequestList.get(0).getReportBackDuration());
		assertEquals(readingType, reportRequestList.get(0).getReadingType());
		assertEquals(reportRequestId, reportRequestList.get(0).getReportRequestId());

		Long reportRequestPrivateId = reportRequestList.get(0).getId();

		String intervalId = "intervalId";
		long start = System.currentTimeMillis();
		String xmlDuration = "P1D";
		Long confidence = 80L;
		Float accuracy = 1F;
		Float value = 3F;
		// create VEN oadrUpdateReport float payload
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
		List<ReportDataDto> reportDataList = mockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/" + reportSpecifierId, HttpStatus.OK_200,
				ReportDataDto.class);
		assertEquals(1, reportDataList.size());

		mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/fakeReportSpecifierId",
				HttpStatus.NOT_ACCEPTABLE_406, null);

		reportDataList = mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, ReportDataDto.class);
		assertEquals(1, reportDataList.size());

		mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/" + reportSpecifierId + "/rid/fakeRid",
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
		reportDataList = mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/" + reportSpecifierId, HttpStatus.OK_200,
				ReportDataDto.class);
		assertEquals(1, reportDataList.size());

		reportDataList = mockMvc.getRestJsonControllerAndExpectList(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_ENDPOINT + OadrDataBaseSetup.VEN + "/report/data/" + reportSpecifierId + "/rid/" + rid,
				HttpStatus.OK_200, ReportDataDto.class);
		assertEquals(1, reportDataList.size());

		assertEquals(new Double(Math.floor(new Long(start) / 1000) * 1000),
				new Double(reportDataList.get(0).getStart()));
		assertEquals(confidence, reportDataList.get(0).getConfidence());
		assertNull(reportDataList.get(0).getValue());
		assertEquals(new Float(0), reportDataList.get(0).getOadrCapacityCurrent());
		assertEquals(new Float(0), reportDataList.get(0).getOadrCapacityNormal());
		assertEquals(new Float(0), reportDataList.get(0).getOadrCapacityMin());
		assertEquals(new Float(0), reportDataList.get(0).getOadrCapacityMax());

		reportDataPrivateId = reportDataList.get(0).getId();

		// cleanup data
		otherReportDataService.delete(reportDataPrivateId);

		otherReportRequestService.delete(reportRequestPrivateId);
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
