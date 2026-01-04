package com.cht.travelmanagement.Controllers.User;

import java.math.BigDecimal;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Payment;

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

public class PaymentsController implements Initializable {

    @FXML
    private TableView<Payment> payments_table;
    @FXML
    private TableColumn<Payment, Integer> paymentId_col;
    @FXML
    private TableColumn<Payment, Integer> bookingId_col;
    @FXML
    private TableColumn<Payment, String> clientName_col;
    @FXML
    private TableColumn<Payment, String> packageName_col;
    @FXML
    private TableColumn<Payment, BigDecimal> amount_col;
    @FXML
    private TableColumn<Payment, String> paymentDate_col;
    @FXML
    private TableColumn<Payment, String> method_col;
    @FXML
    private TableColumn<Payment, String> status_col;
    @FXML
    private TableColumn<Payment, String> reference_col;
    @FXML
    private TextField search_fld;
    @FXML
    private Label totalReceived_lbl;
    @FXML
    private Label totalPending_lbl;
    @FXML
    private Label totalCount_lbl;

    private ObservableList<Payment> paymentsList;
    private FilteredList<Payment> filteredPayments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadPayments();
        setupSearch();
        updateStats();
    }

    private void setupTableColumns() {
        paymentId_col.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        bookingId_col.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        clientName_col.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        packageName_col.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        reference_col.setCellValueFactory(new PropertyValueFactory<>("referenceNumber"));

        // Amount with currency formatting
        amount_col.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amount_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("₱%,.2f", amount));
                    setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
                }
            }
        });

        // Date formatting
        paymentDate_col.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPaymentDate() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getPaymentDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
                );
            }
            return new SimpleStringProperty("");
        });

        // Method with display name
        method_col.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getMethodDisplay())
        );

        // Status with badge styling
        status_col.setCellValueFactory(new PropertyValueFactory<>("status"));
        status_col.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(status);
                    badge.setPadding(new Insets(4, 10, 4, 10));
                    badge.setStyle(getStatusStyle(status));
                    setGraphic(badge);
                }
            }
        });
    }

    private String getStatusStyle(String status) {
        return switch (status.toUpperCase()) {
            case "PAID" ->
                "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            case "PENDING" ->
                "-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            case "FAILED" ->
                "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            case "REFUNDED" ->
                "-fx-background-color: #d1ecf1; -fx-text-fill: #0c5460; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
            default ->
                "-fx-background-color: #e2e3e5; -fx-text-fill: #383d41; -fx-background-radius: 12; -fx-font-size: 11; -fx-font-weight: bold;";
        };
    }

    private void loadPayments() {
        paymentsList = Model.getInstance().getAllPayments();
        filteredPayments = new FilteredList<>(paymentsList, p -> true);
        payments_table.setItems(filteredPayments);
    }

    private void setupSearch() {
        search_fld.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredPayments.setPredicate(payment -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return payment.getClientName().toLowerCase().contains(lowerCaseFilter)
                        || payment.getReferenceNumber().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(payment.getBookingId()).contains(lowerCaseFilter)
                        || payment.getPackageName().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    private void updateStats() {
        BigDecimal totalPaid = BigDecimal.ZERO;
        BigDecimal totalPending = BigDecimal.ZERO;

        for (Payment payment : paymentsList) {
            if ("PAID".equalsIgnoreCase(payment.getStatus())) {
                totalPaid = totalPaid.add(payment.getAmount());
            } else if ("PENDING".equalsIgnoreCase(payment.getStatus())) {
                totalPending = totalPending.add(payment.getAmount());
            }
        }

        totalReceived_lbl.setText(String.format("₱%,.2f", totalPaid));
        totalPending_lbl.setText(String.format("₱%,.2f", totalPending));
        totalCount_lbl.setText(String.valueOf(paymentsList.size()));
    }
}
