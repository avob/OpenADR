package com.avob.openadr.server.oadr20b.ven;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

import com.avob.openadr.security.exception.OadrSecurityException;

public class VENJettyServerCustomizer implements JettyServerCustomizer {

    private int port;
    private KeyStore keystore;
    private KeyStore truststore;
    private String keystorePass;
    private String[] protocols;
    private String[] ciphers;

    public VENJettyServerCustomizer(int port, KeyStore keystore, String keystorePass, KeyStore truststore,
            String[] protocols, String[] ciphers) {
        this.port = port;
        this.keystore = keystore;
        this.truststore = truststore;
        this.keystorePass = keystorePass;
        this.protocols = protocols;
        this.ciphers = ciphers;
    }

    private HttpConfiguration getHttpConfiguration() {
        HttpConfiguration config = new HttpConfiguration();
        config.setSecureScheme("https");
        config.setSecurePort(port);
        config.setSendXPoweredBy(false);
        config.setSendServerVersion(false);
        config.addCustomizer(new SecureRequestCustomizer());

        return config;
    }

    private SslContextFactory getSslContextFactory()
            throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException,
            OadrSecurityException, UnrecoverableKeyException, KeyManagementException {
        // SSL Context Factory
        SslContextFactory sslContextFactory = new SslContextFactory();

        SSLContext sslContext = SSLContext.getInstance("TLS");

        // init key manager factory
        KeyStore createKeyStore = keystore;
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(createKeyStore, keystorePass.toCharArray());

        // init trust manager factory
        KeyStore createTrustStore = truststore;
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(createTrustStore);

        // init ssl context
        String seed = UUID.randomUUID().toString();

        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                new SecureRandom(seed.getBytes()));

        sslContextFactory.setSslContext(sslContext);

        // jetty 9 silently exclude required oadr20a default
        // cipher suite
        sslContextFactory.setExcludeCipherSuites("^.*_(MD5)$");
        sslContextFactory.setExcludeProtocols("SSLv3");
        sslContextFactory.setIncludeProtocols(this.protocols);
        sslContextFactory.setIncludeCipherSuites(this.ciphers);

        // require client certificate authentication
        sslContextFactory.setWantClientAuth(true);

        return sslContextFactory;

    }

    @Override
    public void customize(Server server) {

        try {
            HttpConfiguration config = getHttpConfiguration();

            HttpConnectionFactory http1 = new HttpConnectionFactory(config);

            SslConnectionFactory ssl = new SslConnectionFactory(this.getSslContextFactory(), "http/1.1");

            ServerConnector connector = new ServerConnector(server, ssl, http1);

            connector.setPort(port);
            Connector[] connectors = { connector };
            server.setConnectors(connectors);

        } catch (OadrSecurityException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        } catch (CertificateException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (KeyStoreException e) {
            throw new IllegalArgumentException(e);
        } catch (KeyManagementException e) {
            throw new IllegalArgumentException(e);
        } catch (UnrecoverableKeyException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
