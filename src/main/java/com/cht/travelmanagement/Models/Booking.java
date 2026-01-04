package com.cht.travelmanagement.Models;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Booking {

    private final IntegerProperty bookingId;
    private final IntegerProperty employeeId;
    private final IntegerProperty clientId;
    private final StringProperty packageName;
    private final StringProperty clientName;
    private final IntegerProperty packageId;
    private final StringProperty destination;
    private final ObjectProperty<LocalDate> bookingDate;
    private final StringProperty status;
    private final IntegerProperty paxCount;

    ;
	
	public Booking(int bookingId, int employeeId, int clientId, String clientName, int packageId, String packageName, StringProperty destination, LocalDate bookingDate, String status, int paxCount) {
        this.bookingId = new SimpleIntegerProperty(this, "bookingId", bookingId);
        this.employeeId = new SimpleIntegerProperty(this, "employeeId", employeeId);
        this.clientId = new SimpleIntegerProperty(this, "clientId", clientId);
        this.clientName = new SimpleStringProperty(this, "clientName", clientName);
        this.packageName = new SimpleStringProperty(this, "packageName", packageName);
        this.packageId = new SimpleIntegerProperty(this, "packageId", packageId);
        this.destination = destination;
        this.bookingDate = new SimpleObjectProperty<>(this, "bookingDate", bookingDate);
        this.status = new SimpleStringProperty(this, "isConfirmed", status);
        this.paxCount = new SimpleIntegerProperty(this, "paxCount", paxCount);
    }

    public Booking(int bookingId, String clientName, String destination, String packageName, LocalDate startDate,
            String status) {
        this.bookingId = new SimpleIntegerProperty(this, "bookingId", bookingId);
        this.employeeId = new SimpleIntegerProperty(this, "employeeId", 0);
        this.clientId = new SimpleIntegerProperty(this, "clientId", 0);
        this.packageId = new SimpleIntegerProperty(this, "packageId", 0);
        this.destination = new SimpleStringProperty(this, "destination", destination);
        this.clientName = new SimpleStringProperty(this, "clientName", clientName);
        this.packageName = new SimpleStringProperty(this, "packageName", packageName);
        this.bookingDate = new SimpleObjectProperty<>(this, "bookingDate", startDate);
        this.status = new SimpleStringProperty(this, "isConfirmed", status);
        this.paxCount = new SimpleIntegerProperty(this, "paxCount", 0);

    }

    public int getBookingId() {
        return bookingId.get();
    }

    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public int getEmployeeId() {
        return employeeId.get();
    }

    public IntegerProperty employeeIdProperty() {
        return employeeId;
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public String getDestination() {
        return destination.get();
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public String getClientName() {
        return clientName.get();
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public int getPackageId() {
        return packageId.get();
    }

    public IntegerProperty packageIdProperty() {
        return packageId;
    }

    public String getPackageName() {
        return packageName.get();
    }

    public StringProperty packageNameProperty() {
        return packageName;
    }

    public LocalDate getBookingDate() {
        return bookingDate.get();
    }

    public ObjectProperty<LocalDate> bookingDateProperty() {
        return bookingDate;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public int getPaxCount() {
        return paxCount.get();
    }

    public IntegerProperty paxCountProperty() {
        return paxCount;
    }

}
