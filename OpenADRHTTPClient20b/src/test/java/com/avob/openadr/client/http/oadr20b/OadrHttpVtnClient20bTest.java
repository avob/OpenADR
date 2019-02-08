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

import com.avob.openadr.client.http.oadr20b.vtn.OadrHttpVtnClient20b;
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
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpVtnClient20bTest {

    @Test
    public void testOadrDistributeEvent() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException,
            OadrSecurityException, JAXBException, Oadr20bException, URISyntaxException, Oadr20bHttpLayerException,
            Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

        OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

        OadrHttpVtnClient20b OadrHttpVtnClient20b = new OadrHttpVtnClient20b(OadrHttpClient20b);

        OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
                .newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();

        when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrDistributeEventType>>post(
                Matchers.<JAXBElement<OadrDistributeEventType>>anyObject(), Matchers.any(), Matchers.any()))
                        .thenReturn(mockOadrResponseType);

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

        OadrHttpVtnClient20b.oadrDistributeEvent(mockDistributeEvent);

        OadrHttpVtnClient20b.oadrDistributeEvent("http://localhost:8080", mockDistributeEvent);

    }
}
