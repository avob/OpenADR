package com.avob.openadr.model.oadr20b.xmlsignature;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.TestUtils;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;

public class OadrXMLSignatureNodeRedTest {

	private final String payload = "<p0:oadrPayload xmlns:p0=\"http://openadr.org/oadr-2.0b/2012/07\"><p0:oadrSignedObject p0:Id=\"signedObject\"><p0:oadrResponse><p1:eiResponse xmlns:p1=\"http://docs.oasis-open.org/ns/energyinterop/201110\"><p1:responseCode>200</p1:responseCode><p1:responseDescription>OK</p1:responseDescription><p2:requestID xmlns:p2=\"http://docs.oasis-open.org/ns/energyinterop/201110/payloads\">39dcfb9d-545d-4393-bf8b-bbe9b4707fc8</p2:requestID></p1:eiResponse><p1:venID xmlns:p1=\"http://docs.oasis-open.org/ns/energyinterop/201110\">B5:FD:68:14:C1:67:7B:F8:55:37</p1:venID></p0:oadrResponse></p0:oadrSignedObject><xmldsig:Signature xmlns:xmldsig=\"http://www.w3.org/2000/09/xmldsig#\"><xmldsig:SignedInfo><xmldsig:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><xmldsig:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><xmldsig:Reference URI=\"\"><xmldsig:Transforms><xmldsig:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></xmldsig:Transforms><xmldsig:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><xmldsig:DigestValue>Dm3GcBsBRXxMpCW0jFtrf0sdtW8=</xmldsig:DigestValue></xmldsig:Reference></xmldsig:SignedInfo><xmldsig:SignatureValue>MXdxtItlb2CWGqnotn9ISSJgZylIS4uR3c4roxKCGdCCu82HP880DyaQQu3c2fsAo4m0i+QSNytHFpq+JxrPktXtWwPP0It7tfqEWp3jT09vhcbX8KR6VJFYldNc0ro0Uv7ahxh8EYvdb70XekuIOic22LgIkNdHRfsrckYPPHmTR0FkvrsR9uC/YjifL0r8fKru8VhxX2qUgnCUhwyTkxjDCKumbmdWujjYbFxqT1EnGqQiuLKCHrXg4IGXkUYMY2W2nst8TcPGt5IT119qCegy4gtP8dhkrwK8tJGwpfWpS/a4h/DOVps2qudarwlfdraTU7a3s4oMbVEA1IvrUQ==</xmldsig:SignatureValue><xmldsig:KeyInfo><xmldsig:KeyValue xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><xmldsig:RSAKeyValue><xmldsig:Modulus>hD60xhD6rOhHhURS9tRV/SIXhA4R+MUqnu0HnVnbBzO4yoz2fKF/Cej5THeGT7gwxEcFCa+cLuY7Po8GywVZgX0jyuy1yPgUfhqiD8VqT6kE08lYOsSiYI/b5/sLSGIUdkU7IxqIPsTN1mJoOfpTacqa0xOn2Ua6J7wgw6UT9CUSzMQ1w9tlLjWlNZotl3oEYNF1kcGM5JJ5eF3n2kyHNsngy7u642dFVrrIqOWzxQF6ifF1sX0dkNBrGaywoq4gUFW+zqRXeYhlo5tTe0315IPMXo4Bhm2XngXZnD6aA/MCP7pCWzYrTlILgZTpFZQ2XABXExFSw0ua0I/wDL98rw==</xmldsig:Modulus><xmldsig:Exponent>AQAB</xmldsig:Exponent></xmldsig:RSAKeyValue></xmldsig:KeyValue></xmldsig:KeyInfo></xmldsig:Signature></p0:oadrPayload>";
	private Oadr20bJAXBContext jaxbContext;

