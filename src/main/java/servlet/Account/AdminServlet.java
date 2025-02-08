package servlet.Account;
import services.AdminService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/roles/*")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "NO API");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/users/active":
                AdminService.getActivatedUsers(resp);
                break;
            case "/users/deactivated":
                AdminService.getDeactivatedUsers(resp);
                break;

            case "/users/deactivate":
                handleDeactivate(req, resp);
                break;
            case "/users/reactivate":
                handleReactivate(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "NO APIs");
        }

    }
    private void handleDeactivate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        if (email == null || email.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required");
            return;
        }
        AdminService.deactivateUser(email, resp);
    }

    private void handleReactivate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        if (email == null || email.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is required");
            return;
        }
        AdminService.reactivateUser(email, resp);
    }
}
