package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Trip;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TripsController implements Initializable {

    @FXML
    private TableView<Trip> trips_table;
    @FXML
    private TableColumn<Trip, Integer> tripId_col;
    @FXML
    private TableColumn<Trip, String> name_col;
    @FXML
    private TableColumn<Trip, String> description_col;
    @FXML
    private TableColumn<Trip, String> location_col;
    @FXML
    private TableColumn<Trip, String> startDate_col;
    @FXML
    private TableColumn<Trip, String> endDate_col;
    @FXML
    private TableColumn<Trip, Boolean> status_col;
    @FXML
    private TextField search_fld;
    @FXML
    private Label tripCount_lbl;

    private ObservableList<Trip> tripsList;
    private FilteredList<Trip> filteredTrips;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadTrips();
        setupSearch();
    }

    private void setupTableColumns() {
        tripId_col.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        location_col.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Date formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        startDate_col.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getStartDate();
            return new SimpleStringProperty(date != null ? date.format(formatter) : "");
        });

        endDate_col.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getEndDate();
            return new SimpleStringProperty(date != null ? date.format(formatter) : "");
        });

        // Status with badge
        status_col.setCellValueFactory(new PropertyValueFactory<>("active"));
        status_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(active ? "Active" : "Inactive");
                    badge.setPadding(new Insets(4, 10, 4, 10));
                    badge.setStyle(active
                            ? "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;"
                            : "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;"
                    );
                    setGraphic(badge);
                }
            }
        });
    }

    private void loadTrips() {
        tripsList = Model.getInstance().getAllTrips();
        filteredTrips = new FilteredList<>(tripsList, p -> true);
        trips_table.setItems(filteredTrips);
        updateTripCount();
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredTrips.setPredicate(trip -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return trip.getName().toLowerCase().contains(lowerCaseFilter)
                        || trip.getLocation().toLowerCase().contains(lowerCaseFilter)
                        || (trip.getDescription() != null && trip.getDescription().toLowerCase().contains(lowerCaseFilter));
            });
            updateTripCount();
        });
    }

    private void updateTripCount() {
        int count = filteredTrips.size();
        tripCount_lbl.setText(count + (count == 1 ? " trip" : " trips"));
    }
}
