package services;

import com.google.gson.Gson;
import jic.DBConnection;
import model.Notification;
import model.Order;
import model.User;

import java.sql.*;
        import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    private Connection connection;

    public NotificationService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    public boolean markAsRead(long notificationId) {
        String sql = "UPDATE notifications SET status = 'READ' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, notificationId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error marking notification as read", e);
            return false;
        }
    }


    public List<Notification> getAllNotifications() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT n.id, n.message, n.createdDatetime, " +
                "u.id AS user_id, u.firstName, u.lastName, " +
                "o.id AS order_id " +
                "FROM notifications n " +
                "JOIN user u ON n.user_id = u.id " +
                "JOIN orders o ON n.order_id = o.id " +
                "WHERE n.status = 'UNREAD'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("user_id"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));

                Order order = new Order();
                order.setId(rs.getLong("order_id"));

                Notification notification = new Notification.Builder()
                        .setId(rs.getLong("id"))
                        .setMessage(rs.getString("message"))
                        .setCreatedDatetime(rs.getTimestamp("createdDatetime"))
                        .setUser(user)
                        .setOrder(order)
                        .build();

                notifications.add(notification);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching notifications", e);
        }

        return notifications;
    }
}
