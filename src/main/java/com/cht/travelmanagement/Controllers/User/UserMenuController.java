package com.cht.travelmanagement.Controllers.User;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    public Button addBooking_btn;
    public Button dashboard_btn;
    public Button clients_btn;
    public Button bookings_btn;
    public Button tour_package_btn;
    public Button trips_btn;
    public Button hotel_btn;
    public Button transportation_btn;
    public Button payment_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBooking_btn.setOnAction(event -> {
            onNewBookingButtonClicked();});
        dashboard_btn.setOnAction(event -> {onDashboardButtonClicked();});
        bookings_btn.setOnAction(event -> {onBookingsButtonClicked();});
        clients_btn.setOnAction(event -> {onClientsButtonClicked();});
        tour_package_btn.setOnAction(event -> {onTourPackageButtonClicked();});
        trips_btn.setOnAction(event -> {onTripsButtonClicked();});
        hotel_btn.setOnAction(event -> {onHotelButtonClicked();});
        transportation_btn.setOnAction(event -> {onTransportationButtonClicked();});
        payment_btn.setOnAction(event -> {onPaymentButtonClicked();});
        logout_btn.setOnAction(event -> {onLogoutButtonClicked();});
    }

    private void onNewBookingButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.NEW_BOOKING);
    }

    private void onDashboardButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.DASHBOARD);
    }
    private void onBookingsButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.BOOKINGS);
    }
    private void onClientsButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.CLIENTS);
    }
    private  void onTourPackageButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.TOUR_PACKAGES);
    }
    private void onTripsButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.TRIPS);
    }
    private void onHotelButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.HOTELS);
    }
    private void onTransportationButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.TRANSPORTATION);
    }
    private void onPaymentButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.PAYMENTS);
    }
    private void onLogoutButtonClicked() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setUserLoggedInSuccessfully(false);
    }
}
