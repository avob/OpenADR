package com.avob.openadr.model.oadr20b.eiregisterparty;

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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType.OadrExtensions;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType.OadrExtensions.OadrExtension;
import com.avob.openadr.model.oadr20b.oadr.OadrInfo;
import com.avob.openadr.model.oadr20b.oadr.OadrServiceNameType;
import com.avob.openadr.model.oadr20b.oadr.OadrServiceSpecificInfo;
import com.avob.openadr.model.oadr20b.oadr.OadrServiceSpecificInfo.OadrService;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;

public class Oadr20bCreatedPartyRegistrationTest {

	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreatedPartyRegistrationTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validatingMarshalUnmarshalTest()
			throws DatatypeConfigurationException, Oadr20bMarshalException, Oadr20bUnmarshalException {

		String requestId = "requestId";
		int responseCode = 200;
		String vtnId = "vtn";
		String venId = "ven";
		OadrServiceSpecificInfo oadrServiceSpecificInfo = new OadrServiceSpecificInfo();
		OadrService service = new OadrService();
		service.setOadrServiceName(OadrServiceNameType.EI_EVENT);
		OadrInfo info = new OadrInfo();
		info.setOadrKey("key");
		info.setOadrValue("value");
		service.getOadrInfo().add(info);
		oadrServiceSpecificInfo.getOadrService().add(service);
		OadrExtensions oadrExtensions = new OadrExtensions();
		OadrExtension extension = new OadrExtension();
		extension.setOadrExtensionName("myextensionname");
		extension.getOadrInfo().add(info);
		oadrExtensions.getOadrExtension().add(extension);
		OadrCreatedPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(requestId, responseCode).build(), venId, vtnId)
				.addOadrProfile(Oadr20bEiRegisterPartyBuilders.newOadr20bOadrProfileBuilder("2.0b")
						.addTransport(OadrTransportType.SIMPLE_HTTP).build())
				.addOadrProfile(Arrays.asList(Oadr20bEiRegisterPartyBuilders.newOadr20bOadrProfileBuilder("2.0b")
						.addTransport(OadrTransportType.SIMPLE_HTTP).build()))
				.withOadrRequestedOadrPollFreq("PT1H").withResponseDescription("mouaiccool")
				.withOadrServiceSpecificInfo(oadrServiceSpecificInfo).withOadrExtensions(oadrExtensions).build();
		String marshalRoot = jaxbContext.marshalRoot(request, true);
		Object unmarshal = jaxbContext.unmarshal(marshalRoot, true);
		assertNotNull(unmarshal);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {
		File file = new File("src/test/resources/eiregisterparty/unvalidatingOadrCreatedPartyRegistration.xml");
		boolean assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreatedPartyRegistrationType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiregisterparty/oadrCreatedPartyRegistration.xml");
		OadrCreatedPartyRegistrationType unmarshal = jaxbContext.unmarshal(file,
				OadrCreatedPartyRegistrationType.class);
		assertEquals("REQ_123", unmarshal.getEiResponse().getRequestID());
		assertEquals("200", unmarshal.getEiResponse().getResponseCode());
		assertEquals("OK", unmarshal.getEiResponse().getResponseDescription());
		assertEquals("VTN", unmarshal.getVtnID());

		assertEquals(2, unmarshal.getOadrProfiles().getOadrProfile().size());
		assertEquals("2.0a", unmarshal.getOadrProfiles().getOadrProfile().get(0).getOadrProfileName());
		assertEquals(1,
				unmarshal.getOadrProfiles().getOadrProfile().get(0).getOadrTransports().getOadrTransport().size());
		assertEquals(OadrTransportType.SIMPLE_HTTP, unmarshal.getOadrProfiles().getOadrProfile().get(0)
				.getOadrTransports().getOadrTransport().get(0).getOadrTransportName());
		assertEquals("2.0b", unmarshal.getOadrProfiles().getOadrProfile().get(1).getOadrProfileName());
		assertEquals(2,
				unmarshal.getOadrProfiles().getOadrProfile().get(1).getOadrTransports().getOadrTransport().size());
		assertEquals(OadrTransportType.SIMPLE_HTTP, unmarshal.getOadrProfiles().getOadrProfile().get(1)
				.getOadrTransports().getOadrTransport().get(0).getOadrTransportName());
		assertEquals(OadrTransportType.XMPP, unmarshal.getOadrProfiles().getOadrProfile().get(1).getOadrTransports()
				.getOadrTransport().get(1).getOadrTransportName());

		assertEquals(1, unmarshal.getOadrExtensions().getOadrExtension().size());
		assertEquals("My Extension", unmarshal.getOadrExtensions().getOadrExtension().get(0).getOadrExtensionName());
		assertEquals(1, unmarshal.getOadrExtensions().getOadrExtension().get(0).getOadrInfo().size());
		assertEquals("A Key",
				unmarshal.getOadrExtensions().getOadrExtension().get(0).getOadrInfo().get(0).getOadrKey());
		assertEquals("A Value",
				unmarshal.getOadrExtensions().getOadrExtension().get(0).getOadrInfo().get(0).getOadrValue());

		File file2 = new File("src/test/resources/eiregisterparty/genOadrCreatePartyRegistration.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreatedPartyRegistration(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

}
