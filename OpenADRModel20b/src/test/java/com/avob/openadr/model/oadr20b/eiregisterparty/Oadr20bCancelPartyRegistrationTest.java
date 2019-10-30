package com.avob.openadr.model.oadr20b.eiregisterparty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.assertj.core.util.Files;
import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;

public class Oadr20bCancelPartyRegistrationTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCancelPartyRegistrationTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String registrationId = "registrationId";
		String venId = "venId";
		OadrCancelPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, venId).build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrCancelPartyRegistration.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCancelPartyRegistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrCancelPartyRegistration.xml");
		OadrCancelPartyRegistrationType unmarshal = jaxbContext.unmarshal(file, OadrCancelPartyRegistrationType.class);
		assertEquals("REQ_12345", unmarshal.getRequestID());
		assertEquals("REG_54321", unmarshal.getRegistrationID());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrCancelPartyRegistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCancelPartyRegistration(unmarshal), file2);
		assertTrue(file2.exists());
		Files.delete(file2);

	}

}
