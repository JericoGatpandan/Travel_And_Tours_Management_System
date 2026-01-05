package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Repository.TourPackageRepository;
import com.cht.travelmanagement.Models.TourPackage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TourPackageRepositoryImpl implements TourPackageRepository {

    public final ObservableList<TourPackage> tourPackages;

    public TourPackageRepositoryImpl() {
        this.tourPackages = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<TourPackage> getTourPackages() {
        String query = "SELECT * FROM package";
        tourPackages.clear();
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                TourPackage tourPackage = mapResultSetToTourPackage(resultSet);
                tourPackages.add(tourPackage);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tourPackages;
    }

    @Override
    public ObservableList<TourPackage> getActiveTourPackages() {
        String query = "SELECT * FROM package WHERE IsActive = 1";
        ObservableList<TourPackage> activePackages = FXCollections.observableArrayList();
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                TourPackage tourPackage = mapResultSetToTourPackage(resultSet);
                activePackages.add(tourPackage);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return activePackages;
    }

    @Override
    public ObservableList<TourPackage> searchTourPackages(String searchTerm) {
        String query = "SELECT * FROM package WHERE Name LIKE ? OR Destination LIKE ? OR Description LIKE ?";
        ObservableList<TourPackage> searchResults = FXCollections.observableArrayList();
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TourPackage tourPackage = mapResultSetToTourPackage(resultSet);
                    searchResults.add(tourPackage);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return searchResults;
    }

    @Override
    public TourPackage getTourPackageById(int packageId) {
        String query = "SELECT * FROM package WHERE PackageId = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, packageId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToTourPackage(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean createTourPackage(TourPackage tourPackage) {
        String query = "INSERT INTO package (Name, Description, Destination, Duration, MaxPax, Inclusions, Price, IsActive, CreatedByEmployeeId, ImagePath) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tourPackage.getPackageName());
            preparedStatement.setString(2, tourPackage.getDescription());
            preparedStatement.setString(3, tourPackage.getDestination());
            preparedStatement.setInt(4, tourPackage.getDurationDays());
            preparedStatement.setInt(5, tourPackage.getMaxParticipants());
            preparedStatement.setString(6, tourPackage.getInclusions());
            preparedStatement.setInt(7, tourPackage.getPrice());
            preparedStatement.setBoolean(8, tourPackage.getIsActive());
            preparedStatement.setInt(9, tourPackage.getCreatedBy());
            preparedStatement.setString(10, tourPackage.getImagePath());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTourPackage(TourPackage tourPackage) {
        String query = "UPDATE package SET Name = ?, Description = ?, Destination = ?, Duration = ?, "
                + "MaxPax = ?, Inclusions = ?, Price = ?, IsActive = ?, ImagePath = ? WHERE PackageId = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tourPackage.getPackageName());
            preparedStatement.setString(2, tourPackage.getDescription());
            preparedStatement.setString(3, tourPackage.getDestination());
            preparedStatement.setInt(4, tourPackage.getDurationDays());
            preparedStatement.setInt(5, tourPackage.getMaxParticipants());
            preparedStatement.setString(6, tourPackage.getInclusions());
            preparedStatement.setInt(7, tourPackage.getPrice());
            preparedStatement.setBoolean(8, tourPackage.getIsActive());
            preparedStatement.setString(9, tourPackage.getImagePath());
            preparedStatement.setInt(10, tourPackage.getPackageId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteTourPackage(int packageId) {
        // First check if package has any bookings
        String checkQuery = "SELECT COUNT(*) FROM booking WHERE PackageID = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, packageId);
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Package has bookings, just deactivate instead of deleting
                    return togglePackageStatus(packageId, false);
                }
            }

            // No bookings, safe to delete
            String deleteQuery = "DELETE FROM package WHERE PackageId = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, packageId);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean togglePackageStatus(int packageId, boolean isActive) {
        String query = "UPDATE package SET IsActive = ? WHERE PackageId = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setInt(2, packageId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getActivePackageCount() {
        String query = "SELECT COUNT(*) FROM package WHERE IsActive = 1";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private TourPackage mapResultSetToTourPackage(ResultSet resultSet) throws SQLException {
        int packageId = resultSet.getInt("PackageId");
        String packageName = resultSet.getString("Name");
        String description = resultSet.getString("Description");
        String destination = resultSet.getString("Destination");
        int durationDays = resultSet.getInt("Duration");
        int maxParticipants = resultSet.getInt("MaxPax");
        String inclusions = resultSet.getString("Inclusions");
        int price = resultSet.getInt("Price");
        boolean isActive = resultSet.getBoolean("IsActive");
        int createdBy = resultSet.getInt("CreatedByEmployeeId");
        String imagePath = resultSet.getString("ImagePath");

        return new TourPackage(packageId, packageName, description, destination,
                durationDays, maxParticipants, inclusions, price, isActive, createdBy, imagePath);
    }
}
