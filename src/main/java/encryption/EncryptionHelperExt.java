package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionHelperExt {

    public static String consistentHashPassword(String password) {
        try {
            MessageDigest nopeek = MessageDigest.getInstance("SHA-256");
            byte[] hash = nopeek.digest(password.getBytes());
            String hashString = Base64.getEncoder().encodeToString(hash);
            for (int i = 0; i < 5; i++) {
                nopeek.reset();
                hash = nopeek.digest(hashString.getBytes());
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
    public static String genny() {
        return Long.toHexString(System.nanoTime());
    }

    public void main(String args[]){
        System.out.println(consistentHashPassword(args[0]));
        System.out.println(hashPassword("Batman"));

//        return genny();
    }

}
