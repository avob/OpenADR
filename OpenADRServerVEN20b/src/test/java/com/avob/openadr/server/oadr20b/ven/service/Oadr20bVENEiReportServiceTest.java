package com.avob.openadr.server.oadr20b.ven.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VEN20bApplicationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VEN20bApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVENEiReportServiceTest {

	@Value("${oadr.vtn.myvtn.vtnid}")
	private String vtnHttpId;

	@Value("${oadr.vtn.myvtn.venUrl}")
	private String venUrl;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	@Resource
	private Oadr20bJAXBContext oadr20bJAXBContext;

	@Resource
	private Oadr20bVENEiReportService oadr20bVENEiReportService;

	@Resource
	private Oadr20bVENPayloadService oadr20bVENPayloadService;

	@Test
	public void oadrUpdateReportTest()
			throws Oadr20bApplicationLayerException, Oadr20bMarshalException, Oadr20bUnmarshalException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {

		String reportId = "";
		String reportrequestId = "";
		String reportSpecifierId = "";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_HISTORY_GREENBUTTON;
		long createdTimestamp = 0L;
		Long startTimestamp = 0L;
		String duration = "PT1S";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(reportId,
				reportSpecifierId,reportrequestId,  reportName, createdTimestamp, startTimestamp, duration).build();
		OadrUpdateReportType oadrUpdateReport = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportBuilder("", multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId())
				.addReport(report).addReport(Lists.newArrayList(report)).build();

		String request = oadr20bVENPayloadService.report(vtnHttpId, venUrl,
				oadr20bJAXBContext.marshalRoot(oadrUpdateReport));

		OadrUpdatedReportType resp = oadr20bJAXBContext.unmarshal(request, OadrUpdatedReportType.class);
		assertNotNull(resp);
		assertEquals("200", resp.getEiResponse().getResponseCode());

	}

	@Test
	public void oadrRegisterReportTest()
			throws Oadr20bApplicationLayerException, Oadr20bMarshalException, Oadr20bUnmarshalException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {

		String reportId = "";
		String reportRequestId = "";
		String reportSpecifierId = "";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_HISTORY_GREENBUTTON;
		long createdTimestamp = 0L;
		Long startTimestamp = 0L;
		String duration = "PT1S";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(reportId,
				reportSpecifierId, reportRequestId, reportName, createdTimestamp, startTimestamp, duration).build();
		OadrRegisterReportType oadrRegisterReport = Oadr20bEiReportBuilders
				.newOadr20bRegisterReportBuilder("", multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId())
				.addOadrReport(report).build();

		String request = oadr20bVENPayloadService.report(vtnHttpId, venUrl,
				oadr20bJAXBContext.marshalRoot(oadrRegisterReport));

		OadrRegisteredReportType resp = oadr20bJAXBContext.unmarshal(request, OadrRegisteredReportType.class);
		assertNotNull(resp);
		assertEquals(resp.getEiResponse().getResponseCode(), "200");
	}

	@Test
	public void oadrCancelReportTest()
			throws Oadr20bApplicationLayerException, Oadr20bMarshalException, Oadr20bUnmarshalException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {
		OadrCancelReportType oadrCancelReport = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder("", multiVtnConfig.getMultiConfig(vtnHttpId, venUrl).getVenId(), false)
				.addReportRequestId("").build();
		String request = oadr20bVENPayloadService.report(vtnHttpId, venUrl,
				oadr20bJAXBContext.marshalRoot(oadrCancelReport));

		OadrCanceledReportType resp = oadr20bJAXBContext.unmarshal(request, OadrCanceledReportType.class);
		assertNotNull(resp);
		assertEquals(resp.getEiResponse().getResponseCode(),
				String.valueOf(Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453));
	}

}
