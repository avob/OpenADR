package com.avob.openadr.client.http.oadr20a;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.avob.openadr.client.http.OadrHttpClient;
import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.Oadr20aUrlPath;
import com.avob.openadr.model.oadr20a.exception.Oadr20aException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aHttpLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;

/**
 * Oadr 2.0a simple https client
 * 
 * use TLSv1, TLSv1.1, TLSv1.2 with a given PEM key
 * 
 * @author bertrand
 *
 */
public class OadrHttpClient20a {

	private OadrHttpClient client;

	private Oadr20aJAXBContext jaxbContext;

	public OadrHttpClient20a(OadrHttpClient client) throws JAXBException {

		this.jaxbContext = Oadr20aJAXBContext.getInstance();
		this.client = client;
	}

	/**
	 * Generic oadr 2.0a using default host/credentials
	 * 
	 * @param payload
	 * @param responseKlass
	 * @return
	 * @throws Oadr20aException
	 * @throws URISyntaxException
	 */
	public <T> T post(Object payload, String path, Class<T> responseKlass)
			throws Oadr20aException, Oadr20aHttpLayerException {
		return this.post(null, path, null, payload, responseKlass);
	}

	/**
	 * Generic oadr 2.0a using given host/credentials
	 * 
	 * @param payload
	 * @param responseKlass
	 * @return
	 * @throws Oadr20aException
	 * @throws Oadr20aHttpLayerException
	 * @throws URISyntaxException
	 */
	public <T> T post(String host, String path, HttpClientContext context, Object payload, Class<T> responseKlass)
			throws Oadr20aException, Oadr20aHttpLayerException {
		try {
			HttpPost post = new HttpPost();
			String marshal = jaxbContext.marshal(payload);
			StringEntity stringEntity = new StringEntity(marshal);
			post.setEntity(stringEntity);
			HttpResponse response = client.execute(post, host, Oadr20aUrlPath.OADR_BASE_PATH + path, context);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Oadr20aHttpLayerException(response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase());
			} else {
				return jaxbContext.unmarshal(EntityUtils.toString(entity, "UTF-8"), responseKlass);
			}

		} catch (Oadr20aMarshalException | IOException | URISyntaxException e) {
			throw new Oadr20aException(e);
		}
	}

}
