package com.cht.travelmanagement.Models;

import com.cht.travelmanagement.Controllers.User.BookingWizard.BookingNavigationController;
import com.cht.travelmanagement.Models.Repository.AccommodationRepository;
import com.cht.travelmanagement.Models.Repository.BookingRepository;
import com.cht.travelmanagement.Models.Repository.ClientRepository;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.Models.Repository.Implementation.AccommodationRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.BookingRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.ClientRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.EmployeeRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.PaymentsRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.TourPackageRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.TripRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.PaymentsRepository;
import com.cht.travelmanagement.Models.Repository.TourPackageRepository;
import com.cht.travelmanagement.Models.Repository.TripRepository;
import com.cht.travelmanagement.View.AdminViewFactory;
import com.cht.travelmanagement.View.UserViewFactory;
import com.cht.travelmanagement.View.ViewFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {

    private static Model model;
    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;
    private final AdminViewFactory adminViewFactory;
    private final UserViewFactory userViewFactory;

    public final ObservableList<TourPackage> tourPackages;
    public final ObservableList<Booking> bookings;
    public final BookingNavigationController bookingNavigationController;

    private boolean userLoggedInSuccessfully;
    private Employee authenticatedUser;
    private String authenticatedUserEmail;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        this.adminViewFactory = new AdminViewFactory();
        this.userViewFactory = new UserViewFactory();
        this.userLoggedInSuccessfully = false;

        this.tourPackages = FXCollections.observableArrayList();
        this.bookings = FXCollections.observableArrayList();

        this.bookingNavigationController = new BookingNavigationController();

    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public AdminViewFactory getAdminViewFactory() {
        return adminViewFactory;
    }

    public UserViewFactory getUserViewFactory() {
        return userViewFactory;
    }

    /**
     * User Methods Section
     */
    public void setUserLoggedInSuccessfully(boolean status) {
        this.userLoggedInSuccessfully = status;
    }

    public boolean getUserLoggedInSuccessfully() {
        return this.userLoggedInSuccessfully;
    }

    /**
     * Set Authenticated User
     */
    public void setAuthenticatedUser(Employee employee) {
        this.authenticatedUser = employee;
    }

    /**
     * Get Authenticated User
     */
    public Employee getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    /**
     * Set Authenticated User Email (during login)
     */
    public void setAuthenticatedUserEmail(String email) {
        this.authenticatedUserEmail = email;
    }

    /**
     * Get Authenticated User Email
     */
    public String getAuthenticatedUserEmail() {
        return this.authenticatedUserEmail;
    }

    /**
     * Get Clients from Database
     */
    public ObservableList<Client> getClients() {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        return clientRepository.getClients();
    }

    /**
     * Get Dashboard Data from Database
     */
    public int[] getDashboardData() {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        int clientCount = getClients().size();
        return bookingRepository.getDashboardData(clientCount);
    }

    /**
     * Get Tour Packages from Database
     */
    public ObservableList<TourPackage> getTourPackages() {
        TourPackageRepository bookingRepository = new TourPackageRepositoryImpl();
        return bookingRepository.getTourPackages();
    }

    public ObservableList<Booking> getRecentBookings() {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.getRecentBookings();
    }

    public ObservableList<Booking> getAllBookings() {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.getAllBookings();
    }

    /**
     * Load Authenticated User from Database
     */
    public void loadAuthenticatedUser() {
        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        employeeRepository.getAuthenticatedUser();
    }

    /**
     * Get Employee Repository
     */
    public EmployeeRepository getEmployeeRepository() {
        return new EmployeeRepositoryImpl();
    }

    /**
     * Get shared booking data
     */
    public BookingData getBookingData() {
        return getUserViewFactory().getBookingData();
    }

    /**
     * Get Hotels from Database
     */
    public ObservableList<Accommodation> getHotels() {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.getHotels();
    }

    /**
     * Get Vehicles from Database
     */
    public ObservableList<Vehicle> getVehicles() {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.getVehicles();
    }

    /**
     * Search clients by name or email
     */
    public ObservableList<Client> searchClients(String searchTerm) {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        return clientRepository.searchClients(searchTerm);
    }

    /**
     * Create a new booking
     */
    public boolean createBooking(BookingData bookingData) {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.createBooking(bookingData, getAuthenticatedUser().getEmployeeId());
    }

    /**
     * Cancel a booking by ID
     */
    public boolean cancelBooking(int bookingId) {
        BookingRepository bookingRepository = new BookingRepositoryImpl();
        return bookingRepository.cancelBooking(bookingId);
    }

    /**
     * Create a new client
     */
    public int createClient(String name, String email, String contactNumber, String address, String customerType) {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        return clientRepository.createClient(name, email, contactNumber, address, customerType);
    }

    /**
     * Update an existing client
     */
    public boolean updateClient(int clientId, String name, String email, String contactNumber, String address, String customerType) {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        return clientRepository.updateClient(clientId, name, email, contactNumber, address, customerType);
    }

    /**
     * Delete a client
     */
    public boolean deleteClient(int clientId) {
        ClientRepository clientRepository = new ClientRepositoryImpl();
        return clientRepository.deleteClient(clientId);
    }

    /**
     * Get all payments
     */
    public ObservableList<Payment> getAllPayments() {
        PaymentsRepository paymentsRepository = new PaymentsRepositoryImpl();
        return paymentsRepository.getAllPayments();
    }

    /**
     * Get all trips
     */
    public ObservableList<Trip> getAllTrips() {
        TripRepository tripRepository = new TripRepositoryImpl();
        return tripRepository.getAllTrips();
    }

    /**
     * Get all accommodations
     */
    public ObservableList<Accommodation> getAllAccommodations() {
        AccommodationRepository accommodationRepository = new AccommodationRepositoryImpl();
        return accommodationRepository.getAllAccommodations();
    }
    
    /**
     * 
     */
}
