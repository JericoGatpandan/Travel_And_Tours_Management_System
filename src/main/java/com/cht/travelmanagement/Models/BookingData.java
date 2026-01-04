package com.cht.travelmanagement.Models;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Shared data model for the booking wizard. Holds all booking information
 * across wizard steps with observable properties for real-time UI updates.
 */
public class BookingData {

    // Step 1: Customer Information
    private final IntegerProperty clientId = new SimpleIntegerProperty(-1);
    private final StringProperty clientName = new SimpleStringProperty("");
    private final StringProperty clientEmail = new SimpleStringProperty("");
    private final StringProperty clientContact = new SimpleStringProperty("");
    private final StringProperty clientDestination = new SimpleStringProperty("");
    private final StringProperty travelType = new SimpleStringProperty("");
    private final IntegerProperty paxCount = new SimpleIntegerProperty(1);
    private final BooleanProperty isExistingClient = new SimpleBooleanProperty(false);

    // Step 2: Package Selection
    private final IntegerProperty selectedPackageId = new SimpleIntegerProperty(-1);
    private final StringProperty selectedPackageName = new SimpleStringProperty("");
    private final IntegerProperty packagePrice = new SimpleIntegerProperty(0);
    private final StringProperty packageDestination = new SimpleStringProperty("");
    private final IntegerProperty packageDuration = new SimpleIntegerProperty(0);

    // Step 3: Customization / Add-ons
    private final BooleanProperty includeBreakfast = new SimpleBooleanProperty(false);
    private final BooleanProperty includeInsurance = new SimpleBooleanProperty(false);
    private final BooleanProperty includeGuide = new SimpleBooleanProperty(false);
    private final BooleanProperty includePickup = new SimpleBooleanProperty(false);
    private final StringProperty specialRequests = new SimpleStringProperty("");

    // Add-on prices (in PHP)
    public static final int BREAKFAST_PRICE = 500;
    public static final int INSURANCE_PRICE = 1000;
    public static final int GUIDE_PRICE = 2000;
    public static final int PICKUP_PRICE = 800;

    // Step 4: Hotel Selection
    private final IntegerProperty selectedHotelId = new SimpleIntegerProperty(-1);
    private final StringProperty selectedHotelName = new SimpleStringProperty("");
    private final IntegerProperty hotelPrice = new SimpleIntegerProperty(0);
    private final StringProperty hotelRating = new SimpleStringProperty("");

    // Step 5: Vehicle Selection
    private final IntegerProperty selectedVehicleId = new SimpleIntegerProperty(-1);
    private final StringProperty selectedVehicleName = new SimpleStringProperty("");
    private final StringProperty vehicleType = new SimpleStringProperty("");
    private final IntegerProperty vehiclePrice = new SimpleIntegerProperty(0);

    // Step 6: Confirmation
    private final BooleanProperty termsAccepted = new SimpleBooleanProperty(false);
    private final ObjectProperty<LocalDate> bookingDate = new SimpleObjectProperty<>(LocalDate.now());

    // Completion tracking
    private final IntegerProperty completedSteps = new SimpleIntegerProperty(0);

    public BookingData() {
        // Initialize with default values
    }

    /**
     * Reset all booking data to defaults - used when starting a new booking
     */
    public void reset() {
        // Step 1
        clientId.set(-1);
        clientName.set("");
        clientEmail.set("");
        clientContact.set("");
        clientDestination.set("");
        travelType.set("");
        paxCount.set(1);
        isExistingClient.set(false);

        // Step 2
        selectedPackageId.set(-1);
        selectedPackageName.set("");
        packagePrice.set(0);
        packageDestination.set("");
        packageDuration.set(0);

        // Step 3
        includeBreakfast.set(false);
        includeInsurance.set(false);
        includeGuide.set(false);
        includePickup.set(false);
        specialRequests.set("");

        // Step 4
        selectedHotelId.set(-1);
        selectedHotelName.set("");
        hotelPrice.set(0);
        hotelRating.set("");

        // Step 5
        selectedVehicleId.set(-1);
        selectedVehicleName.set("");
        vehicleType.set("");
        vehiclePrice.set(0);

        // Step 6
        termsAccepted.set(false);
        bookingDate.set(LocalDate.now());

        completedSteps.set(0);
    }

    /**
     * Calculate total price including all add-ons
     */
    public int calculateTotalPrice() {
        int total = packagePrice.get();

        if (includeBreakfast.get()) {
            total += BREAKFAST_PRICE * paxCount.get();
        }
        if (includeInsurance.get()) {
            total += INSURANCE_PRICE * paxCount.get();
        }
        if (includeGuide.get()) {
            total += GUIDE_PRICE;
        }
        if (includePickup.get()) {
            total += PICKUP_PRICE;
        }

        total += hotelPrice.get();
        total += vehiclePrice.get();

        // Multiply package price by pax count
        total = (packagePrice.get() * paxCount.get()) + getAddonsTotal() + hotelPrice.get() + vehiclePrice.get();

        return total;
    }

