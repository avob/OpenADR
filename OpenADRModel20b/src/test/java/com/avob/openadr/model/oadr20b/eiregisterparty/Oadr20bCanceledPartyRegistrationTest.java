package com.avob.openadr.model.oadr20b.eiregisterparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;

public class Oadr20bCanceledPartyRegistrationTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCanceledPartyRegistrationTest() {
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
		int responseCode = 200;
		String registrationId = "registrationId";
		String venId = "venId";
		OadrCanceledPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(requestId, responseCode, registrationId, venId).build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrCanceledPartyRegistration.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCanceledPartyRegistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrCanceledPartyRegistration.xml");
		OadrCanceledPartyRegistrationType unmarshal = jaxbContext.unmarshal(file,
				OadrCanceledPartyRegistrationType.class);
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("REG_54321", unmarshal.getRegistrationID());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrCanceledPartyRegistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCanceledPartyRegistration(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

}
