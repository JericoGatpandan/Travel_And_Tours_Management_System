package com.cht.travelmanagement.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for Hotel/Accommodation used in booking wizard
 */
public class Hotel {

    private final IntegerProperty hotelId;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty contact;
    private final StringProperty amenities;
    private final IntegerProperty numberOfRooms;
    private final StringProperty roomType;
    private final IntegerProperty rating; // 3, 4, or 5 stars
    private final IntegerProperty pricePerNight;

    public Hotel(int hotelId, String name, String address, String contact,
            String amenities, int numberOfRooms, String roomType, int rating, int pricePerNight) {
        this.hotelId = new SimpleIntegerProperty(hotelId);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.contact = new SimpleStringProperty(contact);
        this.amenities = new SimpleStringProperty(amenities);
        this.numberOfRooms = new SimpleIntegerProperty(numberOfRooms);
        this.roomType = new SimpleStringProperty(roomType);
        this.rating = new SimpleIntegerProperty(rating);
        this.pricePerNight = new SimpleIntegerProperty(pricePerNight);
    }

    // Getters
    public int getHotelId() {
        return hotelId.get();
    }

    public IntegerProperty hotelIdProperty() {
        return hotelId;
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

    public String getRoomType() {
        return roomType.get();
    }

    public StringProperty roomTypeProperty() {
        return roomType;
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
}
