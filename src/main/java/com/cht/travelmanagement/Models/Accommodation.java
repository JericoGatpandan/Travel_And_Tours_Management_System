package com.cht.travelmanagement.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Accommodation {

    private final IntegerProperty accommodationId;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty contact;
    private final StringProperty amenities;
    private final IntegerProperty numberOfRooms;
    private final StringProperty defaultRoomType;

    public Accommodation(int accommodationId, String name, String address, String contact,
            String amenities, int numberOfRooms, String defaultRoomType) {
        this.accommodationId = new SimpleIntegerProperty(accommodationId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.contact = new SimpleStringProperty(contact);
        this.amenities = new SimpleStringProperty(amenities);
        this.numberOfRooms = new SimpleIntegerProperty(numberOfRooms);
        this.defaultRoomType = new SimpleStringProperty(defaultRoomType);
    }

    // Legacy constructor for compatibility
    public Accommodation(int accommodationId, String name, String address) {
        this(accommodationId, name, address, "", "", 0, "Standard");
    }

    // Getters and Property accessors
    public int getAccommodationId() {
        return accommodationId.get();
    }

    public IntegerProperty accommodationIdProperty() {
        return accommodationId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getContact() {
        return contact.get();
    }

    public StringProperty contactProperty() {
        return contact;
    }

    public String getAmenities() {
        return amenities.get();
    }

    public StringProperty amenitiesProperty() {
        return amenities;
    }

    public int getNumberOfRooms() {
        return numberOfRooms.get();
    }

    public IntegerProperty numberOfRoomsProperty() {
        return numberOfRooms;
    }

    public String getDefaultRoomType() {
        return defaultRoomType.get();
    }

    public StringProperty defaultRoomTypeProperty() {
        return defaultRoomType;
    }

    /**
     * Get amenities as a formatted list
     */
    public String getAmenitiesFormatted() {
        if (amenities.get() == null || amenities.get().isEmpty()) {
            return "N/A";
        }
        return amenities.get().replace(";", ", ");
    }
}
