package services;

import encryption.EncryptionHelperExt;
import util.RandomPasswordUtil;
import jic.DBConnection;
import services.EmailService;
import model.User;
import org.json.JSONObject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.github.cdimascio.dotenv.Dotenv;

public class AuthService {
//HIDE LATER AND CALL {${SECRET_KEY}}
//    private static final Dotenv env = Dotenv.load();
    private static final String SECRET_KEY = "73c73aa573911d7c35832470160e1ac92e4605b4ceb0e9a7ba082f68198446cf21c1202db58f759fae0ee96f337130c39e56d5092429ddb0b769cda0c2758d7304b2252f4bedf755df92419aa4ee13f3c0e86be8add3b26c3b019ec7a49cb3afa3a40c8574ff474a5073a4750a87e313f62dff8311d319043fc9b15df27b1ba95c231d36c38cee2cdbc5d98a0f8da0c805447728d3422238491e083b682eb6d48b0f6003dee3e9c14f6578fcff2fa2586e4bb562b6fd41534b62e85c1e32812045462999d8553b624c9cdcccb28626bbc99167a550cb468d900adf06a985b811b73ad202cab100ac58fb9f8b36133e8630220d39b39644b3436dde3e0589fa76";
//    Connection c = null;
//    ResultSet rs= null;
//    private PreparedStatement stmt = null;


    public static void handleOTPResends(String email, HttpServletResponse resp) throws IOException {
        try {Connection c = DBConnection.getInstance().getConnection();
            if (c == null || c.isClosed()) {
                sendErrorResponse(resp, "DB Fail");
                return;
            }
            if (email.isEmpty()) {
                sendErrorResponse(resp, "Email Needed");
                return;
            }
            String query = "SELECT otp, otpExpiration, isActivated FROM user WHERE email = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
//                String storedOtp = rs.getString("otp");
//                Timestamp storedOtpExpiration = rs.getTimestamp("otpExpiration");
                int isActivated = rs.getInt("isActivated");

                if (isActivated == 1) {
                    sendErrorResponse(resp, "User Activated");
                    return;
                }

                String newOtp = RandomPasswordUtil.generateOTP(6);
                Timestamp newOtpExpiration = getOtpExpirationTime();

                String updateQuery = "UPDATE user SET otp = ?, otpExpiration = ? WHERE email = ?";
                stmt = c.prepareStatement(updateQuery);
                stmt.setString(1, newOtp);
                stmt.setTimestamp(2, newOtpExpiration);
                stmt.setString(3, email);
                stmt.executeUpdate();

                EmailService emailService = new EmailService();
                try {
                    emailService.resendOtpEmail(email, newOtp, newOtpExpiration);
                    sendSuccessResponse(resp, "OTP resent success.");
                } catch (MessagingException e) {
                    sendErrorResponse(resp, "OTP Problem" + e.getMessage());
                }
            } else {
                sendErrorResponse(resp, "Error 1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error 2 " + e.getMessage());
        }
    }

    public static void handleSignup(String email, String password, String firstName, String lastName, HttpServletResponse resp) throws IOException {
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            sendErrorResponse(resp, "Email, password, first name, and last name are required.");
            return;
        }


