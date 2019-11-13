package com.avob.openadr.model.oadr20b;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
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
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrPollType;
import com.avob.openadr.model.oadr20b.oadr.OadrQueryRegistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestEventType;
import com.avob.openadr.model.oadr20b.oadr.OadrRequestReregistrationType;
import com.avob.openadr.model.oadr20b.oadr.OadrResponseType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;

public class Oadr20bJAXBContext {

	private static final boolean DEFAULT_VALIDATE_XML_PAYLOAD = false;
	public static final String SHARED_RESOURCE_PATH = "target/maven-shared-archive-resources";
	public static final String XSD_PATH = "oadr_20b.xsd";
	public static final String XSD_AVOB_PATH = "oadr_avob.xsd";

	private static Oadr20bJAXBContext instance = null;

	private Schema schema;

	private JAXBContext jaxbContext;

	private Oadr20bJAXBContext(Schema schema) throws JAXBException {

		StringJoiner joiner = new StringJoiner(":");
		joiner.add("com.avob.openadr.model.oadr20b.atom");
		joiner.add("com.avob.openadr.model.oadr20b.ei");
		joiner.add("com.avob.openadr.model.oadr20b.emix");
		joiner.add("com.avob.openadr.model.oadr20b.gml");
		joiner.add("com.avob.openadr.model.oadr20b.greenbutton");
		joiner.add("com.avob.openadr.model.oadr20b.iso");
		joiner.add("com.avob.openadr.model.oadr20b.oadr");
		joiner.add("com.avob.openadr.model.oadr20b.power");
		joiner.add("com.avob.openadr.model.oadr20b.pyld");
		joiner.add("com.avob.openadr.model.oadr20b.siscale");
		joiner.add("com.avob.openadr.model.oadr20b.strm");
		joiner.add("com.avob.openadr.model.oadr20b.xcal");
		joiner.add("com.avob.openadr.model.oadr20b.xmldsig");
		joiner.add("com.avob.openadr.model.oadr20b.xmldsig.properties");
		joiner.add("com.avob.openadr.model.oadr20b.xmldsig11");
		joiner.add("com.avob.openadr.model.oadr20b.avob");

		jaxbContext = JAXBContext.newInstance(joiner.toString());

		this.schema = schema;

	}

	public static Oadr20bJAXBContext getInstance() throws JAXBException {
		return Oadr20bJAXBContext.getInstance(null);
	}

	public synchronized static Oadr20bJAXBContext getInstance(String xsdFolderPath) throws JAXBException {
		if (instance == null) {
			Schema loadedSchema = null;
			if (xsdFolderPath != null) {
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				File xsdFile = new File(xsdFolderPath + "/" + XSD_PATH);
				File xsdAvobFile = new File(xsdFolderPath + "/" + XSD_AVOB_PATH);
				if (xsdFile.exists() && xsdAvobFile.exists()) {
					try {
						loadedSchema = sf
								.newSchema(new Source[] { new StreamSource(xsdFile), new StreamSource(xsdAvobFile) });
					} catch (SAXException e) {
						loadedSchema = null;
					}
				}
			}
			instance = new Oadr20bJAXBContext(loadedSchema);
		}
		return instance;
	}

	public Object unmarshal(String payload) throws Oadr20bUnmarshalException {
		return this.unmarshal(payload, DEFAULT_VALIDATE_XML_PAYLOAD);
	}

