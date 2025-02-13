package services;

import constant.RoleAuthorityEnum;
import jic.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleService {

    public void insertRolesIntoDatabase() {
        Connection c = DBConnection.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {


            if (c == null || c.isClosed()) {
                System.out.println("Database connection failed. Please try again later.");
                return;
            }

            String queryCheck = "SELECT COUNT(*) FROM role WHERE authority = ?";
            String queryInsert = "INSERT INTO role (authority) VALUES (?)";

            for (RoleAuthorityEnum role : RoleAuthorityEnum.values()) {
                // Check if the role already exists in the database
                stmt = c.prepareStatement(queryCheck);
                stmt.setString(1, role.name());  // Assuming the authority is the name of the enum

                rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    continue;
                }

                stmt = c.prepareStatement(queryInsert);
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
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
