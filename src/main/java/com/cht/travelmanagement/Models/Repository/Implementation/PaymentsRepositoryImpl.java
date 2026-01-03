package com.cht.travelmanagement.Models.Repository.Implementation;

import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Repository.PaymentsRepository;

import java.sql.Connection;

public class PaymentsRepositoryImpl implements PaymentsRepository {
    @Override
    public void getAllPayments() {
        String query = "SELECT * FROM payment";

        try (Connection conn = DatabaseDriver.getConnection()) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
