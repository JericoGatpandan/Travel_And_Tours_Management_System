package com.cht.travelmanagement.Controllers.Admin;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Employee;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.Models.Repository.Implementation.EmployeeRepositoryImpl;
import com.cht.travelmanagement.View.AlertUtility;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class EmployeeManagementController implements Initializable {

    // Header Controls
    @FXML
    public Button addNewBtn;
    @FXML
    public TextField searchField;
    @FXML
    public Button searchBtn;
    @FXML
    public Button refreshBtn;
    @FXML
    public CheckBox showInactiveCheckbox;

    // Table
    @FXML
    public TableView<Employee> employeesTable;
    @FXML
    public TableColumn<Employee, Integer> idColumn;
    @FXML
    public TableColumn<Employee, String> nameColumn;
    @FXML
    public TableColumn<Employee, String> emailColumn;
    @FXML
    public TableColumn<Employee, String> contactColumn;
    @FXML
    public TableColumn<Employee, String> roleColumn;
    @FXML
    public TableColumn<Employee, String> statusColumn;
    @FXML
    public TableColumn<Employee, Void> actionsColumn;

    // Form
    @FXML
    public TitledPane formPane;
    @FXML
    public TextField nameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField contactField;
    @FXML
    public ComboBox<String> roleComboBox;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public CheckBox isActiveCheckbox;
    @FXML
    public Label passwordHintLabel;
    @FXML
    public Button cancelBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Label formMessageLabel;

    private final EmployeeRepository employeeRepository;
    private Employee selectedEmployee = null;
    private boolean isEditMode = false;

    public EmployeeManagementController() {
        this.employeeRepository = new EmployeeRepositoryImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupForm();
        loadEmployees();
        setupEventHandlers();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().employeeIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        contactColumn.setCellValueFactory(cellData -> cellData.getValue().contactNumberProperty());
        roleColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().isManager() ? "Manager" : "Staff"));
        statusColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().isActive() ? "Active" : "Inactive"));

        // Status column styling
        statusColumn.setCellFactory(column -> new TableCell<Employee, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Active".equals(item)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Role column styling
        roleColumn.setCellFactory(column -> new TableCell<Employee, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Manager".equals(item)) {
                        setStyle("-fx-text-fill: #8e44ad; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #2980b9;");
                    }
                }
            }
        });

        // Actions column with Edit and Delete buttons
        actionsColumn.setCellFactory(column -> new TableCell<Employee, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button toggleBtn = new Button("Toggle");
            private final HBox actionBox = new HBox(5, editBtn, toggleBtn);

            {
                editBtn.getStyleClass().add("action-button-edit");
                toggleBtn.getStyleClass().add("action-button-toggle");
                actionBox.setAlignment(Pos.CENTER);

                editBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    editEmployee(emp);
                });

                toggleBtn.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    toggleEmployeeStatus(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Employee emp = getTableView().getItems().get(getIndex());
                    toggleBtn.setText(emp.isActive() ? "Deactivate" : "Activate");
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setupForm() {
        // Setup role combo box
        roleComboBox.setItems(FXCollections.observableArrayList("Staff", "Manager"));
        roleComboBox.setValue("Staff");

        // Hide password hint initially
        passwordHintLabel.setVisible(false);
    }

    private void setupEventHandlers() {
        addNewBtn.setOnAction(event -> showAddForm());
        searchBtn.setOnAction(event -> searchEmployees());
        refreshBtn.setOnAction(event -> loadEmployees());
        cancelBtn.setOnAction(event -> hideForm());
        saveBtn.setOnAction(event -> saveEmployee());
        showInactiveCheckbox.setOnAction(event -> loadEmployees());

        // Search on enter key
        searchField.setOnAction(event -> searchEmployees());
    }

    private void loadEmployees() {
        ObservableList<Employee> employees;
        if (showInactiveCheckbox.isSelected()) {
            employees = employeeRepository.getAllEmployees();
        } else {
            employees = employeeRepository.getActiveEmployees();
        }
        employeesTable.setItems(employees);
        searchField.clear();
    }

    private void searchEmployees() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadEmployees();
        } else {
            ObservableList<Employee> results = employeeRepository.searchEmployees(searchTerm);
            // Filter by active status if checkbox is not selected
            if (!showInactiveCheckbox.isSelected()) {
                results = results.filtered(Employee::isActive);
            }
            employeesTable.setItems(results);
        }
    }

    private void showAddForm() {
        isEditMode = false;
        selectedEmployee = null;
        clearForm();
        formPane.setText("Add New Employee");
        formPane.setExpanded(true);
        formMessageLabel.setText("");
        passwordHintLabel.setVisible(false);
        passwordField.setPromptText("Enter password");
        confirmPasswordField.setPromptText("Confirm password");
    }

    private void editEmployee(Employee emp) {
        isEditMode = true;
        selectedEmployee = emp;
        populateForm(emp);
        formPane.setText("Edit Employee - " + emp.getName());
        formPane.setExpanded(true);
        formMessageLabel.setText("");
        passwordHintLabel.setVisible(true);
        passwordField.setPromptText("Leave blank to keep current");
        confirmPasswordField.setPromptText("Leave blank to keep current");
    }

    private void toggleEmployeeStatus(Employee emp) {
        String action = emp.isActive() ? "deactivate" : "activate";
        if (AlertUtility.showConfirmation("Toggle Status", 
                action.substring(0, 1).toUpperCase() + action.substring(1) + " " + emp.getName() + "?", 
                "Are you sure you want to " + action + " this employee?")) {
            
            boolean success = employeeRepository.toggleEmployeeStatus(emp.getEmployeeId(), !emp.isActive());
            if (success) {
                AlertUtility.showSuccess("Success", "Status Updated", "Employee " + action + "d successfully!");
                loadEmployees();
            } else {
                AlertUtility.showError("Error", "Update Failed", "Failed to " + action + " employee.");
            }
        }
    }

    private void saveEmployee() {
        if (!validateForm()) {
            return;
        }

        String password = passwordField.getText();
        // For edit mode, only use password if it's provided
        if (isEditMode && password.isEmpty()) {
            password = null; // Will keep existing password
        }

        Employee emp = new Employee(
                isEditMode ? selectedEmployee.getEmployeeId() : 0,
                nameField.getText().trim(),
                emailField.getText().trim().toLowerCase(),
                password,
                contactField.getText().trim(),
                "Manager".equals(roleComboBox.getValue()),
                isActiveCheckbox.isSelected()
        );

        boolean success;
        if (isEditMode) {
            success = employeeRepository.updateEmployee(emp);
        } else {
            success = employeeRepository.createEmployee(emp);
        }

        if (success) {
            AlertUtility.showSuccess("Success", isEditMode ? "Employee Updated" : "Employee Created",
                    isEditMode ? "Employee updated successfully!" : "Employee created successfully!");
            hideForm();
            loadEmployees();
        } else {
            formMessageLabel.setText("Error saving employee. Please try again.");
            formMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("• Full name is required\n");
        }
        if (emailField.getText().trim().isEmpty()) {
            errors.append("• Email is required\n");
        } else if (!emailField.getText().trim().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.append("• Invalid email format\n");
        } else {
            // Check if email already exists
            int excludeId = isEditMode ? selectedEmployee.getEmployeeId() : 0;
            if (employeeRepository.isEmailExists(emailField.getText().trim(), excludeId)) {
                errors.append("• Email already exists\n");
            }
        }

        // Password validation for new employees
        if (!isEditMode && passwordField.getText().isEmpty()) {
            errors.append("• Password is required\n");
        }

        // Password confirmation check
        if (!passwordField.getText().isEmpty() && !passwordField.getText().equals(confirmPasswordField.getText())) {
            errors.append("• Passwords do not match\n");
        }

        // Password strength check
        if (!passwordField.getText().isEmpty() && passwordField.getText().length() < 6) {
            errors.append("• Password must be at least 6 characters\n");
        }

        if (errors.length() > 0) {
            formMessageLabel.setText(errors.toString());
            formMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
            return false;
        }

        return true;
    }

    private void populateForm(Employee emp) {
        nameField.setText(emp.getName());
        emailField.setText(emp.getEmail());
        contactField.setText(emp.getContactNumber());
        roleComboBox.setValue(emp.isManager() ? "Manager" : "Staff");
        isActiveCheckbox.setSelected(emp.isActive());
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        contactField.clear();
        roleComboBox.setValue("Staff");
        passwordField.clear();
        confirmPasswordField.clear();
        isActiveCheckbox.setSelected(true);
    }

    private void hideForm() {
        formPane.setExpanded(false);
        clearForm();
        isEditMode = false;
        selectedEmployee = null;
    }
}
