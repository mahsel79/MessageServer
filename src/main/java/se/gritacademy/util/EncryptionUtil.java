package se.gritacademy.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionUtil {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65536;
    private static final byte[] SALT = "GritAcademy2025".getBytes(StandardCharsets.UTF_8);
    private static final String SECRET_KEY;

    static {
        String key = System.getenv("ENCRYPTION_KEY");
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("ENCRYPTION_KEY environment variable must be set");
        }
        SECRET_KEY = key;
    }

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
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT, ITERATIONS, KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate encryption key", e);
        }
    }
}
