package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.Employee;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardController implements Initializable {

    @FXML
    private TableColumn<Booking, String> bookingId_col;

    @FXML
    private TableView<Booking> booking_table;

    @FXML
    private TableColumn<Booking, String> client_col;

    @FXML
    private Label completedTrips_lbl;

    @FXML
    private TableColumn<Booking, String> destination_col;

    @FXML
    private Button newBooking_btn;

    @FXML
    private Label ongoingTrips_lbl;

    @FXML
    private TableColumn<Booking, String> package_col;

    @FXML
    private TableColumn<Booking, String> startDate_col;

    @FXML
    private TableColumn<Booking, String> status_col;

    @FXML
    private Label totalCustomer_lbl;

    @FXML
    private Label upcomingTrips_lbl;

    @FXML
    private Button viewAll_btn;

    @FXML
    private Label welcome_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newBooking_btn.setOnAction(event -> onNewBookingButtonClicked());
        viewAll_btn.setOnAction(event -> onViewAllButtonClicked());

        // Load and display authenticated user information
        Model.getInstance().loadAuthenticatedUser();
        Employee authenticatedUser = Model.getInstance().getAuthenticatedUser();
        if (authenticatedUser != null) {
            welcome_lbl.setText("Welcome back, " + authenticatedUser.getName());
        }

        getDashboardData();
    }

    /**
     * Refreshes the dashboard data.
     * Called when the user navigates back to this view.
     */
    public void refresh() {
        getDashboardData();
    }

    private void onNewBookingButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.NEW_BOOKING);
    }

    private void onViewAllButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.BOOKINGS);
    }

    private void getDashboardData() {
        int[] dashboardData = Model.getInstance().getDashboardData();
        totalCustomer_lbl.setText(String.valueOf(dashboardData[0]));
        ongoingTrips_lbl.setText(String.valueOf(dashboardData[1]));
        upcomingTrips_lbl.setText(String.valueOf(dashboardData[2]));
        completedTrips_lbl.setText(String.valueOf(dashboardData[3]));
        LoadRecentBookings();

    }

    private void LoadRecentBookings() {
        ObservableList<Booking> recentBookings = Model.getInstance().getRecentBookings();
        loadBookings(recentBookings, booking_table);
    }

    /**
     * Load bookings into the TableView with cell value factories.
     */
    private void loadBookings(ObservableList<Booking> bookings, TableView<Booking> booking_table) {
        booking_table.setItems(bookings);

        bookingId_col.setCellValueFactory(cellData -> cellData.getValue().bookingIdProperty().asString());
        client_col.setCellValueFactory(cellData -> cellData.getValue().clientNameProperty());
        package_col.setCellValueFactory(cellData -> cellData.getValue().packageNameProperty());
        destination_col.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());
        startDate_col.setCellValueFactory(cellData -> cellData.getValue().bookingDateProperty().asString());
        status_col.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    }
}
