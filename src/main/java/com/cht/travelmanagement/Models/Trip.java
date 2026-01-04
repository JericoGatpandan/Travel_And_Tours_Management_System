package com.cht.travelmanagement.Models;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for Trip records
 */
public class Trip {

    private final IntegerProperty tripId;
    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty location;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;
    private final BooleanProperty isActive;

    public Trip(int tripId, String name, String description, String location,
            LocalDate startDate, LocalDate endDate, boolean isActive) {
        this.tripId = new SimpleIntegerProperty(tripId);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.isActive = new SimpleBooleanProperty(isActive);
    }

    // Getters and Property accessors
    public int getTripId() {
        return tripId.get();
    }

    public IntegerProperty tripIdProperty() {
        return tripId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public boolean isActive() {
        return isActive.get();
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    /**
     * Get duration in days
     */
    public int getDuration() {
        if (startDate.get() != null && endDate.get() != null) {
            return (int) java.time.temporal.ChronoUnit.DAYS.between(startDate.get(), endDate.get()) + 1;
        }
        return 1;
    }
}
