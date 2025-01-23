////package servlet.categories;
////
////import jic.DBConnection;
////import org.json.JSONArray;
////import org.json.JSONObject;
////
////import javax.servlet.annotation.WebServlet;
////import javax.servlet.http.HttpServlet;
////import javax.servlet.http.HttpServletRequest;
////import javax.servlet.http.HttpServletResponse;
////import java.io.BufferedReader;
////import java.io.IOException;
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////
////
////public class SubCategoryServlet extends HttpServlet {
////    private PreparedStatement stmt = null;
////    private ResultSet rs = null;
////    private Connection conn = null;
////
////    @Override
////    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String path = req.getPathInfo();
////
////        if (path == null || path.isEmpty()) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
////            return;
////        }
////
////        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
////
////        if (path.equals("/all")) {
////            getAllSubCategories(req, resp);
////        } else if (path.startsWith("/category/")) {
////            String[] parts = path.split("/");
////            if (parts.length == 3) {
////                try {
////                    int categoryId = Integer.parseInt(parts[2]);
////                    getSubCategoriesByCategoryId(req, resp, categoryId);
////                } catch (NumberFormatException e) {
////                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
////                }
////            } else {
////                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
////            }
////        } else {
////            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
////        }
////    }
////
////    @Override
////    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String path = req.getPathInfo();
////
////        if (path == null || path.isEmpty() || !path.equals("/")) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
////            return;
////        }
////
////        addSubCategory(req, resp);
////    }
////
////    @Override
////    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String path = req.getPathInfo();
////
////        if (path == null || path.isEmpty() || !path.startsWith("/")) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
////            return;
////        }
////
////        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
////        String[] pathParts = path.split("/");
////
////        if (pathParts.length != 2) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
////            return;
////        }
////
////        String id = pathParts[1];
////        updateSubCategory(req, resp, id);
////    }
////
////    @Override
////    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String path = req.getPathInfo();
////
////        if (path == null || path.isEmpty() || !path.startsWith("/")) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
////            return;
////        }
////
////        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
////        String[] pathParts = path.split("/");
////
////        if (pathParts.length != 2) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
////            return;
////        }
////
////        String id = pathParts[1];
////        deleteSubCategory(req, resp, id);
////    }
////
////    private void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
////            return;
////        }
////
////        String name = jsonObject.optString("name");
////        int categoryId = jsonObject.optInt("category_id", -1);
////
////        if (name == null || name.isEmpty() || categoryId == -1) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
////            return;
////        }
////
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
////                return;
////            }
////
////            String query = "INSERT INTO sub_category (name, category_id) VALUES (?, ?)";
////            stmt = conn.prepareStatement(query);
////            stmt.setString(1, name);
////            stmt.setInt(2, categoryId);
////
////            int rowsAffected = stmt.executeUpdate();
////
////            if (rowsAffected > 0) {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.setContentType("application/json");
////                resp.getWriter().write("{\"message\": \"Subcategory added successfully\"}");
////            } else {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add subcategory");
////            }
////        } catch (Exception e) {
////            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
////
////    private void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
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
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
////            return;
////        }
////
////        String name = jsonObject.optString("name");
////        int categoryId = jsonObject.optInt("category_id", -1);
////
////        if (name == null || name.isEmpty() || categoryId == -1) {
////            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
////            return;
////        }
////
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
////                return;
////            }
////
////            String query = "UPDATE sub_category SET name = ?, category_id = ? WHERE id = ?";
////            stmt = conn.prepareStatement(query);
////            stmt.setString(1, name);
////            stmt.setInt(2, categoryId);
////            stmt.setInt(3, Integer.parseInt(id));
////
////            int rowsAffected = stmt.executeUpdate();
////
////            if (rowsAffected > 0) {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.setContentType("application/json");
////                resp.getWriter().write("{\"message\": \"Subcategory updated successfully\"}");
////            } else {
////                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
////            }
////        } catch (Exception e) {
////            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
////
////    private void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
////                return;
////            }
////
////            String query = "DELETE FROM sub_category WHERE id = ?";
////            stmt = conn.prepareStatement(query);
////            stmt.setInt(1, Integer.parseInt(id));
////
////            int rowsAffected = stmt.executeUpdate();
////
////            if (rowsAffected > 0) {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.setContentType("application/json");
////                resp.getWriter().write("{\"message\": \"Subcategory deleted successfully\"}");
////            } else {
////                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
////            }
////        } catch (Exception e) {
////            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
////
////    private void getAllSubCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
////                return;
////            }
////
////            String query = "SELECT id, name, category_id FROM sub_category";
////            stmt = conn.prepareStatement(query);
////            rs = stmt.executeQuery();
////
////            JSONArray subcategories = new JSONArray();
////
////            while (rs.next()) {
////                JSONObject subcategory = new JSONObject();
////                subcategory.put("id", rs.getInt("id"));
////                subcategory.put("name", rs.getString("name"));
////                subcategory.put("category_id", rs.getInt("category_id"));
////
////                subcategories.put(subcategory);
////            }
////
////            if (subcategories.length() > 0) {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.setContentType("application/json");
////                resp.getWriter().write(subcategories.toString());
////            } else {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.getWriter().write("{\"message\": \"No subcategories found\"}");
////            }
////        } catch (Exception e) {
////            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
////
////    private void getSubCategoriesByCategoryId(HttpServletRequest req, HttpServletResponse resp, int categoryId) throws IOException {
////        try {
////            conn = DBConnection.getConnection();
////            if (conn == null || conn.isClosed()) {
////                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
////                return;
////            }
////
////            String query = "SELECT id, name FROM sub_category WHERE category_id = ?";
////            stmt = conn.prepareStatement(query);
////            stmt.setInt(1, categoryId);
////            rs = stmt.executeQuery();
////
////            JSONArray subcategories = new JSONArray();
////
////            while (rs.next()) {
////                JSONObject subcategory = new JSONObject();
////                subcategory.put("id", rs.getInt("id"));
////                subcategory.put("name", rs.getString("name"));
////
////                subcategories.put(subcategory);
////            }
////
////            if (subcategories.length() > 0) {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.setContentType("application/json");
////                resp.getWriter().write(subcategories.toString());
////            } else {
////                resp.setStatus(HttpServletResponse.SC_OK);
////                resp.getWriter().write("{\"message\": \"No subcategories found for the given category ID\"}");
////            }
////        } catch (Exception e) {
////            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
////        } finally {
////            closeResources();
////        }
////    }
////
////    private void closeResources() {
////        try {
////            if (stmt != null) stmt.close();
////            if (rs != null) rs.close();
////            if (conn != null) conn.close();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////    }
////}
//
//
//
//package servlet.categories;
//
//import jic.DBConnection;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import javax.servlet.annotation.WebServlet;
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
//public class SubCategoryServlet extends HttpServlet {
//    private PreparedStatement stmt = null;
//    private ResultSet rs = null;
//    Connection c = DBConnection.getInstance().getConnection();
//
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        String path = req.getPathInfo();
////
////        if (path == null || path.isEmpty()) {
////            getAllSubCategories(req, resp);
////        } else if (path.startsWith("/category/")) {
////            String[] parts = path.split("/");
////            if (parts.length == 3) {
////                try {
////                    int categoryId = Integer.parseInt(parts[2]);
////                    getSubCategoriesByCategoryId(req, resp, categoryId);
////                } catch (NumberFormatException e) {
////                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID");
////                }
////            } else {
////                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid endpoint");
////            }
////        } else {
////            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
////        }
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty()) {
//            getAllSubCategory(req, resp);
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
//        if (path == null || path.isEmpty() || !path.equals("/")) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid API endpoint");
//            return;
//        }
//
//        addSubCategory(req, resp);
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
//        updateSubCategory(req, resp, id);
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
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//        String[] pathParts = path.split("/");
//
//        if (pathParts.length != 2) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
//            return;
//        }
//
//        String id = pathParts[1];
//        deleteSubCategory(req, resp, id);
//    }
//
//    private void addSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
//        int categoryId = jsonObject.optInt("category_id", -1);
//
//        if (name == null || name.isEmpty() || categoryId == -1) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
//            return;
//        }
//
//        try {
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "INSERT INTO sub_category (name, category_id) VALUES (?, ?)";
//            stmt = c.prepareStatement(query);
//            stmt.setString(1, name);
//            stmt.setInt(2, categoryId);
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_CREATED);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Subcategory added successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add subcategory");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void updateSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
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
//        int categoryId = jsonObject.optInt("category_id", -1);
//
//        if (name == null || name.isEmpty() || categoryId == -1) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Subcategory name and category_id are required");
//            return;
//        }
//
//        try {
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "UPDATE sub_category SET name = ?, category_id = ? WHERE id = ?";
//            stmt = c.prepareStatement(query);
//            stmt.setString(1, name);
//            stmt.setInt(2, categoryId);
//            stmt.setInt(3, Integer.parseInt(id));
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Subcategory updated successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void deleteSubCategory(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
//        try {
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "DELETE FROM sub_category WHERE id = ?";
//            stmt = c.prepareStatement(query);
//            stmt.setInt(1, Integer.parseInt(id));
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write("{\"message\": \"Subcategory deleted successfully\"}");
//            } else {
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Subcategory not found");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void getAllSubCategories(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "SELECT id, name, category_id FROM sub_category";
//            stmt = c.prepareStatement(query);
//            rs = stmt.executeQuery();
//
//            JSONArray subcategories = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject subcategory = new JSONObject();
//                subcategory.put("id", rs.getInt("id"));
//                subcategory.put("name", rs.getString("name"));
//                subcategory.put("category_id", rs.getInt("category_id"));
//
//                subcategories.put(subcategory);
//            }
//
//            if (subcategories.length() > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write(subcategories.toString());
//            } else {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write("{\"message\": \"No subcategories found\"}");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void getSubCategoriesByCategoryId(HttpServletRequest req, HttpServletResponse resp, int categoryId) throws IOException {
//        try {
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "SELECT id, name FROM sub_category WHERE category_id = ?";
//            stmt = c.prepareStatement(query);
//            stmt.setInt(1, categoryId);
//            rs = stmt.executeQuery();
//
//            JSONArray subcategories = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject subcategory = new JSONObject();
//                subcategory.put("id", rs.getInt("id"));
//                subcategory.put("name", rs.getString("name"));
//
//                subcategories.put(subcategory);
//            }
//
//            if (subcategories.length() > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write(subcategories.toString());
//            } else {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write("{\"message\": \"No subcategories found for the given category ID\"}");
//            }
//        } catch (Exception e) {
//            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error during database operation: " + e.getMessage());
//        } finally {
//            closeResources();
//        }
//    }
//
//
//    private void getAllSubCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//
//            if (c == null || c.isClosed()) {
//                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection failed");
//                return;
//            }
//
//            String query = "SELECT id, name, category_id FROM sub_category";  // Query to fetch all subcategories
//            stmt = c.prepareStatement(query);
//            rs = stmt.executeQuery();
//
//            JSONArray subcategories = new JSONArray();
//
//            while (rs.next()) {
//                JSONObject subcategory = new JSONObject();
//                subcategory.put("id", rs.getInt("id"));
//                subcategory.put("name", rs.getString("name"));
//                subcategory.put("category_id", rs.getInt("category_id"));
//
//                subcategories.put(subcategory);
//            }
//
//            if (subcategories.length() > 0) {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setContentType("application/json");
//                resp.getWriter().write(subcategories.toString());  // Return all subcategories as a JSON array
//            } else {
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write("{\"message\": \"No subcategories found\"}");
//            }
//        } catch (Exception e) {
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
//            if (c != null) c.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}


package servlet.categories;

import Interface.SubCategoryServiceImpl;
import logger.Logging;
import logger.Validator;
import services.SubCategoryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

import model.SubCategory;

@WebServlet("/api/subcategories/*")
public class SubCategoryServlet extends HttpServlet {
    private SubCategoryServiceImpl sa;

    @Override
    public void init() {
        SubCategoryServiceImpl ser = new SubCategoryService();

        ser = new Validator(ser);
        ser = new Logging(ser);

        sa = ser;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty() || path.equals("/")) {
            // Call getAllSubCategory, but do not expect a return value
            sa.getAllSubCategory(req, resp);  // This method will write the response directly
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No API endpoint specified");
            return;
        }
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/add":
                addSubCat(req, resp);
                break;
            case "/view":
                viewSubCat(req, resp);
                break;
            case "/delete":
                deleteSubCat(req, resp);
                break;
            case "/update":
                putSubCat(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No matching API endpoint");
        }
    }

    private void addSubCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            sa.addSubCategory(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding subcategory: " + e.getMessage());
        }
    }

    private void viewSubCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            sa.getAllSubCategory(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving subcategories: " + e.getMessage());
        }
    }

    // Handles the deletion of a subcategory by ID
    private void deleteSubCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String id = path != null && path.startsWith("/") ? path.substring(1) : null;

        if (id != null && !id.isEmpty()) {
            try {
                sa.deleteSubCategory(req, resp, id);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting subcategory: " + e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    private void putSubCat(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String id = path != null && path.startsWith("/") ? path.substring(1) : null;

        if (id != null && !id.isEmpty()) {
            try {
                sa.updateSubCategory(req, resp, id);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating subcategory: " + e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String id = path != null && path.startsWith("/") ? path.substring(1) : null;

        if (id != null && !id.isEmpty()) {
            try {
                sa.deleteSubCategory(req, resp, id);
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting subcategory: " + e.getMessage());
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }


}
