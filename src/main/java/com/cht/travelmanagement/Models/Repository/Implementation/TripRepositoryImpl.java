package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Repository.TripRepository;
import com.cht.travelmanagement.Models.Trip;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TripRepositoryImpl implements TripRepository {

    @Override
    public ObservableList<Trip> getAllTrips() {
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        String query = "SELECT * FROM trip ORDER BY StartDate";

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trip trip = new Trip(
                        rs.getInt("TripID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("EndDate").toLocalDate(),
                        rs.getBoolean("IsActive")
                );
                trips.add(trip);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching trips: " + e.getMessage());
            e.printStackTrace();
        }

        return trips;
    }

    @Override
    public ObservableList<Trip> getTripsByPackageId(int packageId) {
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        String query = """
            SELECT t.* FROM trip t 
            INNER JOIN packagetrips pt ON t.TripID = pt.TripID 
            WHERE pt.PackageID = ? 
            ORDER BY pt.Sequence
        """;

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Trip trip = new Trip(
                        rs.getInt("TripID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("EndDate").toLocalDate(),
                        rs.getBoolean("IsActive")
                );
                trips.add(trip);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching trips for package: " + e.getMessage());
            e.printStackTrace();
        }

        return trips;
    }

    @Override
    public ObservableList<Trip> getActiveTrips() {
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        String query = "SELECT * FROM trip WHERE IsActive = 1 ORDER BY StartDate";

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trip trip = new Trip(
                        rs.getInt("TripID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("EndDate").toLocalDate(),
                        rs.getBoolean("IsActive")
                );
                trips.add(trip);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching active trips: " + e.getMessage());
            e.printStackTrace();
        }

        return trips;
    }
}
