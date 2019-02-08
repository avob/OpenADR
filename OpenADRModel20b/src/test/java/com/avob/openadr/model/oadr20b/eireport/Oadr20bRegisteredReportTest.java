package com.avob.openadr.model.oadr20b.eireport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;

public class Oadr20bRegisteredReportTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bRegisteredReportTest() {
		try {
			jaxbContext = Oadr20bJAXBContext.getInstance();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String venId = "venId";
		OadrReportRequestType reportRequest = null;
		int responseCode = 200;
		OadrRegisteredReportType request = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder(requestId, responseCode, venId).addReportRequest(reportRequest)
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eireport/unvalidatingOadrRegisteredReport.xml");
		assertion = false;
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
