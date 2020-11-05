package com.avob.openadr.model.oadr20b.xmlsignature;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureProperties;
import javax.xml.crypto.dsig.SignatureProperty;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bInitializationException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.xmldsig.ObjectType;
import com.avob.openadr.model.oadr20b.xmldsig.SignaturePropertiesType;
import com.avob.openadr.model.oadr20b.xmldsig.SignaturePropertyType;
import com.avob.openadr.model.oadr20b.xmldsig.properties.ReplayProtectType;

public class OadrXMLSignatureHandler {

	public static final String RSA_SHA256_ALGORITHM = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";

	public static final String ECDSA_SHA256_ALGORITHM = "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256";

	private static final Oadr20bJAXBContext jaxbContext;

	private static final JAXBContext replayProtectContext;

	private static final String SIGNEDOBJECT_PAYLOAD_ID = "signedObject";
	private static final String SIGNATUREOBJECT_PAYLOAD_ID = "prop";
	static {
		try {
			jaxbContext = Oadr20bJAXBContext.getInstance();
			replayProtectContext = JAXBContext.newInstance(ReplayProtectType.class);
			Security.addProvider(new BouncyCastleProvider());
		} catch (JAXBException e) {
			throw new Oadr20bInitializationException(e);
		}
	}

	private OadrXMLSignatureHandler() {
	}

	private static <T> List<T> asElement(List<Object> list, Class<T> klass) {
		List<T> res = new ArrayList<>();
		if (list != null) {
			for (Object obj : list) {
				if (obj instanceof JAXBElement) {
					JAXBElement<?> el = (JAXBElement<?>) obj;
					if (klass.equals(el.getValue().getClass())) {
						res.add(klass.cast(el.getValue()));
					}
				}

			}
		}
		return res;
	}

	private static void validateReplayProtect(OadrPayload payload, long nowDate, long millisecondAcceptedDelay)
			throws Oadr20bXMLSignatureValidationException {
		if (payload.getSignature() == null) {
			throw new Oadr20bXMLSignatureValidationException("XML Signature not found");
		}
		List<ObjectType> objects = payload.getSignature().getObject();
		if (objects == null) {
			throw new Oadr20bXMLSignatureValidationException("XML Signature must include ReplayProtect payload");
		}
		ReplayProtectType replayProtect = null;
		List<SignaturePropertiesType> propertiesList = new ArrayList<>();
		for (ObjectType objectType : objects) {
			propertiesList.addAll(OadrXMLSignatureHandler.<SignaturePropertiesType>asElement(objectType.getContent(),
					SignaturePropertiesType.class));
		}
		List<SignaturePropertyType> propertyList = new ArrayList<>();
		for (SignaturePropertiesType properties : propertiesList) {
			propertyList.addAll(properties.getSignatureProperty());
		}
		for (SignaturePropertyType property : propertyList) {
			for (Object object : property.getContent()) {
				if (object instanceof JAXBElement) {
					JAXBElement<?> el = (JAXBElement<?>) object;
					if (el.getValue() instanceof ReplayProtectType) {
						if (replayProtect == null) {
							replayProtect = (ReplayProtectType) el.getValue();
						} else {
							throw new Oadr20bXMLSignatureValidationException("Multiple ReplayProtect payload found");
						}
					}
				}
			}
		}

		if (replayProtect != null) {
			XMLGregorianCalendar timestamp = replayProtect.getTimestamp();

			Long xmlCalendarToTimestamp = Oadr20bFactory.xmlCalendarToTimestamp(timestamp);

			if (nowDate - xmlCalendarToTimestamp > millisecondAcceptedDelay) {
				throw new Oadr20bXMLSignatureValidationException("Invalid ReplayProtect timestamp");
			}
		}

	}

