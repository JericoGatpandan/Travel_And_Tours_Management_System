package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.Hotel;
import com.cht.travelmanagement.Models.Vehicle;

import javafx.collections.ObservableList;

public interface BookingRepository {

    int[] getDashboardData(int clientCount);

    ObservableList<Booking> getRecentBookings();

    ObservableList<Booking> getAllBookings();

    ObservableList<Hotel> getHotels();

    ObservableList<Vehicle> getVehicles();

    boolean createBooking(BookingData bookingData, int employeeId);

    boolean cancelBooking(int bookingId);
}
