package com.avob.openadr.model.oadr20b.eievent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.assertj.core.util.Files;
import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;

public class Oadr20bRequestEventTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bRequestEventTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		OadrRequestEventType request = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(null, null).build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eievent/unvalidatingOadrRequestEvent.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrRequestEventType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eievent/oadrRequestEvent.xml");
		OadrRequestEventType unmarshal = jaxbContext.unmarshal(file, OadrRequestEventType.class);
		assertEquals("REQ_12345", unmarshal.getEiRequestEvent().getRequestID());
		assertEquals("VEN_1234", unmarshal.getEiRequestEvent().getVenID());
		assertEquals(new Long(5), unmarshal.getEiRequestEvent().getReplyLimit());

		File file2 = new File("src/test/resources/eievent/genOadrRequestEvent.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrRequestEvent(unmarshal), file2);
		assertTrue(file2.exists());
		Files.delete(file2);
	}

}
