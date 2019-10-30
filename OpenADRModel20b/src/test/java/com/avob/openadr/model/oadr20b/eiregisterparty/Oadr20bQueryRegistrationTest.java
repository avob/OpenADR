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
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;

public class Oadr20bQueryRegistrationTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bQueryRegistrationTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;

		OadrQueryRegistrationType request = Oadr20bEiRegisterPartyBuilders.newOadr20bQueryRegistrationBuilder(requestId)
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrQueryRegistration.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrQueryRegistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrQueryRegistration.xml");
		OadrQueryRegistrationType unmarshal = jaxbContext.unmarshal(file, OadrQueryRegistrationType.class);
		assertEquals("String", unmarshal.getRequestID());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrQueryRegistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrQueryRegistration(unmarshal), file2);
		assertTrue(file2.exists());
		Files.delete(file2);

	}

}
