package com.avob.openadr.model.oadr20b.eiopt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;

public class Oadr20bCancelOptTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCancelOptTest() {
		try {
			jaxbContext = Oadr20bJAXBContext.getInstance();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String optId = "optId";
		String venId = "venId";
		OadrCancelOptType request = Oadr20bEiOptBuilders.newOadr20bCancelOptBuilder(requestId, optId, venId).build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eiopt/unvalidatingOadrCancelOpt.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCancelOptType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiopt/oadrCancelOpt.xml");
		OadrCancelOptType unmarshal = jaxbContext.unmarshal(file, OadrCancelOptType.class);
		assertEquals("REQ_54321", unmarshal.getRequestID());
		assertEquals("OPT_12345", unmarshal.getOptID());
		assertEquals("VEN_123", unmarshal.getVenID());
		File file2 = new File("src/test/resources/eiopt/genOadrCancelOpt.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCancelOpt(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}
}
