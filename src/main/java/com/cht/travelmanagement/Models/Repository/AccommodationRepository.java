package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Accommodation;

import javafx.collections.ObservableList;

public interface AccommodationRepository {

    /**
     * Get all accommodations from the database
     */
    ObservableList<Accommodation> getAllAccommodations();
}
