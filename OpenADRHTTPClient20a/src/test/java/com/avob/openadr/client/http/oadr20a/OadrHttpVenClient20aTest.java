package com.avob.openadr.client.http.oadr20a;

import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.avob.openadr.client.http.oadr20a.ven.OadrHttpVenClient20a;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.oadr.OadrCreatedEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrRequestEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpVenClient20aTest {

	@Test
	public void testOadrRequestEvent() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
			OadrSecurityException, JAXBException, URISyntaxException, Oadr20aException, Oadr20aHttpLayerException {

		OadrHttpClient20a oadrHttpClient20a = Mockito.mock(OadrHttpClient20a.class);

		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType eiActivePeriod = Oadr20aBuilders.newOadr20aEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		String signalId = "0";
		String signalName = "simple";
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		float currentValue = 0;

		EiEventSignalType eiEventSignalType = Oadr20aBuilders
				.newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue).build();

		String venId = "ven1";
		EiTargetType eiTarget = Oadr20aBuilders.newOadr20aEiTargetTypeBuilder().addVenId(venId).build();

		Long createdTimespamp = 0L;
		String eventId = "0";
		long modificationNumber = 0L;
		String marketContext = "";
		EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
		EventDescriptorType eventDescriptor = Oadr20aBuilders.newOadr20aEventDescriptorTypeBuilder(createdTimespamp,
				eventId, modificationNumber, marketContext, status).build();

		OadrEvent oadrEvent = Oadr20aBuilders.newOadr20aDistributeEventOadrEventBuilder()
				.withActivePeriod(eiActivePeriod).addEiEventSignal(eiEventSignalType)
				.addEiEventSignal( Arrays.asList(eiEventSignalType)).withEiTarget(eiTarget)
				.withEventDescriptor(eventDescriptor).build();

		OadrDistributeEvent mockDistributeEvent = Oadr20aBuilders.newOadr20aDistributeEventBuilder("", "")
				.addOadrEvent(oadrEvent).build();

		oadrHttpClient20a = Mockito.mock(OadrHttpClient20a.class);

		OadrHttpVenClient20a oadrHttpVenClient20a = new OadrHttpVenClient20a(oadrHttpClient20a);

		when(oadrHttpClient20a.<OadrDistributeEvent>post(Matchers.<OadrRequestEvent>anyObject(), Matchers.any(),
				Matchers.any())).thenReturn(mockDistributeEvent);

		OadrRequestEvent requestEvent = new OadrRequestEvent();
		oadrHttpVenClient20a.oadrRequestEvent(requestEvent);
	}

	@Test
	public void testOadrCreatedEvent()
			throws OadrSecurityException, JAXBException, UnrecoverableKeyException, NoSuchAlgorithmException,
			KeyStoreException, URISyntaxException, Oadr20aException, Oadr20aHttpLayerException {

		OadrHttpClient20a oadrHttpClient20a = Mockito.mock(OadrHttpClient20a.class);

		OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", HttpStatus.SC_OK).build();

		OadrHttpVenClient20a oadrHttpVenClient20aTestClass = new OadrHttpVenClient20a(oadrHttpClient20a);

		when(oadrHttpClient20a.<OadrResponse>post(Matchers.<OadrCreatedEvent>anyObject(), Matchers.any(),
				Matchers.any())).thenReturn(mockOadrResponse);

		OadrCreatedEvent createdEvent = new OadrCreatedEvent();
		oadrHttpVenClient20aTestClass.oadrCreatedEvent(createdEvent);

	}
}
