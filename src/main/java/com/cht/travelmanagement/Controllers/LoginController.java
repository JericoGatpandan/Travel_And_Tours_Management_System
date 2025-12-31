package com.cht.travelmanagement.Controllers;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.View.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> acc_selector;
    public Label loginMessageLabel;
    public TextField usernameTextField;
    public PasswordField passwordPasswordField;
    public Button loginButton;
    public Button cancelButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.USER, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoggedInAccountType());
        acc_selector.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoggedInAccountType(acc_selector.getValue()));
        loginButton.setOnAction(event -> onLoginButtonClicked());
    }

    private void onLoginButtonClicked() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();

        Stage stage = (Stage) loginMessageLabel.getScene().getWindow();
        if (Model.getInstance().getViewFactory().getLoggedInAccountType() == AccountType.USER) {
            Model.getInstance().evaluateLoginCredentials(username, password);
            if (Model.getInstance().getUserLoggedInSuccessfully()) {
                Model.getInstance().getUserViewFactory().showUserDashboardWindow();
                Model.getInstance().getViewFactory().closeStage(stage);
            } else {
                loginMessageLabel.setText("Invalid Credentials. Please try again.");
                usernameTextField.clear();
                passwordPasswordField.clear();
            }
        } else {
            Model.getInstance().getAdminViewFactory().showAdminWindow();
        }
    }
}
