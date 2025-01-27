package services;

import Interface.OfferServiceImpl;
import model.Item;
import model.Offer;
import jic.DBConnection;
import model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

public class OfferService implements OfferServiceImpl {

        private static final Logger logger = Logger.getLogger(services.ItemService.class.getName());
        private Connection connection;

        public OfferService() {
            this.connection = DBConnection.getInstance().getConnection();
        }

        @Override
        public Offer saveOffer(Offer offer) {
            String sql = "INSERT INTO offer (image) " +
                    "VALUES (?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, offer.getImage());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            offer.setId(generatedKeys.getLong(1));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error saving item", e);
            }
            return offer;
        }

    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String sql = "Select * from offer";


        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Offer offer = new Offer();
                offer.setId(rs.getLong("id"));
                offer.setImage(rs.getString("image"));

                offers.add(offer);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching items", e);
        }
        return offers;
    }
}
