package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Employee;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.PasswordUtils;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.View.AccountType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Override
    public void evaluateLoginCredentials(String email, String password, AccountType accountType, boolean userLoggedInSuccessfully) {
        String verifyLogin = "SELECT COUNT(1) FROM employee WHERE email = ? AND password = ? AND isActive = 1";

        if (accountType == AccountType.ADMIN) {
            verifyLogin += " AND isManager = 1";
        }
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(verifyLogin);) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, PasswordUtils.hashPassword(password));
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
                    Employee employee = mapResultSetToEmployee(resultSet);
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

    @Override
    public ObservableList<Employee> getAllEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee ORDER BY name ASC";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = mapResultSetToEmployee(resultSet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public ObservableList<Employee> getActiveEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee WHERE isActive = 1 ORDER BY name ASC";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = mapResultSetToEmployee(resultSet);
                employees.add(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public ObservableList<Employee> searchEmployees(String searchTerm) {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        String query = "SELECT * FROM employee WHERE name LIKE ? OR email LIKE ? ORDER BY name ASC";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = mapResultSetToEmployee(resultSet);
                    employees.add(employee);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        String query = "SELECT * FROM employee WHERE employeeId = ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEmployee(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean createEmployee(Employee employee) {
        String query = "INSERT INTO employee (name, email, password, contactNumber, isManager, isActive) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            
            String hashedPassword = PasswordUtils.hashPassword(employee.getPassword());
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, employee.getContactNumber());
            preparedStatement.setBoolean(5, employee.isManager());
            preparedStatement.setBoolean(6, employee.isActive());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        // Check if password is being updated
        String query;
        boolean updatePassword = employee.getPassword() != null && !employee.getPassword().isEmpty();

        if (updatePassword) {
            query = "UPDATE employee SET name = ?, email = ?, password = ?, contactNumber = ?, isManager = ?, isActive = ? WHERE employeeId = ?";
        } else {
            query = "UPDATE employee SET name = ?, email = ?, contactNumber = ?, isManager = ?, isActive = ? WHERE employeeId = ?";
        }

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());

            if (updatePassword) {
                preparedStatement.setString(3, PasswordUtils.hashPassword(employee.getPassword()));
                preparedStatement.setString(4, employee.getContactNumber());
                preparedStatement.setBoolean(5, employee.isManager());
                preparedStatement.setBoolean(6, employee.isActive());
                preparedStatement.setInt(7, employee.getEmployeeId());
            } else {
                preparedStatement.setString(3, employee.getContactNumber());
                preparedStatement.setBoolean(4, employee.isManager());
                preparedStatement.setBoolean(5, employee.isActive());
                preparedStatement.setInt(6, employee.getEmployeeId());
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int employeeId) {
        // Check if employee has any bookings
        String checkQuery = "SELECT COUNT(*) FROM booking WHERE EmployeeID = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, employeeId);
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Employee has bookings, just deactivate instead of deleting
                    return toggleEmployeeStatus(employeeId, false);
                }
            }

            // No bookings, safe to delete
            String deleteQuery = "DELETE FROM employee WHERE employeeId = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, employeeId);
                int rowsAffected = deleteStmt.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean toggleEmployeeStatus(int employeeId, boolean isActive) {
        String query = "UPDATE employee SET isActive = ? WHERE employeeId = ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBoolean(1, isActive);
            preparedStatement.setInt(2, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email, int excludeEmployeeId) {
        String query = "SELECT COUNT(*) FROM employee WHERE email = ? AND employeeId != ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, excludeEmployeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public int getActiveEmployeeCount() {
        String query = "SELECT COUNT(*) FROM employee WHERE isActive = 1";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public ObservableList<Object[]> getEmployeePerformance() {
        ObservableList<Object[]> performance = FXCollections.observableArrayList();
        String query = "SELECT e.name, COUNT(b.BookingID) as bookingCount "
                + "FROM employee e "
                + "LEFT JOIN booking b ON e.employeeId = b.EmployeeID "
                + "WHERE e.isActive = 1 "
                + "GROUP BY e.employeeId, e.name "
                + "ORDER BY bookingCount DESC";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int bookingCount = resultSet.getInt("bookingCount");
                performance.add(new Object[]{name, bookingCount});
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return performance;
    }

    @Override
    public ObservableList<Object[]> getTopPerformers(int limit) {
        ObservableList<Object[]> topPerformers = FXCollections.observableArrayList();
        String query = "SELECT e.name, COUNT(b.BookingID) as bookingCount, "
                + "COALESCE(SUM(pay.amount), 0) as totalSales "
                + "FROM employee e "
                + "LEFT JOIN booking b ON e.employeeId = b.EmployeeID "
                + "LEFT JOIN payment pay ON b.BookingID = pay.bookingId AND pay.status = 'PAID' "
                + "WHERE e.isActive = 1 "
                + "GROUP BY e.employeeId, e.name "
                + "ORDER BY bookingCount DESC, totalSales DESC "
                + "LIMIT ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int rank = 1;
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int bookingCount = resultSet.getInt("bookingCount");
                    double totalSales = resultSet.getDouble("totalSales");
                    topPerformers.add(new Object[]{rank++, name, bookingCount, totalSales});
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return topPerformers;
    }

    private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
        int employeeId = resultSet.getInt("employeeId");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        String contactNumber = resultSet.getString("contactNumber");
        boolean isManager = resultSet.getBoolean("isManager");
        boolean isActive = resultSet.getBoolean("isActive");

        return new Employee(employeeId, name, email, password, contactNumber, isManager, isActive);
    }

}
