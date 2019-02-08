package com.avob.openadr.model.oadr20b.xmlsignature;

import java.io.StringWriter;
import java.io.Writer;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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
import javax.xml.crypto.dsig.SignatureMethod;
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
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.datatype.XMLGregorianCalendar;
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
		} catch (JAXBException e) {
			throw new Oadr20bInitializationException(e);
		}
	}

	private OadrXMLSignatureHandler() {
	}

	private static <T> List<T> asElement(List<Object> list, Class<T> klass) {
		List<T> res = new ArrayList<T>();
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
		List<SignaturePropertiesType> propertiesList = new ArrayList<SignaturePropertiesType>();
		for (ObjectType objectType : objects) {
			propertiesList.addAll(OadrXMLSignatureHandler.<SignaturePropertiesType>asElement(objectType.getContent(),
					SignaturePropertiesType.class));
		}
		List<SignaturePropertyType> propertyList = new ArrayList<SignaturePropertyType>();
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

		if (replayProtect == null) {
			throw new Oadr20bXMLSignatureValidationException("XML Signature must include ReplayProtect payload");
		}

		XMLGregorianCalendar timestamp = replayProtect.getTimestamp();

		Long xmlCalendarToTimestamp = Oadr20bFactory.xmlCalendarToTimestamp(timestamp);

		if (nowDate - xmlCalendarToTimestamp > millisecondAcceptedDelay) {
			throw new Oadr20bXMLSignatureValidationException("Invalid ReplayProtect timestamp");
		}
	}

	/**
	 * validate oadrpayload signature.
	 * 
	 * @param payload
	 * @return
	 */
	public static void validate(OadrPayload payload, long nowDate, long millisecondAcceptedDelay)
			throws Oadr20bXMLSignatureValidationException {

		validateReplayProtect(payload, nowDate, millisecondAcceptedDelay);

		DOMResult res = new DOMResult();
		try {
			jaxbContext.marshal(payload, res);
		} catch (Oadr20bMarshalException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}
		Document doc = (Document) res.getNode();

		// Find Signature element
		NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		if (nl.getLength() == 0) {
			throw new Oadr20bXMLSignatureValidationException("Cannot find Signature element");
		}

		// Retreive Assertion Node to be signed.
		XPathFactory xPathfactory = XPathFactory.newInstance();

		XPath xpath = xPathfactory.newXPath();
		Element assertionNode = null;
		javax.xml.xpath.XPathExpression compile;
		try {
			compile = xpath.compile("//*[local-name()='oadrSignedObject']");
			assertionNode = (Element) compile.evaluate(doc, XPathConstants.NODE);
			assertionNode.setIdAttribute("oadr:Id", true);
		} catch (XPathExpressionException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}

		// Create a DOM XMLSignatureFactory that will be used to unmarshal the
		// document containing the XMLSignature
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
				new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());

		// Create a DOMValidateContext and specify a KeyValue KeySelector
		// and document context
		DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(0));
		valContext.setIdAttributeNS(assertionNode, "http://openadr.org/oadr-2.0b/2012/07", "Id");

		XMLSignature signature;
		try {
			signature = fac.unmarshalXMLSignature(valContext);
		} catch (MarshalException e) {
			throw new Oadr20bXMLSignatureValidationException(e);
		}

		DOMValidateContext context = new DOMValidateContext(new KeyValueKeySelector(), doc.getDocumentElement());
		context.setIdAttributeNS(assertionNode, "http://openadr.org/oadr-2.0b/2012/07", "Id");

		try {
			boolean validate = signature.validate(context);
			if (!validate) {
				if (!signature.getSignatureValue().validate(context)) {
					throw new Oadr20bXMLSignatureValidationException("XMLSignature SignatureValue validation fail");
				}
				for (Object object : signature.getSignedInfo().getReferences()) {
					if (object instanceof Reference) {
						Reference ref = (Reference) object;
						if (!ref.validate(context)) {
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
		Element signedObjectNode = null;
		Element oadrPayloadNode = null;
		javax.xml.xpath.XPathExpression compile;
		try {
			compile = xpath.compile("//*[local-name()='oadrSignedObject']");
			signedObjectNode = (Element) compile.evaluate(doc, XPathConstants.NODE);
			signedObjectNode.setIdAttribute("oadr:Id", true);
			compile = xpath.compile("//*[local-name()='oadrPayload']");
			oadrPayloadNode = (Element) compile.evaluate(doc, XPathConstants.NODE);

		} catch (XPathExpressionException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		// instanciate sign context
		DOMSignContext dsc = new DOMSignContext(privateKey, oadrPayloadNode, signedObjectNode);
		// manipulate java signature generation to conform to oadr expectation
		dsc.setDefaultNamespacePrefix("xmldsig");
		dsc.putNamespacePrefix("http://openadr.org/oadr-2.0b/2012/07/xmldsig-properties", "properties");
		// set id of signedObjectNode (required for URI reference/sign a part of
		// doc)
		dsc.setIdAttributeNS(signedObjectNode, "http://openadr.org/oadr-2.0b/2012/07", "Id");

		BouncyCastleProvider provider = new BouncyCastleProvider();
		Security.addProvider(provider);

		PublicKey publicKey = certificate.getPublicKey();

		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
				new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());

		SignedInfo si = null;
		try {
			// require digest signedObjectNode
			Reference ref = fac.newReference("#" + SIGNEDOBJECT_PAYLOAD_ID,
					fac.newDigestMethod(DigestMethod.SHA256, null),
					Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
					// null,
					null, null);

			// require digest replayProtect
			Reference refProp = fac.newReference("#" + SIGNATUREOBJECT_PAYLOAD_ID,
					fac.newDigestMethod(DigestMethod.SHA256, null),
					Collections.singletonList(
							fac.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null)),
					null, null);

			List<Reference> refs = new ArrayList<Reference>();
			refs.add(ref);
			refs.add(refProp);

			// create signedInfo node containing references
			if (privateKey instanceof BCRSAPrivateKey) {
				si = fac.newSignedInfo(
						fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
						fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), refs);
			} else if (privateKey instanceof BCECPrivateKey) {
				si = fac.newSignedInfo(
						fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
						fac.newSignatureMethod(ECDSA_SHA256_ALGORITHM, null), refs);
			}

		} catch (NoSuchAlgorithmException e) {
			throw new Oadr20bXMLSignatureException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		KeyInfo ki = null;
		try {
			KeyInfoFactory kif = fac.getKeyInfoFactory();
			KeyValue kv = kif.newKeyValue(publicKey);
			List<KeyValue> keyInfoItems = new ArrayList<KeyValue>();
			keyInfoItems.add(kv);

			ki = kif.newKeyInfo(keyInfoItems);
		} catch (KeyException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		List<XMLStructure> lstruct = null;
		try {
			ReplayProtectType replayProtect = Oadr20bFactory.createReplayProtectType(nonce, createdtimestamp);

			DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
			newInstance.setNamespaceAware(true);

			List<DOMStructure> list = new ArrayList<DOMStructure>();
			JAXBElement<ReplayProtectType> createReplayProtect = Oadr20bFactory.createReplayProtect(replayProtect);
			DOMResult replayRes;
			replayRes = new DOMResult(dbf.newDocumentBuilder().newDocument());

			replayProtectContext.createMarshaller().marshal(createReplayProtect, replayRes);
			Document replay = (Document) replayRes.getNode();
			Element documentElement = replay.getDocumentElement();
			list.add(new DOMStructure(documentElement));

			List<XMLStructure> objectContent = new ArrayList<XMLStructure>();
			SignatureProperty newSignatureProperty = fac.newSignatureProperty(list, "", null);
			List<SignatureProperty> listSignatureProperty = new ArrayList<SignatureProperty>();
			listSignatureProperty.add(newSignatureProperty);
			SignatureProperties newSignatureProperties = fac.newSignatureProperties(listSignatureProperty, null);
			objectContent.add(newSignatureProperties);
			XMLObject newXMLObject = fac.newXMLObject(objectContent, SIGNATUREOBJECT_PAYLOAD_ID, null, null);
			lstruct = new ArrayList<XMLStructure>();
			lstruct.add(newXMLObject);
		} catch (ParserConfigurationException e) {
			throw new Oadr20bXMLSignatureException(e);
		} catch (JAXBException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		// sign payload
		XMLSignature signature = fac.newXMLSignature(si, ki, lstruct, null, null);
		try {
			signature.sign(dsc);
		} catch (MarshalException e) {
			throw new Oadr20bXMLSignatureException(e);
		} catch (XMLSignatureException e) {
			throw new Oadr20bXMLSignatureException(e);
		}

		// manipulate payload namespace to include those used in Signature
		// payload
		oadrPayloadNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmldsig11",
				"http://www.w3.org/2009/xmldsig11#");

		oadrPayloadNode.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xmldsig",
				"http://www.w3.org/2000/09/xmldsig#");

		DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
		LSSerializer serializer = domImplLS.createLSSerializer();

		LSOutput lsOutput = domImplLS.createLSOutput();
		// set utf8 xml prolog
		lsOutput.setEncoding("UTF-8");
		Writer stringWriter = new StringWriter();
		lsOutput.setCharacterStream(stringWriter);
		serializer.write(doc, lsOutput);

		return stringWriter.toString();

	}

}
