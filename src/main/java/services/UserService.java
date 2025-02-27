package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import encryption.EncryptionHelperExt;
import jic.DBConnection;
import model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class UserService {

    public static void updateUserDetails(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User updatedUser = mapper.readValue(req.getReader(), User.class);

        if (userId == null) {
            sendErrorResponse(resp, "User ID is missing");
            return;
        }

        Connection c = DBConnection.getInstance().getConnection();
        if (c == null) {
            sendErrorResponse(resp, "Database connection failed");
            return;
        }

        try {
            String sql = "UPDATE user SET firstName = ?, lastName = ? WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, updatedUser.getFirstName());
            ps.setString(2, updatedUser.getLastName());
            ps.setLong(3, userId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                sendSuccessResponse(resp, "User details updated successfully");
            } else {
                sendErrorResponse(resp, "Update failed");
            }
        } catch (SQLException e) {
            sendErrorResponse(resp, "SQL Error: " + e.getMessage());
        }
    }


    public static void changeUserPassword(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> requestBody = mapper.readValue(req.getReader(), Map.class);

        String newPassword = requestBody.get("password");
        if (newPassword == null || newPassword.isEmpty()) {
            sendErrorResponse(resp, "Password cannot be empty");
            return;
        }

        String hashedPassword = EncryptionHelperExt.hashPassword(newPassword);

        Connection c = DBConnection.getInstance().getConnection();
        if (c == null) {
            sendErrorResponse(resp, "Database connection failed");
            return;
        }

        try {
            String sql = "UPDATE user SET password = ? WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, hashedPassword);
            ps.setLong(2, userId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                sendSuccessResponse(resp, "Password updated successfully");
            } else {
                sendErrorResponse(resp, "Update failed");
            }
        } catch (SQLException e) {
            sendErrorResponse(resp, "SQL Error: " + e.getMessage());
        }
    }


    public static void deactivate(HttpServletRequest req, HttpServletResponse resp, Long userId) throws IOException {
        if (userId == null) {
            sendErrorResponse(resp, "User ID is missing");
            return;
        }

        Connection c = DBConnection.getInstance().getConnection();
        if (c == null) {
            sendErrorResponse(resp, "Database connection failed");
            return;
        }

        try {
            String sql = "UPDATE user SET isActivated = 0 WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setLong(1, userId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                sendSuccessResponse(resp, "User account deactivated successfully");
            } else {
                sendErrorResponse(resp, "Deactivation failed - User not found");
            }
        } catch (SQLException e) {
            sendErrorResponse(resp, "SQL Error: " + e.getMessage());
        }
    }


    private static void sendSuccessResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(data));
    }

    private static void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
