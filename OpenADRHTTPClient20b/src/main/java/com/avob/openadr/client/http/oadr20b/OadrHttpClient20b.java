package com.avob.openadr.client.http.oadr20b;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.avob.openadr.client.http.OadrHttpClient;
import com.avob.openadr.model.oadr20b.Oadr20bFactory;
import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bHttpLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.xmlsignature.OadrXMLSignatureHandler;
import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

/**
 * Oadr 2.0b profile HTTP client
 * 
 * @author bzanni
 *
 */
public class OadrHttpClient20b {

	private OadrHttpClient client;

	/**
	 * xml signature
	 */
	private Long replayProtectAcceptedDelaySecond;
	private PrivateKey privateKey;
	private X509Certificate clientCertificate;

	private Oadr20bJAXBContext jaxbContext;

	private boolean validateXmlPayload = false;

	public OadrHttpClient20b(OadrHttpClient client) throws JAXBException, OadrSecurityException {
		this(client, null, null, null, null);
	}

	public OadrHttpClient20b(OadrHttpClient client, String privateKeyPath, String clientCertificatePath,
			Long replayProtectAcceptedDelaySecond) throws JAXBException, OadrSecurityException {
		this(client, privateKeyPath, clientCertificatePath, replayProtectAcceptedDelaySecond, null);
	}

	public OadrHttpClient20b(OadrHttpClient client, String privateKeyPath, String clientCertificatePath,
			Long replayProtectAcceptedDelaySecond, Boolean validateXmlPayload)
			throws JAXBException, OadrSecurityException {
		this.jaxbContext = Oadr20bJAXBContext.getInstance("src/test/resources/oadr20b_schema/");
		this.client = client;

		if (privateKeyPath != null && clientCertificatePath != null) {
			this.privateKey = OadrPKISecurity.parsePrivateKey(privateKeyPath);
			this.clientCertificate = OadrPKISecurity.parseCertificate(clientCertificatePath);
		}

		this.replayProtectAcceptedDelaySecond = replayProtectAcceptedDelaySecond;
		if (validateXmlPayload != null) {
			this.validateXmlPayload = validateXmlPayload;
		}
	}

	private boolean isXmlSignatureEnabled() {
		return this.privateKey != null && this.clientCertificate != null
				&& this.replayProtectAcceptedDelaySecond != null;
	}

	/**
	 * Generic oadr 2.0b using default host/credentials
	 * 
	 * @param payload
	 * @param responseKlass
	 * @return
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20aException
	 * @throws URISyntaxException
	 */
	public <T, I extends JAXBElement<?>> T post(I payload, String path, Class<T> responseKlass) throws Oadr20bException,
			Oadr20bHttpLayerException, Oadr20bXMLSignatureException, Oadr20bXMLSignatureValidationException {
		return this.post(null, path, null, payload, responseKlass);
	}

	/**
	 * Generic oadr 2.0b using given host/credentials
	 * 
	 * @param payload
	 * @param responseKlass
	 * @return
	 * @throws Oadr20bHttpLayerException
	 * @throws Oadr20bXMLSignatureException
	 * @throws Oadr20bXMLSignatureValidationException
	 * @throws Oadr20aException
	 * @throws URISyntaxException
	 */
	public <O, I extends JAXBElement<?>> O post(String host, String path, HttpClientContext context, I payload,
			Class<O> responseKlass) throws Oadr20bException, Oadr20bHttpLayerException, Oadr20bXMLSignatureException,
			Oadr20bXMLSignatureValidationException {
		try {
			HttpPost post = new HttpPost();
			String marshal = null;
			if (isXmlSignatureEnabled()) {
				marshal = this.sign(payload.getValue());
			} else {
				marshal = jaxbContext.marshal(payload, validateXmlPayload);
			}

			StringEntity stringEntity = new StringEntity(marshal);
			post.setEntity(stringEntity);
			HttpResponse response = client.execute(post, host, Oadr20bUrlPath.OADR_BASE_PATH + path, context);

			// if request did not result in 200 http code throw exception
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				EntityUtils.consumeQuietly(response.getEntity());
				throw new Oadr20bHttpLayerException(statusCode,
						String.valueOf(statusCode));
			}

			// if request was a success, validate xml signature if required and then
			// unmarshall response
			if (isXmlSignatureEnabled()) {
				String entity = EntityUtils.toString(response.getEntity(), "UTF-8");
				OadrPayload unmarshal = jaxbContext.unmarshal(entity, OadrPayload.class, validateXmlPayload);
				this.validate(entity, unmarshal);
				EntityUtils.consumeQuietly(response.getEntity());
				if (Object.class.equals(responseKlass)) {
					Object signedObjectFromOadrPayload = Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal);
					return responseKlass.cast(signedObjectFromOadrPayload);
				} else {
					return Oadr20bFactory.getSignedObjectFromOadrPayload(unmarshal, responseKlass);
				}

			} else {
				String resp = EntityUtils.toString(response.getEntity(), "UTF-8");
				return jaxbContext.unmarshal(resp, responseKlass, validateXmlPayload);
			}

		} catch (IOException | URISyntaxException | Oadr20bUnmarshalException | Oadr20bMarshalException e) {
			throw new Oadr20bException(e);
		}
	}

	private String sign(Object object) throws Oadr20bXMLSignatureException {
		String nonce = UUID.randomUUID().toString();
		Long createdtimestamp = System.currentTimeMillis();
		return OadrXMLSignatureHandler.sign(object, this.privateKey, this.clientCertificate, nonce, createdtimestamp);
	}

	private void validate(String raw, OadrPayload payload) throws Oadr20bXMLSignatureValidationException {
		long nowDate = System.currentTimeMillis();
		OadrXMLSignatureHandler.validate(raw, payload, nowDate, replayProtectAcceptedDelaySecond * 1000L);
	}

}
