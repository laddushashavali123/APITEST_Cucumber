package br.com.experian.cucumber.integration.cucumber.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class CypherUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CypherUtil.class);
	private static final String AES = "AES";

	public static String crypt(String value) {
		String passSalt = SaltUtil.salt(value);
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passSalt.getBytes());
			return bytesToHex(md.digest());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes)
			result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		return result.toString();
	}

	public static String encryptAES(String text, String key) {
		try {
			Key keySpec = new SecretKeySpec(key.getBytes(), AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] bytes = cipher.doFinal(text.getBytes());
			String encryptedValue = new String(Base64.getEncoder().encode(bytes));
			return encryptedValue;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String decryptAES(String text, String key) {
		try {
			Key keySpec = new SecretKeySpec(key.getBytes(), AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] bytes = Base64.getDecoder().decode(text.getBytes());
			byte[] decValue = cipher.doFinal(bytes);
			String decryptedValue = new String(decValue);
			return decryptedValue;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
}