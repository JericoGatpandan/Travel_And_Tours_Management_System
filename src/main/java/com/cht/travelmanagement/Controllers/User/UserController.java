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
                case NEW_BOOKING -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getNewBookingView());
                case BOOKINGS -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getBookingListView());
                case CLIENTS -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getClientListView());
                case TOUR_PACKAGES ->  user_parent.setCenter(Model.getInstance().getUserViewFactory().getTourPackageListView());
                case TRIPS -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getTripsListView());
                case HOTELS -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getHotelsListView());
                case TRANSPORTATION -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getTransportationListView());
                case PAYMENTS -> user_parent.setCenter(Model.getInstance().getUserViewFactory().getPaymentsListView());
                default -> {
                    user_parent.setCenter(Model.getInstance().getUserViewFactory().getUserDashboardPane());
                    // Refresh dashboard data when navigating back to it
                    DashboardController dashboard = Model.getInstance().getUserViewFactory().getDashboardController();
                    if (dashboard != null) {
                        dashboard.refresh();
                    }
                }
            }
        });
    }
}
