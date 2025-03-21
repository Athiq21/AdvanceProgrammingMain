package servlet;

import com.google.gson.Gson;
import model.Item;
import services.ItemService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/items/*")
public class UpdaterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UpdaterServlet.class.getName());
    private final ItemService itemService = new ItemService();
    private final Gson gson = new Gson();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            BufferedReader reader = req.getReader();
            Item item = gson.fromJson(reader, Item.class);

            logger.info("Received request to update item: " + item.getId());

            boolean isUpdated = itemService.updateItem(item);

            if (isUpdated) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Item updated successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Failed to update item\"}");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating item", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Server error occurred\"}");
        }
    }

}