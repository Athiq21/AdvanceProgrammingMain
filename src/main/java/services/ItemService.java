package services;

import model.Item;
import jic.DBConnection;
import model.User;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemService implements services.ItemServiceImpl {

    private static final Logger logger = Logger.getLogger(ItemService.class.getName());
    private static Connection connection;

    public ItemService() {
        this.connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public Item saveItem(Item item) {
        String sql = "INSERT INTO item (name, description, color, mileage, transmission, fueltype, price, created_by, category_id, subcategory_id, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getColor());
            stmt.setString(4, item.getMileage());
            stmt.setString(5, item.getTransmission());
            stmt.setString(6, item.getFuelType());
            stmt.setString(7, item.getPrice());
            stmt.setInt(8, item.getCreatedBy());
            stmt.setInt(9, item.getCategoryId());
            stmt.setInt(10, item.getSubcategoryId());
            stmt.setString(11, item.getImageBlob());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving item", e);
        }
        return item;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, u.id AS user_id, u.email AS user_email, u.firstName AS user_firstName, u.lastName AS user_lastName " +
                "FROM item i " +
                "LEFT JOIN user u ON i.created_by = u.id";


        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setColor(rs.getString("color"));
                item.setMileage(rs.getString("mileage"));
                item.setTransmission(rs.getString("transmission"));
                item.setFuelType(rs.getString("fueltype"));
                item.setCreatedBy(rs.getInt("created_by"));
                item.setPrice(rs.getString("price"));
                item.setCategoryId(rs.getInt("category_id"));
                item.setSubcategoryId(rs.getInt("subcategory_id"));
                item.setImageBlob(rs.getString("image"));
                item.setStatus(rs.getString("status"));
                items.add(item);

                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setEmail(rs.getString("user_email")); // Correct alias
                user.setFirstName(rs.getString("user_firstName")); // Correct alias
                user.setLastName(rs.getString("user_lastName"));
                item.setUser(user);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching items", e);
        }
        return items;
    }

    public List<Item> getItemsBySubcategory(int subcategoryId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM item WHERE subcategory_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subcategoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getLong("id"));
                    item.setName(rs.getString("name"));
                    item.setDescription(rs.getString("description"));
                    item.setColor(rs.getString("color"));
                    item.setMileage(rs.getString("mileage"));
                    item.setTransmission(rs.getString("transmission"));
                    item.setFuelType(rs.getString("fueltype"));
                    item.setCreatedBy(rs.getInt("created_by"));
                    item.setPrice(rs.getString("price"));
                    item.setCategoryId(rs.getInt("category_id"));
                    item.setSubcategoryId(rs.getInt("subcategory_id"));
                    item.setImageBlob(rs.getString("image"));
                    item.setStatus(rs.getString("status"));
                    items.add(item);

//                    User user = new User();
//                    user.setId(rs.getInt("user_id"));
//                    user.setEmail(rs.getString("user_email"));
//                    user.setFirstName(rs.getString("user_firstName"));
//                    user.setLastName(rs.getString("user_lastName"));
//                    item.setUser(user);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching items by subcategory", e);
        }
        return items;
    }

    public static boolean deleteItemById(long id) {
        String sql = "DELETE FROM item WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting item", e);
            return false;
        }
    }


    public boolean updateItem(Item item) {
        String sql = "UPDATE item SET name = ?, description = ?, color = ?, mileage = ?, transmission = ?, fueltype = ?, price = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setString(3, item.getColor());
            stmt.setString(4, item.getMileage());
            stmt.setString(5, item.getTransmission());
            stmt.setString(6, item.getFuelType());
            stmt.setString(7, item.getPrice());
            stmt.setString(8, item.getStatus());
            stmt.setLong(9, item.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating item", e);
            return false;
        }
    }


    public boolean updateItemStatusAndOrders(Long itemId, String status) {
        String updateItemSql = "UPDATE item SET status = ? WHERE id = ?";
        String updateOrderSql = "UPDATE orders SET status = 'completed' WHERE item_id = ?";

        PreparedStatement stmtUpdateItem = null;
        PreparedStatement stmtUpdateOrders = null;

        try {
            connection.setAutoCommit(false);
            stmtUpdateItem = connection.prepareStatement(updateItemSql);
            stmtUpdateItem.setString(1, status);
            stmtUpdateItem.setLong(2, itemId);
            int itemRowsAffected = stmtUpdateItem.executeUpdate();

            stmtUpdateOrders = connection.prepareStatement(updateOrderSql);
            stmtUpdateOrders.setLong(1, itemId);
            int orderRowsAffected = stmtUpdateOrders.executeUpdate();


            if (itemRowsAffected > 0 && orderRowsAffected > 0) {
                connection.commit();
                return true;
            } else {
                connection.rollback(); // Rollback if any update fails
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating it", e);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling", rollbackEx);
            }
            return false;
        } finally {
            try {
                if (stmtUpdateItem != null) stmtUpdateItem.close();
                if (stmtUpdateOrders != null) stmtUpdateOrders.close();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing resources", e);
            }
        }
    }



}
