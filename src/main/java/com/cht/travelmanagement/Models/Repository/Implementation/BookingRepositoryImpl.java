package com.cht.travelmanagement.Models.Repository.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.BookingData;
import com.cht.travelmanagement.Models.DatabaseDriver;
import com.cht.travelmanagement.Models.Hotel;
import com.cht.travelmanagement.Models.Repository.BookingRepository;
import com.cht.travelmanagement.Models.Vehicle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookingRepositoryImpl implements BookingRepository {

    public BookingRepositoryImpl() {
    }

    /**
     * Fetches recent bookings from the database.
     *
     * @return An ObservableList of Booking objects representing recent
     * bookings.
     */
    @Override
    public ObservableList<Booking> getRecentBookings() {
        ObservableList<Booking> recentBookings = FXCollections.observableArrayList();
        String query = "SELECT b.BookingID AS TripID, c.name AS CustomerName, p.Name AS PackageName, p.Destination, MIN(t.StartDate) AS StartDate, "
                + "CASE WHEN MAX(t.EndDate) < CURRENT_DATE THEN 'Completed' "
                + "WHEN MIN(t.StartDate) <= CURRENT_DATE AND MAX(t.EndDate) >= CURRENT_DATE THEN 'Ongoing' "
                + "ELSE 'Upcoming' END AS Status "
                + "FROM booking b "
                + "JOIN client c ON b.ClientID = c.clientId "
                + "JOIN package p ON b.PackageID = p.PackageID "
                + "JOIN packagetrips pt ON p.PackageID = pt.PackageID "
                + "JOIN trip t ON pt.TripID = t.TripID "
                + "WHERE b.Status = 'confirmed' "
                + "GROUP BY b.BookingID, c.name, p.Name, p.Destination "
                + "ORDER BY StartDate ASC "
                + "LIMIT 10;";
        return getBookings(recentBookings, query);

    }

    @Override
    public ObservableList<Booking> getAllBookings() {
        ObservableList<Booking> allBookings = FXCollections.observableArrayList();
        String query = "SELECT b.BookingID AS TripID, c.name AS CustomerName, p.Name AS PackageName, p.Destination, MIN(t.StartDate) AS StartDate, "
                + "CASE WHEN MAX(t.EndDate) < CURRENT_DATE THEN 'Completed' "
                + "WHEN MIN(t.StartDate) <= CURRENT_DATE AND MAX(t.EndDate) >= CURRENT_DATE THEN 'Ongoing' "
                + "ELSE 'Upcoming' END AS Status "
                + "FROM booking b "
                + "JOIN client c ON b.ClientID = c.clientId "
                + "JOIN package p ON b.PackageID = p.PackageID "
                + "JOIN packagetrips pt ON p.PackageID = pt.PackageID "
                + "JOIN trip t ON pt.TripID = t.TripID "
                + "WHERE b.Status = 'confirmed' "
                + "GROUP BY b.BookingID, c.name, p.Name, p.Destination "
                + "ORDER BY StartDate ASC;";
        return getBookings(allBookings, query);
    }

    private ObservableList<Booking> getBookings(ObservableList<Booking> allBookings, String query) {
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int bookingId = resultSet.getInt("TripID");
                String clientName = resultSet.getString("CustomerName");
                String destination = resultSet.getString("Destination");
                String packageName = resultSet.getString("PackageName");
                LocalDate startDate = resultSet.getDate("StartDate").toLocalDate();
                String status = resultSet.getString("Status");

                Booking booking = new Booking(bookingId, clientName, destination, packageName, startDate, status);
                allBookings.add(booking);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allBookings;
    }

    @Override
    public ObservableList<Hotel> getHotels() {
        ObservableList<Hotel> hotels = FXCollections.observableArrayList();
        String query = "SELECT accommodationId, name, address, contact, amenities, numberOfRooms, defaultRoomType FROM accommodation";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int hotelId = resultSet.getInt("accommodationId");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String contact = resultSet.getString("contact");
                String amenities = resultSet.getString("amenities");
                int numberOfRooms = resultSet.getInt("numberOfRooms");
                String roomType = resultSet.getString("defaultRoomType");

                // Determine rating based on amenities/name (simplified logic)
                int rating = determineHotelRating(name, amenities);
                int pricePerNight = calculateHotelPrice(rating);

                Hotel hotel = new Hotel(hotelId, name, address, contact, amenities, numberOfRooms, roomType, rating, pricePerNight);
                hotels.add(hotel);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return hotels;
    }

    private int determineHotelRating(String name, String amenities) {
        // Simple rating logic based on amenities
        if (amenities == null) {
            return 3;
        }

        int score = 3;
        if (amenities.toLowerCase().contains("pool")) {
            score++;
        }
        if (amenities.toLowerCase().contains("beachfront")) {
            score++;
        }
        if (name.toLowerCase().contains("resort")) {
            score++;
        }
        if (name.toLowerCase().contains("deluxe")) {
            score++;
        }

        return Math.min(score, 5);
    }

    @Override
    public boolean cancelBooking(int bookingId) {
        String query = "UPDATE booking SET Status = 'cancelled' WHERE BookingID = ?";
        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, bookingId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    private int calculateHotelPrice(int rating) {
        return switch (rating) {
            case 3 ->
                2500;
            case 4 ->
                4000;
            case 5 ->
                6500;
            default ->
                2000;
        };
    }

    @Override
    public ObservableList<Vehicle> getVehicles() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
        String query = "SELECT VehicleID, Type, Capacity, PlateNumber, ProviderName FROM vehicle WHERE Type != 'Plane'";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int vehicleId = resultSet.getInt("VehicleID");
                String type = resultSet.getString("Type");
                int capacity = resultSet.getInt("Capacity");
                String plateNumber = resultSet.getString("PlateNumber");
                String providerName = resultSet.getString("ProviderName");
                int pricePerDay = calculateVehiclePrice(type, capacity);

                Vehicle vehicle = new Vehicle(vehicleId, type, capacity, plateNumber, providerName, pricePerDay);
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vehicles;
    }

    private int calculateVehiclePrice(String type, int capacity) {
        if (capacity <= 4) {
            return 1500; // Sedan

        }
        if (capacity <= 10) {
            return 2500; // Van

        }
        return 5000; // Bus
    }

    @Override
    public boolean createBooking(BookingData bookingData, int employeeId) {
        String insertQuery = "INSERT INTO booking (EmployeeID, ClientID, PackageID, BookingDate, Status, PaxCount) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, bookingData.getClientId());
            preparedStatement.setInt(3, bookingData.getSelectedPackageId());
            preparedStatement.setDate(4, java.sql.Date.valueOf(bookingData.getBookingDate()));
            preparedStatement.setString(5, "confirmed");
            preparedStatement.setInt(6, bookingData.getPaxCount());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    // Could add additional booking details here (add-ons, special requests, etc.)
                    System.out.println("Booking created with ID: " + bookingId);
                    return true;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int[] getDashboardData(int clientCount) {
        int[] dashboardData = new int[4];

        System.out.println("Total Clients: " + clientCount);

        String bookingDatesQuery = "SELECT \r\n"
                + //
                "    SUM(IF(TripEndDate < CURRENT_DATE, 1, 0)) AS CompletedTrips,\r\n"
                + //
                "    SUM(IF(TripStartDate <= CURRENT_DATE AND TripEndDate >= CURRENT_DATE, 1, 0)) AS OngoingTrips,\r\n"
                + //
                "    SUM(IF(TripStartDate > CURRENT_DATE, 1, 0)) AS UpcomingTrips\r\n"
                + //
                "FROM (\r\n"
                + //
                "    SELECT \r\n"
                + //
                "        MIN(T.StartDate) AS TripStartDate,\r\n"
                + //
                "        MAX(T.EndDate) AS TripEndDate\r\n"
                + //
                "    FROM booking B \r\n"
                + //
                "        JOIN packagetrips PT ON B.PackageID = PT.PackageID\r\n"
                + //
                "        JOIN trip T ON PT.TripID = T.TripID\r\n"
                + //
                "        WHERE B.Status = 'Confirmed'\r\n"
                + //
                "    GROUP BY B.BookingID\r\n"
                + //
                "\r\n"
                + //
                ") AS BookingDates;";
        try (Connection conn = DatabaseDriver.getConnection(); PreparedStatement pstmt = conn.prepareStatement(bookingDatesQuery); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int completedTrips = rs.getInt("CompletedTrips");
                int ongoingTrips = rs.getInt("OngoingTrips");
                int upcomingTrips = rs.getInt("UpcomingTrips");

                dashboardData[0] = clientCount;
                dashboardData[1] = completedTrips;
                dashboardData[2] = ongoingTrips;
                dashboardData[3] = upcomingTrips;

                System.out.println("Completed Trips: " + completedTrips);
                System.out.println("Ongoing Trips: " + ongoingTrips);
                System.out.println("Upcoming Trips: " + upcomingTrips);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dashboardData;
    }

    @Override
    public double getTotalSales() {
        String query = "SELECT COALESCE(SUM(amount), 0) as totalSales FROM payment WHERE status = 'PAID'";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble("totalSales");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getTotalBookingsCount() {
        String query = "SELECT COUNT(*) as total FROM booking";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public ObservableList<Object[]> getPopularPackages() {
        ObservableList<Object[]> popularPackages = FXCollections.observableArrayList();
        String query = "SELECT p.Name, COUNT(b.BookingID) as bookingCount "
                + "FROM package p "
                + "LEFT JOIN booking b ON p.PackageID = b.PackageID "
                + "WHERE p.IsActive = 1 "
                + "GROUP BY p.PackageID, p.Name "
                + "ORDER BY bookingCount DESC "
                + "LIMIT 5";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String packageName = resultSet.getString("Name");
                int bookingCount = resultSet.getInt("bookingCount");
                popularPackages.add(new Object[]{packageName, bookingCount});
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return popularPackages;
    }

    @Override
    public ObservableList<Booking> getRecentBookingsWithDetails(int limit) {
        ObservableList<Booking> recentBookings = FXCollections.observableArrayList();
        String query = "SELECT b.BookingID, c.name AS CustomerName, p.Name AS PackageName, "
                + "p.Destination, b.BookingDate, b.Status "
                + "FROM booking b "
                + "JOIN client c ON b.ClientID = c.clientId "
                + "JOIN package p ON b.PackageID = p.PackageID "
                + "ORDER BY b.BookingDate DESC "
                + "LIMIT ?";

        try (Connection connection = DatabaseDriver.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, limit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int bookingId = resultSet.getInt("BookingID");
                    String clientName = resultSet.getString("CustomerName");
                    String packageName = resultSet.getString("PackageName");
                    String destination = resultSet.getString("Destination");
                    LocalDate bookingDate = resultSet.getDate("BookingDate").toLocalDate();
                    String status = resultSet.getString("Status");

                    Booking booking = new Booking(bookingId, clientName, destination, packageName, bookingDate, status);
                    recentBookings.add(booking);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recentBookings;
    }
}
