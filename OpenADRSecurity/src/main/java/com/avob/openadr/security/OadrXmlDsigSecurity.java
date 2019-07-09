package com.avob.openadr.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

public class OadrXmlDsigSecurity {

	public static byte[] signer(byte[] messageBytes, PrivateKey privateKey)
			throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException,
			IOException, DataLengthException, CryptoException {

		AsymmetricKeyParameter pk = PrivateKeyFactory.createKey(privateKey.getEncoded());
		RSADigestSigner signer = new RSADigestSigner(new SHA512Digest());
		signer.init(true, pk);
		signer.update(messageBytes, 0, messageBytes.length);

		return signer.generateSignature();

	}

}
