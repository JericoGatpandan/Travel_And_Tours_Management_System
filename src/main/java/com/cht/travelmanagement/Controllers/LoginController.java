package com.cht.travelmanagement.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Repository.EmployeeRepository;
import com.cht.travelmanagement.Models.Repository.Implementation.EmployeeRepositoryImpl;
import com.cht.travelmanagement.View.AccountType;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    public ChoiceBox<AccountType> acc_selector;
    @FXML
    public Label loginMessageLabel;
    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordPasswordField;
    @FXML
    public TextField passwordTextField;
    @FXML
    public Button loginButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button togglePasswordBtn;
    @FXML
    public FontAwesomeIconView togglePasswordIcon;

    private final EmployeeRepository employeeRepository;
    private boolean passwordVisible = false;

    public LoginController() {
        this.employeeRepository = new EmployeeRepositoryImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        acc_selector.setItems(FXCollections.observableArrayList(AccountType.USER, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoggedInAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoggedInAccountType(acc_selector.getValue()));
        loginButton.setOnAction(event -> onLoginButtonClicked());

        // Setup password toggle
        setupPasswordToggle();

        // Exit Application
        cancelButton.setOnAction(event -> Model.getInstance().getViewFactory().closeStage((Stage) cancelButton.getScene().getWindow()));
    }

    private void setupPasswordToggle() {
        // Bind the text fields bidirectionally
        passwordTextField.textProperty().bindBidirectional(passwordPasswordField.textProperty());

        // Toggle button action
        togglePasswordBtn.setOnAction(event -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                // Show password
                passwordPasswordField.setVisible(false);
                passwordPasswordField.setManaged(false);
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                togglePasswordIcon.setGlyphName("EYE_SLASH");
            } else {
                // Hide password
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                passwordPasswordField.setVisible(true);
                passwordPasswordField.setManaged(true);
                togglePasswordIcon.setGlyphName("EYE");
            }
        });
    }

    private void onLoginButtonClicked() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        AccountType selectedAccountType = acc_selector.getValue();
        Stage stage = (Stage) loginMessageLabel.getScene().getWindow();

        employeeRepository.evaluateLoginCredentials(username, password, selectedAccountType, Model.getInstance().getUserLoggedInSuccessfully());

        if (Model.getInstance().getUserLoggedInSuccessfully()) {
            // Store the authenticated user's email for later retrieval
            Model.getInstance().setAuthenticatedUserEmail(username);

            if (selectedAccountType == AccountType.USER) {
                Model.getInstance().getUserViewFactory().showUserDashboardWindow();
            } else {
                Model.getInstance().getAdminViewFactory().showAdminWindow();
            }
            Model.getInstance().getViewFactory().closeStage(stage);
        } else {
            loginMessageLabel.setText("Invalid Credentials or Access Denied.");
            usernameTextField.clear();
            passwordPasswordField.clear();
        }
    }
}
