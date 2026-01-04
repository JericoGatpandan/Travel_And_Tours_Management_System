package com.cht.travelmanagement.Controllers.User.BookingWizard;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class BookingStep1Controller implements Initializable {

    @FXML
    private TextField search;
    @FXML
    private ListView<Client> search_results_list;
    @FXML
    private Label selected_client_lbl;
    @FXML
    private Label clear_selection_btn;
    @FXML
    private TextField fname_fld;
    @FXML
    private TextField contact_fld;
    @FXML
    private TextField email_fld;
    @FXML
    private ChoiceBox<String> destination_box;
    @FXML
    private TextField travel_type_fld;
    @FXML
    private TextField pax_fld;

    private BookingData bookingData;
    private ObservableList<Client> searchResults;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingData = Model.getInstance().getBookingData();
        searchResults = FXCollections.observableArrayList();

        // Initialize destination choices
        destination_box.setItems(FXCollections.observableArrayList(
                "Hokkaido, Japan",
                "Hong Kong & Macau",
                "Bali, Indonesia",
                "Taipei & Taichung, Taiwan",
                "Other"
        ));

        // Set default pax count
        pax_fld.setText("1");

        // Setup search functionality with dropdown
        setupSearchWithDropdown();

        // Setup field listeners to update booking data
        setupFieldListeners();

        // Load existing data if returning to this step
        loadExistingData();
    }

    private void setupSearchWithDropdown() {
        // Setup custom cell factory for the ListView
        search_results_list.setCellFactory(listView -> new ClientListCell());
        search_results_list.setItems(searchResults);

        // Search as user types
        search.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().length() < 2) {
                hideSearchResults();
                return;
            }
            performSearch(newVal.trim());
        });

        // Handle selection from list
        search_results_list.setOnMouseClicked(event -> {
            Client selected = search_results_list.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectClient(selected);
            }
        });

        // Handle keyboard navigation
        search.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DOWN -> {
                    if (search_results_list.isVisible() && !searchResults.isEmpty()) {
                        search_results_list.requestFocus();
                        search_results_list.getSelectionModel().selectFirst();
                    }
                }
                case ESCAPE ->
                    hideSearchResults();
                case ENTER -> {
                    if (!searchResults.isEmpty()) {
                        selectClient(searchResults.get(0));
                    }
                }
                default -> {
                }
            }
        });

        // Handle enter key on list
        search_results_list.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> {
                    Client selected = search_results_list.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        selectClient(selected);
                    }
                }
                case ESCAPE -> {
                    hideSearchResults();
                    search.requestFocus();
                }
                default -> {
                }
            }
        });

        // Hide results when search field loses focus (with delay to allow click)
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !search_results_list.isFocused()) {
                // Small delay to allow click on list item
                javafx.application.Platform.runLater(() -> {
                    if (!search_results_list.isFocused()) {
                        hideSearchResults();
                    }
                });
            }
        });
    }

    private void performSearch(String searchTerm) {
        ObservableList<Client> results = Model.getInstance().searchClients(searchTerm);
        searchResults.clear();

        if (!results.isEmpty()) {
            searchResults.addAll(results);
            showSearchResults();
        } else {
            hideSearchResults();
        }
    }

    private void showSearchResults() {
        search_results_list.setVisible(true);
        search_results_list.setManaged(true);
        // Adjust height based on number of results
        int height = Math.min(searchResults.size() * 60, 200);
        search_results_list.setPrefHeight(height);
    }

    private void hideSearchResults() {
        search_results_list.setVisible(false);
        search_results_list.setManaged(false);
        searchResults.clear();
    }

    private void selectClient(Client client) {
        // Fill form fields with client data
        fname_fld.setText(client.getName());
        email_fld.setText(client.getEmail());
        contact_fld.setText(client.getContactNumber());

        // Update booking data
        bookingData.setClientId(client.getClientId());
        bookingData.setClientName(client.getName());
        bookingData.setClientEmail(client.getEmail());
        bookingData.setClientContact(client.getContactNumber());
        bookingData.setExistingClient(true);

        // Show selected client indicator
        String clientType = client.getCustomerType() != null ? client.getCustomerType() : "REGULAR";
        String badge = switch (clientType.toUpperCase()) {
            case "VIP" ->
                "⭐ VIP Customer";
            case "CORPORATE" ->
                "🏢 Corporate Client";
            default ->
                "👤 Regular Customer";
        };
        selected_client_lbl.setText("✓ Selected: " + client.getName() + " (" + badge + ")");
        selected_client_lbl.setVisible(true);
        selected_client_lbl.setManaged(true);
        clear_selection_btn.setVisible(true);
        clear_selection_btn.setManaged(true);

        // Clear search field and hide results
        search.clear();
        hideSearchResults();

        // Disable name and contact fields since client is selected (optional UX improvement)
        fname_fld.setEditable(false);
        fname_fld.setStyle("-fx-background-color: #f0f0f0;");
        email_fld.setEditable(false);
        email_fld.setStyle("-fx-background-color: #f0f0f0;");
        contact_fld.setEditable(false);
        contact_fld.setStyle("-fx-background-color: #f0f0f0;");

        System.out.println("Selected existing client: " + client.getName() + " (ID: " + client.getClientId() + ", Type: " + clientType + ")");
    }

    /**
     * Handle clear selection button click
     */
    @FXML
    public void handleClearSelection() {
        clearSelectedClient();
    }

    /**
     * Clear the selected client and allow entering new client details
     */
    public void clearSelectedClient() {
        bookingData.setClientId(-1);
        bookingData.setExistingClient(false);

        fname_fld.clear();
        fname_fld.setEditable(true);
        fname_fld.setStyle("");

        email_fld.clear();
        email_fld.setEditable(true);
        email_fld.setStyle("");

        contact_fld.clear();
        contact_fld.setEditable(true);
        contact_fld.setStyle("");

        selected_client_lbl.setVisible(false);
        selected_client_lbl.setManaged(false);
        clear_selection_btn.setVisible(false);
        clear_selection_btn.setManaged(false);
    }

    private void setupFieldListeners() {
        // Name field
        fname_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setClientName(newVal);
            if (!bookingData.isExistingClient()) {
                bookingData.setClientId(-1);
            }
        });

        // Email field
        email_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setClientEmail(newVal);
        });

        // Contact field
        contact_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setClientContact(newVal);
        });

        // Destination field
        destination_box.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                bookingData.setClientDestination(newVal);
            }
        });

        // Travel type field
        travel_type_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            bookingData.setTravelType(newVal);
        });

        // Pax count field - only allow numbers
        pax_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                pax_fld.setText(newVal.replaceAll("[^\\d]", ""));
            } else if (!newVal.isEmpty()) {
                try {
                    int pax = Integer.parseInt(newVal);
                    if (pax > 0 && pax <= 50) {
                        bookingData.setPaxCount(pax);
                    }
                } catch (NumberFormatException e) {
                    pax_fld.setText("1");
                }
            }
        });
    }

    private void loadExistingData() {
        // If returning to this step, load previously entered data
        if (!bookingData.getClientName().isEmpty()) {
            fname_fld.setText(bookingData.getClientName());
            email_fld.setText(bookingData.getClientEmail());
            contact_fld.setText(bookingData.getClientContact());

            if (!bookingData.getClientDestination().isEmpty()) {
                destination_box.setValue(bookingData.getClientDestination());
            }

            if (!bookingData.getTravelType().isEmpty()) {
                travel_type_fld.setText(bookingData.getTravelType());
            }

            pax_fld.setText(String.valueOf(bookingData.getPaxCount()));

            // If existing client, show the indicator and lock fields
            if (bookingData.isExistingClient() && bookingData.getClientId() > 0) {
                selected_client_lbl.setText("✓ Selected: " + bookingData.getClientName() + " (Returning Customer)");
                selected_client_lbl.setVisible(true);
                selected_client_lbl.setManaged(true);
                clear_selection_btn.setVisible(true);
                clear_selection_btn.setManaged(true);

                fname_fld.setEditable(false);
                fname_fld.setStyle("-fx-background-color: #f0f0f0;");
                email_fld.setEditable(false);
                email_fld.setStyle("-fx-background-color: #f0f0f0;");
                contact_fld.setEditable(false);
                contact_fld.setStyle("-fx-background-color: #f0f0f0;");
            }
        }
    }

    /**
     * Validate all required fields before proceeding
     */
    public boolean validateFields() {
        return !fname_fld.getText().trim().isEmpty()
                && !email_fld.getText().trim().isEmpty()
                && !contact_fld.getText().trim().isEmpty()
                && !pax_fld.getText().trim().isEmpty();
    }

    /**
     * Custom ListCell to display client information in search results
     */
    private static class ClientListCell extends ListCell<Client> {

        private final VBox container;
        private final Label nameLabel;
        private final Label detailsLabel;
        private final Label typeLabel;

        public ClientListCell() {
            container = new VBox(2);
            container.setPadding(new Insets(8, 12, 8, 12));

            nameLabel = new Label();
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            detailsLabel = new Label();
            detailsLabel.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");

            typeLabel = new Label();
            typeLabel.setStyle("-fx-font-size: 11px;");

            container.getChildren().addAll(nameLabel, detailsLabel, typeLabel);
        }

        @Override
        protected void updateItem(Client client, boolean empty) {
            super.updateItem(client, empty);

            if (empty || client == null) {
                setGraphic(null);
                setText(null);
            } else {
                nameLabel.setText(client.getName());
                detailsLabel.setText("📧 " + client.getEmail() + "  📱 " + client.getContactNumber());

                String clientType = client.getCustomerType() != null ? client.getCustomerType() : "REGULAR";
                switch (clientType.toUpperCase()) {
                    case "VIP" -> {
                        typeLabel.setText("⭐ VIP Customer");
                        typeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #f39c12;");
                        container.setStyle("-fx-background-color: #fff9e6;");
                    }
                    case "CORPORATE" -> {
                        typeLabel.setText("🏢 Corporate Client");
                        typeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #3498db;");
                        container.setStyle("-fx-background-color: #e8f4fd;");
                    }
                    default -> {
                        typeLabel.setText("👤 Regular Customer");
                        typeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #27ae60;");
                        container.setStyle("-fx-background-color: white;");
                    }
                }

                setGraphic(container);
            }
        }
    }
}
