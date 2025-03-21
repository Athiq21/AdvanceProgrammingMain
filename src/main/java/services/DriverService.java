package services;

import jic.DBConnection;

import model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverService {

        private static final Logger logger = Logger.getLogger(DriverService.class.getName());
        private Connection connection;

        public DriverService() {
            this.connection = DBConnection.getInstance().getConnection();
        }

        public Driver saveDriver(Driver driver) {
            String sql = "INSERT INTO driver (name, nic, phone_number) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, driver.getName());
                stmt.setString(2, driver.getNic());
                stmt.setString(3, driver.getPhoneNumber());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            driver.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error saving driver", e);
            }
            return driver;
        }

        public boolean assignItemToDriver(long itemId, long driverId) {
            String sql = "UPDATE item SET driver_id = ? WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, driverId);
                stmt.setLong(2, itemId);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error assigning item to driver", e);
                return false;
            }
        }

    public boolean removeDriverFromItem(long itemId) {
        String sql = "UPDATE item SET driver_id = NULL WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error removing driver from item", e);
            return false;
        }
    }

    public boolean deleteDriver(long driverId) {
        String sql = "DELETE FROM driver WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, driverId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting driver", e);
            return false;
        }
    }

    public boolean updateDriver(long driverId, String name, String nic, String phoneNumber) {
        String sql = "UPDATE driver SET name = ?, nic = ?, phone_number = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, nic);
            stmt.setString(3, phoneNumber);
            stmt.setLong(4, driverId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating driver", e);
            return false;
        }
    }

    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM driver";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Driver driver = new Driver();
                driver.setId(rs.getLong("id"));
                driver.setName(rs.getString("name"));
                driver.setNic(rs.getString("nic"));
                driver.setPhoneNumber(rs.getString("phone_number"));
                drivers.add(driver);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching drivers", e);
        }
        return drivers;
    }

    public Driver getDriverById(long driverId) {
        String sql = "SELECT * FROM driver WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Driver driver = new Driver();
                    driver.setId(rs.getLong("id"));
                    driver.setName(rs.getString("name"));
                    driver.setNic(rs.getString("nic"));
                    driver.setPhoneNumber(rs.getString("phone_number"));
                    return driver;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching driver by ID", e);
        }
        return null;
    }

}


