package servlet;

import com.google.gson.Gson;
import model.Notification;
import services.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/notifications/*")
public class NotificationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "NO API");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/read":
                doread(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NO API");
        }
    }

    private void doread(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();

        try {
            BufferedReader reader = req.getReader();
            Map<String, Object> requestBody = gson.fromJson(reader, Map.class);

            if (!requestBody.containsKey("notificationId")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing notificationId");
                return;
            }

            long notificationId = ((Number) requestBody.get("notificationId")).longValue();
            boolean success = notificationService.markAsRead(notificationId);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Notification marked as read\"}");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to mark as read");
            }

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request format");
        }
    }

    private NotificationService notificationService;
    @Override
    public void init() {
        this.notificationService = new NotificationService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Notification> notifications = notificationService.getAllNotifications();
        Gson gson = new Gson();

        resp.getWriter().write(gson.toJson(notifications));
    }
}
