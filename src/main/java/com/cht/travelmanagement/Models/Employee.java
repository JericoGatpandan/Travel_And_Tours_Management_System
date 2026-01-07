package com.cht.travelmanagement.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee extends Person {
    private final IntegerProperty employeeId;
    private final StringProperty password;
    private final BooleanProperty isManager;
    private final BooleanProperty isActive;

    public Employee(int employeeId, String name, String email, String password, String contactNumber, boolean isManager, boolean isActive) {
        super(name, email, contactNumber);
        this.employeeId = new SimpleIntegerProperty(this, "employeeId", employeeId);
        this.password = new SimpleStringProperty(this, "password", password);
        this.isManager = new SimpleBooleanProperty(this, "isManager", isManager);
        this.isActive = new SimpleBooleanProperty(this, "isActive", isActive);
    }
    
    public int getEmployeeId() { return employeeId.get(); }
    public IntegerProperty employeeIdProperty() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId.set(employeeId); }

    public String getPassword() { return password.get(); }
    public StringProperty passwordProperty() { return password; }
    public void setPassword(String password) { this.password.set(password); }

    public boolean isManager() { return isManager.get(); }
    public BooleanProperty isManagerProperty() { return isManager; }
    public void setIsManager(boolean isManager) { this.isManager.set(isManager); }

    public boolean isActive() { return isActive.get(); }
    public BooleanProperty isActiveProperty() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive.set(isActive); }
}