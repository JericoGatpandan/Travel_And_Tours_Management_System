package com.cht.travelmanagement.View;

import com.cht.travelmanagement.Controllers.Admin.AdminController;
import com.cht.travelmanagement.Controllers.Admin.AdminDashboardController;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminViewFactory extends ViewFactory {

    // Views
    private Parent adminDashboardView;
    private AdminDashboardController dashboardController;
    private Parent packageManagementView;
    private Parent employeeManagementView;

    private final ObjectProperty<AdminMenuOption> adminSelectedMenuItem;
    private String loggedInEmployeeEmail;

    public AdminViewFactory() {
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<AdminMenuOption> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/Admin-view.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public String setLoggedInEmployeeEmail(String email) {
        this.loggedInEmployeeEmail = email;
        return email;
    }

    public String getLoggedInEmployeeEmail() {
        return loggedInEmployeeEmail;
    }

    public AdminDashboardController getDashboardController() {
        return dashboardController;
    }

    public Parent getAdminDashboardView() {
        if (adminDashboardView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/AdminDashboard-view.fxml"));
                adminDashboardView = loader.load();
                dashboardController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminDashboardView;
    }

    public Parent getPackageManagementView() {
        if (packageManagementView == null) {
            try {
                packageManagementView = FXMLLoader.load(getClass().getResource("/Views/Admin/PackageManagement-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return packageManagementView;
    }

    public Parent getEmployeeManagementView() {
        if (employeeManagementView == null) {
            try {
                employeeManagementView = FXMLLoader.load(getClass().getResource("/Views/Admin/EmployeeManagement-view.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return employeeManagementView;
    }

    /**
     * Reset admin views (useful when logging out and logging back in)
     */
    public void resetViews() {
        adminDashboardView = null;
        dashboardController = null;
        packageManagementView = null;
        employeeManagementView = null;
    }
}
