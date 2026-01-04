package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.UserMenuOption;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class BookingsController implements Initializable {

    @FXML
    private TableView<Booking> booking_table;

    @FXML
    private TableColumn<Booking, String> bookingId_col;

    @FXML
    private TableColumn<Booking, String> client_col;

    @FXML
    private TableColumn<Booking, String> destination_col;

    @FXML
    private TableColumn<Booking, String> endDate_col;

    @FXML
    private Button newBooking_btn;

    @FXML
    private TableColumn<Booking, String> package_col;

    @FXML
    private TextField search_fld;

    @FXML
    private TableColumn<Booking, String> startDate_col;

    @FXML
    private TableColumn<Booking, String> status_col;

    @FXML
    private TableColumn<Booking, Void> actions_col;

    @FXML
    private Label bookingCount_lbl;

    @FXML
    private Label totalBookings_lbl;

    @FXML
    private Label confirmedBookings_lbl;

    @FXML
    private Label pendingBookings_lbl;

    @FXML
    private Label cancelledBookings_lbl;

    private ObservableList<Booking> allBookings;
    private FilteredList<Booking> filteredBookings;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadAllBookings();
        setupSearch();
        setupActions();
    }

    private void setupTable() {
        bookingId_col.setCellValueFactory(cellData
                -> new SimpleStringProperty("BK-" + String.format("%04d", cellData.getValue().getBookingId())));

        client_col.setCellValueFactory(cellData -> cellData.getValue().clientNameProperty());
        package_col.setCellValueFactory(cellData -> cellData.getValue().packageNameProperty());
        destination_col.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());

        startDate_col.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookingDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getBookingDate().format(DATE_FORMATTER));
            }
            return new SimpleStringProperty("-");
        });

        endDate_col.setCellValueFactory(cellData -> {
            if (cellData.getValue().getBookingDate() != null) {
                return new SimpleStringProperty(cellData.getValue().getBookingDate().plusDays(7).format(DATE_FORMATTER));
            }
            return new SimpleStringProperty("-");
        });

        // Status column with badges
        status_col.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        status_col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(status);
                    badge.getStyleClass().add("badge");

                    switch (status.toUpperCase()) {
                        case "CONFIRMED" ->
                            badge.getStyleClass().add("badge-success");
                        case "PENDING" ->
                            badge.getStyleClass().add("badge-warning");
                        case "CANCELLED" ->
                            badge.getStyleClass().add("badge-danger");
                        case "COMPLETED" ->
                            badge.getStyleClass().add("badge-info");
                        default ->
                            badge.getStyleClass().add("badge-secondary");
                    }

                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Actions column
        actions_col.setCellFactory(column -> new TableCell<>() {
            private final Button viewBtn = new Button();
            private final Button editBtn = new Button();
            private final HBox actions = new HBox(8);

            {
                FontAwesomeIconView viewIcon = new FontAwesomeIconView();
                viewIcon.setGlyphName("EYE");
                viewIcon.setSize("14");
                viewBtn.setGraphic(viewIcon);
                viewBtn.getStyleClass().addAll("btn-icon", "btn-info");
                viewBtn.setTooltip(new Tooltip("View Details"));

                FontAwesomeIconView editIcon = new FontAwesomeIconView();
                editIcon.setGlyphName("PENCIL");
                editIcon.setSize("14");
                editBtn.setGraphic(editIcon);
                editBtn.getStyleClass().addAll("btn-icon", "btn-warning");
                editBtn.setTooltip(new Tooltip("Edit Booking"));

                actions.setAlignment(Pos.CENTER);
                actions.getChildren().addAll(viewBtn, editBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Booking booking = getTableView().getItems().get(getIndex());
                    viewBtn.setOnAction(e -> handleViewBooking(booking));
                    editBtn.setOnAction(e -> handleEditBooking(booking));
                    setGraphic(actions);
                }
            }
        });

        booking_table.setPlaceholder(new Label("No bookings found"));
    }

    private void loadAllBookings() {
        allBookings = Model.getInstance().getAllBookings();
        if (allBookings == null) {
            allBookings = FXCollections.observableArrayList();
        }

        filteredBookings = new FilteredList<>(allBookings, p -> true);
        booking_table.setItems(filteredBookings);

        updateStats();
    }

    private void updateStats() {
        int total = allBookings.size();
        long confirmed = allBookings.stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                .count();
        long pending = allBookings.stream()
                .filter(b -> "PENDING".equalsIgnoreCase(b.getStatus()))
                .count();
        long cancelled = allBookings.stream()
                .filter(b -> "CANCELLED".equalsIgnoreCase(b.getStatus()))
                .count();

        bookingCount_lbl.setText(total + " booking" + (total != 1 ? "s" : ""));
        totalBookings_lbl.setText(String.valueOf(total));
        confirmedBookings_lbl.setText(String.valueOf(confirmed));
        pendingBookings_lbl.setText(String.valueOf(pending));
        cancelledBookings_lbl.setText(String.valueOf(cancelled));
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredBookings.setPredicate(booking -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (booking.getClientName() != null
                        && booking.getClientName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (booking.getDestination() != null
                        && booking.getDestination().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (booking.getPackageName() != null
                        && booking.getPackageName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (booking.getStatus() != null
                        && booking.getStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (String.valueOf(booking.getBookingId()).contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
    }

    private void setupActions() {
        newBooking_btn.setOnAction(event -> onNewBookingButtonClicked());
    }

    private void onNewBookingButtonClicked() {
        Model.getInstance().getUserViewFactory().getUserSelectedMenuItem().set(UserMenuOption.NEW_BOOKING);
    }

    private void handleViewBooking(Booking booking) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Booking Details");
        alert.setHeaderText("Booking #BK-" + String.format("%04d", booking.getBookingId()));

        String details = String.format(
                "Client: %s\n"
                + "Package: %s\n"
                + "Destination: %s\n"
                + "Booking Date: %s\n"
                + "Status: %s",
                booking.getClientName(),
                booking.getPackageName(),
                booking.getDestination(),
                booking.getBookingDate() != null ? booking.getBookingDate().format(DATE_FORMATTER) : "N/A",
                booking.getStatus()
        );

        alert.setContentText(details);
        alert.showAndWait();
    }

    private void handleEditBooking(Booking booking) {
        // Could implement edit dialog similar to ClientsController
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Booking");
        alert.setHeaderText("Edit functionality");
        alert.setContentText("Edit booking #BK-" + String.format("%04d", booking.getBookingId()));
        alert.showAndWait();
    }
}
