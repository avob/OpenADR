package com.avob.openadr.client.http.oadr20b;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.avob.openadr.client.http.OadrHttpClient;
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bResponseBuilders;
import com.avob.openadr.model.oadr20b.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20b.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20b.ei.EiTargetType;
import com.avob.openadr.model.oadr20b.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20b.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalNameEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrDistributeEventType.OadrEvent;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.xmlsignature.OadrXMLSignatureHandler;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpClient20bTest {

	public static final String CERT_FOLDER_PATH = "src/test/resources/cert/";

	public static final String XSD_OADR20B_SCHEMA = "src/test/resources/oadr20b_schema";

	private Oadr20bJAXBContext jaxbContext;

	public OadrHttpClient20bTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(XSD_OADR20B_SCHEMA);
	}

	private OadrDistributeEventType createOadrDistributeEvent() {
		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType eiActivePeriod = Oadr20bEiEventBuilders.newOadr20bEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		String signalId = "0";
		SignalNameEnumeratedType signalName = SignalNameEnumeratedType.SIMPLE;
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String xmlDuration = "PT1H";
		float currentValue = 0;
		String intervalId = "intervalId";

		EiEventSignalType eiEventSignalType = Oadr20bEiEventBuilders
				.newOadr20bEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(Oadr20bEiBuilders
						.newOadr20bSignalIntervalTypeBuilder(intervalId, timestampStart, xmlDuration, currentValue)
						.build())
				.build();

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
				.withEventDescriptor(eventDescriptor).withResponseRequired(false).build();

		return Oadr20bEiEventBuilders.newOadr20bDistributeEventBuilder("", "").addOadrEvent(oadrEvent).build();
	}

	private HttpResponse createHttpResponse(int responseCode, String payload)
			throws Oadr20bMarshalException, JAXBException {

		StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 0), responseCode, "");
		HttpResponse response = new BasicHttpResponse(statusLine);
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)));
		response.setEntity(entity);
		return response;
	}

	@Test
	public void givenValidUnsignedPost_DoNotRaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", scOk, "venId")
				.build();
		String marshal = jaxbContext.marshalRoot(mockOadrResponseType);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();
		OadrResponseType post = client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
				Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);

		assertEquals(String.valueOf(scOk), post.getEiResponse().getResponseCode());
	}

	@Test
	public void givenNotSignedResponse_RaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", scOk, "venId")
				.build();
		String marshal = jaxbContext.marshalRoot(mockOadrResponseType);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		String certPath = "src/test/resources/cert/test";
		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient, certPath + ".key", certPath + ".crt", 1200L);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();
		boolean exception = false;
		try {
			client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
					Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
		} catch (Oadr20bException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void givenHttpError_RaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		// HTTP layer error
		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);

		HttpResponse response = this.createHttpResponse(HttpStatus.SC_FORBIDDEN, "");
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();

		boolean exception = false;
		try {
			client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
					Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
		} catch (Oadr20bHttpLayerException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void givenApplicationError_DoNotRaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		// HTTP layer error
		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders
				.newOadr20bResponseBuilder("", HttpStatus.SC_FORBIDDEN, "venId").build();
		String marshal = jaxbContext.marshalRoot(mockOadrResponseType);

		HttpResponse response = this.createHttpResponse(HttpStatus.SC_OK, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();

		client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
				Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);

	}

	@Test
	public void givenUnmarshallingRequest_RaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", scOk, "venId")
				.build();

		String marshal = jaxbContext.marshalRoot(mockOadrResponseType);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient, null, null, null, true);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();
		mockDistributeEvent.setVtnID(null);

		boolean exception = false;
		try {
			client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
					Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
		} catch (Oadr20bException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void givenUnmarshallingResponse_RaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		String marshal = "";

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();

		boolean exception = false;
		try {
			client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
					Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
		} catch (Oadr20bException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void responseNotSignedErrorPostTest() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", scOk, "venId")
				.build();
		String marshal = jaxbContext.marshalRoot(mockOadrResponseType);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		String keyFile = CERT_FOLDER_PATH + "test.key";
		String certFile = CERT_FOLDER_PATH + "test.crt";
		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient, keyFile, certFile, 1200L);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();
		mockDistributeEvent.setVtnID("vtnId");

		boolean exception = false;
		try {
			client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
					Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
		} catch (Oadr20bException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	private String sign(Object object, PrivateKey privateKey, X509Certificate clientCertificate)
			throws Oadr20bXMLSignatureException {
		String nonce = UUID.randomUUID().toString();
		Long createdtimestamp = System.currentTimeMillis();
		return OadrXMLSignatureHandler.sign(object, privateKey, clientCertificate, nonce, createdtimestamp);
	}

	@Test
	public void givenValidReponse_DoNotRaiseException() throws ClientProtocolException, IOException, JAXBException,
			Oadr20bException, Oadr20bMarshalException, URISyntaxException, Oadr20bHttpLayerException,
			Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException, OadrSecurityException {

		String keyFile = CERT_FOLDER_PATH + "test.key";
		String certFile = CERT_FOLDER_PATH + "test.crt";
		PrivateKey key = OadrPKISecurity.parsePrivateKey(keyFile);
		X509Certificate cert = OadrPKISecurity.parseCertificate(certFile);

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponseType mockOadrResponseType = Oadr20bResponseBuilders.newOadr20bResponseBuilder("", scOk, "venId")
				.build();
		String sign = sign(mockOadrResponseType, key, cert);

		HttpResponse response = this.createHttpResponse(scOk, sign);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20b client = new OadrHttpClient20b(oadrHttpClient, keyFile, certFile, 1200L);

		OadrDistributeEventType mockDistributeEvent = this.createOadrDistributeEvent();
		mockDistributeEvent.setVtnID("vtnId");

		client.post(Oadr20bFactory.createOadrDistributeEvent(mockDistributeEvent),
				Oadr20bUrlPath.OADR_BASE_PATH + Oadr20bUrlPath.EI_EVENT_SERVICE, OadrResponseType.class);
	}

}
