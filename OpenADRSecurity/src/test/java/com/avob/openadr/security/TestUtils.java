package com.avob.openadr.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.avob.openadr.security.exception.OadrSecurityException;

public class TestUtils {

	private TestUtils() {
	}

	public static final String TEST = "src/test/resources/cert/test";
	public static final String TEST_CRT = TEST + ".crt";
	public static final String TEST_KEY = TEST + ".key";
	public static final String TEST_FINGERPRINT_20B = TEST + ".fingerprint";
	public static final String TEST_FINGERPRINT_20A = TEST + ".fingerprint.oadr20a";
	
	
	public static final String TEST_ECC = "src/test/resources/cert/test";
	public static final String TEST_ECC_CRT = TEST_ECC + ".crt";
	public static final String TEST_ECC_KEY = TEST_ECC + ".key";
	public static final String TEST_ECC_FINGERPRINT_20B = TEST_ECC + ".fingerprint";
	public static final String TEST_ECC_FINGERPRINT_20A = TEST_ECC + ".fingerprint.oadr20a";

	public static String readFile(String path) throws OadrSecurityException {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			return reader.readLine();
		} catch (IOException ex) {
			throw new OadrSecurityException("Can't read generated fingerprint file");
		}
	}

	public static String readFile(File file) throws OadrSecurityException {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.readLine();
		} catch (IOException ex) {
			throw new OadrSecurityException("Can't read generated fingerprint file");
		}
	}

}
