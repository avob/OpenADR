package com.avob.openadr.model.oadr20b.xmlsignature;

import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.List;

import javax.xml.crypto.AlgorithmMethod;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.KeySelectorResult;
import javax.xml.crypto.XMLCryptoContext;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class KeyValueKeySelector extends KeySelector {

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;

        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        @Override
        public Key getKey() {
            return pk;
        }
    }

    @Override
    public KeySelectorResult select(KeyInfo keyInfo, KeySelector.Purpose purpose, AlgorithmMethod method,
            XMLCryptoContext context) throws KeySelectorException {
        if (keyInfo == null) {
            throw new KeySelectorException("Null KeyInfo object!");
        }
        SignatureMethod sm = (SignatureMethod) method;
        List<?> list = keyInfo.getContent();

        for (int i = 0; i < list.size(); i++) {
            XMLStructure xmlStructure = (XMLStructure) list.get(i);
            if (xmlStructure instanceof KeyValue) {
                PublicKey pk = null;
                try {
                    pk = ((KeyValue) xmlStructure).getPublicKey();
                } catch (KeyException ke) {
                    throw new KeySelectorException(ke);
                }
                // make sure algorithm is compatible with method
                if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                    return new SimpleKeySelectorResult(pk);
                }
            }
        }
        throw new KeySelectorException("No KeyValue element found!");
    }

    // TODO bertrand: this should also work for key types other than DSA/RSA
    static boolean algEquals(String algURI, String algName) {
        boolean equals = false;
        if ("DSA".equalsIgnoreCase(algName) && algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
            equals = true;
        } else if ("RSA".equalsIgnoreCase(algName) && algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
            equals = true;
        } else if ("RSA".equalsIgnoreCase(algName)
                && algURI.equalsIgnoreCase(OadrXMLSignatureHandler.RSA_SHA256_ALGORITHM)) {
            equals = true;
        } else if (algName.equalsIgnoreCase("EC") && algURI.equalsIgnoreCase(OadrXMLSignatureHandler.ECDSA_SHA256_ALGORITHM)) {
            return true;
        } 
        return equals;
    }
}