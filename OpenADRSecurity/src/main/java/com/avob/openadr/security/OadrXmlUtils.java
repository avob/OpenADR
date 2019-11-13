package com.avob.openadr.security;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrXmlUtils {

	private OadrXmlUtils() {
	}

	public static Schema loadXsdSchema(String xsdFilePath) throws OadrSecurityException {
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		File xsdFile = new File(xsdFilePath);
		if (xsdFile.exists()) {
			try {
				return sf.newSchema(new Source[] { new StreamSource(xsdFile) });
			} catch (SAXException e) {
				throw new OadrSecurityException(e);
			}
		} else {
			throw new OadrSecurityException("Oadr20b XSD schema not loaded");
		}

	}

}
