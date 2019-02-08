package com.avob.openadr.client.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;

public class OadrHttpClient {

    private HttpClient client;

    private HttpClientContext defaultLocalContext;

    private URI defaultBaseUri;

    public OadrHttpClient(HttpClient client, URI defaultBaseUri, HttpClientContext localContext) {
        this.client = client;
        this.defaultLocalContext = localContext;
        this.defaultBaseUri = defaultBaseUri;
    }

    public HttpResponse execute(HttpPost request, String path)
            throws ClientProtocolException, IOException, URISyntaxException {
        URI uri = new URIBuilder(defaultBaseUri).setPath(defaultBaseUri.getPath() + path).build();
        request.setURI(uri);
        return client.execute(request, defaultLocalContext);
    }

    public HttpResponse execute(HttpPost request, String host, String path, HttpClientContext localContext)
            throws ClientProtocolException, IOException, URISyntaxException {
        URI uri = null;
        if (host != null) {
            uri = new URI(host + path);
        } else {
            uri = new URIBuilder(defaultBaseUri).setPath(defaultBaseUri.getPath() + path).build();
            request.setURI(uri);
        }
        request.setURI(uri);

        HttpClientContext context = defaultLocalContext;
        if (localContext != null) {
            context = localContext;
        }

        return client.execute(request, context);

    }
}
