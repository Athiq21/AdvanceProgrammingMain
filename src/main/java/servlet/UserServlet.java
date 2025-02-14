//package servlet;
//
//import services.UserService;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
////@WebServlet("/api/permissions/*")
//public class UserServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "NO API");
//            return;
//        }
//
//        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
//
//        switch (path) {
//            case "/role":
//                UserService.getAdminUsers(resp);
//                break;
//            default:
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NO APIs");
//        }
//
//    }
//}
