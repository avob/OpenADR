package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.Writer;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMResult;

import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiEventBuilders;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiRegisterPartyBuilders;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatePartyRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.server.common.vtn.service.push.DemandResponseEventPublisher;
import com.avob.openadr.server.oadr20b.vtn.VTN20bSecurityApplicationTest;
import com.avob.openadr.server.oadr20b.vtn.service.VenDistributeService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bDemandResponseEventCreateListener;
import com.avob.openadr.server.oadr20b.vtn.service.push.Oadr20bPushListener;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrDataBaseSetup;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiHttpMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockEiXmpp;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpDemandResponseEventMvc;
import com.avob.openadr.server.oadr20b.vtn.utils.OadrMockHttpVenMvc;
import com.avob.openadr.server.oadr20b.vtn.xmpp.XmppConnector;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { VTN20bSecurityApplicationTest.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class Oadr20bVTNEiRegisterPartyControllerTest {

	private static final String EIREGISTERPARTY_ENDPOINT = "/OpenADR2/Simple/2.0b/EiRegisterParty";

	@Value("${oadr.vtnid}")
	private String vtnId;

	@Resource
	private OadrMockEiHttpMvc oadrMockEiHttpMvc;

	@Resource
	private OadrMockEiXmpp oadrMockEiXmpp;

	@Resource
	private OadrMockHttpVenMvc oadrMockHttpVenMvc;

	@Resource
	private OadrMockHttpDemandResponseEventMvc oadrMockHttpDemandResponseEventMvc;

	@Resource
	private JmsTemplate jmsTemplate;

	@Resource
	private XmppConnector xmppConnector;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private Oadr20bPushListener oadr20bPushListener;

	@Resource
	private Oadr20bDemandResponseEventCreateListener oadr20bDemandResponseEventCreateListener;

	private Oadr20bJAXBContext jaxbContext;

	@Before
	public void init() throws JAXBException {

		jaxbContext = Oadr20bJAXBContext.getInstance();

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bPushListener.receiveCommand(invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(VenDistributeService.OADR20B_QUEUE), Mockito.any(String.class));

		Mockito.doAnswer((Answer<?>) invocation -> {
			oadr20bDemandResponseEventCreateListener.receiveEvent(invocation.getArgument(1));
			return null;
		}).when(jmsTemplate).convertAndSend(Mockito.eq(DemandResponseEventPublisher.OADR20B_QUEUE),
				Mockito.any(String.class));

		oadrMockEiXmpp.init();
	}

	@Test
	public void testErrorCase() throws Exception {

		// GET not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.get(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// PUT not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.put(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// DELETE not allowed
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.delete(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED_405));

		// POST without content
		String content = "";
		this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST_400));

		// POST without content
		content = "mouaiccool";
		MvcResult andReturn = this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();

		OadrPayload unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(), OadrPayload.class);
		OadrResponseType signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal,
				OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453),
				signedObjectFromOadrPayload.getEiResponse().getResponseCode());

		// POST with unsigned content
		OadrCreatePartyRegistrationType build = Oadr20bEiRegisterPartyBuilders
				.newOadr20bCreatePartyRegistrationBuilder("requestId", "venId", "2.0b").build();

		OadrPayload createOadrPayload = Oadr20bFactory.createOadrPayload("mypayload", build);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DOMResult res = new DOMResult();
		try {
			jaxbContext.marshal(createOadrPayload, res);
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
		String signed = stringWriter.toString();
		content = signed.replaceAll("\n", "");
		andReturn = this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(), OadrPayload.class);
		signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.INVALID_DATA_454),
				signedObjectFromOadrPayload.getEiResponse().getResponseCode());

		// no signature while expected
		content = jaxbContext.marshalRoot(build);
		andReturn = this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		unmarshal = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(), OadrPayload.class);
		signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.COMPLIANCE_ERROR_459),
				signedObjectFromOadrPayload.getEiResponse().getResponseCode());
		
		// send well formed object meant for another service
		OadrRequestEventType event = Oadr20bEiEventBuilders
				.newOadrRequestEventBuilder(OadrDataBaseSetup.VEN_HTTP_PULL_DSIG, "requestId").withReplyLimit(12)
				.build();
		content = jaxbContext.marshalRoot(event);
		andReturn = this.oadrMockEiHttpMvc
				.perform(MockMvcRequestBuilders.post(EIREGISTERPARTY_ENDPOINT)
						.with(OadrDataBaseSetup.VEN_HTTP_PUSH_SECURITY_SESSION).content(content))
				.andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK_200)).andReturn();
		OadrResponseType resp = jaxbContext.unmarshal(andReturn.getResponse().getContentAsString(),
				OadrResponseType.class);
		assertEquals(String.valueOf(Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453),
				resp.getEiResponse().getResponseCode());


	}

}
