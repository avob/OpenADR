package com.avob.openadr.model.oadr20a;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;

/**
 * Create oadr jaxb context and delegate marshaller/unmarshaller creation
 * methods
 * 
 * @author bertrand
 *
 */
public class Oadr20aJAXBContext {

    private static final String XSD_PATH = "/oadr20a_schema/oadr_20a.xsd";

    private static Oadr20aJAXBContext instance = null;

    private Schema schema;

    private JAXBContext jaxbContext;

    private Oadr20aJAXBContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(
                "com.avob.openadr.model.oadr20a.oadr:com.avob.openadr.model.oadr20a.strm:com.avob.openadr.model.oadr20a.emix");

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {

            URL url = this.getClass().getResource(XSD_PATH);
            File file = new File(url.getPath());

            schema = sf.newSchema(file);

        } catch (SAXException e) {
            throw new JAXBException("can't retreived xsd schema", e);
        }

    }

    public static Oadr20aJAXBContext getInstance() throws JAXBException {
        if (instance == null) {
            synchronized (Oadr20aJAXBContext.class) {
                if (instance == null) {
                    instance = new Oadr20aJAXBContext();
                }
            }
        }
        return instance;
    }

    public Object unmarshal(String payload) throws Oadr20aUnmarshalException {
        return this.unmarshal(payload, true);
    }

    public Object unmarshal(String payload, boolean validate) throws Oadr20aUnmarshalException {
        return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), Object.class,
                validate);
    }

    public <T> T unmarshal(String payload, Class<T> responseKlass) throws Oadr20aUnmarshalException {
        return this.unmarshal(payload, responseKlass, true);
    }

    public <T> T unmarshal(String payload, Class<T> responseKlass, boolean validate) throws Oadr20aUnmarshalException {
        return this.unmarshal(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)), responseKlass,
                validate);
    }

    public <T> T unmarshal(InputStream payload, Class<T> responseKlass) throws Oadr20aUnmarshalException {
        return this.unmarshal(payload, responseKlass, true);
    }

    public <T> T unmarshal(InputStream payload, Class<T> responseKlass, boolean validate)
            throws Oadr20aUnmarshalException {
        Object unmarshal;
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            if (validate) {
                unmarshaller.setSchema(schema);
            }
            unmarshal = unmarshaller.unmarshal(payload);
        } catch (JAXBException e) {
            throw new Oadr20aUnmarshalException(e);
        }
        try {
            return responseKlass.cast(unmarshal);
        } catch (ClassCastException e) {
            throw new Oadr20aUnmarshalException(e);
        }
    }

    public String marshal(Object payload) throws Oadr20aMarshalException {
        return this.marshal(payload, true);
    }

    public String marshal(Object payload, boolean validate) throws Oadr20aMarshalException {

        StringWriter writer = new StringWriter();
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            if (validate) {
                marshaller.setSchema(schema);
            }
            marshaller.marshal(payload, writer);
        } catch (JAXBException e) {
            throw new Oadr20aMarshalException(e);
        }
        return writer.toString();
    }

}
