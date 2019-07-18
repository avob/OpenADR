package com.avob.openadr.server.oadr20b.vtn.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
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
import com.avob.openadr.model.oadr20b.builders.eireport.PowerRealUnitType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.iso.ISO3AlphaCurrencyCodeContentType;
import com.avob.openadr.model.oadr20b.oadr.CurrencyItemDescriptionType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.models.venpoll.VenPollDao;
import com.avob.openadr.server.oadr20b.vtn.models.venreport.capability.ReportCapabilityDto;
import com.avob.openadr.server.oadr20b.vtn.service.VenPollService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVenControllerTest {

	private static final String VEN_URL = "/Ven/";

	@Resource
	private VenPollDao venPollDao;

	@Resource
	private VenPollService venPollService;

	@Resource
	private OadrMockMvc oadrMockMvc;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Test
	public void testReportAction() throws Exception {

		venPollService.deleteAll();

		// test nothing in poll queue
		OadrPollType poll = Oadr20bPollBuilders.newOadr20bPollBuilder(OadrDataBaseSetup.VEN).build();
		OadrResponseType oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				poll, HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/registerparty/requestReregistration", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRequestReregistrationType.class);

		// send register party requestReregistration action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/registerparty/cancelPartyRegistration", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelPartyRegistrationType.class);

		// send request register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/requestRegister", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCreateReportType.class);

		// send own register report action
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/sendRegister", HttpStatus.OK_200);

		Thread.sleep(200);

		// test register report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrRegisterReportType.class);

		// send cancel report action
		String reportRequestId = "reportRequestId";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("reportRequestId", reportRequestId);
		oadrMockMvc.postVenAction(OadrDataBaseSetup.ADMIN_SECURITY_SESSION,
				VEN_URL + OadrDataBaseSetup.VEN + "/report_action/cancel", HttpStatus.OK_200, params);

		Thread.sleep(200);

		// test cancel report payload is in poll queue
		oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll, HttpStatus.OK_200,
				OadrCancelReportType.class);

		oadrResponseType = oadrMockMvc.postOadrPollAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION, poll,
				HttpStatus.OK_200, OadrResponseType.class);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrResponseType.getEiResponse().getResponseCode());

	}

	@Test
	public void subscriptionTest() throws Oadr20bXMLSignatureException, Exception {

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
		OadrRegisteredReportType oadrRegisteredReportType = oadrMockMvc.postEiReportAndExpect(
				OadrDataBaseSetup.ANOTHER_VEN_SECURITY_SESSION, xmlSignatureService.sign(oadrRegisterReportType),
				HttpStatus.OK_200, OadrRegisteredReportType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.TARGET_MISMATCH_462),
				oadrRegisteredReportType.getEiResponse().getResponseCode());

		// sign and push this payload into EiReport controller
		String str = oadrMockMvc.postEiReportAndExpect(OadrDataBaseSetup.VEN_SECURITY_SESSION,
				xmlSignatureService.sign(oadrRegisterReportType), HttpStatus.OK_200, String.class);
		OadrPayload payload = Oadr20bJAXBContext.getInstance().unmarshal(str, OadrPayload.class);
		xmlSignatureService.validate(str, payload);
		oadrRegisteredReportType = payload.getOadrSignedObject().getOadrRegisteredReport();

		assertNotNull(oadrRegisteredReportType);
		assertEquals(String.valueOf(HttpStatus.OK_200), oadrRegisteredReportType.getEiResponse().getResponseCode());
		assertEquals(OadrDataBaseSetup.VEN, oadrRegisteredReportType.getVenID());
		assertNotNull(oadrRegisteredReportType.getOadrReportRequest());

		List<ReportCapabilityDto> reportcapabilityList = oadrMockMvc.getRestJsonControllerAndExpectList(
				OadrDataBaseSetup.ADMIN_SECURITY_SESSION, VEN_URL + OadrDataBaseSetup.VEN + "/report/available",
				HttpStatus.OK_200, ReportCapabilityDto.class);
		assertEquals(1, reportcapabilityList.size());
		assertEquals(reportSpecifierId, reportcapabilityList.get(0).getReportSpecifierId());
		assertEquals(reportName, reportcapabilityList.get(0).getReportName());

		Long reportCapabilityPrivateId = reportcapabilityList.get(0).getId();

//		oadrMockMvc.pos

	}

}
