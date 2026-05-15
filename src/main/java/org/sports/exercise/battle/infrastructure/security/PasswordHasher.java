package org.sports.exercise.battle.infrastructure.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class PasswordHasher {
    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String hashPassword(char[] password) throws Exception{
        try {
            //generate random salt
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);

            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();

            //combine salt and hash
            //format salt:hash
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } finally{
            //zero out the password for security
            Arrays.fill(password, '\0');
        }
    }

    public static boolean verifyPassword(char[] password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);

            byte[] testHash = secretKeyFactory.generateSecret(spec).getEncoded();

            return Arrays.equals(testHash, hash);

        } catch (Exception e) {
            return false;
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
