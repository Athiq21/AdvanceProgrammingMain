package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionHelperExt {

    public static String consistentHashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            String hashString = Base64.getEncoder().encodeToString(hash);
            for (int i = 0; i < 5; i++) {
                digest.reset();
                hash = digest.digest(hashString.getBytes());
                hashString = Base64.getEncoder().encodeToString(hash);
            }

            return hashString;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String hashPassword(String password) {
        return consistentHashPassword(password);
    }
    public static String generateSalt() {
        return Long.toHexString(System.nanoTime());
    }
}
