//package servlet;
//
//import util.RandomPasswordUtil;
//import jic.DBConnection;
//import services.EmailService;
//import org.json.JSONObject;
//import java.io.BufferedReader;
//import javax.mail.MessagingException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.*;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.Jwts;
//
//@WebServlet("/api/auth/*")
//public class AuthServlet extends HttpServlet {
//
//    private PreparedStatement stmt = null;
//    private ResultSet rs = null;
//    private Connection conn = null;
//    private static final String SECRET_KEY ="73c73aa573911d7c35832470160e1ac92e4605b4ceb0e9a7ba082f68198446cf21c1202db58f759fae0ee96f337130c39e56d5092429ddb0b769cda0c2758d7304b2252f4bedf755df92419aa4ee13f3c0e86be8add3b26c3b019ec7a49cb3afa3a40c8574ff474a5073a4750a87e313f62dff8311d319043fc9b15df27b1ba95c231d36c38cee2cdbc5d98a0f8da0c805447728d3422238491e083b682eb6d48b0f6003dee3e9c14f6578fcff2fa2586e4bb562b6fd41534b62e85c1e32812045462999d8553b624c9cdcccb28626bbc99167a550cb468d900adf06a985b811b73ad202cab100ac58fb9f8b36133e8630220d39b39644b3436dde3e0589fa76";
//
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
//            return;
//        }
//
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//
//        if (path.equals("/signup")) {
//            handleSignup(req, resp);
//        }
//        else if(path.equals("/signin")) {
//            handleSignin(req, resp);
//        }
//        else if(path.equals("/otp/validate")) {
//            handleOtpVerification(req, resp);
//        }
//        else if(path.equals("/otp/resend")){
//       handleOtpResend(req,resp);
//        }
//        else {
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
//        }
//    }
//
//    private void handleOtpResend(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        StringBuilder jsonBody = new StringBuilder();
//        try (BufferedReader reader = req.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBody.append(line);
//            }
//        }
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(jsonBody.toString());
//        } catch (Exception e) {
//            sendErrorResponse(resp, "Invalid JSON format.");
//            return;
//        }
//
//        String email = jsonObject.optString("email");
//
//        if (email.isEmpty()) {
//            sendErrorResponse(resp, "Email is required to resend OTP.");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                sendErrorResponse(resp, "Database connection failed. Please try again later.");
//                return;
//            }
//
//            // Retrieve user data and check if the user exists
//            String query = "SELECT otp, otpExpiration, isActivated FROM user WHERE email = ?";
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                String storedOtp = rs.getString("otp");
//                Timestamp storedOtpExpiration = rs.getTimestamp("otpExpiration");
//                int isActivated = rs.getInt("isActivated");
//
//                if (isActivated == 1) {
//                    sendErrorResponse(resp, "User is already activated. OTP is not needed.");
//                    return;
//                }
//
//                // Generate a new OTP, regardless of whether the old OTP is valid or expired
//                String newOtp = RandomPasswordUtil.generateOTP(6);
//                Timestamp newOtpExpiration = getOtpExpirationTime();
//
//                // Update the OTP in the database
//                String updateQuery = "UPDATE user SET otp = ?, otpExpiration = ? WHERE email = ?";
//                stmt = conn.prepareStatement(updateQuery);
//                stmt.setString(1, newOtp);
//                stmt.setTimestamp(2, newOtpExpiration);
//                stmt.setString(3, email);
//                stmt.executeUpdate();
//
//                // Send the new OTP email to the user
//                EmailService emailService = new EmailService();
//                try {
//                    emailService.resendOtpEmail(email, newOtp, newOtpExpiration);
//                    sendSuccessResponse(resp, "OTP resent successfully to your email.");
//                } catch (MessagingException e) {
//                    sendErrorResponse(resp, "Error while sending the OTP email: " + e.getMessage());
//                }
//            } else {
//                sendErrorResponse(resp, "User not found.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            sendErrorResponse(resp, "Error during OTP resend process: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//
////    private void handleOtpResend(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        // Parse JSON body
////        StringBuilder jsonBody = new StringBuilder();
////        try (BufferedReader reader = req.getReader()) {
////            String line;
////            while ((line = reader.readLine()) != null) {
////                jsonBody.append(line);
////            }
////        }
////
////        JSONObject jsonObject;
////        try {
////            jsonObject = new JSONObject(jsonBody.toString());
////        } catch (Exception e) {
////            sendErrorResponse(resp, "Invalid JSON format.");
////            return;
////        }
////
////        String email = jsonObject.optString("email");
////
////        // Validate the input
////        if (email.isEmpty()) {
////            sendErrorResponse(resp, "Email is required to resend OTP.");
////            return;
////        }
////
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                sendErrorResponse(resp, "Database connection failed. Please try again later.");
////                return;
////            }
////
////            // Retrieve user data and check if the user exists
////            String query = "SELECT otp, otpExpiration, isActivated FROM vehuser WHERE email = ?";
////            stmt = conn.prepareStatement(query);
////            stmt.setString(1, email);
////            rs = stmt.executeQuery();
////
////            if (rs.next()) {
////                String storedOtp = rs.getString("otp");
////                Timestamp storedOtpExpiration = rs.getTimestamp("otpExpiration");
////                int isActivated = rs.getInt("isActivated");
////
////                if (isActivated == 1) {
////                    sendErrorResponse(resp, "User is already activated. OTP is not needed.");
////                    return;
////                }
////
////                // If OTP expired or does not exist, generate a new OTP
////                if (storedOtp == null || storedOtpExpiration.before(new Timestamp(System.currentTimeMillis()))) {
////                    String newOtp = RandomPasswordUtil.generateOTP(6);
////                    Timestamp newOtpExpiration = getOtpExpirationTime();
////
////                    // Update the OTP in the database
////                    String updateQuery = "UPDATE vehuser SET otp = ?, otpExpiration = ? WHERE email = ?";
////                    stmt = conn.prepareStatement(updateQuery);
////                    stmt.setString(1, newOtp);
////                    stmt.setTimestamp(2, newOtpExpiration);
////                    stmt.setString(3, email);
////                    stmt.executeUpdate();
////
////                    EmailService emailService = new EmailService();
////                    try {
////                        emailService.resendOtpEmail(email, newOtp, newOtpExpiration);
////                        sendSuccessResponse(resp, "OTP resent successfully to your email.");
////                    } catch (MessagingException e) {
////                        sendErrorResponse(resp, "Error while sending the OTP email: " + e.getMessage());
////                    }
////                } else {
////                    sendErrorResponse(resp, "OTP is still valid. Please check your email.");
////                }
////            } else {
////                sendErrorResponse(resp, "User not found.");
////            }
////        } catch (SQLException e) {
////            e.printStackTrace();
////            sendErrorResponse(resp, "Error during OTP resend process: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
//
//    private void handleSignin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        // Parse JSON body
//        StringBuilder jsonBody = new StringBuilder();
//        try (BufferedReader reader = req.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBody.append(line);
//            }
//        }
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(jsonBody.toString());
//        } catch (Exception e) {
//            sendErrorResponse(resp, "Invalid JSON format.");
//            return;
//        }
//
//        String email = jsonObject.optString("email");
//        String password = jsonObject.optString("password");
//
//        if (email == null || password == null) {
//            sendErrorResponse(resp, "Email and password are required for sign-in.");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                sendErrorResponse(resp, "Database connection failed. Please try again later.");
//                return;
//            }
//
//            String query = "SELECT u.password, u.isActivated, u.id, u.firstName, u.lastName, u.role_id, r.authority "
//                    + "FROM user u "
//                    + "JOIN role r ON u.role_id = r.id "
//                    + "WHERE u.email = ?";
//
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                String storedPassword = rs.getString("password");
//                int isActivated = rs.getInt("isActivated");
//                int userId = rs.getInt("id");
//                String firstName = rs.getString("firstName");
//                String lastName = rs.getString("lastName");
//                int roleId = rs.getInt("role_id");
//                String roleName = rs.getString("authority");
//
//                if (storedPassword.equals(password)) {
//                    if (isActivated == 1) {
//                        String accessToken = generateJwtToken(userId, email);
//
//                        sendSuccessResponseWithToken(resp, accessToken, userId, email, firstName, lastName,roleId,roleName);
//                    } else {
//                        sendErrorResponse(resp, "Account not activated. Please verify your email with the OTP.");
//                    }
//                } else {
//                    sendErrorResponse(resp, "Invalid email or password.");
//                }
//            } else {
//                sendErrorResponse(resp, "User not found.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            sendErrorResponse(resp, "Error during sign-in process: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//
//    private void sendSuccessResponseWithToken(HttpServletResponse resp, String accessToken, int userId, String email, String firstName, String lastName, int roleId, String roleName) throws IOException {
//        resp.setStatus(HttpServletResponse.SC_OK);
//        resp.setContentType("application/json");
//        String jsonResponse = "{"
//                + "\"message\": \"Sign-in successful.\", "
//                + "\"access_token\": \"" + accessToken + "\", "
//                + "\"user\": {"
//                + "\"id\": \"" + userId + "\", "
//                + "\"email\": \"" + email + "\", "
//                + "\"firstName\": \"" + firstName + "\", "
//                + "\"lastName\": \"" + lastName + "\""
//                + "}, "
//                + "\"role\": ["
//                + "{"
//                + "\"role_id\":  \"" + roleId + "\", "
//                + "\"roleName\":  \"" + roleName + "\""
//                + "}"
//                + "]"
//                + "}";
//
//        resp.getWriter().write(jsonResponse);
//    }
//
//
//    private String generateJwtToken(int userId, String email) {
//        long now = System.currentTimeMillis();
//        long expirationTime = now + 60 * 60 * 1000;
//
//        return Jwts.builder()
//                .setSubject(String.valueOf(userId))
//                .claim("email", email)
//                .setIssuedAt(new Date(now))
//                .setExpiration(new Date(expirationTime))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    private void sendSuccessResponseWithToken(HttpServletResponse resp, String jwtToken) throws IOException {
//        resp.setStatus(HttpServletResponse.SC_OK);
//        resp.setContentType("application/json");
//        resp.getWriter().write("{\"message\":\"Sign-in successful.\", \"access_token\":\"" + jwtToken + "\"}");
//    }
//
//
//
//
////    private void handleSignup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String email = req.getParameter("email");
////        String password = req.getParameter("password");
////        String firstName = req.getParameter("firstName");
////        String lastName = req.getParameter("lastName");
////
////        if (email == null || firstName == null || lastName == null || password == null) {
////            sendErrorResponse(resp, "Email, password, first name, and last name are required.");
////            return;
////        }
////
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                sendErrorResponse(resp, "Database connection failed. Please try again later.");
////                return;
////            }
////
////            if (isEmailExists(email)) {
////                sendErrorResponse(resp, "Email already in use. Please use a different email.");
////                return;
////            }
////
////            String otp = RandomPasswordUtil.generateOTP(6);
////            Timestamp otpExpiration = getOtpExpirationTime();
////
////            insertUser(email, password, firstName, lastName, otp, otpExpiration);
////
////            EmailService emailService = new EmailService();
////            emailService.sendOtpEmail(email, otp, otpExpiration);
////
////            sendSuccessResponse(resp, "User signed up successfully. OTP sent to your email for verification.");
////        } catch (Exception e) {
////            e.printStackTrace();
////            sendErrorResponse(resp, "Error during sign-up process: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////
////    }
//
//    private void handleSignup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        // Parse JSON body
//        StringBuilder jsonBody = new StringBuilder();
//        try (BufferedReader reader = req.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBody.append(line);
//            }
//        }
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(jsonBody.toString());
//        } catch (Exception e) {
//            sendErrorResponse(resp, "Invalid JSON format.");
//            return;
//        }
//
//        // Extract required fields from the JSON
//        String email = jsonObject.optString("email");
//        String password = jsonObject.optString("password");
//        String firstName = jsonObject.optString("firstName");
//        String lastName = jsonObject.optString("lastName");
//
//        // Validate the input
//        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
//            sendErrorResponse(resp, "Email, password, first name, and last name are required.");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                sendErrorResponse(resp, "Database connection failed. Please try again later.");
//                return;
//            }
//
//            if (isEmailExists(email)) {
//                sendErrorResponse(resp, "Email already in use. Please use a different email.");
//                return;
//            }
//
//            String otp = RandomPasswordUtil.generateOTP(6);
//            Timestamp otpExpiration = getOtpExpirationTime();
//
//            int rid = getRoleId("ROLE_USER");
//
//            insertUser(email, password, firstName, lastName, otp, otpExpiration,rid);
//
//            EmailService emailService = new EmailService();
//            emailService.sendOtpEmail(email, otp, otpExpiration);
//
//            sendSuccessResponse(resp, "User signed up successfully. OTP sent to your email for verification.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            sendErrorResponse(resp, "Error during sign-up process: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private int getRoleId(String roleName) throws SQLException {
//        String query = "SELECT id FROM role WHERE authority = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, roleName);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("id"); // Return the role ID
//                } else {
//                    throw new SQLException("Role not found: " + roleName);
//                }
//            }
//        }
//    }
//
//
//    private Timestamp getOtpExpirationTime() {
//        long currentTime = System.currentTimeMillis();
//        return new Timestamp(currentTime + 5 * 60 * 1000);  // 5 minutes expiration
//    }
//
//    private void handleOtpVerification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        // Parse JSON body
//        StringBuilder jsonBody = new StringBuilder();
//        try (BufferedReader reader = req.getReader()) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonBody.append(line);
//            }
//        }
//
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject(jsonBody.toString());
//        } catch (Exception e) {
//            sendErrorResponse(resp, "Invalid JSON format.");
//            return;
//        }
//
//        String enteredOtp = jsonObject.optString("otp");
//        String email = jsonObject.optString("email");
//
//        // Validate the input
//        if (enteredOtp.isEmpty()) {
//            sendErrorResponse(resp, "OTP is required for verification.");
//            return;
//        }
//        if (email.isEmpty()) {
//            sendErrorResponse(resp, "Email is required for OTP verification.");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                sendErrorResponse(resp, "Database connection failed. Please try again later.");
//                return;
//            }
//
//            // Retrieve OTP and expiration from the database
//            String query = "SELECT otp, otpExpiration, isActivated FROM user WHERE email = ?";
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                String storedOtp = rs.getString("otp");
//                Timestamp storedOtpExpiration = rs.getTimestamp("otpExpiration");
//                int isActivated = rs.getInt("isActivated");
//
//                // Check if OTP matches and is not expired
//                if (storedOtp.equals(enteredOtp) && storedOtpExpiration.after(new Timestamp(System.currentTimeMillis()))) {
//                    // Update the user as activated
//                    if (isActivated == 0) {
//                        updateUserActivation(email);  // Set isActivated to 1
//                        sendSuccessResponse(resp, "OTP verified successfully. User activated.");
//                    } else {
//                        sendSuccessResponse(resp, "OTP verified, but the user is already activated.");
//                    }
//                } else {
//                    sendErrorResponse(resp, "Invalid OTP or OTP has expired.");
//                }
//            } else {
//                sendErrorResponse(resp, "User not found.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            sendErrorResponse(resp, "Error during OTP verification: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//
//    private void updateUserActivation(String email) throws SQLException {
//        String query = "UPDATE user SET isActivated = 1 WHERE email = ?";
//        try {
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Error while updating user activation status", e);
//        }
//    }
//
//
//    private boolean isEmailExists(String email) throws SQLException {
//        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
//        try {
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt(1) > 0;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Error while checking email existence", e);
//        }
//        return false;
//    }
//
//    private void insertUser(String email, String password, String firstName, String lastName, String otp, Timestamp otpExpiration, int role_id) throws SQLException {
//        String query = "INSERT INTO user (email, password, firstName, lastName, otp, otpExpiration, role_id) VALUES (?, ?, ?, ?, ?, ?,?)";
//        try {
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, email);
//            stmt.setString(2, password);
//            stmt.setString(3, firstName);
//            stmt.setString(4, lastName);
//            stmt.setString(5, otp);
//            stmt.setTimestamp(6, otpExpiration);
//            stmt.setInt(7, role_id);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new SQLException("Error while inserting user and OTP into the database", e);
//        }
//
//    }
//
//    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
//        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        resp.setContentType("application/json");
//        resp.getWriter().write("{\"error\":\"" + message + "\"}");
//    }
//
//    private void sendSuccessResponse(HttpServletResponse resp, String message) throws IOException {
//        resp.setStatus(HttpServletResponse.SC_OK);
//        resp.setContentType("application/json");
//        resp.getWriter().write("{\"message\":\"" + message + "\"}");
//    }
//
//    private void closeResources() {
//        try {
//            if (rs != null) {
//                rs.close();
//            }
//            if (stmt != null) {
//                stmt.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

