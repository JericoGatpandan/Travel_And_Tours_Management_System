package com.cht.travelmanagement.Models;

import java.math.BigDecimal;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for Payment records
 */
public class Payment {

    private final IntegerProperty paymentId;
    private final IntegerProperty bookingId;
    private final ObjectProperty<BigDecimal> amount;
    private final ObjectProperty<LocalDate> paymentDate;
    private final StringProperty method;
    private final StringProperty status;
    private final StringProperty referenceNumber;

    // Joined fields for display
    private final StringProperty clientName;
    private final StringProperty packageName;

    public Payment(int paymentId, int bookingId, BigDecimal amount, LocalDate paymentDate,
            String method, String status, String referenceNumber) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.amount = new SimpleObjectProperty<>(amount);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.method = new SimpleStringProperty(method);
        this.status = new SimpleStringProperty(status);
        this.referenceNumber = new SimpleStringProperty(referenceNumber);
        this.clientName = new SimpleStringProperty("");
        this.packageName = new SimpleStringProperty("");
    }

    public Payment(int paymentId, int bookingId, BigDecimal amount, LocalDate paymentDate,
            String method, String status, String referenceNumber,
            String clientName, String packageName) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.amount = new SimpleObjectProperty<>(amount);
        this.paymentDate = new SimpleObjectProperty<>(paymentDate);
        this.method = new SimpleStringProperty(method);
        this.status = new SimpleStringProperty(status);
        this.referenceNumber = new SimpleStringProperty(referenceNumber);
        this.clientName = new SimpleStringProperty(clientName);
        this.packageName = new SimpleStringProperty(packageName);
    }

    // Getters and Property accessors
    public int getPaymentId() {
        return paymentId.get();
    }

    public IntegerProperty paymentIdProperty() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId.get();
    }

    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public BigDecimal getAmount() {
        return amount.get();
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate.get();
    }

    public ObjectProperty<LocalDate> paymentDateProperty() {
        return paymentDate;
    }

    public String getMethod() {
        return method.get();
    }

    public StringProperty methodProperty() {
        return method;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getReferenceNumber() {
        return referenceNumber.get();
    }

    public StringProperty referenceNumberProperty() {
        return referenceNumber;
    }

    public String getClientName() {
        return clientName.get();
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String name) {
        clientName.set(name);
    }

    public String getPackageName() {
        return packageName.get();
    }

    public StringProperty packageNameProperty() {
        return packageName;
    }

    public void setPackageName(String name) {
        packageName.set(name);
    }

    /**
     * Get formatted amount string
     */
    public String getFormattedAmount() {
        return String.format("₱%,.2f", amount.get());
    }

    /**
     * Get display-friendly method name
     */
    public String getMethodDisplay() {
        return switch (method.get().toLowerCase()) {
            case "gcash" ->
                "GCash";
            case "bank" ->
                "Bank Transfer";
            case "card" ->
                "Credit/Debit Card";
            case "cash" ->
                "Cash";
            default ->
                method.get();
        };
    }
}
