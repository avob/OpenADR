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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.security.exception.OadrSecurityException;

public class Oadr20bCancelReportTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCancelReportTest() throws JAXBException, OadrSecurityException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest() throws Oadr20bMarshalException, Oadr20bUnmarshalException {

		String requestId = "requestId";
		String venId = "venId";
		boolean reportToFollow = true;
		String reportId = "reportId";
		OadrCancelReportType request = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(requestId, venId, reportToFollow).addReportRequestId(reportId)
				.addReportRequestId(Arrays.asList("mouaiccool")).build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eireport/unvalidatingOadrCancelReport.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCancelReportType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eireport/oadrCancelReport.xml");
		OadrCancelReportType unmarshal = jaxbContext.unmarshal(file, OadrCancelReportType.class);
		assertEquals("REQ_12345", unmarshal.getRequestID());
		assertEquals(1, unmarshal.getReportRequestID().size());
		assertEquals("REP_54321", unmarshal.getReportRequestID().get(0));
		assertEquals(true, unmarshal.isReportToFollow());
		assertEquals("VEN_123", unmarshal.getVenID());

		File file2 = new File("src/test/resources/eireport/genOadrCancelReport.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCancelReport(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
