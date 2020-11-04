package com.avob.openadr.model.oadr20b.poll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;

public class OadrPollTest {
	private Oadr20bJAXBContext jaxbContext;

	public OadrPollTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String venId = null;
		OadrPollType request = Oadr20bPollBuilders.newOadr20bPollBuilder(venId).build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/poll/unvalidatingOadrPoll.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrPollType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/poll/oadrPoll.xml");
		OadrPollType unmarshal = jaxbContext.unmarshal(file, OadrPollType.class);
		assertEquals("VEN_123", unmarshal.getVenID());

		File file2 = new File("src/test/resources/poll/genOadrPoll.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrPoll(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
