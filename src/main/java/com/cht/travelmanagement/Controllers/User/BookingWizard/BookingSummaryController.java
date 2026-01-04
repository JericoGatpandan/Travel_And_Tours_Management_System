package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;

public class BookingSummaryController implements Initializable {

    @FXML
    private Label complete_percent_lbl;
    @FXML
    private Label customer_lbl;
    @FXML
    private Label package_lbl;
    @FXML
    private Label hotel_lbl;
    @FXML
    private Label transport_lbl;
    @FXML
    private Label total_cost_lbl;
    @FXML
    private Text cost_hint_txt;
    @FXML
    private Label completion_count_lbl;
    @FXML
    private ProgressBar progress_bar;

    private BookingData bookingData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        setupBindings();
        updateSummary();

        // Listen for step changes to update summary
        Model.getInstance().getUserViewFactory().getBookingStep().addListener((obs, oldVal, newVal) -> {
            updateSummary();
        });
    }

    private void setupBindings() {
        // Bind customer name
        bookingData.clientNameProperty().addListener((obs, oldVal, newVal) -> updateCustomerSection());
        bookingData.paxCountProperty().addListener((obs, oldVal, newVal) -> updateCustomerSection());

        // Bind package selection
        bookingData.selectedPackageNameProperty().addListener((obs, oldVal, newVal) -> updatePackageSection());
        bookingData.packagePriceProperty().addListener((obs, oldVal, newVal) -> {
            updatePackageSection();
            updateTotalCost();
        });

        // Bind hotel selection
        bookingData.selectedHotelNameProperty().addListener((obs, oldVal, newVal) -> updateHotelSection());
        bookingData.hotelPriceProperty().addListener((obs, oldVal, newVal) -> {
            updateHotelSection();
            updateTotalCost();
        });

        // Bind vehicle selection
        bookingData.selectedVehicleNameProperty().addListener((obs, oldVal, newVal) -> updateTransportSection());
        bookingData.vehiclePriceProperty().addListener((obs, oldVal, newVal) -> {
            updateTransportSection();
            updateTotalCost();
        });

        // Bind add-ons
        bookingData.includeBreakfastProperty().addListener((obs, oldVal, newVal) -> updateTotalCost());
        bookingData.includeInsuranceProperty().addListener((obs, oldVal, newVal) -> updateTotalCost());
        bookingData.includeGuideProperty().addListener((obs, oldVal, newVal) -> updateTotalCost());
        bookingData.includePickupProperty().addListener((obs, oldVal, newVal) -> updateTotalCost());
    }

    private void updateSummary() {
        updateCustomerSection();
        updatePackageSection();
        updateHotelSection();
        updateTransportSection();
        updateTotalCost();
        updateProgress();
    }

    private void updateCustomerSection() {
        if (customer_lbl != null) {
            if (!bookingData.getClientName().isEmpty()) {
                customer_lbl.setText(bookingData.getClientName() + " (" + bookingData.getPaxCount() + " pax)");
                customer_lbl.setStyle("-fx-text-fill: #27ae60;");
            } else {
                customer_lbl.setText("Not set");
                customer_lbl.setStyle("-fx-text-fill: #7F8C8D;");
            }
        }
    }

    private void updatePackageSection() {
        if (package_lbl != null) {
            if (bookingData.getSelectedPackageId() > 0) {
                package_lbl.setText(bookingData.getSelectedPackageName());
                package_lbl.setStyle("-fx-text-fill: #27ae60;");
            } else {
                package_lbl.setText("Not selected");
                package_lbl.setStyle("-fx-text-fill: #7F8C8D;");
            }
        }
    }

    private void updateHotelSection() {
        if (hotel_lbl != null) {
            if (bookingData.getSelectedHotelId() > 0) {
                hotel_lbl.setText(bookingData.getSelectedHotelName());
                hotel_lbl.setStyle("-fx-text-fill: #27ae60;");
            } else {
                hotel_lbl.setText("Not selected");
                hotel_lbl.setStyle("-fx-text-fill: #7F8C8D;");
            }
        }
    }

    private void updateTransportSection() {
        if (transport_lbl != null) {
            if (bookingData.getSelectedVehicleId() > 0) {
                transport_lbl.setText(bookingData.getVehicleType());
                transport_lbl.setStyle("-fx-text-fill: #27ae60;");
            } else {
                transport_lbl.setText("Not selected");
                transport_lbl.setStyle("-fx-text-fill: #7F8C8D;");
            }
        }
    }

    private void updateTotalCost() {
        if (total_cost_lbl != null) {
            int total = bookingData.calculateTotalPrice();
            total_cost_lbl.setText("PHP " + String.format("%,d", total));

            if (cost_hint_txt != null) {
                if (total > 0) {
                    cost_hint_txt.setText(getBreakdown());
                } else {
                    cost_hint_txt.setText("Add items to see total");
                }
            }
        }
    }

    private String getBreakdown() {
        StringBuilder sb = new StringBuilder();

        if (bookingData.getPackagePrice() > 0) {
            sb.append("Package: PHP ").append(String.format("%,d", bookingData.getPackagePrice() * bookingData.getPaxCount())).append("\n");
        }

        int addons = bookingData.getAddonsTotal();
        if (addons > 0) {
            sb.append("Add-ons: PHP ").append(String.format("%,d", addons)).append("\n");
        }

        if (bookingData.getHotelPrice() > 0) {
            sb.append("Hotel: PHP ").append(String.format("%,d", bookingData.getHotelPrice())).append("\n");
        }

        if (bookingData.getVehiclePrice() > 0) {
            sb.append("Transport: PHP ").append(String.format("%,d", bookingData.getVehiclePrice()));
        }

        return sb.length() > 0 ? sb.toString().trim() : "Add items to see total";
    }

    private void updateProgress() {
        int completedCount = bookingData.getCompletedStepsCount();
        int totalSteps = 5; // Customer, Package, Add-ons (always complete), Hotel, Transport

        double progress = (double) completedCount / totalSteps;
        int percentage = (int) (progress * 100);

        if (complete_percent_lbl != null) {
            complete_percent_lbl.setText(percentage + "% Complete");
        }

        if (completion_count_lbl != null) {
            completion_count_lbl.setText(completedCount + "/" + totalSteps);
        }

        if (progress_bar != null) {
            progress_bar.setProgress(progress);

            // Change color based on progress
            if (progress >= 1.0) {
                progress_bar.setStyle("-fx-accent: #28a745;"); // Green when complete
            } else if (progress >= 0.5) {
                progress_bar.setStyle("-fx-accent: #ffc107;"); // Yellow when halfway
            } else {
                progress_bar.setStyle("-fx-accent: #007bff;"); // Blue otherwise
            }
        }
    }
}
