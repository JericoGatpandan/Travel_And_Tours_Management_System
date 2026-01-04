package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Payment;
import com.cht.travelmanagement.Models.Repository.PaymentsRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PaymentsRepositoryImpl implements PaymentsRepository {

    @Override
    public ObservableList<Payment> getAllPayments() {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        String query = """
            SELECT p.*, c.name as clientName, pkg.Name as packageName 
            FROM payment p 
            LEFT JOIN booking b ON p.bookingId = b.BookingID 
            LEFT JOIN client c ON b.ClientID = c.clientId 
            LEFT JOIN package pkg ON b.PackageID = pkg.PackageID 
            ORDER BY p.paymentDate DESC
        """;

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("paymentId"),
                        rs.getInt("bookingId"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("paymentDate").toLocalDate(),
                        rs.getString("method"),
                        rs.getString("status"),
                        rs.getString("referenceNumber"),
                        rs.getString("clientName"),
                        rs.getString("packageName")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching payments: " + e.getMessage());
            e.printStackTrace();
        }

        return payments;
    }

    @Override
    public ObservableList<Payment> getPaymentsByBookingId(int bookingId) {
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        String query = "SELECT * FROM payment WHERE bookingId = ? ORDER BY paymentDate DESC";

        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("paymentId"),
                        rs.getInt("bookingId"),
                        rs.getBigDecimal("amount"),
                        rs.getDate("paymentDate").toLocalDate(),
                        rs.getString("method"),
                        rs.getString("status"),
                        rs.getString("referenceNumber")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching payments for booking: " + e.getMessage());
            e.printStackTrace();
        }

        return payments;
    }
}
