package com.cht.travelmanagement.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for Vehicle used in booking wizard
 */
public class Vehicle {

    private final IntegerProperty vehicleId;
    private final StringProperty type;
    private final IntegerProperty capacity;
    private final StringProperty plateNumber;
    private final StringProperty providerName;
    private final IntegerProperty pricePerDay;

    public Vehicle(int vehicleId, String type, int capacity, String plateNumber,
            String providerName, int pricePerDay) {
        this.vehicleId = new SimpleIntegerProperty(vehicleId);
        this.type = new SimpleStringProperty(type);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.plateNumber = new SimpleStringProperty(plateNumber);
        this.providerName = new SimpleStringProperty(providerName);
        this.pricePerDay = new SimpleIntegerProperty(pricePerDay);
    }

    // Getters
    public int getVehicleId() {
        return vehicleId.get();
    }

    public IntegerProperty vehicleIdProperty() {
        return vehicleId;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getCapacity() {
        return capacity.get();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public String getPlateNumber() {
        return plateNumber.get();
    }

    public StringProperty plateNumberProperty() {
        return plateNumber;
    }

    public String getProviderName() {
        return providerName.get();
    }

    public StringProperty providerNameProperty() {
        return providerName;
    }

    public int getPricePerDay() {
        return pricePerDay.get();
    }

    public IntegerProperty pricePerDayProperty() {
        return pricePerDay;
    }

    /**
     * Get display name with capacity
     */
    public String getDisplayName() {
        return type.get() + " (" + capacity.get() + " seats) - " + providerName.get();
    }
}
