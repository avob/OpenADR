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
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiOptBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
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
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
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

	@Test
	public void oadrCreatedReportTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();

		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrCreatedReportType payload = new OadrCreatedReportType();
		OadrHttpVenClient20bTestClass.oadrCreatedReport(payload);
	}

	@Test
	public void oadrCreateReportTest() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCreatedReportType build = Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder("", HttpStatus.SC_OK, "venId").build();

		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCreatedReportType, JAXBElement<OadrCreatedReportType>>post(
				Matchers.<JAXBElement<OadrCreatedReportType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCreateReportType payload = new OadrCreateReportType();
		OadrHttpVenClient20bTestClass.oadrCreateReport(payload);
	}

	@Test
	public void oadrUpdateReportTest() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrUpdatedReportType build = Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrUpdatedReportType, JAXBElement<OadrUpdatedReportType>>post(
				Matchers.<JAXBElement<OadrUpdatedReportType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrUpdateReportType payload = new OadrUpdateReportType();
		OadrHttpVenClient20bTestClass.oadrUpdateReport(payload);
	}

	@Test
	public void oadrRegisterReportTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrRegisteredReportType build = Oadr20bEiReportBuilders
				.newOadr20bRegisteredReportBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrRegisteredReportType, JAXBElement<OadrRegisteredReportType>>post(
				Matchers.<JAXBElement<OadrRegisteredReportType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrRegisterReportType payload = new OadrRegisterReportType();
		OadrHttpVenClient20bTestClass.oadrRegisterReport(payload);
	}

	@Test
	public void oadrUpdatedReportTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrUpdatedReportType payload = new OadrUpdatedReportType();
		OadrHttpVenClient20bTestClass.oadrUpdatedReport(payload);
	}

	@Test
	public void oadrRegisteredReportTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrRegisteredReportType payload = new OadrRegisteredReportType();
		OadrHttpVenClient20bTestClass.oadrRegisteredReport(payload);
	}

	@Test
	public void oadrCancelReportTest() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCanceledReportType build = Oadr20bEiReportBuilders
				.newOadr20bCanceledReportBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCanceledReportType, JAXBElement<OadrCanceledReportType>>post(
				Matchers.<JAXBElement<OadrCanceledReportType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCancelReportType payload = new OadrCancelReportType();
		OadrHttpVenClient20bTestClass.oadrCancelReport(payload);
	}

	@Test
	public void oadrCanceledReportTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrCanceledReportType payload = new OadrCanceledReportType();
		OadrHttpVenClient20bTestClass.oadrCanceledReport(payload);
	}

	@Test
	public void oadrPollTest() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<Object, JAXBElement<Object>>post(Matchers.<JAXBElement<Object>>anyObject(),
				Matchers.any(), Matchers.any())).thenReturn(new Object());

		OadrPollType payload = new OadrPollType();
		OadrHttpVenClient20bTestClass.oadrPoll(payload);
	}

	@Test
	public void oadrCreatePartyRegistrationTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCreatedPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder("", HttpStatus.SC_OK, "venId", "vtnId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCreatedPartyRegistrationType, JAXBElement<OadrCreatedPartyRegistrationType>>post(
				Matchers.<JAXBElement<OadrCreatedPartyRegistrationType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCreatePartyRegistrationType payload = new OadrCreatePartyRegistrationType();
		OadrHttpVenClient20bTestClass.oadrCreatePartyRegistration(payload);
	}

	@Test
	public void oadrCancelPartyRegistrationTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCanceledPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCanceledPartyRegistrationBuilder("", HttpStatus.SC_OK, "venId", "vtnId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCanceledPartyRegistrationType, JAXBElement<OadrCanceledPartyRegistrationType>>post(
				Matchers.<JAXBElement<OadrCanceledPartyRegistrationType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCancelPartyRegistrationType payload = new OadrCancelPartyRegistrationType();
		OadrHttpVenClient20bTestClass.oadrCancelPartyRegistration(payload);
	}

	@Test
	public void oadrResponseReregisterPartyTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrResponseType payload = new OadrResponseType();
		OadrHttpVenClient20bTestClass.oadrResponseReregisterParty(payload);
	}

	@Test
	public void oadrCanceledPartyRegistrationTypeTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_OK, "venId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrResponseType, JAXBElement<OadrResponseType>>post(
				Matchers.<JAXBElement<OadrResponseType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(mockOadrResponseType);

		OadrCanceledPartyRegistrationType payload = new OadrCanceledPartyRegistrationType();
		OadrHttpVenClient20bTestClass.oadrCanceledPartyRegistrationType(payload);
	}

	@Test
	public void oadrQueryRegistrationTypeTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCreatedPartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatedPartyRegistrationBuilder("", HttpStatus.SC_OK, "venId", "vtnId").build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCreatedPartyRegistrationType, JAXBElement<OadrCreatedPartyRegistrationType>>post(
				Matchers.<JAXBElement<OadrCreatedPartyRegistrationType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrQueryRegistrationType payload = new OadrQueryRegistrationType();
		OadrHttpVenClient20bTestClass.oadrQueryRegistrationType(payload);
	}

	@Test
	public void oadrCreateOptTest() throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCreatedOptType build = Oadr20bEiOptBuilders.newOadr20bCreatedOptBuilder("", HttpStatus.SC_OK, "OPT_IN")
				.build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCreatedOptType, JAXBElement<OadrCreatedOptType>>post(
				Matchers.<JAXBElement<OadrCreatedOptType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCreateOptType payload = new OadrCreateOptType();
		OadrHttpVenClient20bTestClass.oadrCreateOpt(payload);
	}

	@Test
	public void oadrCancelOptTypeTest() throws Oadr20bException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {

		OadrHttpClient20b OadrHttpClient20b = Mockito.mock(OadrHttpClient20b.class);

		OadrCanceledOptType build = Oadr20bEiOptBuilders.newOadr20bCanceledOptBuilder("", HttpStatus.SC_OK, "OPT_IN")
				.build();
		OadrHttpVenClient20b OadrHttpVenClient20bTestClass = new OadrHttpVenClient20b(OadrHttpClient20b);

		when(OadrHttpClient20b.<OadrCanceledOptType, JAXBElement<OadrCanceledOptType>>post(
				Matchers.<JAXBElement<OadrCanceledOptType>>anyObject(), Matchers.any(), Matchers.any()))
						.thenReturn(build);

		OadrCancelOptType payload = new OadrCancelOptType();
		OadrHttpVenClient20bTestClass.oadrCancelOptType(payload);
	}

}
