package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Client;

import javafx.collections.ObservableList;

public interface ClientRepository {

    ObservableList<Client> getClients();

    ObservableList<Client> searchClients(String searchTerm);

    void deleteClient();

    void updateClient();

    void createClient();

    int createClient(String name, String email, String contactNumber, String address, String customerType);
}
