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
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

public class Oadr20bRegisteredReportTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bRegisteredReportTest() throws JAXBException {
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
		OadrReportRequestType reportRequest = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
				.addSpecifierPayload(
						Oadr20bFactory.createTemperature(Oadr20bFactory
								.createTemperatureType(TemperatureUnitType.CELSIUS, SiScaleCodeType.KILO)),
						ReadingTypeEnumeratedType.DIRECT_READ, rid)
				.build();

		int responseCode = 200;
		OadrRegisteredReportType request = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder(requestId, responseCode, venId).addReportRequest(reportRequest)
				.build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eireport/unvalidatingOadrRegisteredReport.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrRegisteredReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrRegisteredReport.xml");
		OadrRegisteredReportType unmarshal = jaxbContext.unmarshal(file, OadrRegisteredReportType.class);
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());

		File file2 = new File("src/test/resources/eireport/genOadrRegisteredReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrRegisteredReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
