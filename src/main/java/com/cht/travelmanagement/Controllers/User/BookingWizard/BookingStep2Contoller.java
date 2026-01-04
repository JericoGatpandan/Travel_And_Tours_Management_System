package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.TourPackage;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BookingStep2Contoller implements Initializable {

    @FXML
    private GridPane package_grid;

    private BookingData bookingData;
    private ObservableList<TourPackage> tourPackages;
    private VBox selectedCard = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();
        loadTourPackages();
    }

    private void loadTourPackages() {
        tourPackages = Model.getInstance().getTourPackages();

        if (package_grid == null) {
            // If grid is not in FXML, we need to find it differently
            System.out.println("Loading " + tourPackages.size() + " tour packages");
            return;
        }

        package_grid.getChildren().clear();
        package_grid.setHgap(20);
        package_grid.setVgap(20);
        package_grid.setPadding(new Insets(20));

        int col = 0;
        int row = 0;

        for (TourPackage pkg : tourPackages) {
            VBox packageCard = createPackageCard(pkg);
            package_grid.add(packageCard, col, row);

            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        // Highlight previously selected package
        highlightSelectedPackage();
    }

    private VBox createPackageCard(TourPackage pkg) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(getCardStyle(false));
        card.setPrefWidth(350);
        card.setMinHeight(280);

        // Package name
        Label nameLabel = new Label(pkg.getPackageName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        nameLabel.setWrapText(true);

        // Destination
        Label destLabel = new Label("📍 " + pkg.getDestination());
        destLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");

        // Duration
        Label durationLabel = new Label("🗓 " + pkg.getDurationDays() + " Days");
        durationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");

        // Description
        Label descLabel = new Label(pkg.getDescription());
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(60);

        // Inclusions
        Label inclusionsLabel = new Label("✓ " + pkg.getInclusions());
        inclusionsLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60;");
        inclusionsLabel.setWrapText(true);

        // Max participants
        Label maxPaxLabel = new Label("👥 Max " + pkg.getMaxParticipants() + " participants");
        maxPaxLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");

        // Price
        Label priceLabel = new Label("PHP " + String.format("%,.2f", (double) pkg.getPrice()));
        priceLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        // Select button
        Button selectBtn = new Button("Select Package");
        selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;");
        selectBtn.setMaxWidth(Double.MAX_VALUE);

        selectBtn.setOnAction(e -> selectPackage(pkg, card));
        selectBtn.setOnMouseEntered(e -> selectBtn.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;"));
        selectBtn.setOnMouseExited(e -> selectBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-padding: 10 20; -fx-cursor: hand; -fx-background-radius: 5;"));

        // Spacer
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(nameLabel, destLabel, durationLabel, descLabel, inclusionsLabel, maxPaxLabel, spacer, priceLabel, selectBtn);

        // Click anywhere on card to select
        card.setOnMouseClicked(e -> selectPackage(pkg, card));
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

        // Store package ID for reference
        card.setUserData(pkg.getPackageId());

        return card;
    }

    private void selectPackage(TourPackage pkg, VBox card) {
        // Deselect previous card
        if (selectedCard != null) {
            selectedCard.setStyle(getCardStyle(false));
        }

        // Select new card
        selectedCard = card;
        card.setStyle(getCardStyle(true));

        // Update booking data
        bookingData.setSelectedPackageId(pkg.getPackageId());
        bookingData.setSelectedPackageName(pkg.getPackageName());
        bookingData.setPackagePrice(pkg.getPrice());
        bookingData.setPackageDestination(pkg.getDestination());
        bookingData.setPackageDuration(pkg.getDurationDays());

        System.out.println("Selected package: " + pkg.getPackageName() + " (PHP " + pkg.getPrice() + ")");
    }

    private void highlightSelectedPackage() {
        if (bookingData.getSelectedPackageId() > 0 && package_grid != null) {
            package_grid.getChildren().forEach(node -> {
                if (node instanceof VBox card) {
                    Integer pkgId = (Integer) card.getUserData();
                    if (pkgId != null && pkgId == bookingData.getSelectedPackageId()) {
                        selectedCard = card;
                        card.setStyle(getCardStyle(true));
                    }
                }
            });
        }
    }

    private String getCardStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #e8f4fd; -fx-border-color: #007bff; -fx-border-width: 3; "
                    + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,123,255,0.4), 10, 0, 0, 2);";
        }
        return "-fx-background-color: #fff; -fx-border-color: #e0e0e0; -fx-border-width: 1; "
                + "-fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);";
    }
}
