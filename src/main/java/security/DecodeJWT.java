//package security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.JwtException;
//
//import java.nio.charset.StandardCharsets;
//
//public class DecodeJWT {
//
//    private static final String SECRET_KEY = "73c73aa573911d7c35832470160e1ac92e4605b4ceb0e9a7ba082f68198446cf21c1202db58f759fae0ee96f337130c39e56d5092429ddb0b769cda0c2758d7304b2252f4bedf755df92419aa4ee13f3c0e86be8add3b26c3b019ec7a49cb3afa3a40c8574ff474a5073a4750a87e313f62dff8311d319043fc9b15df27b1ba95c231d36c38cee2cdbc5d98a0f8da0c805447728d3422238491e083b682eb6d48b0f6003dee3e9c14f6578fcff2fa2586e4bb562b6fd41534b62e85c1e32812045462999d8553b624c9cdcccb28626bbc99167a550cb468d900adf06a985b811b73ad202cab100ac58fb9f8b36133e8630220d39b39644b3436dde3e0589fa76";
//
//
//    /**
//     * Extracts the user ID from a given JWT token.
//     *
//     * @param token The JWT token.
//     * @return The user ID as a Long.
//     * @throws Exception If the token is invalid or the user ID cannot be parsed.
//     */
//    public static Long extractUserId(String token) throws Exception {
//        try {
//            // Parse the JWT and extract claims
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)) // Use the same secret key used to sign the token
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // Extract the "sub" claim (user ID)
//            String userIdString = claims.getSubject();
//            if (userIdString == null || userIdString.isEmpty()) {
//                throw new Exception("Token does not contain a valid 'sub' claim.");
//            }
//
//            return Long.parseLong(userIdString);
//        } catch (JwtException | NumberFormatException e) {
//            throw new Exception("Invalid token or unable to extract user ID", e);
//        }
//    }
//}
