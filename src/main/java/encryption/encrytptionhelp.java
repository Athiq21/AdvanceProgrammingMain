//package encryption;
//
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESKeySpec;
//import java.net.URLEncoder;
//import java.io.UnsupportedEncodingException;
//import encryption.EncryptionHelperExt;
//import java.security.spec.KeySpec;
//
//public class encrytptionhelp {
//
//    private static final String PASS_PHRASE1 = "1234ABCD";
//
//        public static String encrypt(String msg,boolean encode) throws Exception {
//
//                KeySpec keySpec = new DESKeySpec(PASS_PHRASE1.getBytes());
//                SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
//                Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
//                ecipher.init(Cipher.ENCRYPT_MODE, key);
//
//                byte[] utf8 = msg.getBytes("UTF8");
//
//                byte[] enc = ecipher.doFinal(utf8);
//
//
//                String strReturnVal = new sun.misc.BASE64Encoder().encode(enc);
//
//                if (encode)
//                {
//                  strReturnVal = EncryptionHelperExt.encodeURL(strReturnVal);
//                }
//
//                return strReturnVal;
//
//            /*
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            return null;
//            */
//
//        }
//
//
//        public static String encodeURL(String url) throws Exception{
//
//                return URLEncoder.encode(url,"UTF-8");
//
//        }
//
//
//
//
//        public static String decrypt(String msg) throws Exception{
//
//                KeySpec keySpec = new DESKeySpec(PASS_PHRASE1.getBytes());
//                SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
//                Cipher decipher = Cipher.getInstance(key.getAlgorithm());
//                decipher.init(Cipher.DECRYPT_MODE, key);
//
//                byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(msg);
//
//                byte[] utf8 = decipher.doFinal(dec);
//
//                return new String(utf8, "UTF8");
//
//            /*
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            }
//            return null;
//            */
//
//        }
//}
