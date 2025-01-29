package services;
import Interface.OrderServiceImpl;
import jic.DBConnection;
import model.Item;
import model.Order;
import model.User;
import org.json.HTTP;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService implements OrderServiceImpl {

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());
    private Connection connection;

    public OrderService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public Order saveOrder(Order order) {
        //add status remov
        String sql = "INSERT INTO orders ( startdate, enddate,paymentMethod, user_id, item_id) VALUES ( ?, ?, ?, ?,?)";
        String athiqitem = "UPDATE item SET status = 'unavailable' WHERE id = ?";

        PreparedStatement stmtOrder = null;
        PreparedStatement stmtUpdateItem = null;

        try {
            connection.setAutoCommit(false);

            stmtOrder = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmtOrder.setString(1, order.getStartDate());
            stmtOrder.setString(2, order.getEndDate());
            stmtOrder.setString(3, order.getPaymentMethod());
            stmtOrder.setLong(4, order.getUser().getId());
            stmtOrder.setLong(5,order.getItem().getId());

            int rowsAffected = stmtOrder.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    }
                }
            }
            stmtUpdateItem = connection.prepareStatement(athiqitem);
            stmtUpdateItem.setLong(1, order.getItem().getId());
            stmtUpdateItem.executeUpdate();


            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
            }
            logger.log(Level.SEVERE, "Error saving order and updating item status", e);
        } finally {
            try {
                if (stmtOrder != null) {
                    stmtOrder.close();
                }
                if (stmtUpdateItem != null) {
                    stmtUpdateItem.close();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources", e);
            }
        }

        return order;
    }

    public List<Order> getAllOrders(HttpServletRequest req) {
        String sql = "SELECT o.id, o.startdate, o.enddate, o.paymentMethod, o.status " +
                "u.id AS userId, u.firstName AS firstName,u.lastName AS lastName, " +
                "i.id AS itemId, i.name AS itemName, i.image AS itemImage, i.price AS itemPrice " +
                "FROM orders o " +
                "JOIN user u ON o.user_id = u.id " +
                "JOIN item i ON o.item_id = i.id ";

        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setStartDate(rs.getString("startdate"));
                order.setEndDate(rs.getString("enddate"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setStatus(rs.getString("status"));

                User user = new User();
                user.setId(rs.getLong("userId"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                order.setUser(user);

                Item item = new Item();
                item.setId(rs.getLong("itemId"));
                item.setName(rs.getString("itemName"));
                item.setPrice(rs.getString("itemPrice"));
//                String fullImageUrl = "http://localhost:8080/api/item/images/" + rs.getString("itemImage");
//                item.setImageBlob(fullImageUrl);
                String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
                String fullImageUrl = baseUrl + "item/images/" + rs.getString("itemImage");
                item.setImageBlob(fullImageUrl);
                order.setItem(item);

                orders.add(order);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all orders", e);
        }

        return orders;
    }

    public List<Order> getOrdersByUserId(Long userId, HttpServletRequest req) {
        String sql = "SELECT o.id, o.startdate, o.enddate, o.paymentMethod,o.status, " +
                "u.id AS userId, u.firstName AS firstName,u.lastName AS lastName, " +
                "i.id AS itemId, i.name AS itemName, i.image AS itemImage , i.price AS itemPrice " +
                "FROM orders o " +
                "JOIN user u ON o.user_id = u.id " +
                "JOIN item i ON o.item_id = i.id " +
                "WHERE o.user_id = ?";


        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setStartDate(rs.getString("startdate"));
                    order.setEndDate(rs.getString("enddate"));
                    order.setPaymentMethod(rs.getString("paymentMethod"));
                    order.setStatus(rs.getString("status"));

                    User user = new User();
                    user.setId(rs.getLong("userId"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    order.setUser(user);

                    Item item = new Item();
                    item.setId(rs.getLong("itemId"));
                    item.setName(rs.getString("itemName"));
                    item.setPrice(rs.getString("itemPrice"));
//                    String fullImageUrl = "http://localhost:8080/api/item/images/" + rs.getString("itemImage");
//                    item.setImageBlob(fullImageUrl);
                    String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
                    String fullImageUrl = baseUrl + "item/images/" + rs.getString("itemImage");
                    item.setImageBlob(fullImageUrl);
                    order.setItem(item);

                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching orders for user with ID " + userId, e);
        }

        return orders;
    }

}