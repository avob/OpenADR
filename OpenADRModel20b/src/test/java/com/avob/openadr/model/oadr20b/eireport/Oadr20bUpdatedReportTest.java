package com.avob.openadr.model.oadr20b.eireport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.assertj.core.util.Files;
import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

public class Oadr20bUpdatedReportTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bUpdatedReportTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String venId = "venId";
		int responseCode = 200;
		OadrCancelReportType oadrCancelReport = null;
		OadrUpdatedReportType request = Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder(requestId, responseCode, venId).withOadrCancelReport(oadrCancelReport)
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eireport/unvalidatingOadrUpdatedReport.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrUpdatedReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrUpdatedReport.xml");
		OadrUpdatedReportType unmarshal = jaxbContext.unmarshal(file, OadrUpdatedReportType.class);
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());

		File file2 = new File("src/test/resources/eireport/genOadrUpdatedReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrUpdatedReport(unmarshal), file2);
		assertTrue(file2.exists());
		Files.delete(file2);

	}
}
