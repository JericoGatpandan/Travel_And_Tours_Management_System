package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.*;

import javafx.collections.ObservableList;

public interface BookingRepository {

    int[] getDashboardData(int clientCount);

    ObservableList<Booking> getRecentBookings();

    ObservableList<Booking> getAllBookings();

    ObservableList<Accommodation> getHotels();

    ObservableList<Vehicle> getVehicles();

    boolean createBooking(BookingData bookingData, int employeeId);

    boolean cancelBooking(int bookingId);

    // Admin Dashboard Methods
    double getTotalSales();

    int getTotalBookingsCount();

    ObservableList<Object[]> getPopularPackages();

    ObservableList<Booking> getRecentBookingsWithDetails(int limit);
}
