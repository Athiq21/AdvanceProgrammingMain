package services;

import Interface.SubCategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jic.DBConnection;
import model.Category;
import model.SubCategory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubCategoryService implements SubCategoryServiceImpl {
    private PreparedStatement stmt = null;
    private ResultSet rs = null;
    Connection c = DBConnection.getInstance().getConnection();

    @Override
    public void getSubCategoriesByCategoryId(HttpServletRequest req, HttpServletResponse resp, int categoryId) throws IOException {
        List<SubCategory> subCategoryList = new ArrayList<>();
        try {
            if (c == null || c.isClosed()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
                return;
            }

            String query = "SELECT sc.id, sc.name, sc.category_id, c.name AS category_name " +
                    "FROM sub_category sc " +
                    "JOIN category c ON sc.category_id = c.id " +
                    "WHERE sc.category_id = ?";
            stmt = c.prepareStatement(query);
            stmt.setInt(1, categoryId);
            rs = stmt.executeQuery();


            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setId(rs.getInt("id"));
                subCategory.setName(rs.getString("name"));

                Category category = new  Category(rs.getInt("category_id"), rs.getString("category_name"));
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));

                subCategory.setCategory(category);

                subCategoryList.add(subCategory);
            }

            // Send the list of subcategories as JSON response
            sendSubCategoryResponse(resp, subCategoryList);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    @Override
    public void getAllSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<SubCategory> subCategoryList = new ArrayList<>();
        try {
            if (c == null || c.isClosed()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
                return;
            }

            // Query to get subcategories with category details
            String query = "SELECT sc.id, sc.name, sc.category_id, c.name AS category_name " +
                    "FROM sub_category sc " +
                    "JOIN category c ON sc.category_id = c.id";
            stmt = c.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                SubCategory subCategory = new SubCategory();
                subCategory.setId(rs.getInt("id"));
                subCategory.setName(rs.getString("name"));

                Category category = new  Category(rs.getInt("category_id"), rs.getString("category_name"));
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));

                subCategory.setCategory(category);

                subCategoryList.add(subCategory);
            }

            // Send the list of subcategories as JSON response
            sendSubCategoryResponse(resp, subCategoryList);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void sendSubCategoryResponse(HttpServletResponse resp, List<SubCategory> subCategories) throws IOException {
        // Convert the list of SubCategory objects to a list of maps
        List<Map<String, Object>> response = subCategories.stream()
                .map(sub -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", sub.getId());
                    map.put("name", sub.getName());

                    // Add the full category object in the response
                    Category category = sub.getCategory();
                    if (category != null) {
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put("id", category.getId());
                        categoryMap.put("name", category.getName());
                        map.put("category", categoryMap);  // Add category object to subcategory map
                    }

                    return map;
                })
                .collect(Collectors.toList());

        // Convert the list to JSON and send it as response
        resp.setContentType("application/json");
        resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }


    @Override
    public void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jsonBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonBody.toString());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return;
        }

        String name = jsonObject.optString("name");
        int categoryId = jsonObject.optInt("category_id", -1);

        if (name == null || name.isEmpty() || categoryId == -1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
            return;
        }

        try {
            if (c == null || c.isClosed()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
                return;
            }

            String query = "INSERT INTO sub_category (name, category_id) VALUES (?, ?)";
            stmt = c.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, categoryId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Subcategory added successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add subcategory");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    @Override
    public void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        StringBuilder jsonBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonBody.toString());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return;
        }

        String name = jsonObject.optString("name");
        int categoryId = jsonObject.optInt("category_id", -1);

        if (name == null || name.isEmpty() || categoryId == -1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
            return;
        }

        try {
            if (c == null || c.isClosed()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
                return;
            }

            String query = "UPDATE sub_category SET name = ?, category_id = ? WHERE id = ?";
            stmt = c.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, Integer.parseInt(id));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Subcategory updated successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    @Override
    public void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        try {
            if (c == null || c.isClosed()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
                return;
            }

            String query = "DELETE FROM sub_category WHERE id = ?";
            stmt = c.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(id));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Subcategory deleted successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
//            e.printStackTrace();
        }
    }
}
