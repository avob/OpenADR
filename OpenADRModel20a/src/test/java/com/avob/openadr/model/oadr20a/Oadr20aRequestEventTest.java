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
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;

public class Oadr20aRequestEventTest {

	private Oadr20aJAXBContext jaxbContext;

	public Oadr20aRequestEventTest() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance(TestUtils.XSD_OADR20A_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException, FileNotFoundException {

		OadrRequestEvent request = Oadr20aBuilders.newOadrRequestEventBuilder(null, null).build();

		boolean assertion = false;
		try {
			jaxbContext.marshal(request);
		} catch (Oadr20aMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/unvalidatingOadrRequestEvent.xml");
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
		File file = new File("src/test/resources/oadrRequestEvent.xml");
		OadrRequestEvent unmarshal = jaxbContext.unmarshal(new FileInputStream(file), OadrRequestEvent.class);
		assertEquals("pyld:requestID", unmarshal.getEiRequestEvent().getRequestID());
		assertEquals("ei:venID", unmarshal.getEiRequestEvent().getVenID());
		assertEquals(Long.valueOf(0), unmarshal.getEiRequestEvent().getReplyLimit());

		jaxbContext.marshal(unmarshal);
	}
}
