package services;

import model.Item;
import jic.DBConnection;

import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemService implements services.ItemServiceImpl {

    private static final Logger logger = Logger.getLogger(ItemService.class.getName());
    private Connection connection;

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
            stmt.setString(8, item.getCreatedBy());
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
}
