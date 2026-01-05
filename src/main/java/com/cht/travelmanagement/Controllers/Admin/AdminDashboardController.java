package com.cht.travelmanagement.Controllers.Admin;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Booking;
import com.cht.travelmanagement.Models.Repository.BookingRepository;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.Models.Repository.Implementation.BookingRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.EmployeeRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.Implementation.TourPackageRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.TourPackageRepository;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminDashboardController implements Initializable {

    // Stats Labels
    @FXML
    public Label totalSalesLabel;
    @FXML
    public Label totalBookingsLabel;
    @FXML
    public Label activePackagesLabel;
    @FXML
    public Label totalEmployeesLabel;

    // Charts
    @FXML
    public PieChart popularPackagesChart;
    @FXML
    public BarChart<String, Number> employeePerformanceChart;
    @FXML
    public CategoryAxis employeeAxis;
    @FXML
    public NumberAxis bookingsAxis;

    // Tables
    @FXML
    public TableView<Booking> recentBookingsTable;
    @FXML
    public TableColumn<Booking, Integer> bookingIdColumn;
    @FXML
    public TableColumn<Booking, String> clientNameColumn;
    @FXML
    public TableColumn<Booking, String> packageNameColumn;
    @FXML
    public TableColumn<Booking, String> bookingDateColumn;
    @FXML
    public TableColumn<Booking, String> bookingStatusColumn;

    @FXML
    public TableView<Object[]> topPerformersTable;
    @FXML
    public TableColumn<Object[], Integer> rankColumn;
    @FXML
    public TableColumn<Object[], String> employeeNameColumn;
    @FXML
    public TableColumn<Object[], Integer> bookingsCountColumn;
    @FXML
    public TableColumn<Object[], String> totalSalesColumn;

    // Buttons
    @FXML
    public Button refreshBtn;

    private final BookingRepository bookingRepository;
    private final TourPackageRepository tourPackageRepository;
    private final EmployeeRepository employeeRepository;
    private final NumberFormat currencyFormat;

    public AdminDashboardController() {
        this.bookingRepository = new BookingRepositoryImpl();
        this.tourPackageRepository = new TourPackageRepositoryImpl();
        this.employeeRepository = new EmployeeRepositoryImpl();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTables();
        loadDashboardData();

        refreshBtn.setOnAction(event -> refresh());
    }

    private void setupTables() {
        // Recent Bookings Table
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        packageNameColumn.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        bookingDateColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookingDate().toString()));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Top Performers Table
        rankColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleObjectProperty<>((Integer) cellData.getValue()[0]));
        employeeNameColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty((String) cellData.getValue()[1]));
        bookingsCountColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleObjectProperty<>((Integer) cellData.getValue()[2]));
        totalSalesColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(currencyFormat.format((Double) cellData.getValue()[3])));
    }

    private void loadDashboardData() {
        loadStats();
        loadPopularPackagesChart();
        loadEmployeePerformanceChart();
        loadRecentBookings();
        loadTopPerformers();
    }

    private void loadStats() {
        // Total Sales
        double totalSales = bookingRepository.getTotalSales();
        totalSalesLabel.setText(currencyFormat.format(totalSales));

        // Total Bookings
        int totalBookings = bookingRepository.getTotalBookingsCount();
        totalBookingsLabel.setText(String.valueOf(totalBookings));

        // Active Packages
        int activePackages = tourPackageRepository.getActivePackageCount();
        activePackagesLabel.setText(String.valueOf(activePackages));

        // Active Employees
        int activeEmployees = employeeRepository.getActiveEmployeeCount();
        totalEmployeesLabel.setText(String.valueOf(activeEmployees));
    }

    private void loadPopularPackagesChart() {
        popularPackagesChart.getData().clear();
        ObservableList<Object[]> popularPackages = bookingRepository.getPopularPackages();

        for (Object[] packageData : popularPackages) {
            String packageName = (String) packageData[0];
            int bookingCount = (Integer) packageData[1];
            if (bookingCount > 0) {
                PieChart.Data slice = new PieChart.Data(packageName + " (" + bookingCount + ")", bookingCount);
                popularPackagesChart.getData().add(slice);
            }
        }

        if (popularPackagesChart.getData().isEmpty()) {
            popularPackagesChart.getData().add(new PieChart.Data("No Bookings Yet", 1));
        }
    }

    private void loadEmployeePerformanceChart() {
        employeePerformanceChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings");

        ObservableList<Object[]> performance = employeeRepository.getEmployeePerformance();

        for (Object[] empData : performance) {
            String employeeName = (String) empData[0];
            int bookingCount = (Integer) empData[1];
            // Shorten name if too long
            if (employeeName.length() > 12) {
                employeeName = employeeName.substring(0, 10) + "...";
            }
            series.getData().add(new XYChart.Data<>(employeeName, bookingCount));
        }

        employeePerformanceChart.getData().add(series);
    }

    private void loadRecentBookings() {
        ObservableList<Booking> recentBookings = bookingRepository.getRecentBookingsWithDetails(10);
        recentBookingsTable.setItems(recentBookings);
    }

    private void loadTopPerformers() {
        ObservableList<Object[]> topPerformers = employeeRepository.getTopPerformers(5);
        topPerformersTable.getItems().clear();
        topPerformersTable.getItems().addAll(topPerformers);
    }

    public void refresh() {
        loadDashboardData();
    }
}
