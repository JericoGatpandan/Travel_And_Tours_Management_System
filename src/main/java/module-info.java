module com.cht.travelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.cht.travelmanagement to javafx.fxml;
    exports com.cht.travelmanagement;
    exports com.cht.travelmanagement.Controllers;
    exports  com.cht.travelmanagement.Controllers.Admin;
    exports  com.cht.travelmanagement.Controllers.User;
    exports   com.cht.travelmanagement.Models;
    exports  com.cht.travelmanagement.View;
}