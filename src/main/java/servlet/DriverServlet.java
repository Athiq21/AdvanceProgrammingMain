package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Driver;
import services.DriverService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/driver/*")
public class DriverServlet extends HttpServlet {

    private DriverService driverService;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        super.init();
        driverService = new DriverService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || path.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No path provided");
            return;
        }

        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        switch (path) {
            case "/save":
                saveDriver(request, response);
                break;
            case "/assign-item":
                assignItemToDriver(request, response);
                break;
            case "/remove-item":
                removeDriverFromItem(request, response);
                break;
            case "/get-all":
                getAllDrivers(response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + path);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        updateDriver(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        deleteDriver(request, response);
    }

    private void saveDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Driver driver = parseJson(request, Driver.class);

        if (driver == null || driver.getName() == null || driver.getNic() == null || driver.getPhoneNumber() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
            return;
        }

        Driver savedDriver = driverService.saveDriver(driver);
        sendJsonResponse(response, savedDriver);
    }

    private void getAllDrivers(HttpServletResponse response) throws IOException {
        List<Driver> drivers = driverService.getAllDrivers();
        sendJsonResponse(response, drivers);
    }

    private void updateDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Driver driver = parseJson(request, Driver.class);

        if (driver == null || driver.getId() == 0 || driver.getName() == null || driver.getNic() == null || driver.getPhoneNumber() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
            return;
        }

        boolean updated = driverService.updateDriver(driver.getId(), driver.getName(), driver.getNic(), driver.getPhoneNumber());
        sendJsonResponse(response, updated ? "Update Successful" : "Update Failed");
    }

    private void deleteDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = parseJson(request, JsonObject.class);
        if (jsonObject == null || !jsonObject.has("driver_id")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required field: driver_id");
            return;
        }

        long driverId = jsonObject.get("driver_id").getAsLong();
        boolean deleted = driverService.deleteDriver(driverId);
        sendJsonResponse(response, deleted ? "Driver Deleted" : "Delete Failed");
    }

    private void assignItemToDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = parseJson(request, JsonObject.class);
        if (jsonObject == null || !jsonObject.has("item_id") || !jsonObject.has("driver_id")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields: item_id, driver_id");
            return;
        }

        long itemId = jsonObject.get("item_id").getAsLong();
        long driverId = jsonObject.get("driver_id").getAsLong();
        boolean success = driverService.assignItemToDriver(itemId, driverId);
        sendJsonResponse(response, success ? "Assignment Successful" : "Assignment Failed");
    }

    private void removeDriverFromItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonObject = parseJson(request, JsonObject.class);
        if (jsonObject == null || !jsonObject.has("item_id")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required field: item_id");
            return;
        }

        long itemId = jsonObject.get("item_id").getAsLong();
        boolean success = driverService.removeDriverFromItem(itemId);
        sendJsonResponse(response, success ? "Driver Removed from Item" : "Operation Failed");
    }

    // Utility method to parse JSON request body
    private <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        try (BufferedReader reader = request.getReader()) {
            return gson.fromJson(reader, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    // Utility method to send JSON responses
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(data));
    }
}
