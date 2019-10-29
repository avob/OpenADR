package com.avob.openadr.security;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.avob.openadr.security.exception.OadrSecurityException;

public class OadrFingerprintSecurityTest {

	@Test
	public void getOadr20aFingerprintTest() throws OadrSecurityException {
		String oadr20aFingerprint = OadrFingerprintSecurity.getOadr20aFingerprint("src/test/resources/cert/test.crt");
		assertEquals(readFile("src/test/resources/cert/test.fingerprint.oadr20a"), oadr20aFingerprint);
	}

	@Test
	public void getOadr20bFingerprintTest() throws OadrSecurityException {
		String oadr20bFingerprint = OadrFingerprintSecurity.getOadr20bFingerprint("src/test/resources/cert/test.crt");
		assertEquals(readFile("src/test/resources/cert/test.fingerprint"), oadr20bFingerprint);
	}

	private String readFile(String path) throws OadrSecurityException {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			return reader.readLine();

		} catch (IOException ex) {
			throw new OadrSecurityException("Can't read generated fingerprint file");
		}
	}

}
