package com.cht.travelmanagement.Models;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {

    private final IntegerProperty clientId;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty address;
    private final StringProperty contactNumber;
    private final StringProperty customerType;
    private final ObjectProperty<LocalDate> dateRegistered;
    private final StringProperty destination;
    private final StringProperty tripStatus;
    private final StringProperty tripDates;

    public Client(int clientId, String name, String email, String address, String contactNumber, String customerType, LocalDate dateRegistered) {
        this.clientId = new SimpleIntegerProperty(this, "clientId", clientId);
        this.name = new SimpleStringProperty(this, "name", name);
        this.email = new SimpleStringProperty(this, "email", email);
        this.address = new SimpleStringProperty(this, "address", address);
        this.contactNumber = new SimpleStringProperty(this, "contactNumber", contactNumber);
        this.customerType = new SimpleStringProperty(this, "customerType", customerType);
        this.dateRegistered = new SimpleObjectProperty<>(this, "dateRegistered", dateRegistered);
        this.destination = new SimpleStringProperty(this, "destination", "");
        this.tripStatus = new SimpleStringProperty(this, "tripStatus", "");
        this.tripDates = new SimpleStringProperty(this, "tripDates", "");
    }

    public Client(int clientId, String name, String email, String contactNumber, int assignedEmployeeId) {
        this.clientId = new SimpleIntegerProperty(this, "clientId", clientId);
        this.name = new SimpleStringProperty(this, "name", name);
        this.email = new SimpleStringProperty(this, "email", email);
        this.address = new SimpleStringProperty(this, "address", "");
        this.contactNumber = new SimpleStringProperty(this, "contactNumber", contactNumber);
        this.customerType = new SimpleStringProperty(this, "customerType", "");
        this.dateRegistered = new SimpleObjectProperty<>(this, "dateRegistered", LocalDate.now());
        this.destination = new SimpleStringProperty(this, "destination", "");
        this.tripStatus = new SimpleStringProperty(this, "tripStatus", "");
        this.tripDates = new SimpleStringProperty(this, "tripDates", "");
    }

    public Client(int clientId, String name, String email, String contactNumber, String destination, String tripStatus, String tripDates) {
        this.clientId = new SimpleIntegerProperty(this, "clientId", clientId);
        this.name = new SimpleStringProperty(this, "name", name);
        this.email = new SimpleStringProperty(this, "email", email);
        this.address = new SimpleStringProperty(this, "address", "");
        this.contactNumber = new SimpleStringProperty(this, "contactNumber", contactNumber);
        this.customerType = new SimpleStringProperty(this, "customerType", "");
        this.dateRegistered = new SimpleObjectProperty<>(this, "dateRegistered", LocalDate.now());
        this.destination = new SimpleStringProperty(this, "destination", destination);
        this.tripStatus = new SimpleStringProperty(this, "tripStatus", tripStatus);
        this.tripDates = new SimpleStringProperty(this, "tripDates", tripDates);
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber.get();
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public String getCustomerType() {
        return customerType.get();
    }

    public StringProperty customerTypeProperty() {
        return customerType;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered.get();
    }

    public ObjectProperty<LocalDate> dateRegisteredProperty() {
        return dateRegistered;
    }

    public String getDestination() {
        return destination.get();
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public String getTripStatus() {
        return tripStatus.get();
    }

    public StringProperty tripStatusProperty() {
        return tripStatus;
    }

    public String getTripDates() {
        return tripDates.get();
    }

    public StringProperty tripDatesProperty() {
        return tripDates;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
