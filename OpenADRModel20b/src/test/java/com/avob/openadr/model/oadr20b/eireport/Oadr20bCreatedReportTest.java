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
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;

public class Oadr20bCreatedReportTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreatedReportTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest() throws Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		String venId = "venId";
		String reportId = "reportId";
		int responseCode = 200;
		OadrCreatedReportType request = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(requestId, responseCode, venId).addPendingReportRequestId(reportId)
				.build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eireport/unvalidatingOadrCreatedReport.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreatedReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrCreatedReport.xml");
		OadrCreatedReportType unmarshal = jaxbContext.unmarshal(file, OadrCreatedReportType.class);
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals(2, unmarshal.getOadrPendingReports().getReportRequestID().size());
		assertEquals("RR_54321", unmarshal.getOadrPendingReports().getReportRequestID().get(0));
		assertEquals("RR_54322", unmarshal.getOadrPendingReports().getReportRequestID().get(1));

		File file2 = new File("src/test/resources/eireport/genOadrCreatedReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreatedReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
