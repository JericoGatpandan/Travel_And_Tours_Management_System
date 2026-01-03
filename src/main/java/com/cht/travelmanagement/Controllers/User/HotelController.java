package com.cht.travelmanagement.Controllers.User;

import com.cht.travelmanagement.Models.Accommodation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class HotelController implements Initializable {
    @FXML
    private Button addHotel_btn;

    @FXML
    private ListView<Accommodation> hotelListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
