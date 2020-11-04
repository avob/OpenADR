package com.avob.openadr.model.oadr20b.eireport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

public class Oadr20bCreateReportTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreateReportTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest() throws Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		String venId = "venId";
		String reportRequestId = "reportRequestId";
		String reportSpecifierId = "reportSpecifierId";
		String granularity = "PT1H";
		String reportBackDuration = "PT1H";
		String rid = "rid";
		OadrReportRequestType build = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(
						Oadr20bFactory.createTemperature(Oadr20bFactory
								.createTemperatureType(TemperatureUnitType.CELSIUS, SiScaleCodeType.KILO)),
						ReadingTypeEnumeratedType.DIRECT_READ, rid)
				.build();

		OadrCreateReportType request = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(requestId, venId)
				.addReportRequest(build).build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eireport/unvalidatingOadrCreateReport.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreateReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrCreateReport.xml");
		OadrCreateReportType unmarshal = jaxbContext.unmarshal(file, OadrCreateReportType.class);
		assertEquals("REQ_12345", unmarshal.getRequestID());
		assertEquals("VEN_123", unmarshal.getVenID());
		assertEquals(1, unmarshal.getOadrReportRequest().size());
		assertEquals("RR_54321", unmarshal.getOadrReportRequest().get(0).getReportRequestID());
		assertEquals("PT0M",
				unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getGranularity().getDuration());
		assertEquals("PT0M",
				unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getReportBackDuration().getDuration());
		assertEquals("RS_12345", unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getReportSpecifierID());
		assertEquals("2001-12-17T09:30:47Z", unmarshal.getOadrReportRequest().get(0).getReportSpecifier()
				.getReportInterval().getProperties().getDtstart().getDateTime().toString());
		assertEquals("PT2H", unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getReportInterval()
				.getProperties().getDuration().getDuration());
		assertEquals(2, unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().size());
		assertEquals("123",
				unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(0).getRID());
		assertEquals("x-notApplicable", unmarshal.getOadrReportRequest().get(0).getReportSpecifier()
				.getSpecifierPayload().get(0).getReadingType());
		assertEquals("124",
				unmarshal.getOadrReportRequest().get(0).getReportSpecifier().getSpecifierPayload().get(1).getRID());
		assertEquals("x-notApplicable", unmarshal.getOadrReportRequest().get(0).getReportSpecifier()
				.getSpecifierPayload().get(1).getReadingType());

		File file2 = new File("src/test/resources/eireport/genOadrCreateReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreateReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
