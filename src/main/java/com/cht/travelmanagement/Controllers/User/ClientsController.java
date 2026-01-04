package com.cht.travelmanagement.Controllers.User;

import java.net.URL;
import java.util.ResourceBundle;

import com.cht.travelmanagement.Models.Client;
import com.cht.travelmanagement.Models.Model;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> clients_table;

    @FXML
    private TableColumn<Client, String> clientName_col;

    @FXML
    private TableColumn<Client, String> contactNum_col;

    @FXML
    private TextField customerSearch_fld;

    @FXML
    private TableColumn<Client, String> destination_col;

    @FXML
    private TableColumn<Client, String> email_col;

    @FXML
    private TableColumn<Client, String> tripDates_col;

    @FXML
    private TableColumn<Client, String> tripStatus_col;

    @FXML
    private TableColumn<Client, Void> actions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns with property value factories
        clientName_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactNum_col.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        destination_col.setCellValueFactory(new PropertyValueFactory<>("destination"));
        tripStatus_col.setCellValueFactory(new PropertyValueFactory<>("tripStatus"));
        tripDates_col.setCellValueFactory(new PropertyValueFactory<>("tripDates"));

        // Load clients for the authenticated employee
        loadEmployeeClients();
    }

    private void loadEmployeeClients() {
        int employeeId = Model.getInstance().getAuthenticatedUser().getEmployeeId();
        var clientList = Model.getInstance().getEmployeeRepository().getEmployeeClientList(employeeId);
        clients_table.setItems(clientList);
    }


   

   
    

}
