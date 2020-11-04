package com.avob.openadr.client.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import com.avob.openadr.security.OadrPKISecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpClientBuilder {

	private HttpClientBuilder create;

	/**
	 * protocol config
	 */
	private String[] protocols;
	private String[] ciphers;

	/**
	 * SSL trusted certificate
	 */
	private List<String> trustedCertificateFilePath;

	/**
	 * default x509 client certificate
	 */
	private String clientPrivateKeyPemFilePath;
	private String clientCertificatePemFilePath;

	/**
	 * default Digest/Basic authentication
	 */
	private CredentialsProvider credsProvider;
	private HttpClientContext localContext = HttpClientContext.create();

	/**
	 * polling client
	 */
	private Integer totalConnection;
	private Integer totalPerRouteConnection;

	/**
	 * timeout
	 */
	private RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

	/**
	 * default host
	 */
	private URI defaultUri;

	/**
	 * default http headers
	 */
	private List<Header> headers = new ArrayList<>();

	/**
	 * enable http
	 */
	private boolean enableHttp = false;

	public OadrHttpClientBuilder() {
		create = HttpClientBuilder.create();
	}

	public OadrHttpClientBuilder withTrustedCertificate(List<String> trustedCertificateFilePath) {
		this.trustedCertificateFilePath = trustedCertificateFilePath;
		return this;
	}

	public OadrHttpClientBuilder withProtocol(String[] protocols, String[] ciphers) {
		this.protocols = protocols;
		this.ciphers = ciphers;
		return this;
	}

	public OadrHttpClientBuilder withX509Authentication(String clientPrivateKeyPemFilePath,
			String clientCertificatePemFilePath) throws OadrSecurityException {
		this.clientPrivateKeyPemFilePath = clientPrivateKeyPemFilePath;
		this.clientCertificatePemFilePath = clientCertificatePemFilePath;
		return this;
	}

	public static String buildNonce(String key) {
		// expirationTime + ":" + md5Hex(expirationTime + ":" + key)
		
		String dateTimeString = Long.toString(OffsetDateTime.now().plusMinutes(5).toEpochSecond() * 1000);
		String nonce = dateTimeString + ":" + OadrPKISecurity.md5Hex(dateTimeString + ":" + key);
		return Base64.getEncoder().encodeToString(nonce.getBytes());
	}

	public OadrHttpClientBuilder withDefaultDigestAuthentication(String host, String realm, String key, String username,
			String password) throws OadrSecurityException {

		this.withDefaultHost(host);

		HttpHost target = new HttpHost(defaultUri.getHost(), defaultUri.getPort(), defaultUri.getScheme());

		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
				new UsernamePasswordCredentials(username, password));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate DIGEST scheme object, initialize it and add it to the local
		// auth cache
		DigestScheme digestAuth = new DigestScheme();
		// Suppose we already know the realm name
		digestAuth.overrideParamter("realm", realm);
		// Suppose we already know the expected nonce value
		digestAuth.overrideParamter("nonce", buildNonce(key));
		authCache.put(target, digestAuth);

		localContext.setAuthCache(authCache);

		return this;
	}

	public OadrHttpClientBuilder withDefaultBasicAuthentication(String host, String username, String password)
			throws OadrSecurityException {

		this.withDefaultHost(host);
		HttpHost target = new HttpHost(defaultUri.getHost(), defaultUri.getPort(), defaultUri.getScheme());

		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(target.getHostName(), target.getPort()),
				new UsernamePasswordCredentials(username, password));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(target, basicAuth);

		localContext.setAuthCache(authCache);

		return this;
	}

	/**
	 * setup default header
	 * 
	 * @param header
	 * @param value
	 * @return
	 */
	public OadrHttpClientBuilder withHeader(String header, String value) {
		headers.add(new BasicHeader(header, value));
		return this;
	}

	/**
	 * set default host
	 * 
	 * @param host
	 * @return
	 * @throws URISyntaxException
	 */
	public OadrHttpClientBuilder withDefaultHost(String defaultHost) throws OadrSecurityException {
		try {
			defaultUri = new URI(defaultHost);

			if (defaultUri.getHost() == null) {
				throw new OadrSecurityException("given url must specify target host");
			}
		} catch (URISyntaxException e) {
			throw new OadrSecurityException(e);
		}
		return this;
	}

	/**
	 * setup PoolingHttpClientConnectionManager
	 * 
	 * @param totalConnection
	 * @param totalPerRouteConnection
	 * @return
	 */
	public OadrHttpClientBuilder withPooling(int totalConnection, int totalPerRouteConnection) {
		this.totalConnection = totalConnection;
		this.totalPerRouteConnection = totalPerRouteConnection;
		return this;
	}

	public OadrHttpClientBuilder withTimeout(int timeoutMilli) {
		requestConfigBuilder.setConnectTimeout(timeoutMilli);
		return this;
	}

	public OadrHttpClientBuilder enableHttp(boolean enable) {
		this.enableHttp = enable;
		return this;
	}

	public OadrHttpClient build() throws OadrSecurityException {

		String password = UUID.randomUUID().toString();
		SSLContext sc = OadrPKISecurity.createSSLContext(clientPrivateKeyPemFilePath, clientCertificatePemFilePath,
				this.trustedCertificateFilePath, password);
		SSLConnectionSocketFactory createSSLSocketFactory = new SSLConnectionSocketFactory(sc, protocols, ciphers,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		RegistryBuilder<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", createSSLSocketFactory);

		if (this.enableHttp) {
			register.register("http", PlainConnectionSocketFactory.getSocketFactory());
		}

		Registry<ConnectionSocketFactory> build = register.build();

		HttpClientConnectionManager connectionManager;
		if (totalConnection != null && totalPerRouteConnection != null) {
			connectionManager = new PoolingHttpClientConnectionManager(build);
			((PoolingHttpClientConnectionManager) connectionManager).setMaxTotal(totalConnection);
			((PoolingHttpClientConnectionManager) connectionManager).setDefaultMaxPerRoute(totalPerRouteConnection);
		} else {
			connectionManager = new BasicHttpClientConnectionManager(build);
		}

		HttpClientBuilder builder = create.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(requestConfigBuilder.build());

		if (credsProvider != null) {
			builder.setDefaultCredentialsProvider(credsProvider);
		}

		return new OadrHttpClient(builder.setDefaultHeaders(headers).build(), defaultUri, localContext);

	}

}
