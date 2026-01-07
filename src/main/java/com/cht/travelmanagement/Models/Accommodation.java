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
    private final IntegerProperty rating;
    private final IntegerProperty pricePerNight;

    public Accommodation(int accommodationId, String name, String address, String contact,
            String amenities, int numberOfRooms, String defaultRoomType, int rating, int pricePerNight) {
        this.accommodationId = new SimpleIntegerProperty(accommodationId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.contact = new SimpleStringProperty(contact);
        this.amenities = new SimpleStringProperty(amenities);
        this.numberOfRooms = new SimpleIntegerProperty(numberOfRooms);
        this.defaultRoomType = new SimpleStringProperty(defaultRoomType);
        this.rating = new SimpleIntegerProperty(rating);
        this.pricePerNight = new SimpleIntegerProperty(pricePerNight);
    }

    // Legacy constructor for compatibility
    public Accommodation(int accommodationId, String name, String address) {
        this(accommodationId, name, address, "", "", 0, "Standard", 3, 0);
    }

    public Accommodation(int accommodationId, String name, String address, String contact, String amenities, int numberOfRooms, String defaultRoomType) {
        this.accommodationId = new SimpleIntegerProperty(accommodationId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.contact = new SimpleStringProperty(contact);
        this.amenities = new SimpleStringProperty(amenities);
        this.numberOfRooms = new SimpleIntegerProperty(numberOfRooms);
        this.defaultRoomType = new SimpleStringProperty(defaultRoomType);
        this.rating = new SimpleIntegerProperty(3);
        this.pricePerNight = new SimpleIntegerProperty(0);

    }

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

    public int getRating() {
        return rating.get();
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public int getPricePerNight() {
        return pricePerNight.get();
    }

    public IntegerProperty pricePerNightProperty() {
        return pricePerNight;
    }

    /**
     * Get star rating as string (e.g., "★★★★★")
     */
    public String getStarRating() {
        return "★".repeat(rating.get()) + "☆".repeat(5 - rating.get());
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
