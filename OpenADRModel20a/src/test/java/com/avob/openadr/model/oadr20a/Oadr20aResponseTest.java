package com.avob.openadr.model.oadr20a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

public class Oadr20aResponseTest {

	private Oadr20aJAXBContext jaxbContext;

	public Oadr20aResponseTest() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance(TestUtils.XSD_OADR20A_SCHEMA);
	}

	@Test
	public void marshalTest() throws DatatypeConfigurationException, Oadr20aMarshalException, FileNotFoundException {
		String requestId = null;
		Integer responseCode = 200;
		String responseDescription = "";

		OadrResponse response = Oadr20aBuilders.newOadr20aResponseBuilder(requestId, responseCode)
				.withDescription(responseDescription).build();

		boolean assertion = false;
		try {
			jaxbContext.marshal(response);
		} catch (Oadr20aMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		response = Oadr20aBuilders
				.newOadr20aResponseBuilder(Oadr20aBuilders.newOadr20aEiResponseBuilder(requestId, responseCode).build())
				.withDescription(responseDescription).build();

		assertion = false;
		try {
			jaxbContext.marshal(response);
		} catch (Oadr20aMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/unvalidatingOadrResponse.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(new FileInputStream(file), OadrDistributeEvent.class);
		} catch (Oadr20aUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalTest() throws Oadr20aUnmarshalException, Oadr20aMarshalException, FileNotFoundException {

		File file = new File("src/test/resources/oadrResponse.xml");
		OadrResponse unmarshal = jaxbContext.unmarshal(new FileInputStream(file), OadrResponse.class);
		assertNotNull(unmarshal.getEiResponse());
		assertEquals("124", unmarshal.getEiResponse().getResponseCode());
		assertEquals("2", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("3", unmarshal.getEiResponse().getRequestID());

		jaxbContext.marshal(unmarshal);

	}
}
