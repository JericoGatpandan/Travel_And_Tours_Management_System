package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Accommodation;
import com.cht.travelmanagement.Models.Model;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AccommodationsController implements Initializable {

    @FXML
    private FlowPane accommodations_grid;
    @FXML
    private TextField search_fld;
    @FXML
    private Label accommodationCount_lbl;

    private ObservableList<Accommodation> accommodationsList;
    private FilteredList<Accommodation> filteredAccommodations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAccommodations();
        setupSearch();
    }

    private void loadAccommodations() {
        accommodationsList = Model.getInstance().getAllAccommodations();
        filteredAccommodations = new FilteredList<>(accommodationsList, p -> true);

        renderAccommodations();
        updateCount();
    }

    private void renderAccommodations() {
        accommodations_grid.getChildren().clear();

        for (Accommodation acc : filteredAccommodations) {
            accommodations_grid.getChildren().add(createAccommodationCard(acc));
        }
    }

    private VBox createAccommodationCard(Accommodation acc) {
        VBox card = new VBox(12);
        card.setPrefWidth(320);
        card.setMinWidth(280);
        card.setMaxWidth(350);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 0);");

        // Hotel icon and name header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView hotelIcon = new FontAwesomeIconView();
        hotelIcon.setGlyphName("BUILDING");
        hotelIcon.setSize("24");
        hotelIcon.setStyle("-fx-fill: #3498db;");

        Label nameLabel = new Label(acc.getName());
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        nameLabel.setWrapText(true);

        header.getChildren().addAll(hotelIcon, nameLabel);

        // Address
        HBox addressRow = createInfoRow("MAP_MARKER", acc.getAddress());

        // Contact
        HBox contactRow = createInfoRow("PHONE", acc.getContact() != null && !acc.getContact().isEmpty()
                ? acc.getContact() : "No contact info");

        // Room type badge
        Label roomTypeBadge = new Label(acc.getDefaultRoomType());
        roomTypeBadge.setStyle("-fx-background-color: #e8f4fd; -fx-text-fill: #1976d2; "
                + "-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-size: 11;");

        // Number of rooms
        HBox roomsRow = new HBox(10);
        roomsRow.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView bedIcon = new FontAwesomeIconView();
        bedIcon.setGlyphName("BED");
        bedIcon.setSize("14");
        bedIcon.setStyle("-fx-fill: #95A5A6;");

        Label roomsLabel = new Label(acc.getNumberOfRooms() + " rooms available");
        roomsLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");

        roomsRow.getChildren().addAll(bedIcon, roomsLabel);

        // Amenities
        if (acc.getAmenities() != null && !acc.getAmenities().isEmpty()) {
            Label amenitiesLabel = new Label("✓ " + acc.getAmenitiesFormatted());
            amenitiesLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11;");
            amenitiesLabel.setWrapText(true);
            card.getChildren().addAll(header, addressRow, contactRow, roomTypeBadge, roomsRow, amenitiesLabel);
        } else {
            card.getChildren().addAll(header, addressRow, contactRow, roomTypeBadge, roomsRow);
        }

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #f8f9fa; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 15, 0, 0, 0);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 0);"
        ));

        return card;
    }

    private HBox createInfoRow(String iconName, String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName(iconName);
        icon.setSize("14");
        icon.setStyle("-fx-fill: #95A5A6;");

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
        label.setWrapText(true);

        row.getChildren().addAll(icon, label);
        return row;
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredAccommodations.setPredicate(acc -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return acc.getName().toLowerCase().contains(lowerCaseFilter)
                        || acc.getAddress().toLowerCase().contains(lowerCaseFilter);
            });
            renderAccommodations();
            updateCount();
        });
    }

    private void updateCount() {
        int count = filteredAccommodations.size();
        accommodationCount_lbl.setText(count + (count == 1 ? " accommodation" : " accommodations"));
    }
}
