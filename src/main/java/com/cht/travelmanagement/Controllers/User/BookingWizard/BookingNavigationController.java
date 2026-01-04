package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BookingNavigationController implements Initializable {

    @FXML
    public Button back_btn;
    @FXML
    public Label status_lbl;
    @FXML
    public Button next_btn;

    private BookingData bookingData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        int currentStep = Model.getInstance().getUserViewFactory().getBookingStep().get();
        updateUI(currentStep);

        back_btn.setOnMouseClicked(event -> onBackBtnClicked());
        next_btn.setOnMouseClicked(event -> onNextBtnClicked());

        // Listen for step changes to update UI
        Model.getInstance().getUserViewFactory().getBookingStep().addListener((obs, oldVal, newVal) -> {
            updateUI(newVal.intValue());
        });
    }

    public void onBackBtnClicked() {
        int currentStep = Model.getInstance().getUserViewFactory().getBookingStep().get();

        if (currentStep > 1) {
            currentStep--;
            Model.getInstance().getUserViewFactory().getBookingStep().set(currentStep);
        } else {
            // Go back to booking list or dashboard
            Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.BOOKINGS);
        }
    }

    public void onNextBtnClicked() {
        int currentStep = Model.getInstance().getUserViewFactory().getBookingStep().get();

        // Validate current step before proceeding
        if (!validateCurrentStep(currentStep)) {
            return;
        }

        if (currentStep < 6) {
            currentStep++;
            Model.getInstance().getUserViewFactory().getBookingStep().set(currentStep);
        } else {
            // Final step - submit booking
            submitBooking();
        }
    }

    private boolean validateCurrentStep(int step) {
        if (!bookingData.validateStep(step)) {
            String errorMessage = bookingData.getValidationError(step);
            showValidationError(errorMessage);
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please complete this step");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void submitBooking() {
        // Create new client if needed
        if (bookingData.getClientId() <= 0) {
            int newClientId = Model.getInstance().createClient(
                    bookingData.getClientName(),
                    bookingData.getClientEmail(),
                    bookingData.getClientContact(),
                    "",
                    bookingData.getTravelType().equalsIgnoreCase("Business") ? "CORPORATE" : "REGULAR"
            );

            if (newClientId > 0) {
                bookingData.setClientId(newClientId);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to create client.");
                return;
            }
        }

        // Create booking
        boolean success = Model.getInstance().createBooking(bookingData);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Booking created successfully!\n\n"
                    + "Client: " + bookingData.getClientName() + "\n"
                    + "Package: " + bookingData.getSelectedPackageName() + "\n"
                    + "Total: PHP " + String.format("%,.2f", (double) bookingData.calculateTotalPrice()));

            // Reset and go back to bookings list
            Model.getInstance().getUserViewFactory().resetBookingWizard();
            Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.BOOKINGS);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create booking. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateUI(int currentStep) {
        updateLabel(currentStep);
        updateButtons(currentStep);
    }

    private void updateLabel(int currentStep) {
        status_lbl.setText("Step " + currentStep + " of 6");
    }

    private void updateButtons(int currentStep) {
        // Update back button
        if (currentStep == 1) {
            back_btn.setText("Cancel");
        } else {
            back_btn.setText("Back");
        }

        // Update next button
        if (currentStep == 6) {
            next_btn.setText("Submit Booking");
            next_btn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        } else {
            next_btn.setText("Next");
            next_btn.setStyle("");
        }
    }
}
