package com.avob.openadr.model.oadr20b;

/**
 * Oadr20a protocols/cyphers have been copy paste
 * 
 * TODO bertrand: read oadr20b spec and fill correct informations
 * 
 * @author bertrand
 *
 */
public class Oadr20bSecurity {

    private static final String[] PROTOCOLS = new String[] { "TLSv1.2" };

    private static final String[] CIPHERS = new String[] { "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_GCM_SHA256"};

    private Oadr20bSecurity() {
    }

    public static String[] getProtocols() {
        return PROTOCOLS;
    }

    public static String[] getCiphers() {
        return CIPHERS;
    }

}
