package com.cht.travelmanagement.Models;

import com.cht.travelmanagement.View.AdminViewFactory;
import com.cht.travelmanagement.View.UserViewFactory;
import com.cht.travelmanagement.View.ViewFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final AdminViewFactory adminViewFactory;
    private final UserViewFactory userViewFactory;

    private boolean userLoggedInSuccessfully;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.adminViewFactory = new AdminViewFactory();
        this.userViewFactory = new UserViewFactory();
        this.userLoggedInSuccessfully = false;

    }

    public  static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public AdminViewFactory getAdminViewFactory() {
        return adminViewFactory;
    }

    public UserViewFactory getUserViewFactory() {
        return userViewFactory;
    }

    /**
     * User Methods Section
     */
    public void setUserLoggedInSuccessfully(boolean status) {
        this.userLoggedInSuccessfully = status;
    }

    public boolean getUserLoggedInSuccessfully() {
        return this.userLoggedInSuccessfully;
    }
    public void evaluateLoginCredentials(String email, String password) {
        String verifyLogin = "SELECT COUNT(1) FROM employee WHERE email = ? AND  password = ?";
        try (Connection connection = DatabaseDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(verifyLogin);
        ) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 1) {
                        this.userLoggedInSuccessfully = true;
                    } else {
                        System.out.println("Invalid Login. Please try again.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
