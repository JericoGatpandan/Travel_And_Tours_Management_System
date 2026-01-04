package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Trip;

import javafx.collections.ObservableList;

public interface TripRepository {

    /**
     * Get all trips from the database
     */
    ObservableList<Trip> getAllTrips();

    /**
     * Get trips for a specific package
     */
    ObservableList<Trip> getTripsByPackageId(int packageId);

    /**
     * Get active trips only
     */
    ObservableList<Trip> getActiveTrips();
}
