package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.Client;

import javafx.collections.ObservableList;

public interface ClientRepository {

    ObservableList<Client> getClients();

    ObservableList<Client> searchClients(String searchTerm);

    boolean deleteClient(int clientId);

    boolean updateClient(int clientId, String name, String email, String contactNumber, String address, String customerType);

    void createClient();

    int createClient(String name, String email, String contactNumber, String address, String customerType);
}
