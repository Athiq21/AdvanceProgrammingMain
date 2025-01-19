//package servlet.categories;
//
//import jic.DBConnection;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//
//public class CategoryServlet extends HttpServlet {
//    private PreparedStatement stmt = null;
//    private ResultSet rs = null;
//    private Connection conn = null;
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
//            return;
//        }
//
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//
//        if (path.equals("/all")) {
//            getAllCat(req, resp);
//        } else {
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
//        }
//    }
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
//        if (path.equals("/add")) {
//            addCat(req, resp);  // Add a new category
//        } else {
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty() || !path.startsWith("/")) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
//            return;
//        }
//
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//        String[] pathParts = path.split("/");
//
//        if (pathParts.length != 2) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
//            return;
//        }
//
//        String id = pathParts[1];
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
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
//            return;
//        }
//
//        String name = jsonObject.optString("name");
//
//        if (name == null || name.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "UPDATE category SET name = ? WHERE id = ?";
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, name);
//            stmt.setInt(2, Integer.parseInt(id));  // Set the category ID from the URL
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Category updated successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
//            }
//        } catch (SQLException e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty() || !path.startsWith("/")) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
//            return;
//        }
//
//        // Extract the ID from the path
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//        String[] pathParts = path.split("/");
//
//        if (pathParts.length != 2) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
//            return;
//        }
//
//        String id = pathParts[1];  // The category ID from the URL
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "DELETE FROM category WHERE id = ?";
//            stmt = conn.prepareStatement(query);
//            stmt.setInt(1, Integer.parseInt(id));  // Set the category ID from the URL
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Category deleted successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
//            }
//        } catch (SQLException e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//
//    private void addCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
//            return;
//        }
//
//        String name = jsonObject.optString("name");
//
//        if (name == null || name.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
//            return;
//        }
//
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "INSERT INTO category (name) VALUES (?)";
//            stmt = conn.prepareStatement(query);
//            stmt.setString(1, name);
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Category added successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add category");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    // Method to get all categories
//    private void getAllCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            conn = DBConnection.getConnection();
//            if (conn == null || conn.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "SELECT id, name FROM category";
//            stmt = conn.prepareStatement(query);
//            rs = stmt.executeQuery();
//
//            JSONArray categories = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject category = new JSONObject();
//                category.put("id", rs.getInt("id"));
//                category.put("name", rs.getString("name"));
//
//                categories.put(category);
//            }
//
//            if (categories.length() > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write(categories.toString());
//            } else {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write("{\"message\": \"No categories found\"}");
//            }
//        } catch (SQLException e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void closeResources() {
//        try {
//            if (stmt != null) stmt.close();
//            if (rs != null) rs.close();
//            if (conn != null) conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
//

package servlet.categories;

import model.Category;
import org.json.JSONArray;
import org.json.JSONObject;
import services.CategoryService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        if (path.equals("/all")) {
            getAllCategories(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        if (path.equals("/add")) {
            addCategory(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty() || !path.startsWith("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        String[] pathParts = path.split("/");

        if (pathParts.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return;
        }

        String id = pathParts[1];
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

        if (name == null || name.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
            return;
        }

        try {
            Category category = new Category(Integer.parseInt(id), name);
            if (CategoryService.updateCategory(category)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Category updated successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty() || !path.startsWith("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        String[] pathParts = path.split("/");

        if (pathParts.length != 2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
            return;
        }

        String id = pathParts[1];  // Category ID from URL

        try {
            if (CategoryService.deleteCategory(Integer.parseInt(id))) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Category deleted successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    // Method to get all categories
    private void getAllCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Category> categories = CategoryService.getAllCategories();

            if (categories.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"No categories found\"}");
                return;
            }

            JSONArray jsonArray = new JSONArray();
            for (Category category : categories) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", category.getId());
                jsonObject.put("name", category.getName());
                jsonArray.put(jsonObject);
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonArray.toString());

        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
        }
    }

    // Method to add a new category
    private void addCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

        if (name == null || name.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category name is required");
            return;
        }

        try {
            Category category = new Category(0, name);
            if (CategoryService.addCategory(category)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"message\": \"Category added successfully\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add category");
            }
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
