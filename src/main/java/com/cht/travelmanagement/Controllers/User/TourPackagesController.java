package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.TourPackage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TourPackagesController implements Initializable {

    @FXML
    private FlowPane packages_grid;
    @FXML
    private TextField search_fld;
    @FXML
    private Label packageCount_lbl;

    private ObservableList<TourPackage> packagesList;
    private FilteredList<TourPackage> filteredPackages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPackages();
        setupSearch();
    }

    private void loadPackages() {
        packagesList = Model.getInstance().getTourPackages();
        filteredPackages = new FilteredList<>(packagesList, p -> true);

        renderPackages();
        updatePackageCount();
    }

    private void renderPackages() {
        packages_grid.getChildren().clear();

        for (TourPackage pkg : filteredPackages) {
            packages_grid.getChildren().add(createPackageCard(pkg));
        }
    }

    private VBox createPackageCard(TourPackage pkg) {
        VBox card = new VBox(12);
        card.setPrefWidth(320);
        card.setMinWidth(280);
        card.setMaxWidth(350);
        card.setPadding(new Insets(0));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 0);");

        // Package Image
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(180);
        imageContainer.setMinHeight(180);
        imageContainer.setMaxHeight(180);
        imageContainer.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 12 12 0 0;");

        if (pkg.getImagePath() != null && !pkg.getImagePath().isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/" + pkg.getImagePath()));
                if (image != null && !image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(320);
                    imageView.setFitHeight(180);
                    imageView.setPreserveRatio(false);
                    imageView.setStyle("-fx-background-radius: 12 12 0 0;");
                    // Clip to rounded corners
                    javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(320, 180);
                    clip.setArcWidth(24);
                    clip.setArcHeight(24);
                    imageView.setClip(clip);
                    imageContainer.getChildren().add(imageView);
                } else {
                    imageContainer.getChildren().add(createPlaceholderImage(pkg.getDestination()));
                }
            } catch (Exception e) {
                imageContainer.getChildren().add(createPlaceholderImage(pkg.getDestination()));
            }
        } else {
            imageContainer.getChildren().add(createPlaceholderImage(pkg.getDestination()));
        }

        // Content container
        VBox content = new VBox(10);
        content.setPadding(new Insets(15, 20, 20, 20));

        // Destination badge
        Label destBadge = new Label(pkg.getDestination());
        destBadge.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1976d2; "
                + "-fx-padding: 4 12; -fx-background-radius: 12; -fx-font-size: 11;");

        // Package name
        Label nameLabel = new Label(pkg.getPackageName());
        nameLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        nameLabel.setWrapText(true);

        // Description
        Label descLabel = new Label(pkg.getDescription());
        descLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13;");
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(60);

        // Info row
        HBox infoRow = new HBox(15);
        infoRow.setAlignment(Pos.CENTER_LEFT);

        // Duration
        HBox durationBox = createInfoItem("CALENDAR", pkg.getDurationDays() + " Days");

        // Max pax
        HBox paxBox = createInfoItem("USERS", "Max " + pkg.getMaxParticipants() + " pax");

        infoRow.getChildren().addAll(durationBox, paxBox);

        // Inclusions
        Label inclusionsLabel = new Label("✓ " + pkg.getInclusions().replace(",", " • "));
        inclusionsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11;");
        inclusionsLabel.setWrapText(true);

        // Price
        HBox priceRow = new HBox();
        priceRow.setAlignment(Pos.CENTER_LEFT);
        Label priceLabel = new Label("₱" + String.format("%,d", pkg.getPrice()));
        priceLabel.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        Label perPersonLabel = new Label(" / person");
        perPersonLabel.setStyle("-fx-text-fill: #95A5A6; -fx-font-size: 12;");
        priceRow.getChildren().addAll(priceLabel, perPersonLabel);

        // Status badge
        HBox statusRow = new HBox();
        statusRow.setAlignment(Pos.CENTER_RIGHT);
        Label statusBadge = new Label(pkg.getIsActive() ? "Active" : "Inactive");
        statusBadge.setStyle(pkg.getIsActive()
                ? "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-padding: 4 10; -fx-background-radius: 10; -fx-font-size: 10;"
                : "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-padding: 4 10; -fx-background-radius: 10; -fx-font-size: 10;");
        statusRow.getChildren().add(statusBadge);

        content.getChildren().addAll(destBadge, nameLabel, descLabel, infoRow, inclusionsLabel, priceRow, statusRow);
        card.getChildren().addAll(imageContainer, content);

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

    private VBox createPlaceholderImage(String destination) {
        VBox placeholder = new VBox(8);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setPrefWidth(320);
        placeholder.setPrefHeight(180);
        placeholder.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2); -fx-background-radius: 12 12 0 0;");

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName("PLANE");
        icon.setSize("48");
        icon.setStyle("-fx-fill: rgba(255,255,255,0.8);");

        Label destLabel = new Label(destination);
        destLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");

        placeholder.getChildren().addAll(icon, destLabel);
        return placeholder;
    }

    private HBox createInfoItem(String iconName, String text) {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setGlyphName(iconName);
        icon.setSize("12");
        icon.setStyle("-fx-fill: #95A5A6;");

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12;");

        box.getChildren().addAll(icon, label);
        return box;
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredPackages.setPredicate(pkg -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return pkg.getPackageName().toLowerCase().contains(lowerCaseFilter)
                        || pkg.getDestination().toLowerCase().contains(lowerCaseFilter)
                        || pkg.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
            renderPackages();
            updatePackageCount();
        });
    }

    private void updatePackageCount() {
        int count = filteredPackages.size();
        packageCount_lbl.setText(count + (count == 1 ? " package" : " packages"));
    }
}
