package com.cht.travelmanagement.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

public class AdminController implements Initializable {

    @FXML
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getAdminViewFactory().getAdminSelectedMenuItem().addListener((observable, oldVal, newVal) -> {
            switch (newVal) {
                case PACKAGE_MANAGEMENT ->
                    admin_parent.setCenter(Model.getInstance().getAdminViewFactory().getPackageManagementView());
                case EMPLOYEE_MANAGEMENT ->
                    admin_parent.setCenter(Model.getInstance().getAdminViewFactory().getEmployeeManagementView());
                default -> {
                    admin_parent.setCenter(Model.getInstance().getAdminViewFactory().getAdminDashboardView());
                    // Refresh dashboard data when navigating back to it
                    AdminDashboardController dashboard = Model.getInstance().getAdminViewFactory().getDashboardController();
                    if (dashboard != null) {
                        dashboard.refresh();
                    }
                }
            }
        });
    }
}
