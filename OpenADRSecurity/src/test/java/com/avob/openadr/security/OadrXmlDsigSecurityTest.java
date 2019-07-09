package com.avob.openadr.security;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import org.junit.Test;

public class OadrXmlDsigSecurityTest {

	@Test
	public void signerTest() throws NoSuchAlgorithmException {

		KeyPair generateEccKeyPair = OadrHttpSecurity.generateEccKeyPair();

		PrivateKey privateKey = generateEccKeyPair.getPrivate();
//		OadrXmlDsigSecurity.signer(messageBytes, privateKey);

	}

}
