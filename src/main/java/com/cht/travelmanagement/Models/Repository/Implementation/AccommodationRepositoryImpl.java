package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.Accommodation;
import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Repository.AccommodationRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccommodationRepositoryImpl implements AccommodationRepository {

    @Override
    public ObservableList<Accommodation> getAllAccommodations() {
        ObservableList<Accommodation> accommodations = FXCollections.observableArrayList();
        String query = "SELECT * FROM accommodation ORDER BY name";

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Accommodation accommodation = new Accommodation(
                        rs.getInt("accommodationId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("contact"),
                        rs.getString("amenities"),
                        rs.getInt("numberOfRooms"),
                        rs.getString("defaultRoomType")
                );
                accommodations.add(accommodation);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching accommodations: " + e.getMessage());
            e.printStackTrace();
        }

        return accommodations;
    }
}
