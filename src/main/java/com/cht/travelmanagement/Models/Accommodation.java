package com.cht.travelmanagement.Models;

import com.mysql.cj.conf.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Accommodation {
    private final StringProperty name;
    private final StringProperty address;

    public Accommodation(int accommodationId, String name, String address) {
        this.name = new SimpleStringProperty(this, "name", name);
        this.address = new SimpleStringProperty(this, "address", address);
    }




}