    /**
     * Get add-ons total
     */
    public int getAddonsTotal() {
        int total = 0;
        if (includeBreakfast.get()) {
            total += BREAKFAST_PRICE * paxCount.get();
        }
        if (includeInsurance.get()) {
            total += INSURANCE_PRICE * paxCount.get();
        }
        if (includeGuide.get()) {
            total += GUIDE_PRICE;
        }
        if (includePickup.get()) {
            total += PICKUP_PRICE;
        }
        return total;
    }

    /**
     * Check if step 1 (Customer) is complete
     */
    public boolean isStep1Complete() {
        return !clientName.get().isEmpty()
                && !clientEmail.get().isEmpty()
                && !clientContact.get().isEmpty()
                && paxCount.get() > 0;
    }

    /**
     * Check if step 2 (Package) is complete
     */
    public boolean isStep2Complete() {
        return selectedPackageId.get() > 0;
    }

    /**
     * Check if step 3 (Customization) is complete - always valid
     */
    public boolean isStep3Complete() {
        return true; // Add-ons are optional
    }

    /**
     * Check if step 4 (Hotel) is complete
     */
    public boolean isStep4Complete() {
        return selectedHotelId.get() > 0;
    }

    /**
     * Check if step 5 (Vehicle) is complete
     */
    public boolean isStep5Complete() {
        return selectedVehicleId.get() > 0;
    }

    /**
     * Check if step 6 (Confirmation) is ready
     */
    public boolean isStep6Complete() {
        return termsAccepted.get();
    }

    /**
     * Validate current step before proceeding
     */
    public boolean validateStep(int step) {
        return switch (step) {
            case 1 ->
                isStep1Complete();
            case 2 ->
                isStep2Complete();
            case 3 ->
                isStep3Complete();
            case 4 ->
                isStep4Complete();
            case 5 ->
                isStep5Complete();
            case 6 ->
                isStep6Complete();
            default ->
                false;
        };
    }

    /**
     * Get validation error message for a step
     */
    public String getValidationError(int step) {
        return switch (step) {
            case 1 ->
                "Please fill in all customer information fields.";
            case 2 ->
                "Please select a tour package.";
            case 3 ->
                ""; // No validation needed
            case 4 ->
                "Please select a hotel.";
            case 5 ->
                "Please select a vehicle.";
            case 6 ->
                "Please accept the terms and conditions.";
            default ->
                "Unknown step.";
        };
    }

    /**
     * Count completed steps for progress indicator
     */
    public int getCompletedStepsCount() {
        int count = 0;
        if (isStep1Complete()) {
            count++;
        }
        if (isStep2Complete()) {
            count++;
        }
        if (isStep3Complete()) {
            count++;
        }
        if (isStep4Complete()) {
            count++;
        }
        if (isStep5Complete()) {
            count++;
        }
        return count;
    }

    // =====================================================
    // GETTERS AND SETTERS
    // =====================================================
    // Step 1: Customer Information
    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public String getClientName() {
        return clientName.get();
    }

