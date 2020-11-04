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
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;

public class Oadr20bCanceledOptTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCanceledOptTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {
		String requestId = "requestId";
		int responseCode = 200;
		String optId = "optId";
		OadrCanceledOptType request = Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestId, responseCode, optId)
				.build();
		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eiopt/unvalidatingOadrCanceledOpt.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCanceledOptType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiopt/oadrCanceledOpt.xml");
		OadrCanceledOptType unmarshal = jaxbContext.unmarshal(file, OadrCanceledOptType.class);
		assertEquals("Req_12345", unmarshal.getEiResponse().getRequestID());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("OPT_54321", unmarshal.getOptID());
		File file2 = new File("src/test/resources/eiopt/genOadrCanceledOpt.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCanceledOpt(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
