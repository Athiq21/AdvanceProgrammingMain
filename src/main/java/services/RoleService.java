package services;

import constant.RoleAuthorityEnum;
import jic.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleService {

    public void insertRolesIntoDatabase() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Get database connection
            conn = DBConnection.getConnection();

            if (conn == null || conn.isClosed()) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String queryCheck = "SELECT COUNT(*) FROM role WHERE authority = ?";
            String queryInsert = "INSERT INTO role (authority) VALUES (?)";

            for (RoleAuthorityEnum role : RoleAuthorityEnum.values()) {
                // Check if the role already exists in the database
                stmt = conn.prepareStatement(queryCheck);
                stmt.setString(1, role.name());  // Assuming the authority is the name of the enum

                rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    continue;
                }

                stmt = conn.prepareStatement(queryInsert);
                stmt.setString(1, role.name()); // Assuming the name of the enum is used as authority

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