	/**
	 * validate oadrpayload signature.
	 * 
	 * @param payload
	 * @return
	 */
	public static void validate(String raw, OadrPayload payload, long nowDate, long millisecondAcceptedDelay)
			throws Oadr20bXMLSignatureValidationException {

		validateReplayProtect(payload, nowDate, millisecondAcceptedDelay);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder builder = null;
		Document doc;
		try {
			builder = dbf.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(raw)));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}

		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

		NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (nl.getLength() == 0) {
			throw new Oadr20bXMLSignatureValidationException("Cannot find Signature element");
		}

		DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));

		XMLSignature signature;
		try {
			signature = fac.unmarshalXMLSignature(valContext);
		} catch (MarshalException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}
		try {
			boolean validate = signature.validate(valContext);
			if (!validate) {
				if (!signature.getSignatureValue().validate(valContext)) {
					throw new Oadr20bXMLSignatureValidationException("XMLSignature SignatureValue validation fail");
				}
				for (Object object : signature.getSignedInfo().getReferences()) {
					if (object instanceof Reference) {
						Reference ref = (Reference) object;
						if (!ref.validate(valContext)) {
							throw new Oadr20bXMLSignatureValidationException(
									"XMLSignature Reference uri: " + ref.getURI() + " validation fail");
						}
					}
				}
			}
		} catch (XMLSignatureException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}

	}

	public static String sign(Object oadrObject, PrivateKey privateKey, X509Certificate certificate, String nonce,
			Long createdtimestamp) throws Oadr20bXMLSignatureException {
		if (createdtimestamp < 0) {
			throw new Oadr20bXMLSignatureException("createdtimestamp must be positive");
		}

		return sign(Oadr20bFactory.createOadrPayload(SIGNEDOBJECT_PAYLOAD_ID, oadrObject), privateKey, certificate,
				nonce, createdtimestamp);
	}

	/**
	 * sign oadrpayload and return document as a string
	 * 
	 * @param payload
	 * @param privateKey
	 * @param certificate
	 * @param nonce
	 * @param createdtimestamp
	 * @return
	 */
	private static String sign(OadrPayload payload, PrivateKey privateKey, X509Certificate certificate, String nonce,
			Long createdtimestamp) throws Oadr20bXMLSignatureException {

		// marshall payload as xmlstructure
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DOMResult res = new DOMResult();
		try {
			jaxbContext.marshal(payload, res);
		} catch (Oadr20bMarshalException e) {
			throw new Oadr20bXMLSignatureException(e);
		}
		Document doc = (Document) res.getNode();

		// Retreive nodes in xmlstructure
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		Element oadrPayloadNode = null;
		javax.xml.xpath.XPathExpression compile;
		try {
			compile = xpath.compile("//*[local-name()='oadrPayload']");
			oadrPayloadNode = (Element) compile.evaluate(doc, XPathConstants.NODE);

		} catch (XPathExpressionException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		// instanciate sign context
		DOMSignContext dsc = new DOMSignContext(privateKey, oadrPayloadNode);
		// manipulate java signature generation to conform to oadr expectation
		dsc.setDefaultNamespacePrefix("xmldsig");

		XMLSignatureFactory fac = null;
		SignedInfo si = null;
		try {
			fac = XMLSignatureFactory.getInstance("DOM");

			Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA256, null),
					Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
					null, null);

			// require digest replayProtect
			Reference refProp = fac.newReference("#" + SIGNATUREOBJECT_PAYLOAD_ID,
					fac.newDigestMethod(DigestMethod.SHA256, null),
					Collections.singletonList(
							fac.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null)),
					null, null);

			List<Reference> refs = new ArrayList<>();
			refs.add(ref);
			refs.add(refProp);

			// create signedInfo node containing references
			if (privateKey instanceof BCRSAPrivateKey) {
				si = fac.newSignedInfo(
						fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
						fac.newSignatureMethod(RSA_SHA256_ALGORITHM, null), refs);
			} else if (privateKey instanceof BCECPrivateKey) {
				si = fac.newSignedInfo(
						fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
						fac.newSignatureMethod(ECDSA_SHA256_ALGORITHM, null), refs);
			}

		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		List<XMLObject> lstruct = null;
		try {
			ReplayProtectType replayProtect = Oadr20bFactory.createReplayProtectType(nonce, createdtimestamp);

			DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
			newInstance.setNamespaceAware(true);

			List<DOMStructure> list = new ArrayList<>();
			JAXBElement<ReplayProtectType> createReplayProtect = Oadr20bFactory.createReplayProtect(replayProtect);
			DOMResult replayRes;
			replayRes = new DOMResult(dbf.newDocumentBuilder().newDocument());

			replayProtectContext.createMarshaller().marshal(createReplayProtect, replayRes);
			Document replay = (Document) replayRes.getNode();
			Element documentElement = replay.getDocumentElement();
			list.add(new DOMStructure(documentElement));

			List<XMLStructure> objectContent = new ArrayList<>();
			SignatureProperty newSignatureProperty = fac.newSignatureProperty(list, "", null);
			List<SignatureProperty> listSignatureProperty = new ArrayList<>();
			listSignatureProperty.add(newSignatureProperty);
			SignatureProperties newSignatureProperties = fac.newSignatureProperties(listSignatureProperty, null);
			objectContent.add(newSignatureProperties);
			XMLObject newXMLObject = fac.newXMLObject(objectContent, SIGNATUREOBJECT_PAYLOAD_ID, null, null);

			lstruct = new ArrayList<>();
			lstruct.add(newXMLObject);
		} catch (ParserConfigurationException | JAXBException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		KeyInfo ki = null;

		KeyInfoFactory kif = fac.getKeyInfoFactory();
		List<Object> x509Content = new ArrayList<>();
		x509Content.add(certificate.getSubjectX500Principal().getName());
		x509Content.add(certificate);
		X509Data xd = kif.newX509Data(x509Content);
		ki = kif.newKeyInfo(Collections.singletonList(xd));

		// sign payload
		XMLSignature signature = fac.newXMLSignature(si, ki, lstruct, null, null);
		
		try {
			signature.sign(dsc);
		} catch (MarshalException | XMLSignatureException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

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
		signed = signed.replaceAll("\n", "");
		return signed;

	}

}
