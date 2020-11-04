package com.avob.openadr.model.oadr20b.eiregisterparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;

public class Oadr20bCreatePartyRegistrationTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreatePartyRegistrationTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		String venId = "ven";
		String profilName = "2.0b";
		Boolean pullModel = true;
		boolean reportOnly = true;
		String transportAddress = "transportAddress";
		OadrTransportType transportType = OadrTransportType.SIMPLE_HTTP;
		String venName = "venName";
		boolean xmlSignature = true;
		String registrationId = "registrationId";
		OadrCreatePartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(requestId, venId, profilName).withOadrHttpPullModel(pullModel)
				.withOadrReportOnly(reportOnly).withOadrTransportAddress(transportAddress)
				.withOadrTransportName(transportType).withOadrVenName(venName).withOadrXmlSignature(xmlSignature)
				.withRegistrationId(registrationId).build();
		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrCreatePartyRegistration.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreatePartyRegistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrCreatePartyRegistration.xml");
		OadrCreatePartyRegistrationType unmarshal = jaxbContext.unmarshal(file, OadrCreatePartyRegistrationType.class);
		assertEquals("REQ_123", unmarshal.getRequestID());
		assertEquals("VEN_1234", unmarshal.getVenID());
		assertEquals("2.0b", unmarshal.getOadrProfileName());
		assertEquals(OadrTransportType.SIMPLE_HTTP, unmarshal.getOadrTransportName());
		assertEquals("MyVEN", unmarshal.getOadrVenName());
		assertFalse(unmarshal.isOadrReportOnly());
		assertFalse(unmarshal.isOadrXmlSignature());
		assertTrue(unmarshal.isOadrHttpPullModel());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrCreatePartyRegistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreatePartyRegistration(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

}
