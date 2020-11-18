package com.avob.openadr.model.oadr20b.eireport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;

public class Oadr20bUpdateReportTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bUpdateReportTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest() throws Oadr20bMarshalException, Oadr20bUnmarshalException {

		String requestId = "requestId";
		String venId = "venId";
		String reportId = "reportId";
		String reportrequestId = "reportrequestId";
		String reportSpecifierId = "reportSpecifierId";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.HISTORY_GREENBUTTON;
		long createdTimestamp = 0L;
		Long startTimestamp = 0L;
		String duration = "PT1H";
		OadrReportType reportType = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, reportSpecifierId, reportrequestId, reportName,
						createdTimestamp, startTimestamp, duration)
				.addInterval(Oadr20bEiBuilders
						.newOadr20bReportIntervalTypeBuilder("intervalId", 0L, "PT1H", "rid", 0L, 0F, 0F).build())
				.addInterval(Arrays.asList(Oadr20bEiBuilders
						.newOadr20bReportIntervalTypeBuilder("intervalId", 0L, "PT1H", "rid", 0L, 0F, 0F).build()))
				.build();

		OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
				.addReport(reportType).build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eireport/unvalidatingOadrUpdateReport.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrUpdateReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrUpdateReport.xml");
		OadrUpdateReportType unmarshal = jaxbContext.unmarshal(file, OadrUpdateReportType.class);
		assertEquals("String", unmarshal.getRequestID());
		assertEquals("VEN_123", unmarshal.getVenID());

		File file2 = new File("src/test/resources/eireport/genOadrUpdateReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrUpdateReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

	public void testValidOadrPayloadResourceStatusType() throws Oadr20bMarshalException {
		String venId = "venId";
		String requestId = "requestId";
		String reportSpecifierId = "reportSpecifierId";
		String reportId = "reportId";
		String reportRequestId = "reportRequestId";
		String rid = "rid";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		long createdTimestamp = System.currentTimeMillis();
		String intervalId = "intervalId";
		long start = System.currentTimeMillis();
		String xmlDuration = "P1D";
		Long confidence = 80L;
		Float accuracy = 1F;

		OadrLoadControlStateTypeType loadControlState = Oadr20bFactory.createOadrLoadControlStateTypeType(0f, 0f, 0f,
				0f);
		OadrLoadControlStateType createOadrLoadControlStateType = Oadr20bFactory
				.createOadrLoadControlStateType(loadControlState, null, null, null);

		boolean manualOverride = true;
		boolean online = true;

		OadrPayloadResourceStatusType createOadrPayloadResourceStatusType = Oadr20bFactory
				.createOadrPayloadResourceStatusType(createOadrLoadControlStateType, manualOverride, online);

		OadrReportType reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, reportSpecifierId, reportRequestId, reportName,
						createdTimestamp, start, xmlDuration)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, createOadrPayloadResourceStatusType).build())
				.build();
		OadrUpdateReportType oadrUpdateReportType = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportBuilder(requestId, venId).addReport(reportUpdate).build();

		jaxbContext.marshalRoot(oadrUpdateReportType);
	}

	public void testValidPayloadFloat() throws Oadr20bMarshalException {
		String venId = "venId";
		String requestId = "requestId";
		String reportSpecifierId = "reportSpecifierId";
		String reportId = "reportId";
		String reportRequestId = "reportRequestId";
		String rid = "rid";
		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.METADATA_TELEMETRY_USAGE;
		long createdTimestamp = System.currentTimeMillis();
		String intervalId = "intervalId";
		long start = System.currentTimeMillis();
		String xmlDuration = "P1D";
		Long confidence = 80L;
		Float accuracy = 1F;
		Float value = 3f;

		OadrReportType reportUpdate = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportOadrReportBuilder(reportId, reportSpecifierId, reportRequestId, reportName,
						createdTimestamp, start, xmlDuration)
				.addInterval(Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid,
						confidence, accuracy, value).build())
				.build();
		OadrUpdateReportType oadrUpdateReportType = Oadr20bEiReportBuilders
				.newOadr20bUpdateReportBuilder(requestId, venId).addReport(reportUpdate).build();

		jaxbContext.marshalRoot(oadrUpdateReportType);
	}
}
