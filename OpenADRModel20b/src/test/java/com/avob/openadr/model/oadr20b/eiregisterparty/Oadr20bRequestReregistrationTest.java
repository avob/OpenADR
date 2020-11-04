package com.avob.openadr.model.oadr20b.eiregisterparty;

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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;

public class Oadr20bRequestReregistrationTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bRequestReregistrationTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {
		String venId = "venId";
		OadrRequestReregistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(venId).build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrRequestReregistration.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrRequestReregistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrRequestReregistration.xml");
		OadrRequestReregistrationType unmarshal = jaxbContext.unmarshal(file, OadrRequestReregistrationType.class);
		assertEquals("VEN_123", unmarshal.getVenID());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrRequestReregistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrRequestReregistration(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

}
