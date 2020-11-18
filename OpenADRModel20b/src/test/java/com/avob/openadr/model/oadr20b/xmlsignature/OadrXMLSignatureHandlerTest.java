package com.avob.openadr.model.oadr20b.xmlsignature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.dom.DOMResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.avob.AvobVenServiceRequestType;
import com.avob.openadr.model.oadr20b.avob.KeyTokenType;
import com.avob.openadr.model.oadr20b.avob.PayloadAvobVenServiceRequestType;
import com.avob.openadr.model.oadr20b.avob.PayloadKeyTokenType;
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
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
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
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateType;
import com.avob.openadr.model.oadr20b.oadr.OadrLoadControlStateTypeType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPayloadResourceStatusType;
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
import com.avob.openadr.model.oadr20b.xmldsig.SignatureValueType;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXMLSignatureHandlerTest {

	private static final String RID_ID = "rid";
	private static final String REPORT_SPECIFIER_ID = "reportSpecifierId";
	private static final String REPORT_REQUEST_ID = "reportRequestId";
	private static final String REPORT_ID = "reportId";
	private static final String OADR20b_PROFILE_NAME = "2.0b";
	private static final String VTN_ID = "vtn";
	private static final String REGISTRATION_ID = "registrationId";
	private static final String EVENT_ID = "eventId";
	private static final String OPT_ID = "optId";
	private static final String NONCE = "nonce";
	private static final String VEN_ID = "venId";
	private static final String REQUEST_ID = "REQ_12345";
	private static final String PRIVATE_KEY_FILE_PATH = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_privkey.pem";
	private static final String CERTIFICATE_FILE_PATH = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_cert.pem";

	private PrivateKey privateKey;
	private X509Certificate certificate;
	private Oadr20bJAXBContext jaxbContext;

	public OadrXMLSignatureHandlerTest() throws OadrSecurityException, JAXBException {
		privateKey = OadrPKISecurity.parsePrivateKey(PRIVATE_KEY_FILE_PATH);
		certificate = OadrPKISecurity.parseCertificate(CERTIFICATE_FILE_PATH);
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	private void validate(String payload) throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrPayload unmarshal = jaxbContext.unmarshal(payload, OadrPayload.class, true);
		OadrXMLSignatureHandler.validate(payload, unmarshal, 0, 10);
	}

	@Test
	public void testInvalidCreatedTimestamp() throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		boolean exception = false;
		try {
			validate(OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, -10L));
		} catch (Oadr20bXMLSignatureException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void testInvalidReplayProtectTimestamp() throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException {
		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		boolean exception = false;
		try {
			String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);
			OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
			OadrXMLSignatureHandler.validate(sign, unmarshal, 0, -10);
		} catch (Oadr20bXMLSignatureValidationException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	private String oadrPayloadToString(OadrPayload payload) throws Oadr20bXMLSignatureException {
		DOMResult res = new DOMResult();
		try {
			jaxbContext.marshal(payload, res);
		} catch (Oadr20bMarshalException e) {
			throw new Oadr20bXMLSignatureException(e);
		}
		Document doc = (Document) res.getNode();
		DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();
		serializer.getDomConfig().setParameter("xml-declaration", Boolean.FALSE);
		LSOutput lsOutput = domImplLS.createLSOutput();
		// set utf8 xml prolog
		lsOutput.setEncoding("UTF-8");
		Writer stringWriter = new StringWriter();
		lsOutput.setCharacterStream(stringWriter);
		serializer.write(doc, lsOutput);
		return stringWriter.toString().replaceAll("\n", "");
	}

	@Test
	public void testInvalidSign() {

		boolean exception = false;
		try {
			OadrXMLSignatureHandler.sign("moauiccool", privateKey, certificate, NONCE, 0L);
		} catch (Oadr20bXMLSignatureException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void testInvalidValidate() throws Oadr20bXMLSignatureException {

		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);

		boolean exception = false;
		try {
			OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
			OadrXMLSignatureHandler.validate("mouaiccool", unmarshal, 0, -10);
		} catch (Oadr20bXMLSignatureValidationException | Oadr20bUnmarshalException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void testNoSignature() throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException {

		OadrRequestEventType oadrRequestEventType = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder("ven1", "requestId").withReplyLimit(12).build();
		OadrPayload createOadrPayload = Oadr20bFactory.createOadrPayload("mypayload", oadrRequestEventType);

		String content = oadrPayloadToString(createOadrPayload);

		boolean exception = false;
		try {
			OadrPayload unmarshal = jaxbContext.unmarshal(content, OadrPayload.class, true);
			OadrXMLSignatureHandler.validate(content, unmarshal, 0, -10);
		} catch (Oadr20bXMLSignatureValidationException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void testInvalidSignature() throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException {

		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		SignatureValueType value = new SignatureValueType();
		value.setId("myid");
		value.setValue("mouaiccool".getBytes());
		unmarshal.getSignature().setSignatureValue(value);
		String content = oadrPayloadToString(unmarshal);

		boolean exception = false;
		try {
			unmarshal = jaxbContext.unmarshal(content, OadrPayload.class, true);
			OadrXMLSignatureHandler.validate(content, unmarshal, 0, -10);
		} catch (Oadr20bXMLSignatureValidationException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void testInvalidReference() throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException {

		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		unmarshal.getSignature().getObject().clear();
		String content = oadrPayloadToString(unmarshal);

		boolean exception = false;
		try {
			unmarshal = jaxbContext.unmarshal(content, OadrPayload.class, true);
			OadrXMLSignatureHandler.validate(content, unmarshal, 0, -10);
		} catch (Oadr20bXMLSignatureValidationException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void testOadrResponseType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrResponseType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrResponseType.class);
		assertNotNull(signedObjectFromOadrPayload);

	}

	@Test
	public void testOadrResponseType_InvalidSignature()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrResponseType response = Oadr20bResponseBuilders.newOadr20bResponseBuilder(REQUEST_ID, 200, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(response, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrResponseType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrResponseType.class);
		assertNotNull(signedObjectFromOadrPayload);

	}

	@Test
	public void testCreatedEventType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		String venId = VEN_ID;
		int responseCode = 200;
		String eventId = EVENT_ID;
		long modificationNumber = 0L;
		OadrCreatedEventType request = Oadr20bEiEventBuilders
				.newCreatedEventBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseOK(REQUEST_ID), venId)
				.addEventResponse(Oadr20bEiEventBuilders.newOadr20bCreatedEventEventResponseBuilder(eventId,
						modificationNumber, REQUEST_ID, responseCode, OptTypeType.OPT_IN).build())
				.build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreatedEventType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCreatedEventType.class);
		assertNotNull(signedObjectFromOadrPayload);
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
		String xmlDuration = "PT1H";
		String signalId = "0";
		SignalNameEnumeratedType signalName = SignalNameEnumeratedType.SIMPLE;
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String intervalId = "";
		long timestampStartInterval = 3L;
		EiEventSignalType eiEventSignal = Oadr20bEiEventBuilders
				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(Oadr20bEiBuilders.newOadr20bSignalIntervalTypeBuilder(intervalId, timestampStartInterval,
						xmlDuration, currentValue).build())
				.build();

		String[] list = { "a", "b", "c" };
		EiTargetType target = Oadr20bEiBuilders.newOadr20bEiTargetTypeBuilder().addGroupId("groupId")
				.addGroupId(Arrays.asList(list)).addPartyId("partyId").addPartyId(Arrays.asList(list))
				.addResourceId("resourceId").addResourceId(Arrays.asList(list)).addVenId(VEN_ID)
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

		String sign = OadrXMLSignatureHandler.sign(createOadrDistributeEvent, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrDistributeEventType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrDistributeEventType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrRequestEventType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrRequestEventType request = Oadr20bEiEventBuilders.newOadrRequestEventBuilder(VEN_ID, "requestId").build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrRequestEventType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrRequestEventType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCanceledOptType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		int responseCode = 200;
		String optId = OPT_ID;
		OadrCanceledOptType request = Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder(REQUEST_ID, responseCode, optId)
				.build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCanceledOptType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCanceledOptType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCancelOptType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrCancelOptType request = Oadr20bEiOptBuilders.newOadr20bCancelOptBuilder(REQUEST_ID, OPT_ID, VEN_ID).build();
		validate(OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L));
	}

	@Test
	public void testOadrCreatedOptType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		int responseCode = 200;
		OadrCreatedOptType request = Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder(REQUEST_ID, responseCode, OPT_ID)
				.build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreatedOptType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCreatedOptType.class);
		assertNotNull(signedObjectFromOadrPayload);

	}

	@Test
	public void testOadrCreateOptType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		Long createdDatetime = 0L;
		long modificationNumber = 0L;
		OptTypeType optType = OptTypeType.OPT_IN;
		OptReasonEnumeratedType optReason = OptReasonEnumeratedType.OVERRIDE_STATUS;
		OadrCreateOptType request = Oadr20bEiOptBuilders.newOadr20bCreateOptBuilder(REQUEST_ID, VEN_ID, createdDatetime,
				EVENT_ID, modificationNumber, OPT_ID, optType, optReason)
				.withOptReason(OptReasonEnumeratedType.NOT_PARTICIPATING).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreateOptType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCreateOptType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCanceledPartyRegistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrCanceledPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder(Oadr20bResponseBuilders.newOadr20bEiResponseOK(REQUEST_ID),
						REGISTRATION_ID, VEN_ID)
				.build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCanceledPartyRegistrationType signedObjectFromOadrPayload = Oadr20bFactory
				.getSignedObjectFromOadrPayload(unmarshal, OadrCanceledPartyRegistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCancelPartyRegistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrCancelPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCancelPartyRegistrationBuilder(REQUEST_ID, REGISTRATION_ID, VEN_ID).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCancelPartyRegistrationType signedObjectFromOadrPayload = Oadr20bFactory
				.getSignedObjectFromOadrPayload(unmarshal, OadrCancelPartyRegistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCreatedPartyRegistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		int responseCode = 200;
		OadrCreatedPartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder(
						Oadr20bResponseBuilders.newOadr20bEiResponseBuilder(REQUEST_ID, responseCode).build(), VEN_ID,
						VTN_ID)
				.addOadrProfile(Oadr20bEiRegisterPartyBuilders.newOadr20bOadrProfileBuilder(OADR20b_PROFILE_NAME)
						.addTransport(OadrTransportType.SIMPLE_HTTP).build())
				.withRegistrationId(REGISTRATION_ID).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreatedPartyRegistrationType signedObjectFromOadrPayload = Oadr20bFactory
				.getSignedObjectFromOadrPayload(unmarshal, OadrCreatedPartyRegistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCreatePartyRegistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrCreatePartyRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder(REQUEST_ID, VEN_ID, OADR20b_PROFILE_NAME)
				.withOadrReportOnly(true).withOadrTransportName(OadrTransportType.XMPP).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreatePartyRegistrationType signedObjectFromOadrPayload = Oadr20bFactory
				.getSignedObjectFromOadrPayload(unmarshal, OadrCreatePartyRegistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrOadrQueryRegistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrQueryRegistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bQueryRegistrationBuilder(REQUEST_ID).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrQueryRegistrationType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrQueryRegistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrRequestReregistrationType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrRequestReregistrationType request = Oadr20bEiRegisterPartyBuilders
				.newOadr20bRequestReregistrationBuilder(VEN_ID).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrRequestReregistrationType signedObjectFromOadrPayload = Oadr20bFactory
				.getSignedObjectFromOadrPayload(unmarshal, OadrRequestReregistrationType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCanceledReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		int responseCode = 200;
		OadrCanceledReportType request = Oadr20bEiReportBuilders
				.newOadr20bCanceledReportBuilder(REQUEST_ID, responseCode, VEN_ID).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCanceledReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCanceledReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCancelReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		boolean reportToFollow = true;
		OadrCancelReportType request = Oadr20bEiReportBuilders
				.newOadr20bCancelReportBuilder(REQUEST_ID, VEN_ID, reportToFollow).addReportRequestId(REPORT_ID)
				.build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCancelReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCancelReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCreatedReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		int responseCode = 200;
		OadrCreatedReportType request = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(REQUEST_ID, responseCode, VEN_ID).addPendingReportRequestId(REPORT_ID)
				.addPendingReportRequestId(Arrays.asList(REPORT_ID)).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreatedReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCreatedReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrCreateReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String granularity = "PT1H";
		String reportBackDuration = "PT12H";
		WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
				"PT1H");

		TemperatureType temperature = Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS,
				SiScaleCodeType.KILO);

		OadrReportRequestType reportRequest = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(REPORT_REQUEST_ID, REPORT_SPECIFIER_ID, granularity,
						reportBackDuration)
				.addSpecifierPayload(Oadr20bFactory.createTemperature(temperature),
						ReadingTypeEnumeratedType.DIRECT_READ, RID_ID)
				.withWsCalendarIntervalType(calendar).build();

		OadrCreateReportType request = Oadr20bEiReportBuilders.newOadr20bCreateReportBuilder(REQUEST_ID, VEN_ID)
				.addReportRequest(reportRequest).addReportRequest(Arrays.asList(reportRequest)).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrCreateReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrCreateReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrRegisteredReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String granularity = "PT1H";
		String reportBackDuration = "PT12H";
		WsCalendarIntervalType calendar = Oadr20bFactory.createWsCalendarIntervalType(System.currentTimeMillis(),
				"PT1H");

		TemperatureType temperature = Oadr20bFactory.createTemperatureType(TemperatureUnitType.CELSIUS,
				SiScaleCodeType.KILO);

		OadrReportRequestType reportRequest = Oadr20bEiReportBuilders
				.newOadr20bReportRequestTypeBuilder(REPORT_REQUEST_ID, REPORT_SPECIFIER_ID, granularity,
						reportBackDuration)
				.addSpecifierPayload(Oadr20bFactory.createTemperature(temperature),
						ReadingTypeEnumeratedType.DIRECT_READ, RID_ID)
				.withWsCalendarIntervalType(calendar).build();

		int responseCode = 200;
		OadrRegisteredReportType request = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder(REQUEST_ID, responseCode, VEN_ID).addReportRequest(reportRequest)
				.build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrRegisteredReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrRegisteredReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrRegisterReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		OadrRegisterReportType request = Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(REQUEST_ID, VEN_ID)
				.build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrRegisterReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrRegisterReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrUpdatedReportType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String requestId = "requestId";
		String venId = VEN_ID;
		int responseCode = 200;
		OadrCancelReportType oadrCancelReport = null;
		OadrUpdatedReportType request = Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder(requestId, responseCode, venId).withOadrCancelReport(oadrCancelReport)
				.build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrUpdatedReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrUpdatedReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrUpdateReportTypeFloatPayload()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String intervalId = "intervalId";
		long start = 3L;
		String xmlDuration = "PT1H";
		Float value = 3f;
		Long confidence = 1L;
		Float accuracy = 1F;
		IntervalType interval = Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration,
				RID_ID, confidence, accuracy, value).build();

		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.TELEMETRY_STATUS;
		long createdTimestamp = 12L;
		long startTimestamp = 12L;
		String duration = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(REPORT_ID,
				REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName, createdTimestamp, startTimestamp, duration)
				.addInterval(interval).build();

		String requestId = "requestId";
		String venId = VEN_ID;
		OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
				.addReport(report).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrUpdateReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrUpdateReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrUpdateReportTypeKeyTokenPayload()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String intervalId = "intervalId";
		long start = 3L;
		String xmlDuration = "PT1H";
		Long confidence = 1L;
		Float accuracy = 1F;

		List<KeyTokenType> tokens = new ArrayList<>();
		KeyTokenType keyTokenType = new KeyTokenType();
		keyTokenType.setKey("mouaiccool");
		keyTokenType.setValue("mouaiccool");
		tokens.add(keyTokenType);
		PayloadKeyTokenType value = Oadr20bFactory.createPayloadKeyTokenType(tokens);
		IntervalType interval = Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration,
				RID_ID, confidence, accuracy, value).build();

		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.TELEMETRY_STATUS;
		long createdTimestamp = 12L;
		long startTimestamp = 12L;
		String duration = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(REPORT_ID,
				REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName, createdTimestamp, startTimestamp, duration)
				.addInterval(interval).build();

		String requestId = "requestId";
		String venId = VEN_ID;
		OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
				.addReport(report).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrUpdateReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrUpdateReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrUpdateReportTypeAvobVenServiceRequestPayload()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String intervalId = "intervalId";
		long start = 3L;
		String xmlDuration = "PT1H";
		Long confidence = 1L;
		Float accuracy = 1F;
		String rid = "rid";
		PayloadAvobVenServiceRequestType requests = new PayloadAvobVenServiceRequestType();
		AvobVenServiceRequestType req = new AvobVenServiceRequestType();
		req.setCommand("command");
		req.setRefresh(true);
		req.setRequestId("requestId");
		req.setServiceName("servicename");
		requests.getRequests().add(req);
		IntervalType interval = Oadr20bFactory.createAvobVenServiceRequestReportIntervalType(intervalId, start,
				xmlDuration, rid, confidence, accuracy, requests);

		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.TELEMETRY_STATUS;
		long createdTimestamp = 12L;
		long startTimestamp = 12L;
		String duration = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(REPORT_ID,
				REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName, createdTimestamp, startTimestamp, duration)
				.addInterval(interval).build();

		String requestId = "requestId";
		String venId = VEN_ID;
		OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
				.addReport(report).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrUpdateReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrUpdateReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrUpdateReportTypeResourceStatusPayload()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {

		String intervalId = "intervalId";
		long start = 3L;
		String xmlDuration = "PT1H";
		Long confidence = 1L;
		Float accuracy = 1F;

		List<KeyTokenType> tokens = new ArrayList<>();
		KeyTokenType keyTokenType = new KeyTokenType();
		keyTokenType.setKey("mouaiccool");
		keyTokenType.setValue("mouaiccool");
		tokens.add(keyTokenType);
		OadrLoadControlStateTypeType capacity = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateTypeType levelOffset = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateTypeType percentOffset = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateTypeType setPoint = Oadr20bFactory.createOadrLoadControlStateTypeType(0F, 0F, 0F, 0F);
		OadrLoadControlStateType loadControlState = Oadr20bFactory.createOadrLoadControlStateType(capacity, levelOffset,
				percentOffset, setPoint);
		boolean manualOverride = true;
		boolean online = true;
		OadrPayloadResourceStatusType value = Oadr20bFactory.createOadrPayloadResourceStatusType(loadControlState,
				manualOverride, online);
		IntervalType interval = Oadr20bEiBuilders.newOadr20bReportIntervalTypeBuilder(intervalId, start, xmlDuration,
				RID_ID, confidence, accuracy, value).build();

		ReportNameEnumeratedType reportName = ReportNameEnumeratedType.TELEMETRY_STATUS;
		long createdTimestamp = 12L;
		long startTimestamp = 12L;
		String duration = "PT1H";
		OadrReportType report = Oadr20bEiReportBuilders.newOadr20bUpdateReportOadrReportBuilder(REPORT_ID,
				REPORT_SPECIFIER_ID, REPORT_REQUEST_ID, reportName, createdTimestamp, startTimestamp, duration)
				.addInterval(interval).build();

		String requestId = "requestId";
		String venId = VEN_ID;
		OadrUpdateReportType request = Oadr20bEiReportBuilders.newOadr20bUpdateReportBuilder(requestId, venId)
				.addReport(report).build();
		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrUpdateReportType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrUpdateReportType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

	@Test
	public void testOadrPollType()
			throws Oadr20bXMLSignatureException, Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrPollType request = Oadr20bPollBuilders.newOadr20bPollBuilder(VEN_ID).build();

		String sign = OadrXMLSignatureHandler.sign(request, privateKey, certificate, NONCE, 0L);
		validate(sign);
		OadrPayload unmarshal = jaxbContext.unmarshal(sign, OadrPayload.class, true);
		OadrPollType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrPollType.class);
		assertNotNull(signedObjectFromOadrPayload);
	}

}
