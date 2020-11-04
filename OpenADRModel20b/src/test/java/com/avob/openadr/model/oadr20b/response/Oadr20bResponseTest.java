package com.avob.openadr.model.oadr20b.response;

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
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;

public class Oadr20bResponseTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bResponseTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		Integer responseCode = 200;
		String responseDescription = "responseDescription";
		String venId = "venId";
		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(requestId, responseCode, venId)
				.withDescription(responseDescription).build();
		String marshalRoot = jaxbContext.marshalRoot(response, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

		response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(Oadr20bResponseBuilders
						.newOadr20bEiResponseMismatchUsernameVenIdBuilder(requestId, "mouaiccool", venId), venId)
				.build();
		marshalRoot = jaxbContext.marshalRoot(response, true);
		unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

		response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(
				Oadr20bResponseBuilders.newOadr20bEiResponseXmlSignatureRequiredButAbsentBuilder(requestId, venId),
				venId).build();
		marshalRoot = jaxbContext.marshalRoot(response, true);
		unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

		response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(
				Oadr20bResponseBuilders.newOadr20bEiResponseInvalidRegistrationIdBuilder(requestId, venId), venId)
				.build();
		marshalRoot = jaxbContext.marshalRoot(response, true);
		unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

		response = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, responseCode).build(), venId)
				.build();
		marshalRoot = jaxbContext.marshalRoot(response, true);
		unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

	}

	@Test
	public void marshalTest() throws DatatypeConfigurationException, Oadr20bMarshalException {
		File file = new File("src/test/resources/response/unvalidatingOadrResponse.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrDistributeEventType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {

		File file = new File("src/test/resources/response/oadrResponse.xml");
		OadrResponseType unmarshal = jaxbContext.unmarshal(file, OadrResponseType.class);
		assertNotNull(unmarshal.getEiResponse());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());

		File file2 = new File("src/test/resources/response/genOadrResponse.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrResponse(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
