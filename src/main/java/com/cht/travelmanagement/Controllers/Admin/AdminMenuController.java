package com.cht.travelmanagement.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.AdminMenuOption;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminMenuController implements Initializable {

    @FXML
    public Button dashboard_btn;
    @FXML
    public Button package_management_btn;
    @FXML
    public Button employee_management_btn;
    @FXML
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboard_btn.setOnAction(event -> onDashboardButtonClicked());
        package_management_btn.setOnAction(event -> onPackageManagementButtonClicked());
        employee_management_btn.setOnAction(event -> onEmployeeManagementButtonClicked());
        logout_btn.setOnAction(event -> onLogoutButtonClicked());

        Model.getInstance().getAdminViewFactory().getAdminSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            updateButtonStyles(newValue);
        });
    }

    private void onDashboardButtonClicked() {
        Model.getInstance().getAdminViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.DASHBOARD);
    }

    private void onPackageManagementButtonClicked() {
        Model.getInstance().getAdminViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.PACKAGE_MANAGEMENT);
    }

    private void onEmployeeManagementButtonClicked() {
        Model.getInstance().getAdminViewFactory().getAdminSelectedMenuItem().set(AdminMenuOption.EMPLOYEE_MANAGEMENT);
    }

    private void onLogoutButtonClicked() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setUserLoggedInSuccessfully(false);
    }

    private void updateButtonStyles(AdminMenuOption selectedOption) {
        // Reset ALL buttons to default style
        resetButton(dashboard_btn);
        resetButton(package_management_btn);
        resetButton(employee_management_btn);

        // Add "Active" style to the selected one
        switch (selectedOption) {
            case DASHBOARD ->
                setActive(dashboard_btn);
            case PACKAGE_MANAGEMENT ->
                setActive(package_management_btn);
            case EMPLOYEE_MANAGEMENT ->
                setActive(employee_management_btn);
            default ->
                setActive(dashboard_btn);
        }
    }

    private void resetButton(Button button) {
        button.getStyleClass().remove("menu-button-active");
    }

    private void setActive(Button button) {
        if (!button.getStyleClass().contains("menu-button-active")) {
            button.getStyleClass().add("menu-button-active");
        }
    }
}
