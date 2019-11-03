package com.avob.openadr.server.oadr20b.vtn.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.server.common.vtn.models.ven.Ven;
import com.avob.openadr.server.common.vtn.service.VenService;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VenPollServiceTest {

	@Resource
	private VenService venService;

	@Resource
	private VenPollService venPollService;

	private Oadr20bJAXBContext jaxbContext;

	public VenPollServiceTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	@Test
	public void crudTest() throws Oadr20bMarshalException {
		Ven ven = venService.findOneByUsername(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG);

		String retrievePollForVenUsername = venPollService.retrievePollForVenUsername(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG);

		assertNull(retrievePollForVenUsername);

		OadrRequestReregistrationType oadrRequestReregistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG).build();

		String oadrRequestReregistrationTypeStr = jaxbContext
				.marshal(Oadr20bFactory.createOadrRequestReregistration(oadrRequestReregistrationType));

		venPollService.create(ven, oadrRequestReregistrationTypeStr);

		String requestId = "requestId";
		String registrationId = "registrationId";
		String venId = "venId";
		OadrCancelPartyRegistrationType oadrCancelPartyRegistrationType = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, venId).build();

		String oadrCancelPartyRegistrationTypeStr = jaxbContext
				.marshal(Oadr20bFactory.createOadrCancelPartyRegistration(oadrCancelPartyRegistrationType));

		venPollService.create(ven, oadrCancelPartyRegistrationTypeStr);

		retrievePollForVenUsername = venPollService.retrievePollForVenUsername(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG);

		assertNotNull(retrievePollForVenUsername);

		assertEquals(oadrRequestReregistrationTypeStr, retrievePollForVenUsername);

		retrievePollForVenUsername = venPollService.retrievePollForVenUsername(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG);

		assertNotNull(retrievePollForVenUsername);

		assertEquals(oadrCancelPartyRegistrationTypeStr, retrievePollForVenUsername);

		retrievePollForVenUsername = venPollService.retrievePollForVenUsername(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG);

		assertNull(retrievePollForVenUsername);

	}
}
