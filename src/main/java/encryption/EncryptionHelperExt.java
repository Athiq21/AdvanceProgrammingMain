package encryption;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class EncryptionHelperExt {

    public static String encodeURL(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8");
    }
}
