package servlet;

import DTO.Item.UserRoleDTO;
import model.User;

import com.google.gson.Gson;
import services.PermissionService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/permissions/*")
public class PermissionServlet extends HttpServlet {
    private final PermissionService permissionService = new PermissionService();
    private final Gson gson = new Gson();

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        String path = req.getPathInfo();
//
//        if (path == null || path.isEmpty()) {
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No path provided");
//            return;
//        }
//
//        switch (path) {
//            case "/role/4":
//                // Handle the request for users with role ID 1
//                handleGetUsersWithRole1(resp);
//                break;
//            case "/role/5":
//                // Handle the request for users with role ID 2
//                handleGetUsersWithRole2(resp);
//                break;
//            case "/role/6":
//                // Handle the request for users with role ID 3
//                handleGetUsersWithRole3(resp);
//                break;
//            default:
//                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid API path");
//        }
//    }


    @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String path = req.getPathInfo();

            if (path == null || path.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No API path provided");
                return;
            }

            switch (path) {
                case "/update":
                    handleUpdatePermissions(req, resp);
                    break;

                case "/role/4":
                    // Handle the request for users with role ID 1
                    handleGetUsersWithRole1(resp);
                    break;
                case "/role/5":
                    // Handle the request for users with role ID 2
                    handleGetUsersWithRole2(resp);
                    break;
                case "/role/6":
                    // Handle the request for users with role ID 3
                    handleGetUsersWithRole3(resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid API path");
            }
        }

    private void handleUpdatePermissions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        UserRoleDTO requestData = gson.fromJson(reader, UserRoleDTO.class);

        if (requestData.getEmail() == null || requestData.getEmail().isEmpty() ||
                requestData.getRole() == null || requestData.getRole().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email and new role are required");
            return;
        }

        permissionService.updateUserRole(requestData.getEmail(), requestData.getRole(), resp);
    }


    private void handleGetUsersWithRole1(HttpServletResponse resp) throws IOException {
        try {
            permissionService.getUsersWithRoleId1(resp);  // Fetch users with role ID 1
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching users with role ID 1");
        }
    }

    private void handleGetUsersWithRole2(HttpServletResponse resp) throws IOException {
        try {
            permissionService.getUsersWithRoleId2(resp);  // Fetch users with role ID 1
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching users with role ID 1");
        }
    }

    private void handleGetUsersWithRole3(HttpServletResponse resp) throws IOException {
        try {
            permissionService.getUsersWithRoleId3(resp);  // Fetch users with role ID 3
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching users with role ID 3");
        }
    }
    }


