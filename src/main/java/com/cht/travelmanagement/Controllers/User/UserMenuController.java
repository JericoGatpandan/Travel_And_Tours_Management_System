package com.cht.travelmanagement.Controllers.User;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    public Button addBooking_btn;
    public Button dashboard_btn;
    public Button customers_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBooking_btn.setOnAction(event -> {onAddBookingButtonClicked();});
        dashboard_btn.setOnAction(event -> {onDashboardButtonClicked();});
    }

    private void onAddBookingButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.ADD_BOOKING);
    }

    private void onDashboardButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.DASHBOARD);
    }
}
