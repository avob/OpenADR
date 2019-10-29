package com.avob.openadr.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrFingerprintSecurityTest {

	@Test
	public void getOadr20aFingerprintTest() throws OadrSecurityException {
		String oadr20aFingerprint = OadrFingerprintSecurity.getOadr20aFingerprint(TestUtils.TEST_CRT);
		assertEquals(TestUtils.readFile(TestUtils.TEST_FINGERPRINT_20A), oadr20aFingerprint);
	}

	@Test
	public void getOadr20bFingerprintTest() throws OadrSecurityException {
		String oadr20bFingerprint = OadrFingerprintSecurity.getOadr20bFingerprint(TestUtils.TEST_CRT);
		assertEquals(TestUtils.readFile(TestUtils.TEST_FINGERPRINT_20B), oadr20bFingerprint);
	}

}
