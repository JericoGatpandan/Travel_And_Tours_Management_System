package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.Model;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> clients_table;
    @FXML
    private TableColumn<Client, Integer> clientId_col;
    @FXML
    private TableColumn<Client, String> clientName_col;
    @FXML
    private TableColumn<Client, String> email_col;
    @FXML
    private TableColumn<Client, String> contactNum_col;
    @FXML
    private TableColumn<Client, String> address_col;
    @FXML
    private TableColumn<Client, String> customerType_col;
    @FXML
    private TableColumn<Client, String> dateRegistered_col;
    @FXML
    private TableColumn<Client, Void> actions_col;
    @FXML
    private TextField search_fld;
    @FXML
    private Button addClient_btn;

    private ObservableList<Client> clientsList;
    private FilteredList<Client> filteredClients;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadClients();
        setupSearch();
    }

    private void setupTableColumns() {
        clientId_col.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactNum_col.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        address_col.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Customer type with badge styling
        customerType_col.setCellValueFactory(new PropertyValueFactory<>("customerType"));
        customerType_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String type, boolean empty) {
                super.updateItem(type, empty);
                if (empty || type == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(type);
                    badge.setPadding(new Insets(4, 10, 4, 10));
                    badge.setStyle(getBadgeStyle(type));
                    setGraphic(badge);
                }
            }
        });

        // Date registered formatted
        dateRegistered_col.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDateRegistered();
            if (date != null) {
                return new SimpleStringProperty(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            }
            return new SimpleStringProperty("");
        });

        // Actions column with edit and delete buttons
        actions_col.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final HBox container = new HBox(8);

            {
                // Edit button
                FontAwesomeIconView editIcon = new FontAwesomeIconView();
                editIcon.setGlyphName("EDIT");
                editIcon.setSize("14");
                editIcon.setStyle("-fx-fill: #3498db;");
                editBtn.setGraphic(editIcon);
                editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 5;");
                editBtn.setTooltip(new Tooltip("Edit client"));
                editBtn.setOnAction(e -> handleEditClient(getTableRow().getItem()));

                // Delete button
                FontAwesomeIconView deleteIcon = new FontAwesomeIconView();
                deleteIcon.setGlyphName("TRASH");
                deleteIcon.setSize("14");
                deleteIcon.setStyle("-fx-fill: #e74c3c;");
                deleteBtn.setGraphic(deleteIcon);
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 5;");
                deleteBtn.setTooltip(new Tooltip("Delete client"));
                deleteBtn.setOnAction(e -> handleDeleteClient(getTableRow().getItem()));

                container.setAlignment(Pos.CENTER);
                container.getChildren().addAll(editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private String getBadgeStyle(String type) {
        return switch (type.toUpperCase()) {
            case "VIP" ->
                "-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            case "CORPORATE" ->
                "-fx-background-color: #cce5ff; -fx-text-fill: #004085; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            default ->
                "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
        };
    }

    private void loadClients() {
        clientsList = Model.getInstance().getClients();
        filteredClients = new FilteredList<>(clientsList, p -> true);
        clients_table.setItems(filteredClients);
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredClients.setPredicate(client -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return client.getName().toLowerCase().contains(lowerCaseFilter)
                        || client.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || client.getContactNumber().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    private void handleAddClient() {
        showClientDialog(null);
    }

    private void handleEditClient(Client client) {
        if (client != null) {
            showClientDialog(client);
        }
    }

    private void handleDeleteClient(Client client) {
        if (client == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Client");
        confirm.setHeaderText("Delete " + client.getName() + "?");
        confirm.setContentText("This action cannot be undone. Are you sure you want to delete this client?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = Model.getInstance().deleteClient(client.getClientId());
            if (deleted) {
                loadClients(); // Refresh the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not delete client. The client may have existing bookings.");
            }
        }
    }

    private void showClientDialog(Client client) {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle(client == null ? "Add New Client" : "Edit Client");
        dialog.setHeaderText(client == null ? "Enter client details" : "Update client information");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact Number");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        ChoiceBox<String> typeBox = new ChoiceBox<>(FXCollections.observableArrayList("REGULAR", "CORPORATE", "VIP"));
        typeBox.setValue("REGULAR");

        // Pre-fill if editing
        if (client != null) {
            nameField.setText(client.getName());
            emailField.setText(client.getEmail());
            contactField.setText(client.getContactNumber());
            addressField.setText(client.getAddress());
            typeBox.setValue(client.getCustomerType() != null ? client.getCustomerType() : "REGULAR");
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Contact:"), 0, 2);
        grid.add(contactField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Enable/Disable save button based on validation
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        nameField.textProperty().addListener((obs, old, newVal)
                -> saveButton.setDisable(newVal.trim().isEmpty() || emailField.getText().trim().isEmpty()));
        emailField.textProperty().addListener((obs, old, newVal)
                -> saveButton.setDisable(newVal.trim().isEmpty() || nameField.getText().trim().isEmpty()));

        if (client != null) {
            saveButton.setDisable(false);
        }

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Client(
                        client != null ? client.getClientId() : -1,
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        addressField.getText().trim(),
                        contactField.getText().trim(),
                        typeBox.getValue(),
                        client != null ? client.getDateRegistered() : LocalDate.now()
                );
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(c -> {
            boolean success;
            if (client == null) {
                // Create new client
                int newId = Model.getInstance().createClient(
                        c.getName(), c.getEmail(), c.getContactNumber(), c.getAddress(), c.getCustomerType()
                );
                success = newId > 0;
            } else {
                // Update existing client
                success = Model.getInstance().updateClient(
                        client.getClientId(), c.getName(), c.getEmail(), c.getContactNumber(), c.getAddress(), c.getCustomerType()
                );
            }

            if (success) {
                loadClients();
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        client == null ? "Client added successfully." : "Client updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not save client. Please try again.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
