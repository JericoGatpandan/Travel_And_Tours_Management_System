module com.cht.travelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
	requires javafx.base;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.desktop;

    opens com.cht.travelmanagement to javafx.fxml;
    opens com.cht.travelmanagement.Controllers to javafx.fxml;
    opens com.cht.travelmanagement.Controllers.User to javafx.fxml;
    opens com.cht.travelmanagement.Controllers.Admin to javafx.fxml;
    opens com.cht.travelmanagement.Controllers.User.BookingWizard to javafx.fxml;

    exports com.cht.travelmanagement;
    exports com.cht.travelmanagement.Controllers;
    exports  com.cht.travelmanagement.Controllers.Admin;
    exports  com.cht.travelmanagement.Controllers.User;
    exports com.cht.travelmanagement.Controllers.User.BookingWizard;
    exports   com.cht.travelmanagement.Models;
    exports  com.cht.travelmanagement.View;
}