	private final String payload2 = "<oadr:oadrPayload xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:avob=\"http://oadr.avob.com\" xmlns:ei=\"http://docs.oasis-open.org/ns/energyinterop/201110\" xmlns:emix=\"http://docs.oasis-open.org/ns/emix/2011/06\" xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:greenbutton=\"http://naesb.org/espi\" xmlns:ns15=\"http://docs.oasis-open.org/ns/emix/2011/06/siscale\" xmlns:ns16=\"urn:un:unece:uncefact:codelist:standard:5:ISO42173A:2010-04-07\" xmlns:oadr=\"http://openadr.org/oadr-2.0b/2012/07\" xmlns:power=\"http://docs.oasis-open.org/ns/emix/2011/06/power\" xmlns:properties=\"http://openadr.org/oadr-2.0b/2012/07/xmldsig-properties\" xmlns:pyld=\"http://docs.oasis-open.org/ns/energyinterop/201110/payloads\" xmlns:strm=\"urn:ietf:params:xml:ns:icalendar-2.0:stream\" xmlns:xcal=\"urn:ietf:params:xml:ns:icalendar-2.0\" xmlns:xmldsig=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xmldsig11=\"http://www.w3.org/2009/xmldsig11#\"><oadr:oadrSignedObject oadr:Id=\"signedObject\"><oadr:oadrRequestReregistration ei:schemaVersion=\"2.0b\"><ei:venID>71:14:FE:08:AF:38:90:28:D9:C8</ei:venID></oadr:oadrRequestReregistration></oadr:oadrSignedObject><xmldsig:Signature xmlns:xmldsig=\"http://www.w3.org/2000/09/xmldsig#\"><xmldsig:SignedInfo><xmldsig:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><xmldsig:SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><xmldsig:Reference URI=\"\"><xmldsig:Transforms><xmldsig:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></xmldsig:Transforms><xmldsig:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><xmldsig:DigestValue>I/WtzwJTlfT3D6YpKsJw7NaVme4iWUuyrKoELBG2mmE=</xmldsig:DigestValue></xmldsig:Reference><xmldsig:Reference URI=\"#prop\"><xmldsig:Transforms><xmldsig:Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/></xmldsig:Transforms><xmldsig:DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><xmldsig:DigestValue>TRcqt4RX0jMUzoSLOisikMQcbVhsHMX8lwwbTEV74tw=</xmldsig:DigestValue></xmldsig:Reference></xmldsig:SignedInfo><xmldsig:SignatureValue>y+Mg5JURaytuqzvs2SM/qyaNyI21R0WBz7a7GTusM6Ujp5GCJPEuHAu/iWXwG3TVhduPJBP78S2dQ5n2N6cmLLipo6TD11Urzx8NMOqqd+BDpD0qkhsgKvMXf1VLfPhVqhMVyHzKtWUiKHXCJ9wuJajddyUrTrMfQ78Q5tes0z28LDvhzxYKqh1PbzUEyQhkmsSpDVVnoKTohGFjfqouTBRXAu32lp8zbFzecNbIzYRIFqYIgsYnC2ZLoW8AbwtRQkJRE384Q0kokiedVPHeqba1c6oZ1lHZWGWSQtbIwdYSbwLsrxCcU8ZELtk5nmY712laSwAvU1xCUSfcJzubbQ==</xmldsig:SignatureValue><xmldsig:KeyInfo><xmldsig:KeyValue><xmldsig:RSAKeyValue><xmldsig:Modulus>8WigunLswIDrRTWp1LOnK/H+qu8p6eLBenWg4Gu+H0clCIBQvLD/AYk6Qb38pPoKWIH94vVT1OnL5bvVghlL4ZHRXl/UyaWU5THmSTJoCW7Ykj1lBYDwPA5XhK5KFqrkodUavGRtpcIz00M/puEX4w6TfF/kauxzXnh9aY8jVIH5hxa+tAqo0xhB2cKgu3D3AY+bHJtGFUCiAwtN6EXsIVTGZyWYcHunY7VZdgCD3E64Wv/GmKCLkbDv3OObVsoDyyEevUIPUhEGJoDX5KY2MK5GIAoInkd8TyxP6eupbN3w606YhA3vDSIMatT1BuL5U3MJFUlbwPjmNu1TeTro0w==</xmldsig:Modulus><xmldsig:Exponent>AQAB</xmldsig:Exponent></xmldsig:RSAKeyValue></xmldsig:KeyValue></xmldsig:KeyInfo><xmldsig:Object Id=\"prop\"><xmldsig:SignatureProperties><xmldsig:SignatureProperty Target=\"\"><properties:ReplayProtect xmlns:properties=\"http://openadr.org/oadr-2.0b/2012/07/xmldsig-properties\"><properties:timestamp>2019-02-17T10:23:19Z</properties:timestamp><properties:nonce>93f87d91-53af-4f6a-83cc-36b790df0c32</properties:nonce></properties:ReplayProtect></xmldsig:SignatureProperty></xmldsig:SignatureProperties></xmldsig:Object></xmldsig:Signature></oadr:oadrPayload>";

	public OadrXMLSignatureNodeRedTest() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance(TestUtils.XSD_OADR20B_SCHEMA);
	}

	@Test
	public void validate() throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrPayload unmarshal = jaxbContext.unmarshal(payload, OadrPayload.class);
		OadrXMLSignatureHandler.validate(payload, unmarshal, 0, 10);
	}

	@Test
	public void validate2() throws Oadr20bUnmarshalException, Oadr20bXMLSignatureValidationException {
		OadrPayload unmarshal = jaxbContext.unmarshal(payload2, OadrPayload.class);
		OadrXMLSignatureHandler.validate(payload2, unmarshal, 0, 10);
	}

}
