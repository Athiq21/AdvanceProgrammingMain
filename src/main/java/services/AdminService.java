package services;

import jic.DBConnection;
import model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public static void getActivatedUsers(HttpServletResponse resp) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "DB Fail");
            return;
        }

        String query = "SELECT id, email, firstName, lastName, isActivated FROM user WHERE isActivated = 1";

        try (PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<User> activatedUsers = new ArrayList<>();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setIsActivated(rs.getInt("isActivated") == 1);
                activatedUsers.add(user);
            }

            sendSuccessResponse(resp, activatedUsers);

        } catch (SQLException e) {
            sendErrorResponse(resp, "Database Error");
        }
    }

    public static void getDeactivatedUsers(HttpServletResponse resp) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "DB Fail");
            return;
        }

        String query = "SELECT id, email, firstName, lastName, isActivated FROM user WHERE isActivated = 0";

        try (PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            List<User> activatedUsers = new ArrayList<>();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setIsActivated(rs.getInt("isActivated") == 1);
                activatedUsers.add(user);
            }

            sendSuccessResponse(resp, activatedUsers);

        } catch (SQLException e) {
            sendErrorResponse(resp, "Database Error");
        }
    }

    public static void deactivateUser(String email, HttpServletResponse resp) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "DB Fail");
            return;
        }

        String query = "UPDATE user SET isActivated = 0 WHERE email = ?";

        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, email);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                sendSuccessResponse(resp, "User activated successfully.");
            } else {
                sendErrorResponse(resp, "User not found.");
            }

        } catch (SQLException e) {
            sendErrorResponse(resp, "Database Error");
        }
    }

    public static void reactivateUser(String email, HttpServletResponse resp) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "DB Fail");
            return;
        }

        String query = "UPDATE user SET isActivated = 1 WHERE email = ?";

        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setString(1, email);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                sendSuccessResponse(resp, "User reactivated successfully.");
            } else {
                sendErrorResponse(resp, "User not found.");
            }

        } catch (SQLException e) {
            sendErrorResponse(resp, "Database Error");
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
