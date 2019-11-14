package com.avob.openadr.model.oadr20a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.OptTypeType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;

public class Oadr20aCreatedEventTest {

	private Oadr20aJAXBContext jaxbContext;

	public Oadr20aCreatedEventTest() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance(TestUtils.XSD_OADR20A_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException, FileNotFoundException {

		String venId = "venId";
		String requestId = "requestId";
		int responseCode = 200;
		String eventId = null;
		long modificationNumber = 0L;
		OadrCreatedEvent request = Oadr20aBuilders
				.newCreatedEventBuilder(venId, requestId,
						responseCode)
				.addEventResponse(
						Oadr20aBuilders.newOadr20aCreatedEventEventResponseBuilder(eventId, modificationNumber,
								requestId, responseCode, OptTypeType.OPT_IN).withDescription("mouaiccool").build())
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshal(request);
		} catch (Oadr20aMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/unvalidatingOadrCreatedEvent.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(new FileInputStream(file), OadrRequestEvent.class);
		} catch (Oadr20aUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest()
			throws Oadr20aUnmarshalException, Oadr20aMarshalException, FileNotFoundException {
		File file = new File("src/test/resources/oadrCreatedEvent.xml");
		OadrCreatedEvent unmarshal = jaxbContext.unmarshal(new FileInputStream(file), OadrCreatedEvent.class);
		assertEquals("123", unmarshal.getEiCreatedEvent().getEiResponse().getResponseCode());
		assertEquals("pyld:requestID", unmarshal.getEiCreatedEvent().getEiResponse().getRequestID());
		assertEquals("ei:responseDescription",
				unmarshal.getEiCreatedEvent().getEiResponse().getResponseDescription().trim());

		assertEquals("ei:venID", unmarshal.getEiCreatedEvent().getVenID());

		assertEquals(1, unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().size());
		assertEquals("ei:eventID", unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.getQualifiedEventID().getEventID());
		assertEquals(0L, unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.getQualifiedEventID().getModificationNumber());
		assertEquals("124",
				unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getResponseCode());
		assertEquals("pyld:requestID",
				unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getRequestID());
		assertEquals("ei:responseDescription", unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse()
				.get(0).getResponseDescription().trim());

		jaxbContext.marshal(unmarshal);
	}
}
