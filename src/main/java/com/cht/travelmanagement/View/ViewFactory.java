package com.cht.travelmanagement.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private AccountType loggedInAccountType;


    public ViewFactory() {
        this.loggedInAccountType = AccountType.USER;
    }
    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Login-view.fxml"));
        createStage(loader);
    }

    public AccountType getLoggedInAccountType() {
        return loggedInAccountType;
    }

    public void setLoggedInAccountType(AccountType loggedInAccountType) {
        this.loggedInAccountType = loggedInAccountType;
    }

    public void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("CHT Travel & Tour Management System");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