            try (Connection c = DBConnection.getInstance().getConnection()) {
                if (c == null) {
                    sendErrorResponse(resp, "Con null");
                    return;
                }
                if (c.isClosed()) {
                    sendErrorResponse(resp, "Cn close b4 insert");
                    return;
                }


            if (isEmailExists(c, email)) {
                sendErrorResponse(resp, "Email already in use");
                return;
            }

            String Upper2PW = EncryptionHelperExt.consistentHashPassword(password);

            String otp = RandomPasswordUtil.generateOTP(6);
            Timestamp otpExpiration = getOtpExpirationTime();

            int roleId = getRoleId(c, "ROLE_USER");

            insertUser(c, email, Upper2PW, firstName, lastName, otp, otpExpiration, roleId);

            EmailService emailService = new EmailService();
            emailService.sendOtpEmail(email, otp, otpExpiration);

            sendSuccessResponse(resp, "signed up successfully. OTP sent to email");

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error Signup1: " + e.getMessage());
        }
    }

    public static void handleSignin(String email, String password, HttpServletResponse resp) throws IOException {
        try (Connection c = DBConnection.getInstance().getConnection();) {
            if (c == null || c.isClosed()) {
                sendErrorResponse(resp, "Db no work");
                return;
            }

            String query = "SELECT u.password, u.isActivated, u.id, u.firstName, u.lastName, u.role_id, r.authority "
                    + "FROM user u "
                    + "JOIN role r ON u.role_id = r.id "
                    + "WHERE u.email = ?";

            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    boolean isActivated = rs.getBoolean("isActivated");
                    int userId = rs.getInt("id");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    int roleId = rs.getInt("role_id");
                    String roleName = rs.getString("authority");

                    try {
                        String hashedPassword = EncryptionHelperExt.consistentHashPassword(password);
                        if (hashedPassword != null && hashedPassword.equals(storedPassword)) {
                            if (isActivated) {
                                String accessToken = generateJwtToken(userId, email);
                                sendSuccessResponseWithToken(resp, accessToken, userId, email, firstName, lastName, roleId, roleName);
                            } else {
                                sendErrorResponse(resp, "Account not activated. Please verify your email with the OTP.");
                            }
                        } else {
                            sendErrorResponse(resp, "Wrong mail, pw.");
                        }
                    } catch (Exception e) {
                        sendErrorResponse(resp, "PW HASH: " + e.getMessage());
                    }
                } else {
                    sendErrorResponse(resp, "no use");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Signup N/w: " + e.getMessage());
        }
    }


    public static void handleOtpVerification(String email, String enteredOtp, HttpServletResponse resp) throws IOException {
        try (Connection c = DBConnection.getInstance().getConnection();) {
            if (c == null || c.isClosed()) {
                sendErrorResponse(resp, "asdasd");
                return;
            }

            String query = "SELECT otp, otpExpiration, isActivated FROM user WHERE email = ?";
            try (PreparedStatement stmt = c.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String storedOtp = rs.getString("otp");
                    Timestamp storedOtpExpiration = rs.getTimestamp("otpExpiration");
                    boolean isActivated = rs.getBoolean("isActivated");

                    if (storedOtp.equals(enteredOtp) && storedOtpExpiration.after(new Timestamp(System.currentTimeMillis()))) {
                        if (!isActivated) {
                            updateUserActivation(c, email);
                            sendSuccessResponse(resp, "OTP verified successfully. User activated.");
                        } else {
                            sendSuccessResponse(resp, "Error 1.3");
                        }
                    } else {
                        sendErrorResponse(resp, "Error 1.4");
                    }
                } else {
//                    sendErrorResponse(resp, "no use");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Error 1.5 " + e.getMessage());
        }
    }

    private static boolean isEmailExists(Connection conn, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private static void insertUser(Connection c, String email, String Upper2PW, String firstName, String lastName, String otp, Timestamp otpExpiration, int roleId) throws SQLException {
        String query = "INSERT INTO user (email, password, firstName, lastName, otp, otpExpiration, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, Upper2PW);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, otp);
            stmt.setTimestamp(6, otpExpiration);
            stmt.setInt(7, roleId);
            stmt.executeUpdate();
        }
    }

    private static int getRoleId(Connection c, String roleName) throws SQLException {
        String query = "SELECT id FROM role WHERE authority = ?";
        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException(roleName);
            }
        }
    }

    private static Timestamp getOtpExpirationTime() {
        long currentTime = System.currentTimeMillis();
        return new Timestamp(currentTime + 5 * 60 * 1000);
    }

    private static void updateUserActivation(Connection conn, String email) throws SQLException {
        String query = "UPDATE user SET isActivated = 1 WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        }
    }

    private static String generateJwtToken(int userId, String email) {
        long now = System.currentTimeMillis();
        long expirationTime = now + 60 * 60 * 1000;

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static void sendSuccessResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(message );
    }

    private static void sendSuccessResponseWithToken(HttpServletResponse resp, String accessToken, int id, String email, String firstName, String lastName, int roleId, String roleName) throws IOException {
//        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        String jsonResponse = "{"
                + "\"message\": \"Sign-in successful\","
                + "\"accessToken\": \"" + accessToken + "\","
                + "\"user\": {"
                + "\"id\": " + id + ","
                + "\"email\": \"" + email + "\","
                + "\"firstName\": \"" + firstName + "\","
                + "\"lastName\": \"" + lastName + "\","
                + "\"roleId\": " + roleId + ","
                + "\"roleName\": \"" + roleName + "\""
                + "}"
                + "}";
        resp.getWriter().write(jsonResponse);
    }

    private static void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.setContentType("application/json");
        resp.getWriter().write( message);
    }
}