	public Object unmarshal(String payload, boolean validate) throws Oadr20bUnmarshalException {
		if (payload.indexOf("oadrPayload") >= 0) {
			return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), Object.class,
					false);
		} else {
			return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), Object.class,
					validate);
		}

	}

	public <T> T unmarshal(String payload, Class<T> responseKlass) throws Oadr20bUnmarshalException {
		return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), responseKlass);
	}

	public <T> T unmarshal(String payload, Class<T> responseKlass, boolean validate) throws Oadr20bUnmarshalException {
		return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), responseKlass,
				validate);
	}

	public <T> T unmarshal(InputStream payload, Class<T> responseKlass) throws Oadr20bUnmarshalException {
		return this.unmarshal(payload, responseKlass, DEFAULT_VALIDATE_XML_PAYLOAD);
	}

	public <T> T unmarshal(InputStream payload, Class<T> responseKlass, boolean validate)
			throws Oadr20bUnmarshalException {
		Object unmarshal;
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			if (!OadrPayload.class.equals(responseKlass) && validate) {
				unmarshaller.setSchema(schema);

			}
			unmarshal = unmarshaller.unmarshal(payload);
		} catch (JAXBException e) {

			throw new Oadr20bUnmarshalException(e);
		}
		try {
			if (unmarshal instanceof JAXBElement) {
				JAXBElement<?> cast = JAXBElement.class.cast(unmarshal);
				return responseKlass.cast(cast.getValue());
			} else {
				return responseKlass.cast(unmarshal);
			}
		} catch (ClassCastException e) {
			throw new Oadr20bUnmarshalException(e);
		}
	}

	public <T> T unmarshal(File payload, Class<T> responseKlass) throws Oadr20bUnmarshalException {
		return this.unmarshal(payload, responseKlass, DEFAULT_VALIDATE_XML_PAYLOAD);
	}

	public <T> T unmarshal(File payload, Class<T> responseKlass, boolean validate) throws Oadr20bUnmarshalException {
		try {
			return this.unmarshal(new FileInputStream(payload), responseKlass, validate);
		} catch (FileNotFoundException e) {
			throw new Oadr20bUnmarshalException(e);
		}
	}

	public <T extends JAXBElement<?>> String marshal(T payload) throws Oadr20bMarshalException {
		return this.marshal(payload, DEFAULT_VALIDATE_XML_PAYLOAD);
	}

	public <T extends JAXBElement<?>> String marshal(T payload, boolean validate) throws Oadr20bMarshalException {
		StringWriter writer = new StringWriter();
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			if (validate) {
				marshaller.setSchema(schema);
			}
			marshaller.marshal(payload, writer);
		} catch (JAXBException e) {
			throw new Oadr20bMarshalException(e);
		}
		return writer.toString();
	}

	public void marshal(Object payload, File file) throws Oadr20bMarshalException {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.marshal(payload, file);
		} catch (JAXBException e) {
			throw new Oadr20bMarshalException(e);
		}
	}

	public void marshal(Object payload, DOMResult res) throws Oadr20bMarshalException {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.marshal(payload, res);
		} catch (JAXBException e) {
			throw new Oadr20bMarshalException(e);
		}
	}

	public String marshalRoot(Object payload) throws Oadr20bMarshalException {
		return this.marshalRoot(payload, DEFAULT_VALIDATE_XML_PAYLOAD);
	}

	public String marshalRoot(Object payload, boolean validate) throws Oadr20bMarshalException {

		JAXBElement<?> el = null;

		if (payload instanceof OadrDistributeEventType) {
			OadrDistributeEventType value = (OadrDistributeEventType) payload;
			el = Oadr20bFactory.createOadrDistributeEvent(value);

		} else if (payload instanceof OadrCreatedEventType) {
			OadrCreatedEventType value = (OadrCreatedEventType) payload;
			el = Oadr20bFactory.createOadrCreatedEvent(value);

		} else if (payload instanceof OadrRequestEventType) {
			OadrRequestEventType value = (OadrRequestEventType) payload;
			el = Oadr20bFactory.createOadrRequestEvent(value);

		} else if (payload instanceof OadrResponseType) {
			OadrResponseType value = (OadrResponseType) payload;
			el = Oadr20bFactory.createOadrResponse(value);

		} else if (payload instanceof OadrCancelOptType) {
			OadrCancelOptType value = (OadrCancelOptType) payload;
			el = Oadr20bFactory.createOadrCancelOpt(value);

		} else if (payload instanceof OadrCanceledOptType) {
			OadrCanceledOptType value = (OadrCanceledOptType) payload;
			el = Oadr20bFactory.createOadrCanceledOpt(value);

		} else if (payload instanceof OadrCreateOptType) {
			OadrCreateOptType value = (OadrCreateOptType) payload;
			el = Oadr20bFactory.createOadrCreateOpt(value);

		} else if (payload instanceof OadrCreatedOptType) {
			OadrCreatedOptType value = (OadrCreatedOptType) payload;
			el = Oadr20bFactory.createOadrCreatedOpt(value);

		} else if (payload instanceof OadrCancelReportType) {
			OadrCancelReportType value = (OadrCancelReportType) payload;
			el = Oadr20bFactory.createOadrCancelReport(value);

		} else if (payload instanceof OadrCanceledReportType) {
			OadrCanceledReportType value = (OadrCanceledReportType) payload;
			el = Oadr20bFactory.createOadrCanceledReport(value);

		} else if (payload instanceof OadrCreateReportType) {
			OadrCreateReportType value = (OadrCreateReportType) payload;
			el = Oadr20bFactory.createOadrCreateReport(value);

		} else if (payload instanceof OadrCreatedReportType) {
			OadrCreatedReportType value = (OadrCreatedReportType) payload;
			el = Oadr20bFactory.createOadrCreatedReport(value);

		} else if (payload instanceof OadrRegisterReportType) {
			OadrRegisterReportType value = (OadrRegisterReportType) payload;
			el = Oadr20bFactory.createOadrRegisterReport(value);

		} else if (payload instanceof OadrRegisteredReportType) {
			OadrRegisteredReportType value = (OadrRegisteredReportType) payload;
			el = Oadr20bFactory.createOadrRegisteredReport(value);

		} else if (payload instanceof OadrUpdateReportType) {
			OadrUpdateReportType value = (OadrUpdateReportType) payload;
			el = Oadr20bFactory.createOadrUpdateReport(value);

		} else if (payload instanceof OadrUpdatedReportType) {
			OadrUpdatedReportType value = (OadrUpdatedReportType) payload;
			el = Oadr20bFactory.createOadrUpdatedReport(value);

		} else if (payload instanceof OadrCancelPartyRegistrationType) {
			OadrCancelPartyRegistrationType value = (OadrCancelPartyRegistrationType) payload;
			el = Oadr20bFactory.createOadrCancelPartyRegistration(value);

		} else if (payload instanceof OadrCanceledPartyRegistrationType) {
			OadrCanceledPartyRegistrationType value = (OadrCanceledPartyRegistrationType) payload;
			el = Oadr20bFactory.createOadrCanceledPartyRegistration(value);

		} else if (payload instanceof OadrCreatePartyRegistrationType) {
			OadrCreatePartyRegistrationType value = (OadrCreatePartyRegistrationType) payload;
			el = Oadr20bFactory.createOadrCreatePartyRegistration(value);

		} else if (payload instanceof OadrCreatedPartyRegistrationType) {
			OadrCreatedPartyRegistrationType value = (OadrCreatedPartyRegistrationType) payload;
			el = Oadr20bFactory.createOadrCreatedPartyRegistration(value);

		} else if (payload instanceof OadrRequestReregistrationType) {
			OadrRequestReregistrationType value = (OadrRequestReregistrationType) payload;
			el = Oadr20bFactory.createOadrRequestReregistration(value);

		} else if (payload instanceof OadrQueryRegistrationType) {
			OadrQueryRegistrationType value = (OadrQueryRegistrationType) payload;
			el = Oadr20bFactory.createOadrQueryRegistration(value);

		} else if (payload instanceof OadrPollType) {
			OadrPollType value = (OadrPollType) payload;
			el = Oadr20bFactory.createOadrPoll(value);

		} else {
			throw new Oadr20bMarshalException("payload have to be an Oadr20b root element");
		}

		return this.marshal(el, validate);
	}
}
