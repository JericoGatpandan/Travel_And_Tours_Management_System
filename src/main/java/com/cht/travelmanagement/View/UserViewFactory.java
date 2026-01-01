package com.cht.travelmanagement.View;

import com.cht.travelmanagement.Controllers.User.UserController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserViewFactory extends ViewFactory{

    // Views
    private ScrollPane userDashboardView;
    private  BorderPane newBookingView;
    private  AnchorPane bookingListView;
    private AnchorPane customerListView;
    private AnchorPane tourPackageListView;
    private AnchorPane tripsListView;
    private AnchorPane hotelsListView;
    private AnchorPane transportationListView;
    private AnchorPane paymentsListView;

    private final ObjectProperty<UserMenuOption> userSelectedMenuItem;
    public UserViewFactory() {
        this.userSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<UserMenuOption> getUserSelectedMenuItem() {
        return userSelectedMenuItem;
    }

    public ScrollPane getUserDashboardPane() {
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

    public BorderPane getNewBookingView() {
        if (newBookingView == null) {
            try {
                newBookingView = FXMLLoader.load(getClass().getResource("/Views/User/NewBooking-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newBookingView;
    }
    public AnchorPane getBookingListView() {
        if (bookingListView == null) {
            try {
                bookingListView = FXMLLoader.load(getClass().getResource("/Views/User/Bookings-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bookingListView;
    }
    public AnchorPane getClientListView() {
        if (customerListView == null) {
            try {
                customerListView = FXMLLoader.load(getClass().getResource("/Views/User/Clients-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customerListView;
    }
    public AnchorPane getTourPackageListView() {
        if (tourPackageListView == null) {
            try {
                tourPackageListView = FXMLLoader.load(getClass().getResource("/Views/User/TourPackages-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tourPackageListView;
    }
    public AnchorPane getTripsListView() {
        if (tripsListView == null) {
            try {
                tripsListView = FXMLLoader.load(getClass().getResource("/Views/User/Trips-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tripsListView;
    }
    public AnchorPane getHotelsListView() {
        if (hotelsListView == null) {
            try {
                hotelsListView = FXMLLoader.load(getClass().getResource("/Views/User/Hotels-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hotelsListView;
    }
    public AnchorPane getTransportationListView() {
        if (transportationListView == null) {
            try {
                transportationListView = FXMLLoader.load(getClass().getResource("/Views/User/Transportation-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transportationListView;
    }
    public AnchorPane getPaymentsListView() {
        if (paymentsListView == null) {
            try {
                paymentsListView = FXMLLoader.load(getClass().getResource("/Views/User/Payments-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paymentsListView;
    }

}
