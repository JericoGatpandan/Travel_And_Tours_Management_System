package com.cht.travelmanagement.View;

import com.cht.travelmanagement.Controllers.User.UserController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class UserViewFactory extends ViewFactory{

    private AnchorPane userDashboardView;
    private  AnchorPane addBookingView;
    private final ObjectProperty<UserMenuOption> userSelectedMenuItem;
    public UserViewFactory() {
        this.userSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<UserMenuOption> getUserSelectedMenuItem() {
        return userSelectedMenuItem;
    }

    public AnchorPane getUserDashboardPane() {
        if (userDashboardView == null) {
            try {
                userDashboardView = FXMLLoader.load(getClass().getResource("/Views/User/Dashboard-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDashboardView;
    }

    public void showUserDashboardWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/User-view.fxml"));
        UserController userController = new UserController();
        loader.setController(userController);
        createStage(loader);
    }

    public AnchorPane getAddBookingView() {
        if (addBookingView == null) {
            try {
                addBookingView = FXMLLoader.load(getClass().getResource("/Views/User/AddBooking-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addBookingView;
    }
}
