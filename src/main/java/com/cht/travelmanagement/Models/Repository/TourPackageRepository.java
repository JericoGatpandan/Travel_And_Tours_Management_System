package com.cht.travelmanagement.Models.Repository;

import com.cht.travelmanagement.Models.TourPackage;

import javafx.collections.ObservableList;

public interface TourPackageRepository {

    ObservableList<TourPackage> getTourPackages();

    ObservableList<TourPackage> getActiveTourPackages();

    ObservableList<TourPackage> searchTourPackages(String searchTerm);

    TourPackage getTourPackageById(int packageId);

    boolean createTourPackage(TourPackage tourPackage);

    boolean updateTourPackage(TourPackage tourPackage);

    boolean deleteTourPackage(int packageId);

    boolean togglePackageStatus(int packageId, boolean isActive);

    int getActivePackageCount();
}
