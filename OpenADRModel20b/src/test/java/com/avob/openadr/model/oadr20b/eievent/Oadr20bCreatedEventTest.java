package com.avob.openadr.model.oadr20b.eievent;

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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;

public class Oadr20bCreatedEventTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreatedEventTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {

		String venId = "venId";
		String requestId = "requestId";
		int responseCode = 200;
		String eventId = "eventId";
		long modificationNumber = 0L;
		OadrCreatedEventType request = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseOK(requestId),
						venId)
				.addEventResponse(
						Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId, modificationNumber,
								requestId, responseCode, OptTypeType.OPT_IN).withDescription("mouaiccool").build())
				.addEventResponse(
						Arrays.asList(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId,
								modificationNumber, requestId, responseCode, OptTypeType.OPT_IN).build()))
				.build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);

	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		File file = new File("src/test/resources/eievent/unvalidatingOadrCreatedEvent.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrRequestEventType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eievent/oadrCreatedEvent.xml");
		OadrCreatedEventType unmarshal = jaxbContext.unmarshal(file, OadrCreatedEventType.class);
		assertEquals("200", unmarshal.getEiCreatedEvent().getEiResponse().getResponseCode());
		assertEquals("", unmarshal.getEiCreatedEvent().getEiResponse().getRequestID());
		assertEquals("OK", unmarshal.getEiCreatedEvent().getEiResponse().getResponseDescription().trim());

		assertEquals("VEN_123", unmarshal.getEiCreatedEvent().getVenID());

		assertEquals(1, unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().size());
		assertEquals("EVENT_12344", unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.getQualifiedEventID().getEventID());
		assertEquals(0L, unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.getQualifiedEventID().getModificationNumber());
		assertEquals("200",
				unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getResponseCode());
		assertEquals("REQ_12345",
				unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0).getRequestID());
		assertEquals("OK", unmarshal.getEiCreatedEvent().getEventResponses().getEventResponse().get(0)
				.getResponseDescription().trim());

		File file2 = new File("src/test/resources/eievent/genOadrCreatedEvent.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreatedEvent(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();
		
		

	}

}
