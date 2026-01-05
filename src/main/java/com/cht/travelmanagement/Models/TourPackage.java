package com.cht.travelmanagement.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TourPackage {

    private final IntegerProperty packageId;
    private final StringProperty packageName;
    private final StringProperty description;
    private final StringProperty destination;
    private final IntegerProperty durationDays;
    private final IntegerProperty maxParticipants;
    private final StringProperty inclusions;
    private final IntegerProperty price;
    private final BooleanProperty isActive;
    private final IntegerProperty createdBy;
    private final StringProperty imagePath;

    public TourPackage(int packageId, String packageName, String description, String destination,
            int durationDays, int maxParticipants, String inclusions, int price, boolean isActive, int createdBy) {
        this(packageId, packageName, description, destination, durationDays, maxParticipants, inclusions, price, isActive, createdBy, null);
    }

    public TourPackage(int packageId, String packageName, String description, String destination,
            int durationDays, int maxParticipants, String inclusions, int price, boolean isActive, int createdBy, String imagePath) {
        this.packageId = new SimpleIntegerProperty(this, "packageId", packageId);
        this.packageName = new SimpleStringProperty(this, "packageName", packageName);
        this.description = new SimpleStringProperty(this, "description", description);
        this.destination = new SimpleStringProperty(this, "destination", destination);
        this.durationDays = new SimpleIntegerProperty(this, "durationDays", durationDays);
        this.maxParticipants = new SimpleIntegerProperty(this, "maxParticipants", maxParticipants);
        this.inclusions = new SimpleStringProperty(this, "inclusions", inclusions);
        this.price = new SimpleIntegerProperty(this, "price", price);
        this.isActive = new SimpleBooleanProperty(this, "isActive", isActive);
        this.createdBy = new SimpleIntegerProperty(this, "createdBy", createdBy);
        this.imagePath = new SimpleStringProperty(this, "imagePath", imagePath);
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDestination() {
        return destination.get();
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public int getDurationDays() {
        return durationDays.get();
    }

    public IntegerProperty durationDaysProperty() {
        return durationDays;
    }

    public int getMaxParticipants() {
        return maxParticipants.get();
    }

    public IntegerProperty maxParticipantsProperty() {
        return maxParticipants;
    }

    public String getInclusions() {
        return inclusions.get();
    }

    public StringProperty inclusionsProperty() {
        return inclusions;
    }

    public int getPrice() {
        return price.get();
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public boolean getIsActive() {
        return isActive.get();
    }

    public BooleanProperty isActiveProperty() {
        return isActive;
    }

    public int getCreatedBy() {
        return createdBy.get();
    }

    public IntegerProperty createdByProperty() {
        return createdBy;
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

}
