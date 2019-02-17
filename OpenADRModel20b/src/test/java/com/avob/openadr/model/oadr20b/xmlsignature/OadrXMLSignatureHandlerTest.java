package com.avob.openadr.model.oadr20b.xmlsignature;

import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bPollBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.IntervalType;
import com.avob.openadr.model.oadr20b.ei.OptReasonEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.OptTypeType;
import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportRequestType;
import com.avob.openadr.model.oadr20b.oadr.OadrReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrTransportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureType;
import com.avob.openadr.model.oadr20b.oadr.TemperatureUnitType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;
import com.avob.openadr.model.oadr20b.xcal.WsCalendarIntervalType;
import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXMLSignatureHandlerTest {

    private static final String privateKeyFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_privkey.pem";
    private static final String certificateFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_cert.pem";

    private PrivateKey privateKey;
    private X509Certificate certificate;
    private Oadr20bJAXBContext jaxbContext;

    public OadrXMLSignatureHandlerTest() throws OadrSecurityException {
        privateKey = OadrHttpSecurity.parsePrivateKey(privateKeyFilePath);
        certificate = OadrHttpSecurity.parseCertificate(certificateFilePath);
        try {
            jaxbContext = Oadr20bJAXBContext.getInstance();
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void validate(String payload) throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        OadrPayload unmarshal = jaxbContext.unmarshal(payload, OadrPayload.class, true);
        OadrXMLSignatureHandler.validate(payload, unmarshal, 0, 10);
    }

    @Test
    public void testInvalidReplayProtectTimestamp() {
        OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder("REQ_12345", 200, "venId")
                .build();
        boolean exception = false;
        try {
            validate(OadrXMLSignatureHandler.sign(response, privateKey, certificate, "nonce", -10L));
        } catch (Oadr20bUnmarshalException e) {
        } catch (Oadr20bXMLSignatureValidationException e) {
            exception = true;
        } catch (Oadr20bXMLSignatureException e) {
        }
        assertTrue(exception);
    }

    @Test
    public void testOadrResponseType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder("REQ_12345", 200, "venId")
                .build();
        validate(OadrXMLSignatureHandler.sign(response, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testCreatedEventType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String venId = "venId";
        String requestId = "requestId";
        int responseCode = 200;
        String eventId = "eventId";
        long modificationNumber = 0L;
        OadrCreatedEventType request = Oadr20bEiEventBuilders.newCreatedEventBuilder(venId, requestId, responseCode)
                .addEventResponse(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId,
                        modificationNumber, requestId, responseCode, OptTypeType.OPT_IN).build())
                .build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrDistributeEventType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        long timestampStart = 0L;
        String eventXmlDuration = "PT1H";
        String toleranceXmlDuration = "PT5M";
        String notificationXmlDuration = "P1D";
        EiActivePeriodType period = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
                eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

        float currentValue = 3f;
        List<Float> l = new ArrayList<Float>();
        l.add(currentValue);
        String xmlDuration = "PT1H";
        String signalId = "0";
        String signalName = "simple";
        SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
        String intervalId = "";
        long timestampStartInterval = 3L;
        EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
                .newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
                .addInterval(Oadr20bEiBuilders
                        .newOadr20bSignalIntervalTypeBuilder(intervalId, timestampStartInterval, xmlDuration, l)
                        .build())
                .build();

        String[] list = { "a", "b", "c" };
        EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("groupId")
                .addGroupId(Arrays.asList(list)).addPartyId("partyId").addPartyId(Arrays.asList(list))
                .addResourceId("resourceId").addResourceId(Arrays.asList(list)).addVenId("venId")
                .addVenId(Arrays.asList(list)).build();

        long datetime = System.currentTimeMillis();
        String marketContextValue = "";
        String eventId = "";
        long modificationNumber = 0L;
        Long priority = 0L;
        EventStatusEnumeratedType status = EventStatusEnumeratedType.ACTIVE;
        String comment = "";
        EventDescriptorType descriptor = Oadr20bEiEventBuilders
                .newOadr20bEventDescriptorTypeBuilder(datetime, eventId, modificationNumber, marketContextValue, status)
                .withPriority(priority).withVtnComment(comment).build();

        OadrEvent event = Oadr20bEiEventBuilders.newOadr20bDistributeEventOadrEventBuilder().withActivePeriod(period)
                .withEiTarget(target).withEventDescriptor(descriptor).addEiEventSignal(eiEventSignal)
                .withResponseRequired(true).build();

        String requestId = "";
        String vtnId = "";

        OadrDistributeEventType createOadrDistributeEvent = Oadr20bEiEventBuilders
                .newOadr20bDistributeEventBuilder(vtnId, requestId).addOadrEvent(event)
                .withEiResponse(Oadr20bResponseBuilders.newOadr20bEiResponseBuilder("requestId", 200).build()).build();

        validate(OadrXMLSignatureHandler.sign(createOadrDistributeEvent, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrRequestEventType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        OadrRequestEventType request = Oadr20bEiEventBuilders.newOadrRequestEventBuilder("venId", "requestId").build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCanceledOptType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String requestId = "requestId";
        int responseCode = 200;
        String optId = "optId";
        OadrCanceledOptType request = Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(requestId, responseCode, optId)
                .build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCancelOptType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String requestId = "requestId";
        String optId = "optId";
        String venId = "venId";
        OadrCancelOptType request = Oadr20bEiOptBuilders.newOadr20bCancelOptBuilder(requestId, optId, venId).build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreatedOptType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String requestId = "requestId";
        String optId = "optId";
        int responseCode = 200;
        OadrCreatedOptType request = Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(requestId, responseCode, optId)
                .build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreateOptType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String requestId = "requestId";
        String optId = "optId";
        String venId = "venId";
        Long createdDatetime = 0L;
        String eventId = "eventId";
        long modificationNumber = 0L;
        OptTypeType optType = OptTypeType.OPT_IN;
        OptReasonEnumeratedType optReason = OptReasonEnumeratedType.OVERRIDE_STATUS;
        OadrCreateOptType request = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(requestId, venId, createdDatetime,
                eventId, modificationNumber, optId, optType, optReason)
                .withOptReason(OptReasonEnumeratedType.NOT_PARTICIPATING).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCanceledPartyRegistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String requestId = "requestId";
        int responseCode = 200;
        String registrationId = "registrationId";
        String venId = "venId";
        OadrCanceledPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
                .newOadr20bCanceledPartyRegistrationBuilder(requestId, responseCode, registrationId, venId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCancelPartyRegistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String registrationId = "registrationId";
        String venId = "venId";
        OadrCancelPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
                .newOadr20bCancelPartyRegistrationBuilder(requestId, registrationId, venId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreatedPartyRegistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String registrationId = "registrationId";
        int responseCode = 200;
        String vtnId = "vtn";
        String venId = "ven";
        OadrCreatedPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
                .newOadr20bCreatedPartyRegistrationBuilder(requestId, responseCode, venId, vtnId)
                .addOadrProfile(Oadr20bEiRegisterPartyBuilders.newOadr20bOadrProfileBuilder("2.0b")
                        .addTransport(OadrTransportType.SIMPLE_HTTP).build())
                .withRegistrationId(registrationId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreatePartyRegistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String venId = "ven";
        String profilName = "2.0b";
        OadrCreatePartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
                .newOadr20bCreatePartyRegistrationBuilder(requestId, venId, profilName).withOadrReportOnly(true)
                .withOadrTransportName(OadrTransportType.XMPP).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrOadrQueryRegistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        OadrQueryRegistrationType request = Oadr20bEiRegisterPartyBuilders.newOadr20bQueryRegistrationBuilder(requestId)
                .build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrRequestReregistrationType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String venId = "venId";
        OadrRequestReregistrationType request = Oadr20bEiRegisterPartyBuilders
                .newOadr20bRequestReregistrationBuilder(venId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCanceledReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        int responseCode = 200;
        String venId = "venId";
        OadrCanceledReportType request = Oadr20bEiReportBuilders
                .newOadr20bCanceledReportBuilder(requestId, responseCode, venId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCancelReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String venId = "venId";
        boolean reportToFollow = true;
        String reportId = "reportId";
        OadrCancelReportType request = Oadr20bEiReportBuilders
                .newOadr20bCancelReportBuilder(requestId, venId, reportToFollow).addReportRequestId(reportId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreatedReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String venId = "venId";
        String reportId = "reportId";
        int responseCode = 200;
        OadrCreatedReportType request = Oadr20bEiReportBuilders
                .newOadr20bCreatedReportBuilder(requestId, responseCode, venId).addPendingReportRequestId(reportId)
                .build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrCreateReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String reportRequestId = "reportRequestId";
        String reportSpecifierId = "reportSpecifierId";
        String granularity = "PT1H";
        String reportBackDuration = "PT12H";
        WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
                "PT1H");

        TemperatureType temperature = Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS,
                SiScaleCodeType.KILO);

        OadrReportRequestType reportRequest = Oadr20bEiReportBuilders
                .newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
                .addSpecifierPayload(Oadr20bFactory.createTemperature(temperature),
                        ReadingTypeEnumeratedType.DIRECT_READ, "rid")
                .withWsCalendarIntervalType(calendar).build();

        String requestId = "requestId";
        String venId = "venId";
        OadrCreateReportType request = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(requestId, venId)
                .addReportRequest(reportRequest).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrRegisteredReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String reportRequestId = "reportRequestId";
        String reportSpecifierId = "reportSpecifierId";
        String granularity = "PT1H";
        String reportBackDuration = "PT12H";
        WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
                "PT1H");

        TemperatureType temperature = Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS,
                SiScaleCodeType.KILO);

        OadrReportRequestType reportRequest = Oadr20bEiReportBuilders
                .newOadr20bReportRequestTypeBuilder(reportRequestId, reportSpecifierId, granularity, reportBackDuration)
                .addSpecifierPayload(Oadr20bFactory.createTemperature(temperature),
                        ReadingTypeEnumeratedType.DIRECT_READ, "rid")
                .withWsCalendarIntervalType(calendar).build();

        String requestId = "requestId";
        String venId = "venId";
        int responseCode = 200;
        OadrRegisteredReportType request = Oadr20bEiReportBuilders
                .newOadr20bRegisteredReportBuilder(requestId, responseCode, venId).addReportRequest(reportRequest)

                .build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrRegisterReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String venId = "venId";
        String reportRequestId = "reportRequestId";
        OadrRegisterReportType request = Oadr20bEiReportBuilders
                .newOadr20bRegisterReportBuilder(requestId, venId, reportRequestId).build();

        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrUpdatedReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String requestId = "requestId";
        String venId = "venId";
        int responseCode = 200;
        OadrCancelReportType oadrCancelReport = null;
        OadrUpdatedReportType request = Oadr20bEiReportBuilders
                .newOadr20bUpdatedReportBuilder(requestId, responseCode, venId).withOadrCancelReport(oadrCancelReport)
                .build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrUpdateReportType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

        String intervalId = "intervalId";
        long start = 3L;
        String xmlDuration = "PT1H";
        Float value = 3f;
        String rid = "rid";
        Long confidence = 1L;
        Float accuracy = 1F;
        IntervalType interval = Oadr20bEiBuilders
                .newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration, rid, confidence, accuracy, value)
                .build();

        String reportId = "reportId";
        String reportrequestId = "reportrequestId";
        String reportSpecifierId = "reportSpecifierId";
        ReportNameEnumeratedType reportName = ReportNameEnumeratedType.TELEMETRY_STATUS;
        long createdTimestamp = 12L;
        long startTimestamp = 12L;
        String duration = "PT1H";
        OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(reportId,
                reportrequestId, reportSpecifierId, reportName, createdTimestamp, startTimestamp, duration)
                .addInterval(interval).build();

        String requestId = "requestId";
        String venId = "venId";
        OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
                .addReport(report).build();
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

    @Test
    public void testOadrPollType()
            throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
        String venId = "venId";
        OadrPollType request = Oadr20bPollBuilders.newOadr20bPollBuilder(venId).build();
        
        validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, "nonce", 0L));
    }

}
