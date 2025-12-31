package com.cht.travelmanagement.Controllers.User;

import com.cht.travelmanagement.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public BorderPane user_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().addListener((observable, oldVal, newVal) -> {
            switch (newVal) {
                case ADD_BOOKING -> {
                    user_parent.setCenter(Model.getInstance().getUserViewFactory().getAddBookingView());
                }
                default -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getUserDashboardPane());
            }
        });
    }
}
