package com.avob.openadr.client.http.oadr20b;

import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.avob.openadr.client.http.oadr20b.ven.OadrHttpVenClient20b;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpVenClient20bTest {

    @Test
    public void testOadrRequestEvent() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            OadrSecurityException, JAXBException, URISyntaxException, Oadr20bException, Oadr20bHttpLayerException,
            Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

        OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

        long timestampStart = 0L;
        String eventXmlDuration = "PT1H";
        String toleranceXmlDuration = "PT5M";
        String notificationXmlDuration = "P1D";
        EiActivePeriodType eiActivePeriod = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
                eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

        String signalId = "0";
        String signalName = "simple";
        SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
        float currentValue = 0;

        EiEventSignalType eiEventSignalType = Oadr20bEiEventBuilders
                .newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue).build();

        String venId = "ven1";
        EiTargetType eiTarget = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addVenId(venId).build();

        Long createdTimespamp = 0L;
        String eventId = "0";
        long modificationNumber = 0L;
        String marketContext = "";
        EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
        EventDescriptorType eventDescriptor = Oadr20bEiEventBuilders.newOadr20bEventDescriptorTypeBuilder(
                createdTimespamp, eventId, modificationNumber, marketContext, status).build();

        OadrEvent oadrEvent = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder()
                .withActivePeriod(eiActivePeriod).addEiEventSignal(eiEventSignalType).withEiTarget(eiTarget)
                .withEventDescriptor(eventDescriptor).build();

        OadrDistributeEventType mockDistributeEvent = Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("", "")
                .addOadrEvent(oadrEvent).build();

        OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

        OadrHttpVenClient20b OadrHttpVenClient20b = new OadrHttpVenClient20b(OadrHttpClient20b);

        when(OadrHttpClient20b.<OadrDistributeEventType, JAXBElement<OadrRequestEventType>>post(
                Matchers.<JAXBElement<OadrRequestEventType>>anyObject(), Matchers.any(), Matchers.any()))
                        .thenReturn(mockDistributeEvent);

        OadrRequestEventType requestEvent = new OadrRequestEventType();
        OadrHttpVenClient20b.oadrRequestEvent(requestEvent);
    }

    @Test
    public void testOadrCreatedEventType() throws OadrSecurityException, JAXBException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException, URISyntaxException, Oadr20bException,
            Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

        OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

        OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
                .newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();

        OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

        when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrCreatedEventType>>post(
                Matchers.<JAXBElement<OadrCreatedEventType>>anyObject(), Matchers.any(), Matchers.any()))
                        .thenReturn(mockOadrResponseType);

        OadrCreatedEventType createdEvent = new OadrCreatedEventType();
        OadrHttpVenClient20bTestClass.oadrCreatedEvent(createdEvent);

    }
}
