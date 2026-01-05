package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.Employee;
import com.cht.travelmanagement.View.AccountType;

import javafx.collections.ObservableList;

public interface EmployeeRepository {

    void evaluateLoginCredentials(String email, String password, AccountType accountType, boolean userLoggedInSuccessfully);

    void getAuthenticatedUser();

    ObservableList<Client> getEmployeeClientList(int employeeId);

    // Employee CRUD Operations
    ObservableList<Employee> getAllEmployees();

    ObservableList<Employee> getActiveEmployees();

    ObservableList<Employee> searchEmployees(String searchTerm);

    Employee getEmployeeById(int employeeId);

    boolean createEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(int employeeId);

    boolean toggleEmployeeStatus(int employeeId, boolean isActive);

    boolean isEmailExists(String email, int excludeEmployeeId);

    int getActiveEmployeeCount();

    // Dashboard Data
    ObservableList<Object[]> getEmployeePerformance();

    ObservableList<Object[]> getTopPerformers(int limit);
}
