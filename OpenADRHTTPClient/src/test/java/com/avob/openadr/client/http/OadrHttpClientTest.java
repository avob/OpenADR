package com.avob.openadr.client.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import org.apache.http.client.ClientProtocolException;
import org.junit.Test;

import com.avob.openadr.security.OadrHttpSecurity;
import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrHttpClientTest {

    private static final String rsaPrivateKeyPemFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_privkey.pem";
    private static final String rsaClientCertPemFilePath = "src/test/resources/rsa/TEST_RSA_VTN_17011882657_cert.pem";
    private static final String rsaClientCertSHA1Fingerprint = "86:72:C8:0C:8D:18:01:04:2A:33:74:3B:28:B5:90:4D:D2:D4:AD:8F";
    private static final String rsaClientCertSHA256Fingerprint = "81:0F:F6:97:C0:EA:D9:60:D5:BD:64:B6:CF:CB:A5:6F:E6:0B:36:4D:73:3C:8A:7D:CB:97:24:ED:35:24:2A:AD";
    private static final String rsaClientCertOadr20aFingerprint = "74:3B:28:B5:90:4D:D2:D4:AD:8F";
    private static final String rsaClientCertOadr20bFingerprint = "8A:7D:CB:97:24:ED:35:24:2A:AD";
    private static final String rsaTrustedRootCertificate = "src/test/resources/rsa/TEST_OpenADR_RSA_RCA0002_Cert.pem";
    private static final String rsaTrustedIntermediateCertificate = "src/test/resources/rsa/TEST_OpenADR_RSA_SPCA0002_Cert.pem";

    private static final String eccPrivateKeyPemFilePath = "src/test/resources/ecc/TEST_ECC_VTN_17011961812_privkey.pem";
    private static final String eccClientCertPemFilePath = "src/test/resources/ecc/TEST_ECC_VTN_17011961812_cert.pem";
    private static final String eccClientCertSHA1Fingerprint = "6D:9B:CF:E3:82:94:5B:0B:4E:EF:3A:D7:16:3F:5A:73:84:AA:24:A7";
    private static final String eccClientCertSHA256Fingerprint = "A6:83:F7:CE:4E:BC:E7:D5:DC:3E:61:53:7C:0D:0B:42:14:2F:66:3F:BE:49:FD:5B:51:D8:27:E5:B6:94:FF:C2";
    private static final String eccClientCertOadr20aFingerprint = "3A:D7:16:3F:5A:73:84:AA:24:A7";
    private static final String eccClientCertOadr20bFingerprint = "FD:5B:51:D8:27:E5:B6:94:FF:C2";
    private static final String eccTrustedRootCertificate = "src/test/resources/ecc/TEST_OpenADR_ECC_Root_CA3_cert.pem";
    private static final String eccTrustedIntermediateCertificate = "src/test/resources/ecc/TEST_OpenADR_ECC_SHA256_VTN_Int_CA3_cert.pem";

    private static final String[] protocols = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };
    private static final String[] ciphers = new String[] { "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_128_CBC_SHA" };
    private static final int TIMEOUT = 5000;

    @Test
    public void testInitBasicClient() throws OadrSecurityException {

        String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

        new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
                .withDefaultBasicAuthentication("http://localhost:8080", "", "")
                .withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

    }

    @Test
    public void testInitDigestClient() throws OadrSecurityException {

        String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

        new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
                .withDefaultDigestAuthentication("http://localhost:8080", "", "", "", "")
                .withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

    }

    @Test
    public void testInitRSAClient() throws OadrSecurityException, URISyntaxException {

        String[] certs = { rsaTrustedRootCertificate, rsaTrustedIntermediateCertificate };

        new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
                .withX509Authentication(rsaPrivateKeyPemFilePath, rsaClientCertPemFilePath)
                .withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

    }

    @Test
    public void testRSAFingerprint() throws OadrSecurityException {
        String fingerprint = OadrHttpSecurity.getFingerprint(rsaClientCertPemFilePath, "SHA1");
        assertEquals(fingerprint, rsaClientCertSHA1Fingerprint);

        fingerprint = OadrHttpSecurity.getFingerprint(rsaClientCertPemFilePath, "SHA-256");
        assertEquals(fingerprint, rsaClientCertSHA256Fingerprint);

        fingerprint = OadrHttpSecurity.getOadr20aFingerprint(rsaClientCertPemFilePath);
        assertEquals(fingerprint, rsaClientCertOadr20aFingerprint);

        fingerprint = OadrHttpSecurity.getOadr20bFingerprint(rsaClientCertPemFilePath);
        assertEquals(fingerprint, rsaClientCertOadr20bFingerprint);
    }

    @Test
    public void testInitECCClient() throws OadrSecurityException, ClientProtocolException, IOException,
            NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {

        String[] certs = { eccTrustedRootCertificate, eccTrustedIntermediateCertificate };

        new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
                .withX509Authentication(eccPrivateKeyPemFilePath, eccClientCertPemFilePath)
                .withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT).build();

    }

    @Test
    public void testInvalidX509() {

        String[] certs = { eccTrustedRootCertificate, eccTrustedIntermediateCertificate };

        boolean exception = false;
        try {
            new OadrHttpClientBuilder().withPooling(1, 1).withProtocol(protocols, ciphers)
                    .withX509Authentication("", "").withTrustedCertificate(Arrays.asList(certs)).withTimeout(TIMEOUT)
                    .build();
        } catch (OadrSecurityException e) {
            exception = true;
        }
        assertTrue(exception);

    }

    @Test
    public void testInvalidFingerprint() {
        boolean exception = false;
        try {
            OadrHttpSecurity.getFingerprint(rsaClientCertPemFilePath, "");
        } catch (OadrSecurityException e) {
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            OadrHttpSecurity.getFingerprint("", "SHA1");
        } catch (OadrSecurityException e) {
            exception = true;
        }
        assertTrue(exception);

        exception = false;
        try {
            OadrHttpSecurity.getFingerprint(rsaClientCertPemFilePath, null);
        } catch (OadrSecurityException e) {
            exception = true;
        }
        assertTrue(exception);

        String nullStr = null;
        exception = false;
        try {
            OadrHttpSecurity.getFingerprint(nullStr, "SHA-256");
        } catch (OadrSecurityException e) {
            exception = true;
        }
        assertTrue(exception);

    }

    @Test
    public void testECCFingerprint() throws OadrSecurityException {
        String fingerprint = OadrHttpSecurity.getFingerprint(eccClientCertPemFilePath, "SHA1");

        assertEquals(fingerprint, eccClientCertSHA1Fingerprint);

        fingerprint = OadrHttpSecurity.getFingerprint(eccClientCertPemFilePath, "SHA-256");

        assertEquals(fingerprint, eccClientCertSHA256Fingerprint);

        fingerprint = OadrHttpSecurity.getOadr20aFingerprint(eccClientCertPemFilePath);

        assertEquals(fingerprint, eccClientCertOadr20aFingerprint);

        fingerprint = OadrHttpSecurity.getOadr20bFingerprint(eccClientCertPemFilePath);

        assertEquals(fingerprint, eccClientCertOadr20bFingerprint);
    }

}
