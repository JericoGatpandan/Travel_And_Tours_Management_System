package com.cht.travelmanagement.Controllers.User;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookingsController implements Initializable {
    public ListView<Booking> bookingList;
    public Button newBooking_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadAllBookings();
    }
    private void LoadAllBookings() {
        ObservableList<Booking> recentBookings = Model.getInstance().getAllBookings();
        newBooking_btn.setOnAction(event -> {onNewBookingButtonClicked();});

        loadBookings(recentBookings , bookingList);
    }

    private void onNewBookingButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.NEW_BOOKING);
    }

    /**
     * Load bookings into the ListView with custom cells.
     */
    public void loadBookings(ObservableList<Booking> bookings, ListView<Booking> bookingList) {
        bookingList.setItems(bookings);
        bookingList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Booking booking, boolean empty) {
                super.updateItem(booking, empty);
                if (empty || booking == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/User/BookingItem-view.fxml"));
                        Node node = loader.load();
                        BookingItemController controller = loader.getController();
                        controller.setData(booking);
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });
    }
}
