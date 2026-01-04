package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Employee;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.View.AccountType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Override
    public void evaluateLoginCredentials(String email, String password, AccountType accountType, boolean userLoggedInSuccessfully) {
        String verifyLogin = "SELECT COUNT(1) FROM employee WHERE email = ? AND  password = ?";

        if (accountType == AccountType.ADMIN) {
            verifyLogin += " AND isManager = 1";
        }
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(verifyLogin);) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        Model.getInstance().setUserLoggedInSuccessfully(true);

                        Model.getInstance().getAdminViewFactory().setLoggedInEmployeeEmail(email);
                    } else {
                        System.out.println("Invalid Login. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAuthenticatedUser() {
        String query = "SELECT * FROM employee WHERE email = ?";
        var email = Model.getInstance().getAuthenticatedUserEmail();

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    int employeeId = resultSet.getInt("employeeId");
                    String name = resultSet.getString("name");
                    String empEmail = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String contactNumber = resultSet.getString("contactNumber");
                    boolean isManager = resultSet.getBoolean("isManager");
                    boolean isActive = resultSet.getBoolean("isActive");

                    Employee employee = new Employee(employeeId, name, empEmail, password, contactNumber, isManager, isActive);
                    Model.getInstance().setAuthenticatedUser(employee);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the list of clients assigned to a specific employee with their
     * booking information.
     */
    @Override
    public ObservableList<Client> getEmployeeClientList(int employeeId) {
        ObservableList<Client> clientList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT c.clientId, c.name, c.email, c.contactNumber, p.Destination, b.Status, "
                + "CONCAT(MIN(t.StartDate), ' to ', MAX(t.EndDate)) AS TripDates "
                + "FROM client c "
                + "LEFT JOIN booking b ON c.clientId = b.ClientID "
                + "LEFT JOIN package p ON b.PackageID = p.PackageID "
                + "LEFT JOIN packagetrips pt ON p.PackageID = pt.PackageID "
                + "LEFT JOIN trip t ON pt.TripID = t.TripID "
                + "GROUP BY c.clientId, c.name, c.email, c.contactNumber, p.Destination, b.Status "
                + "ORDER BY c.name ASC";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int clientId = resultSet.getInt("clientId");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String contactNumber = resultSet.getString("contactNumber");
                    String destination = resultSet.getString("Destination") != null ? resultSet.getString("Destination") : "N/A";
                    String tripStatus = resultSet.getString("Status") != null ? resultSet.getString("Status") : "No Booking";
                    String tripDates = resultSet.getString("TripDates") != null ? resultSet.getString("TripDates") : "N/A";

                    Client client = new Client(clientId, name, email, contactNumber, destination, tripStatus, tripDates);
                    clientList.add(client);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientList;
    }

}