package servlet;

import org.json.JSONObject;
import services.AuthService;
import services.EmailService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {
//TEST MODE MAKE THE MODIFY ACCESSORS PUBLIC AND CHANGE TO PRIVATE LATER
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "NO API");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/signup":
                handleSignup(req, resp);
                break;
            case "/signin":
                handleSignin(req, resp);
                break;
            case "/otp/validate":
                handleOtpVerification(req, resp);
                break;
            case "/otp/resend":
                handleOtpResend(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NO API");
        }
    }

    private void handleSignup(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = getJsonFromRequest(req);
        String email = jsonObject.optString("email");
        String password = jsonObject.optString("password");
        String firstName = jsonObject.optString("firstName");
        String lastName = jsonObject.optString("lastName");

        AuthService.handleSignup(email, password, firstName, lastName, resp);
    }

    private void handleSignin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = getJsonFromRequest(req);
        String email = jsonObject.optString("email");
        String password = jsonObject.optString("password");

        AuthService.handleSignin(email, password, resp);
    }

    private void handleOtpVerification(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = getJsonFromRequest(req);
        String email = jsonObject.optString("email");
        String enteredOtp = jsonObject.optString("otp");

        AuthService.handleOtpVerification(email, enteredOtp, resp);
    }

    private void handleOtpResend(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = getJsonFromRequest(req);
        String email = jsonObject.optString("email");

        AuthService.handleOTPResends(email, resp);
    }

    private JSONObject getJsonFromRequest(HttpServletRequest req) throws IOException {
        StringBuilder jsonBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
        }
        return new JSONObject(jsonBody.toString());
    }
}
