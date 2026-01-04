package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Vehicle;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransportationController implements Initializable {

    @FXML
    private TableView<Vehicle> vehicles_table;
    @FXML
    private TableColumn<Vehicle, Integer> vehicleId_col;
    @FXML
    private TableColumn<Vehicle, String> type_col;
    @FXML
    private TableColumn<Vehicle, Integer> capacity_col;
    @FXML
    private TableColumn<Vehicle, String> plateNumber_col;
    @FXML
    private TableColumn<Vehicle, String> provider_col;
    @FXML
    private TableColumn<Vehicle, Integer> pricePerDay_col;
    @FXML
    private TextField search_fld;
    @FXML
    private Label vehicleCount_lbl;

    private ObservableList<Vehicle> vehiclesList;
    private FilteredList<Vehicle> filteredVehicles;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadVehicles();
        setupSearch();
    }

    private void setupTableColumns() {
        vehicleId_col.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        plateNumber_col.setCellValueFactory(new PropertyValueFactory<>("plateNumber"));
        provider_col.setCellValueFactory(new PropertyValueFactory<>("providerName"));

        // Capacity with icon
        capacity_col.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capacity_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer capacity, boolean empty) {
                super.updateItem(capacity, empty);
                if (empty || capacity == null) {
                    setText(null);
                } else {
                    setText(capacity + " seats");
                }
            }
        });

        // Price formatting
        pricePerDay_col.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));
        pricePerDay_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("₱%,d", price));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
                }
            }
        });
    }

    private void loadVehicles() {
        vehiclesList = Model.getInstance().getVehicles();
        filteredVehicles = new FilteredList<>(vehiclesList, p -> true);
        vehicles_table.setItems(filteredVehicles);
        updateCount();
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredVehicles.setPredicate(vehicle -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return vehicle.getType().toLowerCase().contains(lowerCaseFilter)
                        || vehicle.getProviderName().toLowerCase().contains(lowerCaseFilter)
                        || vehicle.getPlateNumber().toLowerCase().contains(lowerCaseFilter);
            });
            updateCount();
        });
    }

    private void updateCount() {
        int count = filteredVehicles.size();
        vehicleCount_lbl.setText(count + (count == 1 ? " vehicle" : " vehicles"));
    }
}
