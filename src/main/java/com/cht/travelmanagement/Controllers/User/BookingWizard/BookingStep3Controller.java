package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class BookingStep3Controller implements Initializable {

    @FXML
    private CheckBox breakfast_check;
    @FXML
    private CheckBox insurance_check;
    @FXML
    private CheckBox guide_check;
    @FXML
    private CheckBox pickup_check;
    @FXML
    private TextArea special_requests_area;

    private BookingData bookingData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        setupCheckboxListeners();
        setupTextAreaListener();
        loadExistingData();
    }

    private void setupCheckboxListeners() {
        // Breakfast add-on
        breakfast_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setIncludeBreakfast(newVal);
            updatePriceDisplay();
        });

        // Insurance add-on
        insurance_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setIncludeInsurance(newVal);
            updatePriceDisplay();
        });

        // Private guide add-on
        guide_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setIncludeGuide(newVal);
            updatePriceDisplay();
        });

        // Airport pickup add-on
        pickup_check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setIncludePickup(newVal);
            updatePriceDisplay();
        });
    }

    private void setupTextAreaListener() {
        special_requests_area.textProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setSpecialRequests(newVal);
        });
    }

    private void loadExistingData() {
        // Load previously selected options
        breakfast_check.setSelected(bookingData.isIncludeBreakfast());
        insurance_check.setSelected(bookingData.isIncludeInsurance());
        guide_check.setSelected(bookingData.isIncludeGuide());
        pickup_check.setSelected(bookingData.isIncludePickup());

        if (!bookingData.getSpecialRequests().isEmpty()) {
            special_requests_area.setText(bookingData.getSpecialRequests());
        }
    }

    private void updatePriceDisplay() {
        int addonsTotal = bookingData.getAddonsTotal();
        System.out.println("Add-ons total: PHP " + addonsTotal);
    }

    /**
     * Get summary of selected add-ons
     */
    public String getAddOnsSummary() {
        StringBuilder summary = new StringBuilder();

        if (bookingData.isIncludeBreakfast()) {
            summary.append("• Daily Breakfast\n");
        }
        if (bookingData.isIncludeInsurance()) {
            summary.append("• Travel Insurance\n");
        }
        if (bookingData.isIncludeGuide()) {
            summary.append("• Private Tour Guide\n");
        }
        if (bookingData.isIncludePickup()) {
            summary.append("• Airport Pickup/Drop-off\n");
        }

        if (summary.length() == 0) {
            return "No add-ons selected";
        }

        return summary.toString();
    }
}
