package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Repository.ClientRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientRepositoryImpl implements ClientRepository {

    public final ObservableList<Client> clients;

    public ClientRepositoryImpl() {
        this.clients = FXCollections.observableArrayList();
    }

    @Override
    public ObservableList<Client> getClients() {
        String query = "SELECT * FROM client";
        clients.clear();
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int clientId = resultSet.getInt("clientId");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String contactNumber = resultSet.getString("contactNumber");
                String customerType = resultSet.getString("customerType");
                Date sqlDateRegistered = resultSet.getDate("dateRegistered");
                LocalDate dateRegistered = sqlDateRegistered.toLocalDate();

                Client client = new Client(clientId, name, email, address, contactNumber, customerType, dateRegistered);

                clients.add(client);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    @Override
    public ObservableList<Client> searchClients(String searchTerm) {
        ObservableList<Client> results = FXCollections.observableArrayList();
        String query = "SELECT * FROM client WHERE name LIKE ? OR email LIKE ? OR clientId = ? LIMIT 10";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String likeTerm = "%" + searchTerm + "%";
            preparedStatement.setString(1, likeTerm);
            preparedStatement.setString(2, likeTerm);

            // Try to parse as ID
            int searchId = -1;
            try {
                searchId = Integer.parseInt(searchTerm);
            } catch (NumberFormatException ignored) {
            }
            preparedStatement.setInt(3, searchId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int clientId = resultSet.getInt("clientId");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String contactNumber = resultSet.getString("contactNumber");
                String customerType = resultSet.getString("customerType");
                Date sqlDateRegistered = resultSet.getDate("dateRegistered");
                LocalDate dateRegistered = sqlDateRegistered.toLocalDate();

                Client client = new Client(clientId, name, email, address, contactNumber, customerType, dateRegistered);
                results.add(client);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public boolean deleteClient(int clientId) {
        String query = "DELETE FROM client WHERE clientId = ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, clientId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting client: " + e.getMessage());
            // Check if it's a foreign key constraint error
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("Cannot delete client: Client has existing bookings");
            }
            return false;
        }
    }

    @Override
    public boolean updateClient(int clientId, String name, String email, String contactNumber, String address, String customerType) {
        String query = "UPDATE client SET name = ?, email = ?, contactNumber = ?, address = ?, customerType = ? WHERE clientId = ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, contactNumber);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, customerType);
            preparedStatement.setInt(6, clientId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating client: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void createClient() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int createClient(String name, String email, String contactNumber, String address, String customerType) {
        String insertQuery = "INSERT INTO client (name, email, address, contactNumber, customerType, dateRegistered) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, address != null && !address.isEmpty() ? address : "Not specified");
            preparedStatement.setString(4, contactNumber);
            preparedStatement.setString(5, customerType != null && !customerType.isEmpty() ? customerType : "REGULAR");
            preparedStatement.setDate(6, java.sql.Date.valueOf(LocalDate.now()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating client: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }
}