    public StringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName.set(clientName);
    }

    public String getClientEmail() {
        return clientEmail.get();
    }

    public StringProperty clientEmailProperty() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail.set(clientEmail);
    }

    public String getClientContact() {
        return clientContact.get();
    }

    public StringProperty clientContactProperty() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact.set(clientContact);
    }

    public String getClientDestination() {
        return clientDestination.get();
    }

    public StringProperty clientDestinationProperty() {
        return clientDestination;
    }

    public void setClientDestination(String clientDestination) {
        this.clientDestination.set(clientDestination);
    }

    public String getTravelType() {
        return travelType.get();
    }

    public StringProperty travelTypeProperty() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType.set(travelType);
    }

    public int getPaxCount() {
        return paxCount.get();
    }

    public IntegerProperty paxCountProperty() {
        return paxCount;
    }

    public void setPaxCount(int paxCount) {
        this.paxCount.set(paxCount);
    }

    public boolean isExistingClient() {
        return isExistingClient.get();
    }

    public BooleanProperty isExistingClientProperty() {
        return isExistingClient;
    }

    public void setExistingClient(boolean isExisting) {
        this.isExistingClient.set(isExisting);
    }

    // Step 2: Package Selection
    public int getSelectedPackageId() {
        return selectedPackageId.get();
    }

    public IntegerProperty selectedPackageIdProperty() {
        return selectedPackageId;
    }

    public void setSelectedPackageId(int packageId) {
        this.selectedPackageId.set(packageId);
    }

    public String getSelectedPackageName() {
        return selectedPackageName.get();
    }

    public StringProperty selectedPackageNameProperty() {
        return selectedPackageName;
    }

    public void setSelectedPackageName(String packageName) {
        this.selectedPackageName.set(packageName);
    }

    public int getPackagePrice() {
        return packagePrice.get();
    }

    public IntegerProperty packagePriceProperty() {
        return packagePrice;
    }

    public void setPackagePrice(int price) {
        this.packagePrice.set(price);
    }

    public String getPackageDestination() {
        return packageDestination.get();
    }

    public StringProperty packageDestinationProperty() {
        return packageDestination;
    }

    public void setPackageDestination(String destination) {
        this.packageDestination.set(destination);
    }

    public int getPackageDuration() {
        return packageDuration.get();
    }

    public IntegerProperty packageDurationProperty() {
        return packageDuration;
    }

    public void setPackageDuration(int duration) {
        this.packageDuration.set(duration);
    }

    // Step 3: Customization / Add-ons
    public boolean isIncludeBreakfast() {
        return includeBreakfast.get();
    }

    public BooleanProperty includeBreakfastProperty() {
        return includeBreakfast;
    }

    public void setIncludeBreakfast(boolean include) {
        this.includeBreakfast.set(include);
    }

    public boolean isIncludeInsurance() {
        return includeInsurance.get();
    }

    public BooleanProperty includeInsuranceProperty() {
        return includeInsurance;
    }

    public void setIncludeInsurance(boolean include) {
        this.includeInsurance.set(include);
    }

    public boolean isIncludeGuide() {
        return includeGuide.get();
    }

    public BooleanProperty includeGuideProperty() {
        return includeGuide;
    }

    public void setIncludeGuide(boolean include) {
        this.includeGuide.set(include);
    }

    public boolean isIncludePickup() {
        return includePickup.get();
    }

    public BooleanProperty includePickupProperty() {
        return includePickup;
    }

    public void setIncludePickup(boolean include) {
        this.includePickup.set(include);
    }

    public String getSpecialRequests() {
        return specialRequests.get();
    }

    public StringProperty specialRequestsProperty() {
        return specialRequests;
    }

    public void setSpecialRequests(String requests) {
        this.specialRequests.set(requests);
    }

    // Step 4: Hotel Selection
    public int getSelectedHotelId() {
        return selectedHotelId.get();
    }

    public IntegerProperty selectedHotelIdProperty() {
        return selectedHotelId;
    }

    public void setSelectedHotelId(int hotelId) {
        this.selectedHotelId.set(hotelId);
    }

    public String getSelectedHotelName() {
        return selectedHotelName.get();
    }

    public StringProperty selectedHotelNameProperty() {
        return selectedHotelName;
    }

    public void setSelectedHotelName(String hotelName) {
        this.selectedHotelName.set(hotelName);
    }

    public int getHotelPrice() {
        return hotelPrice.get();
    }

    public IntegerProperty hotelPriceProperty() {
        return hotelPrice;
    }

    public void setHotelPrice(int price) {
        this.hotelPrice.set(price);
    }

    public String getHotelRating() {
        return hotelRating.get();
    }

    public StringProperty hotelRatingProperty() {
        return hotelRating;
    }

    public void setHotelRating(String rating) {
        this.hotelRating.set(rating);
    }

    // Step 5: Vehicle Selection
    public int getSelectedVehicleId() {
        return selectedVehicleId.get();
    }

    public IntegerProperty selectedVehicleIdProperty() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(int vehicleId) {
        this.selectedVehicleId.set(vehicleId);
    }

    public String getSelectedVehicleName() {
        return selectedVehicleName.get();
    }

    public StringProperty selectedVehicleNameProperty() {
        return selectedVehicleName;
    }

    public void setSelectedVehicleName(String vehicleName) {
        this.selectedVehicleName.set(vehicleName);
    }

    public String getVehicleType() {
        return vehicleType.get();
    }

    public StringProperty vehicleTypeProperty() {
        return vehicleType;
    }

    public void setVehicleType(String type) {
        this.vehicleType.set(type);
    }

    public int getVehiclePrice() {
        return vehiclePrice.get();
    }

    public IntegerProperty vehiclePriceProperty() {
        return vehiclePrice;
    }

    public void setVehiclePrice(int price) {
        this.vehiclePrice.set(price);
    }

    // Step 6: Confirmation
    public boolean isTermsAccepted() {
        return termsAccepted.get();
    }

    public BooleanProperty termsAcceptedProperty() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean accepted) {
        this.termsAccepted.set(accepted);
    }

    public LocalDate getBookingDate() {
        return bookingDate.get();
    }

    public ObjectProperty<LocalDate> bookingDateProperty() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate date) {
        this.bookingDate.set(date);
    }

    public int getCompletedSteps() {
        return completedSteps.get();
    }

    public IntegerProperty completedStepsProperty() {
        return completedSteps;
    }

    public void setCompletedSteps(int steps) {
        this.completedSteps.set(steps);
    }
}
