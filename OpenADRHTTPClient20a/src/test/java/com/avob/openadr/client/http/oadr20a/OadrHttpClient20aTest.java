package com.avob.openadr.client.http.oadr20a;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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
import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aUrlPath;
import com.avob.openadr.model.oadr20a.builders.Oadr20aBuilders;
import com.avob.openadr.model.oadr20a.ei.EiActivePeriodType;
import com.avob.openadr.model.oadr20a.ei.EiEventSignalType;
import com.avob.openadr.model.oadr20a.ei.EiTargetType;
import com.avob.openadr.model.oadr20a.ei.EventDescriptorType;
import com.avob.openadr.model.oadr20a.ei.EventStatusEnumeratedType;
import com.avob.openadr.model.oadr20a.ei.SignalTypeEnumeratedType;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent.OadrEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;

public class OadrHttpClient20aTest {

	public static final String XSD_OADR20A_SCHEMA = "src/test/resources/oadr20a_schema";

	private Oadr20aJAXBContext jaxbContext;

	public OadrHttpClient20aTest() throws JAXBException {
		jaxbContext = Oadr20aJAXBContext.getInstance(XSD_OADR20A_SCHEMA);
	}

	private OadrDistributeEvent createOadrDistributeEvent() {
		long timestampStart = 0L;
		String eventXmlDuration = "PT1H";
		String toleranceXmlDuration = "PT5M";
		String notificationXmlDuration = "P1D";
		EiActivePeriodType eiActivePeriod = Oadr20aBuilders.newOadr20aEiActivePeriodTypeBuilder(timestampStart,
				eventXmlDuration, toleranceXmlDuration, notificationXmlDuration).build();

		String signalId = "0";
		String signalName = "simple";
		SignalTypeEnumeratedType signalType = SignalTypeEnumeratedType.LEVEL;
		String xmlDuration = "PT1H";
		float currentValue = 0;
		String intervalId = "intervalId";

		EiEventSignalType eiEventSignalType = Oadr20aBuilders
				.newOadr20aEiEventSignalTypeBuilder(signalId, signalName, signalType, currentValue)
				.addInterval(
						Oadr20aBuilders.newOadr20aIntervalTypeBuilder(intervalId, xmlDuration, currentValue).build())
				.build();

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
				.withActivePeriod(eiActivePeriod).addEiEventSignal(eiEventSignalType).withEiTarget(eiTarget)
				.withEventDescriptor(eventDescriptor).withResponseRequired(false).build();

		return Oadr20aBuilders.newOadr20aDistributeEventBuilder("", "").addOadrEvent(oadrEvent).build();
	}

	private HttpResponse createHttpResponse(int responseCode, String payload)
			throws Oadr20aMarshalException, JAXBException {

		StatusLine statusLine = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 0), responseCode, "");
		HttpResponse response = new BasicHttpResponse(statusLine);
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContent(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)));
		response.setEntity(entity);
		return response;
	}

	@Test
	public void validPostTest() throws ClientProtocolException, IOException, JAXBException, Oadr20aException,
			Oadr20aMarshalException, URISyntaxException, Oadr20aHttpLayerException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", scOk).build();
		String marshal = jaxbContext.marshal(mockOadrResponse);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20a client = new OadrHttpClient20a(oadrHttpClient);

		OadrDistributeEvent mockDistributeEvent = this.createOadrDistributeEvent();
		OadrResponse post = client.post(mockDistributeEvent,
				Oadr20aUrlPath.OADR_BASE_PATH + Oadr20aUrlPath.EI_EVENT_SERVICE, OadrResponse.class);

		assertEquals(String.valueOf(scOk), post.getEiResponse().getResponseCode());
	}

	@Test
	public void httpLayerErrorPostTest() throws ClientProtocolException, IOException, JAXBException, Oadr20aException,
			Oadr20aMarshalException, URISyntaxException, Oadr20aHttpLayerException {

		// HTTP layer error
		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scForbidden = HttpStatus.SC_FORBIDDEN;

		OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", scForbidden).build();
		String marshal = jaxbContext.marshal(mockOadrResponse);

		HttpResponse response = this.createHttpResponse(scForbidden, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20a client = new OadrHttpClient20a(oadrHttpClient);

		OadrDistributeEvent mockDistributeEvent = this.createOadrDistributeEvent();

		boolean exception = false;
		try {
			client.post(mockDistributeEvent, Oadr20aUrlPath.OADR_BASE_PATH + Oadr20aUrlPath.EI_EVENT_SERVICE,
					OadrResponse.class);
		} catch (Oadr20aHttpLayerException e) {
			exception = true;
		}
		assertTrue(exception);

	}

	@Test
	public void requestMarshallingErrorPostTest() throws ClientProtocolException, IOException, JAXBException,
			Oadr20aException, Oadr20aMarshalException, URISyntaxException, Oadr20aHttpLayerException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		OadrResponse mockOadrResponse = Oadr20aBuilders.newOadr20aResponseBuilder("", scOk).build();
		String marshal = jaxbContext.marshal(mockOadrResponse);

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20a client = new OadrHttpClient20a(oadrHttpClient);

		OadrDistributeEvent mockDistributeEvent = this.createOadrDistributeEvent();
		mockDistributeEvent.setVtnID(null);

		boolean exception = false;
		try {
			client.post(mockDistributeEvent, Oadr20aUrlPath.OADR_BASE_PATH + Oadr20aUrlPath.EI_EVENT_SERVICE,
					OadrResponse.class);
		} catch (Oadr20aException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	@Test
	public void responseUnmarshallingErrorPostTest() throws ClientProtocolException, IOException, JAXBException,
			Oadr20aException, Oadr20aMarshalException, URISyntaxException, Oadr20aHttpLayerException {

		OadrHttpClient oadrHttpClient = Mockito.mock(OadrHttpClient.class);
		int scOk = HttpStatus.SC_OK;

		String marshal = "";

		HttpResponse response = this.createHttpResponse(scOk, marshal);
		when(oadrHttpClient.execute(Matchers.<HttpPost>anyObject(), any(), any(), any())).thenReturn(response);

		OadrHttpClient20a client = new OadrHttpClient20a(oadrHttpClient);

		OadrDistributeEvent mockDistributeEvent = this.createOadrDistributeEvent();

		boolean exception = false;
		try {
			client.post(mockDistributeEvent, Oadr20aUrlPath.OADR_BASE_PATH + Oadr20aUrlPath.EI_EVENT_SERVICE,
					OadrResponse.class);
		} catch (Oadr20aException e) {
			exception = true;
		}
		assertTrue(exception);
	}
}
