package com.cht.travelmanagement.View;

import com.cht.travelmanagement.Controllers.Admin.AdminController;
import com.cht.travelmanagement.Controllers.Admin.AdminMenuController;
import com.cht.travelmanagement.Controllers.User.UserController;
import javafx.fxml.FXMLLoader;

public class AdminViewFactory extends ViewFactory{

    public AdminViewFactory() {

    }

    public void showAdminWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Admin/Admin-view.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }
}
