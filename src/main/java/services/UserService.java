//package services;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jic.DBConnection;
//import model.User;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserService {
//
//    public static void getAdminUsers(HttpServletResponse resp) throws IOException {
//        Connection c = DBConnection.getInstance().getConnection();
//
//        if (c == null) {
//            sendErrorResponse(resp, "DB Fail");
//            return;
//        }
//
//        String query ="SELECT id, email, firstName, lastName, isActivated FROM user WHERE isActivated = 1 AND roleID = 4";
//
//        try (PreparedStatement stmt = c.prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//
//            List<User> activatedUsers = new ArrayList<>();
//
//            while (rs.next()) {
//                User user = new User();
//                user.setId(rs.getLong("id"));
//                user.setEmail(rs.getString("email"));
//                user.setFirstName(rs.getString("firstName"));
//                user.setLastName(rs.getString("lastName"));
//                user.setIsActivated(rs.getInt("isActivated") == 1);
//                activatedUsers.add(user);
//            }
//
//            sendSuccessResponse(resp, activatedUsers);
//
//        } catch (SQLException e) {
//            sendErrorResponse(resp, "Database Error");
//        }
//    }
//
//    private static void sendSuccessResponse(HttpServletResponse resp, Object data) throws IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write(new ObjectMapper().writeValueAsString(data));
//    }
//
//    private static void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        resp.getWriter().write("{\"error\": \"" + message + "\"}");
//    }
//}
