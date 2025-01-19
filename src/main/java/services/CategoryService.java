package services;
import jic.DBConnection;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public static List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT id, name FROM category";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }
        }
        return categories;
    }

    public static Category getCategoryById(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT id, name FROM category WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public static boolean addCategory(Category category) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO category (name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean updateCategory(Category category) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE category SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean deleteCategory(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
