package servlet;

import com.google.gson.Gson;
import model.Order;
import model.User;
import model.Item;
import services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

//@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(services.OrderService.class.getName());
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = req.getReader()) {
            Gson gson = new Gson();
            Order order = gson.fromJson(reader, Order.class);

            logger.info("Order received: " + order);

            Order savedOrder = orderService.saveOrder(order);
            logger.info("Order saved: " + savedOrder);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(savedOrder));
        } catch (Exception e) {
            logger.severe("Error occurred while processing the order: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"An error occurred while processing the order\"}");
        }

    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//
//        String path = req.getPathInfo();
//        Gson gson = new Gson();
//
//        try {
//            if ("/all".equals(path)) {
//                List<Order> allOrders = orderService.getAllOrders();
//                logger.info("All orders fetched: " + allOrders);
//
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write(gson.toJson(allOrders));
//            } else if ("/user".equals(path)) {
//                // Fetch orders for the logged-in user
//                Long userId = (Long) req.getSession().getAttribute("userId");
//                if (userId == null) {
//                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    resp.getWriter().write("{\"error\":\"User not logged in\"}");
//                    return;
//                }
//
//                List<Order> userOrders = orderService.getOrdersByUserId(userId);
//                logger.info("Orders for user " + userId + ": " + userOrders);
//
//                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.getWriter().write(gson.toJson(userOrders));
//            } else {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
//            }
//        } catch (Exception e) {
//            logger.severe("Error processing GET request: " + e.getMessage());
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write("{\"error\":\"An error occurred while processing your request\"}");
//        }
//    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        Gson gson = new Gson();

        try {
            if ("/all".equals(path)) {
                List<Order> allOrders = orderService.getAllOrders(req);
                logger.info("All orders fetched: " + allOrders);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(allOrders));
            } else if (path != null && path.startsWith("/user/")) {
                String userIdStr = path.substring("/user/".length());
                try {
                    Long userId = Long.parseLong(userIdStr);
//                    List<Order> userOrders = orderService.getOrdersByUserId(userId);
                    List<Order> userOrders = orderService.getOrdersByUserId(userId, req);
                    logger.info("Orders for user " + userId + ": " + userOrders);

                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(gson.toJson(userOrders));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid user ID format: " + userIdStr);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"error\":\"Invalid user ID format\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
            }
        } catch (Exception e) {
            logger.severe("Error processing GET request: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"An error occurred while processing your request\"}");
        }
    }

}
