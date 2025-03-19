package se.gritacademy.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "ThisIsASecretKey"; // In production, use a secure key management system

    public static String encrypt(String value) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedByteValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedByteValue);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting", e);
        }
    }

    public static String decrypt(String value) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedValue = Base64.getDecoder().decode(value);
            byte[] decryptedByteValue = cipher.doFinal(decryptedValue);
            return new String(decryptedByteValue, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting", e);
        }
    }

    private static Key generateKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }
}
