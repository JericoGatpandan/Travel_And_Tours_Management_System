package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Payment;

import javafx.collections.ObservableList;

public interface PaymentsRepository {

    /**
     * Get all payments from the database
     */
    ObservableList<Payment> getAllPayments();

    /**
     * Get payments for a specific booking
     */
    ObservableList<Payment> getPaymentsByBookingId(int bookingId);
}
