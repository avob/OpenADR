package com.avob.openadr.model.oadr20b.eiopt;

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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;

public class Oadr20bCreatedOptTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreatedOptTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		String optId = "optId";
		int responseCode = 200;
		OadrCreatedOptType request = Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestId, responseCode, optId)
				.withResponseDescription("mouaiccool").build();

		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eiopt/unvalidatingOadrCreatedOpt.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreatedOptType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiopt/oadrCreatedOpt.xml");
		OadrCreatedOptType unmarshal = jaxbContext.unmarshal(file, OadrCreatedOptType.class);
		assertEquals("REQ_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("OPT_12345", unmarshal.getOptID());
		File file2 = new File("src/test/resources/eiopt/genOadrCreatedOpt.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreatedOpt(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
