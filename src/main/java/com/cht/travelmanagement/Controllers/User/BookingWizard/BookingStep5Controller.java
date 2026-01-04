package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Vehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BookingStep5Controller implements Initializable {

    @FXML
    private RadioButton all_vehicle_radio;
    @FXML
    private RadioButton sedan_radio;
    @FXML
    private RadioButton van_radio;
    @FXML
    private RadioButton bus_radio;
    @FXML
    private ToggleGroup vehicle_type_group;
    @FXML
    private VBox vehicle_container;

    private BookingData bookingData;
    private ObservableList<Vehicle> allVehicles;
    private HBox selectedCard = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        loadVehicles();
        setupFilterListeners();
    }

    private void loadVehicles() {
        allVehicles = Model.getInstance().getVehicles();
        displayVehicles(allVehicles);
    }

    private void setupFilterListeners() {
        vehicle_type_group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterVehicles();
            }
        });
    }

    private void filterVehicles() {
        ObservableList<Vehicle> filteredVehicles = FXCollections.observableArrayList();

        if (all_vehicle_radio.isSelected()) {
            filteredVehicles.addAll(allVehicles);
        } else if (sedan_radio.isSelected()) {
            allVehicles.stream().filter(v -> v.getCapacity() <= 4).forEach(filteredVehicles::add);
        } else if (van_radio.isSelected()) {
            allVehicles.stream().filter(v -> v.getCapacity() > 4 && v.getCapacity() <= 15).forEach(filteredVehicles::add);
        } else if (bus_radio.isSelected()) {
            allVehicles.stream().filter(v -> v.getCapacity() > 15).forEach(filteredVehicles::add);
        }

        displayVehicles(filteredVehicles);
    }

    private void displayVehicles(ObservableList<Vehicle> vehicles) {
        vehicle_container.getChildren().clear();
        vehicle_container.setSpacing(15);
        vehicle_container.setPadding(new Insets(10));

        if (vehicles.isEmpty()) {
            Label noResults = new Label("No vehicles found for this filter.");
            noResults.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");
            vehicle_container.getChildren().add(noResults);
            return;
        }

        for (Vehicle vehicle : vehicles) {
            HBox vehicleCard = createVehicleCard(vehicle);
            vehicle_container.getChildren().add(vehicleCard);
        }

        // Highlight previously selected vehicle
        highlightSelectedVehicle();
    }

    private HBox createVehicleCard(Vehicle vehicle) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle(getCardStyle(false));
        card.setAlignment(Pos.CENTER_LEFT);

        // Vehicle icon based on type
        String icon = getVehicleIcon(vehicle.getCapacity());
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 36px;");
        iconLabel.setMinWidth(60);

        // Vehicle info section
        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Vehicle type
        Label typeLabel = new Label(vehicle.getType());
        typeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Capacity
        Label capacityLabel = new Label("👥 " + vehicle.getCapacity() + " seats");
        capacityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");

        // Provider
        Label providerLabel = new Label("🏢 " + vehicle.getProviderName());
        providerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");

        // Plate number
        Label plateLabel = new Label("🚗 " + vehicle.getPlateNumber());
        plateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");

        // Recommendation based on pax count
        int paxCount = bookingData.getPaxCount();
        String recommendation = getRecommendation(vehicle.getCapacity(), paxCount);
        if (!recommendation.isEmpty()) {
            Label recLabel = new Label(recommendation);
            recLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60; -fx-font-style: italic;");
            infoBox.getChildren().addAll(typeLabel, capacityLabel, providerLabel, plateLabel, recLabel);
        } else {
            infoBox.getChildren().addAll(typeLabel, capacityLabel, providerLabel, plateLabel);
        }

        // Price section
        VBox priceBox = new VBox(5);
        priceBox.setAlignment(Pos.CENTER_RIGHT);
        priceBox.setMinWidth(150);

        Label priceLabel = new Label("PHP " + String.format("%,d", vehicle.getPricePerDay()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Label perDayLabel = new Label("per day");
        perDayLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7F8C8D;");

        Button selectBtn = new Button("Select");
        selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;");
        selectBtn.setOnAction(e -> selectVehicle(vehicle, card));
        selectBtn.setOnMouseEntered(e -> selectBtn.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));
        selectBtn.setOnMouseExited(e -> selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));

        priceBox.getChildren().addAll(priceLabel, perDayLabel, selectBtn);

        card.getChildren().addAll(iconLabel, infoBox, priceBox);

        // Click anywhere on card to select
        card.setOnMouseClicked(e -> selectVehicle(vehicle, card));
        card.setOnMouseEntered(e -> {
            if (selectedCard != card) {
                card.setStyle(getCardStyle(false).replace("#fff", "#f8f9fa"));
            }
        });
        card.setOnMouseExited(e -> {
            if (selectedCard != card) {
                card.setStyle(getCardStyle(false));
            }
        });

        // Store vehicle ID for reference
        card.setUserData(vehicle.getVehicleId());

        return card;
    }

    private String getVehicleIcon(int capacity) {
        if (capacity <= 4) {
            return "🚗";
        }
        if (capacity <= 15) {
            return "🚐";
        }
        return "🚌";
    }

    private String getRecommendation(int capacity, int paxCount) {
        if (paxCount <= 4 && capacity >= 4 && capacity <= 6) {
            return "✓ Recommended for your group size";
        } else if (paxCount <= 10 && capacity >= 10 && capacity <= 15) {
            return "✓ Recommended for your group size";
        } else if (paxCount > 10 && capacity >= 30) {
            return "✓ Recommended for your group size";
        }
        return "";
    }

    private void selectVehicle(Vehicle vehicle, HBox card) {
        // Deselect previous card
        if (selectedCard != null) {
            selectedCard.setStyle(getCardStyle(false));
        }

        // Select new card
        selectedCard = card;
        card.setStyle(getCardStyle(true));

        // Update booking data
        int totalDays = bookingData.getPackageDuration();
        int totalVehiclePrice = vehicle.getPricePerDay() * totalDays;

        bookingData.setSelectedVehicleId(vehicle.getVehicleId());
        bookingData.setSelectedVehicleName(vehicle.getType() + " - " + vehicle.getProviderName());
        bookingData.setVehicleType(vehicle.getType());
        bookingData.setVehiclePrice(totalVehiclePrice);

        System.out.println("Selected vehicle: " + vehicle.getType() + " (PHP " + totalVehiclePrice + " for " + totalDays + " days)");
    }

    private void highlightSelectedVehicle() {
        if (bookingData.getSelectedVehicleId() > 0) {
            vehicle_container.getChildren().forEach(node -> {
                if (node instanceof HBox card) {
                    Integer vehicleId = (Integer) card.getUserData();
                    if (vehicleId != null && vehicleId == bookingData.getSelectedVehicleId()) {
                        selectedCard = card;
                        card.setStyle(getCardStyle(true));
                    }
                }
            });
        }
    }

    private String getCardStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #e8f4fd; -fx-border-color: #007bff; -fx-border-width: 2; "
                    + "-fx-border-radius: 8; -fx-background-radius: 8;";
        }
        return "-fx-background-color: #fff; -fx-border-color: #e0e0e0; -fx-border-width: 1; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;";
    }
}
