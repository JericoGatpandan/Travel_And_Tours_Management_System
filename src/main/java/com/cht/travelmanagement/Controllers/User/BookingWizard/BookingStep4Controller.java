package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Accommodation;
import com.cht.travelmanagement.Models.Model;

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

public class BookingStep4Controller implements Initializable {

    @FXML
    private RadioButton all_hotel_radio;
    @FXML
    private RadioButton three_star_radio;
    @FXML
    private RadioButton four_star_radio;
    @FXML
    private RadioButton five_star_radio;
    @FXML
    private ToggleGroup hotel_rating_group;
    @FXML
    private VBox hotel_container;

    private BookingData bookingData;
    private ObservableList<Accommodation> allHotels;
    private HBox selectedCard = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();

        loadHotels();
        setupFilterListeners();
    }

    private void loadHotels() {
        allHotels = Model.getInstance().getHotels();
        displayHotels(allHotels);
    }

    private void setupFilterListeners() {
        hotel_rating_group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterHotels();
            }
        });
    }

    private void filterHotels() {
        ObservableList<Accommodation> filteredHotels = FXCollections.observableArrayList();

        if (all_hotel_radio.isSelected()) {
            filteredHotels.addAll(allHotels);
        } else if (three_star_radio.isSelected()) {
            allHotels.stream().filter(h -> h.getRating() == 3).forEach(filteredHotels::add);
        } else if (four_star_radio.isSelected()) {
            allHotels.stream().filter(h -> h.getRating() == 4).forEach(filteredHotels::add);
        } else if (five_star_radio.isSelected()) {
            allHotels.stream().filter(h -> h.getRating() == 5).forEach(filteredHotels::add);
        }

        displayHotels(filteredHotels);
    }

    private void displayHotels(ObservableList<Accommodation> hotels) {
        hotel_container.getChildren().clear();
        hotel_container.setSpacing(15);
        hotel_container.setPadding(new Insets(10));

        if (hotels.isEmpty()) {
            Label noResults = new Label("No hotels found for this filter.");
            noResults.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");
            hotel_container.getChildren().add(noResults);
            return;
        }

        for (Accommodation hotel : hotels) {
            HBox hotelCard = createHotelCard(hotel);
            hotel_container.getChildren().add(hotelCard);
        }

        // Highlight previously selected hotel
        highlightSelectedHotel();
    }

    private HBox createHotelCard(Accommodation hotel) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle(getCardStyle(false));
        card.setAlignment(Pos.CENTER_LEFT);

        // Hotel info section
        VBox infoBox = new VBox(5);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Hotel name
        Label nameLabel = new Label(hotel.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Star rating
        Label ratingLabel = new Label(hotel.getStarRating());
        ratingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #f39c12;");

        // Address
        Label addressLabel = new Label("📍 " + hotel.getAddress());
        addressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");

        // Room type
        Label roomLabel = new Label("🛏 " + hotel.getDefaultRoomType());
        roomLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");

        // Amenities
        Label amenitiesLabel = new Label("✓ " + (hotel.getAmenities() != null ? hotel.getAmenities().replace(";", " • ") : "Standard amenities"));
        amenitiesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60;");

        infoBox.getChildren().addAll(nameLabel, ratingLabel, addressLabel, roomLabel, amenitiesLabel);

        // Price section
        VBox priceBox = new VBox(5);
        priceBox.setAlignment(Pos.CENTER_RIGHT);
        priceBox.setMinWidth(150);

        Label priceLabel = new Label("PHP " + String.format("%,d", hotel.getPricePerNight()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        Label perNightLabel = new Label("per night");
        perNightLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7F8C8D;");

        Button selectBtn = new Button("Select");
        selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;");
        selectBtn.setOnAction(e -> selectHotel(hotel, card));
        selectBtn.setOnMouseEntered(e -> selectBtn.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));
        selectBtn.setOnMouseExited(e -> selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 5;"));

        priceBox.getChildren().addAll(priceLabel, perNightLabel, selectBtn);

        card.getChildren().addAll(infoBox, priceBox);

        // Click anywhere on card to select
        card.setOnMouseClicked(e -> selectHotel(hotel, card));
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

        // Store hotel ID for reference
        card.setUserData(hotel.getAccommodationId());

        return card;
    }

    private void selectHotel(Accommodation hotel, HBox card) {
        // Deselect previous card
        if (selectedCard != null) {
            selectedCard.setStyle(getCardStyle(false));
        }

        // Select new card
        selectedCard = card;
        card.setStyle(getCardStyle(true));

        // Update booking data
        int totalNights = Math.max(1, bookingData.getPackageDuration() - 1); // Duration minus 1 for check-out day
        int totalHotelPrice = hotel.getPricePerNight() * totalNights;

        bookingData.setSelectedHotelId(hotel.getAccommodationId());
        bookingData.setSelectedHotelName(hotel.getName());
        bookingData.setHotelPrice(totalHotelPrice);
        bookingData.setHotelRating(hotel.getStarRating());

        System.out.println("Selected hotel: " + hotel.getName() + " (PHP " + totalHotelPrice + " for " + totalNights + " nights)");
    }

    private void highlightSelectedHotel() {
        if (bookingData.getSelectedHotelId() > 0) {
            hotel_container.getChildren().forEach(node -> {
                if (node instanceof HBox card) {
                    Integer hotelId = (Integer) card.getUserData();
                    if (hotelId != null && hotelId == bookingData.getSelectedHotelId()) {
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
