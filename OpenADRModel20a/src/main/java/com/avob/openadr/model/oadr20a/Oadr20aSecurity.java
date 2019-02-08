package com.avob.openadr.model.oadr20a;

public class Oadr20aSecurity {

    private static final String[] PROTOCOLS = new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" };

    private static final String[] CIPHERS = new String[] { "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_128_CBC_SHA" };

    private Oadr20aSecurity() {
    }

    public static String[] getProtocols() {
        return PROTOCOLS;
    }

    public static String[] getCiphers() {
        return CIPHERS;
    }

}
