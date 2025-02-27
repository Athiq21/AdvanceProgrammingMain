package servlet;

import services.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "NO API");
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid API endpoint");
            return;
        }

        try {
            Long userId = Long.parseLong(pathParts[2]);
            switch (pathParts[1]) {
                case "update":
                    UserService.updateUserDetails(req, resp, userId);
                    break;
                case "changepassword":
                    UserService.changeUserPassword(req, resp, userId);
                    break;
                case "deactivate":
                    UserService.deactivate(req, resp, userId);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid API endpoint");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
        }
    }
}
