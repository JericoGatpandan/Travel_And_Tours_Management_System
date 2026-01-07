package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.AlertUtility;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class BookingStep6Controller implements Initializable {

    @FXML
    private Label summary_name_lbl;
    @FXML
    private Label summary_date_lbl;
    @FXML
    private Label summary_package_lbl;
    @FXML
    private Label summary_hotel_lbl;
    @FXML
    private Label summary_vehicle_lbl;
    @FXML
    private Label total_price_lbl;
    @FXML
    private CheckBox terms_condition_check;

    private BookingData bookingData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        loadSummaryData();
        setupTermsCheckbox();
    }

    private void loadSummaryData() {
        // Client name
        summary_name_lbl.setText(bookingData.getClientName().isEmpty() ? "--"
                : bookingData.getClientName() + " (" + bookingData.getPaxCount() + " pax)");

        // Travel date
        summary_date_lbl.setText(bookingData.getBookingDate() != null
                ? bookingData.getBookingDate().toString() : LocalDate.now().toString());

        // Package
        if (bookingData.getSelectedPackageId() > 0) {
            summary_package_lbl.setText(bookingData.getSelectedPackageName()
                    + " - " + bookingData.getPackageDestination()
                    + " (" + bookingData.getPackageDuration() + " days)");
        } else {
            summary_package_lbl.setText("No package selected");
        }

        // Hotel
        if (bookingData.getSelectedHotelId() > 0) {
            summary_hotel_lbl.setText(bookingData.getSelectedHotelName()
                    + " " + bookingData.getHotelRating());
        } else {
            summary_hotel_lbl.setText("No hotel selected");
        }

        // Vehicle
        if (bookingData.getSelectedVehicleId() > 0) {
            summary_vehicle_lbl.setText(bookingData.getSelectedVehicleName());
        } else {
            summary_vehicle_lbl.setText("No vehicle selected");
        }

        // Total price
        int totalPrice = bookingData.calculateTotalPrice();
        total_price_lbl.setText("PHP " + String.format("%,.2f", (double) totalPrice));
    }

    private void setupTermsCheckbox() {
        terms_condition_check.setSelected(bookingData.isTermsAccepted());

        terms_condition_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setTermsAccepted(newVal);
        });
    }

    /**
     * Submit the booking - called from navigation controller
     */
    public boolean submitBooking() {
        if (!bookingData.isTermsAccepted()) {
            AlertUtility.showWarning("Terms Required", "Terms Not Accepted", "Please accept the terms and conditions before submitting.");
            return false;
        }

        // Validate all required fields
        if (!validateBooking()) {
            return false;
        }

        // Create new client if needed
        if (bookingData.getClientId() <= 0) {
            int newClientId = Model.getInstance().createClient(
                    bookingData.getClientName(),
                    bookingData.getClientEmail(),
                    bookingData.getClientContact(),
                    "", // Address not collected in this wizard
                    bookingData.getTravelType().equalsIgnoreCase("Business") ? "CORPORATE" : "REGULAR"
            );

            if (newClientId > 0) {
                bookingData.setClientId(newClientId);
                System.out.println("Created new client with ID: " + newClientId);
            } else {
                AlertUtility.showError("Error", "Client Creation Failed", "Failed to create new client. Please try again.");
                return false;
            }
        }

        // Create the booking
        boolean success = Model.getInstance().createBooking(bookingData);

        if (success) {
            AlertUtility.showSuccess("Booking Confirmed", "Booking Successful!",
                    "Your booking has been successfully created!\n\n"
                    + "Client: " + bookingData.getClientName() + "\n"
                    + "Package: " + bookingData.getSelectedPackageName() + "\n"
                    + "Total: PHP " + String.format("%,.2f", (double) bookingData.calculateTotalPrice()));

            // Reset for next booking
            Model.getInstance().getUserViewFactory().resetBookingWizard();

            return true;
        } else {
            AlertUtility.showError("Booking Failed", "Submission Error", "Failed to create booking. Please try again.");
            return false;
        }
    }

    private boolean validateBooking() {
        StringBuilder errors = new StringBuilder();

        if (bookingData.getClientName().isEmpty()) {
            errors.append("• Client name is required\n");
        }
        if (bookingData.getClientEmail().isEmpty()) {
            errors.append("• Client email is required\n");
        }
        if (bookingData.getSelectedPackageId() <= 0) {
            errors.append("• Please select a tour package\n");
        }
        if (bookingData.getSelectedHotelId() <= 0) {
            errors.append("• Please select a hotel\n");
        }
        if (bookingData.getSelectedVehicleId() <= 0) {
            errors.append("• Please select a vehicle\n");
        }

        if (errors.length() > 0) {
            AlertUtility.showWarning("Incomplete Booking", "Missing Information", "Please complete the following:\n\n" + errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Refresh summary when step becomes visible
     */
    public void refreshSummary() {
        loadSummaryData();
    }
}
