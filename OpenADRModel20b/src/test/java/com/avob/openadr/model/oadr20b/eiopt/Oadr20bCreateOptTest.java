package com.avob.openadr.model.oadr20b.eiopt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.eipayload.EndDeviceAssertMridType;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.xcal.VavailabilityType;

public class Oadr20bCreateOptTest {
	private Oadr20bJAXBContext jaxbContext;

	public Oadr20bCreateOptTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void unvalidatingMarshalUnmarshalTest() throws DatatypeConfigurationException {

		String requestId = null;
		String optId = "optId";
		String venId = "venId";
		Long createdDatetime = 0L;
		String eventId = "eventId";
		long modificationNumber = 0L;
		OptTypeType optType = OptTypeType.OPT_IN;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.OVERRIDE_STATUS;
		OadrCreateOptType request = Oadr20bEiOptBuilders
				.newOadr20bCreateOptBuilder(requestId, venId, createdDatetime, eventId, modificationNumber, optId,
						optType, optReason)
				.addTargetedResource("res1").withMarketContext("http://oadr.avob.com")
				.withOadrDeviceClass(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
						.addEndDeviceAsset(Oadr20bFactory
								.createEndDeviceAssetType(EndDeviceAssertMridType.ENERGY_MANAGEMENT_SYSTEM))
						.build())
				.build();

		boolean assertion = false;
		try {
			jaxbContext.marshalRoot(request, true);
		} catch (Oadr20bMarshalException e) {
			assertion = true;
		}

		assertTrue(assertion);

		File file = new File("src/test/resources/eiopt/unvalidatingOadrCreateOpt.xml");
		assertion = false;
		try {
			jaxbContext.unmarshal(file, OadrCreateOptType.class);
		} catch (Oadr20bUnmarshalException e) {
			assertion = true;
		}
		assertTrue(assertion);

	}

	@Test
	public void unmarshalMarshalValidatingTest() throws Oadr20bUnmarshalException, Oadr20bMarshalException {
		File file = new File("src/test/resources/eiopt/oadrCreateOpt.xml");
		OadrCreateOptType unmarshal = jaxbContext.unmarshal(file, OadrCreateOptType.class);
		assertEquals("Opt_1234", unmarshal.getOptID());
		assertEquals(OptTypeType.OPT_IN, unmarshal.getOptType());
		assertEquals("participating", unmarshal.getOptReason());
		assertEquals("VEN_3214", unmarshal.getVenID());
		assertEquals("2001-12-17T09:30:47Z", unmarshal.getCreatedDateTime().toString());
		assertEquals("REQ_12343", unmarshal.getRequestID());
		assertEquals("Event_12345", unmarshal.getQualifiedEventID().getEventID());
		assertEquals(1L, unmarshal.getQualifiedEventID().getModificationNumber());
		assertEquals(1, unmarshal.getEiTarget().getVenID().size());
		assertEquals("VEN_1234", unmarshal.getEiTarget().getVenID().get(0));
		assertEquals(4, unmarshal.getEiTarget().getResourceID().size());
		assertEquals("RES_123534", unmarshal.getEiTarget().getResourceID().get(0));
		assertEquals("RES_234523", unmarshal.getEiTarget().getResourceID().get(1));
		assertEquals("RES_080789", unmarshal.getEiTarget().getResourceID().get(2));
		assertEquals("RES_123567", unmarshal.getEiTarget().getResourceID().get(3));
		assertEquals(1, unmarshal.getOadrDeviceClass().getEndDeviceAsset().size());
		assertEquals("Exterior_Lighting", unmarshal.getOadrDeviceClass().getEndDeviceAsset().get(0).getMrid());

		File file2 = new File("src/test/resources/eiopt/genOadrCreateOpt.xml");
		jaxbContext.marshal(Oadr20bFactory.createOadrCreateOpt(unmarshal), file2);
		assertTrue(file2.exists());
		file2.delete();

	}

	@Test
	public void validCreateEventOpt() throws Oadr20bMarshalException {
		String requestId = "requestId";
		String optId = "optId";
		String venId = "venId";
		Long createdDatetime = 0L;
		String eventId = "eventId";
		long modificationNumber = 0L;
		OptTypeType optType = OptTypeType.OPT_IN;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.OVERRIDE_STATUS;
		OadrCreateOptType request = Oadr20bEiOptBuilders
				.newOadr20bCreateOptBuilder(requestId, venId, createdDatetime, eventId, modificationNumber, optId,
						optType, optReason)
				.addTargetedResource("res1").withMarketContext("http://oadr.avob.com")
				.withOadrDeviceClass(Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder()
						.addEndDeviceAsset(Oadr20bFactory
								.createEndDeviceAssetType(EndDeviceAssertMridType.ENERGY_MANAGEMENT_SYSTEM))
						.build())
				.build();
		jaxbContext.marshalRoot(request);
	}

	@Test
	public void validCreateVavailabilyOpt() throws Oadr20bMarshalException {
		String requestId = "requestId";
		String optId = "optId";
		String venId = "venId";
		Long createdDatetime = 0L;
		OptTypeType optType = OptTypeType.OPT_IN;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.OVERRIDE_STATUS;
		Long start = System.currentTimeMillis();
		String duration = "P1D";
		VavailabilityType vavailabilityType = Oadr20bEiOptBuilders.newOadr20bVavailabilityBuilder()
				.addPeriod(start, duration).build();
		OadrCreateOptType request = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, venId, createdDatetime,
				vavailabilityType, optId, optType, optReason).build();

		jaxbContext.marshalRoot(request);
	}
}
