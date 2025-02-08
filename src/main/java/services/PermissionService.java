package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jic.DBConnection;
import model.Role;
import model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PermissionService {

    public static void getUsersWithRoleId1(HttpServletResponse resp) throws IOException {
        getUsersByRoleId(resp, 1);
    }

    public static void getUsersWithRoleId3(HttpServletResponse resp) throws IOException {
        getUsersByRoleId(resp, 3);
    }

    private static void getUsersByRoleId(HttpServletResponse resp, int roleId) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "Database Connection Failed");
            return;
        }

        String query = "SELECT u.id, u.email, u.firstName, u.lastName, u.isActivated, r.authority " +
                "FROM user u " +
                "JOIN role r ON u.role_id = r.id " +
                "WHERE u.role_id = ?";

        try (PreparedStatement stmt = c.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            ResultSet rs = stmt.executeQuery();

            List<User> users = new ArrayList<>();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setIsActivated(rs.getBoolean("isActivated"));

                Role role = new Role();
                role.setAuthority(rs.getString("authority"));
                user.setRole(role);

                users.add(user);
            }

            sendSuccessResponse(resp, users);

        } catch (SQLException e) {
            sendErrorResponse(resp, "Database Error");
        }
    }


    public static void updateUserRole(String email, String newRole, HttpServletResponse resp) throws IOException {
        Connection c = DBConnection.getInstance().getConnection();

        if (c == null) {
            sendErrorResponse(resp, "Database Connection Failed");
            return;
        }

        String getRoleIdQuery = "SELECT id FROM role WHERE authority = ?";
        String updateUserRoleQuery = "UPDATE user SET role_id = ? WHERE email = ?";

        try (PreparedStatement roleStmt = c.prepareStatement(getRoleIdQuery)) {
            roleStmt.setString(1, newRole);
            ResultSet rs = roleStmt.executeQuery();

            if (rs.next()) {
                int roleId = rs.getInt("id");

                try (PreparedStatement userStmt = c.prepareStatement(updateUserRoleQuery)) {
                    userStmt.setInt(1, roleId);
                    userStmt.setString(2, email);

                    int rowsUpdated = userStmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        sendSuccessResponse(resp, "User role updated successfully.");
                    } else {
                        sendErrorResponse(resp, "User not found.");
                    }
                }

            } else {
                sendErrorResponse(resp, "Role not found.");
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
