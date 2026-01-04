package com.cht.travelmanagement.View;

import java.io.IOException;
import java.util.Objects;

import com.cht.travelmanagement.Controllers.User.DashboardController;
import com.cht.travelmanagement.Controllers.User.UserController;
import com.cht.travelmanagement.Models.BookingData;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class UserViewFactory extends ViewFactory {

    // Views
    private Parent userDashboardView;
    private DashboardController dashboardController; // Capture the controller
    private BorderPane newBookingView;
    private Parent bookingListView;

    private Parent customerListView;
    private Parent tourPackageListView;
    private Parent tripsListView;
    private Parent hotelsListView;
    private Parent transportationListView;
    private Parent paymentsListView;

    private final FXMLPaths fxmlPaths;
    private UserController userController;
    private final ObjectProperty<UserMenuOption> userSelectedMenuItem;
    private final IntegerProperty bookingStep = new SimpleIntegerProperty(1);

    // Shared booking data across all wizard steps
    private final BookingData bookingData;

    // Booking
    private final ObjectProperty<BookingButtons> bookingSelectedButton;
    private AnchorPane bookingStep1View;
    private AnchorPane bookingStep2View;
    private AnchorPane bookingStep3View;
    private AnchorPane bookingStep4View;
    private AnchorPane bookingStep5View;
    private AnchorPane bookingStep6View;

    public UserViewFactory() {
        this.userSelectedMenuItem = new SimpleObjectProperty<>();
        this.bookingSelectedButton = new SimpleObjectProperty<>();
        this.bookingData = new BookingData();

        this.fxmlPaths = new FXMLPaths();
    }

    public ObjectProperty<UserMenuOption> getUserSelectedMenuItem() {
        return userSelectedMenuItem;
    }

    public IntegerProperty getBookingStep() {
        return bookingStep;
    }

    /**
     * Get the shared booking data instance
     */
    public BookingData getBookingData() {
        return bookingData;
    }

    /**
     * Reset booking wizard to initial state (for starting new booking)
     */
    public void resetBookingWizard() {
        bookingData.reset();
        bookingStep.set(1);

        // Clear cached views to force reload
        bookingStep1View = null;
        bookingStep2View = null;
        bookingStep3View = null;
        bookingStep4View = null;
        bookingStep5View = null;
        bookingStep6View = null;
        newBookingView = null;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public Parent getUserDashboardPane() {
        if (userDashboardView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/Dashboard-view.fxml"));
                userDashboardView = loader.load();
                dashboardController = loader.getController();
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
                newBookingView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/BookingWizard/NewBooking-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newBookingView;
    }

    public Parent getBookingListView() {
        if (bookingListView == null) {
            try {
                bookingListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Bookings-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bookingListView;
    }

    public Parent getClientListView() {
        if (customerListView == null) {
            try {
                customerListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Clients-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return customerListView;
    }

    public Parent getTourPackageListView() {
        if (tourPackageListView == null) {
            try {
                tourPackageListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/TourPackages-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tourPackageListView;
    }

    public Parent getTripsListView() {
        if (tripsListView == null) {
            try {
                tripsListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Trips-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tripsListView;
    }

    public Parent getHotelsListView() {
        if (hotelsListView == null) {
            try {
                hotelsListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Accommodations-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hotelsListView;
    }

    public Parent getTransportationListView() {
        if (transportationListView == null) {
            try {
                transportationListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Transportation-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transportationListView;
    }

    public Parent getPaymentsListView() {
        if (paymentsListView == null) {
            try {
                paymentsListView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/User/Payments-view.fxml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paymentsListView;
    }

    /*
     * Booking Process
     */
    public ObjectProperty<BookingButtons> getBookingSelectedButton() {
        return bookingSelectedButton;
    }

    public AnchorPane getBookingStep2View() {
        if (bookingStep2View == null) {
            try {
                bookingStep2View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.PACKAGE_SELECTION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep2View;
    }

    public AnchorPane getBookingStep3View() {
        if (bookingStep3View == null) {
            try {
                bookingStep3View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.CUSTOMIZATION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep3View;
    }

    public AnchorPane getBookingStep4View() {
        if (bookingStep4View == null) {
            try {
                bookingStep4View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.HOTEL_SELECTION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep4View;
    }

    public AnchorPane getBookingStep5View() {
        if (bookingStep5View == null) {
            try {
                bookingStep5View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.VEHICLE_SELECTION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep5View;
    }

    public AnchorPane getBookingStep6View() {
        if (bookingStep6View == null) {
            try {
                bookingStep6View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.CONFIRMATION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep6View;
    }

    public AnchorPane getBookingStep1View() {
        if (bookingStep1View == null) {
            try {
                bookingStep1View = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLPaths.CUSTOMER_INFORMATION)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bookingStep1View;
    }

}
