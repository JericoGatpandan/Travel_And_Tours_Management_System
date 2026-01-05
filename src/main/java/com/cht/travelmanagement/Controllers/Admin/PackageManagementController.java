package com.cht.travelmanagement.Controllers.Admin;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import com.cht.travelmanagement.Models.Model;
import com.cht.travelmanagement.Models.Repository.Implementation.TourPackageRepositoryImpl;
import com.cht.travelmanagement.Models.Repository.TourPackageRepository;
import com.cht.travelmanagement.Models.TourPackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class PackageManagementController implements Initializable {

    // Header Controls
    @FXML
    public Button addNewBtn;
    @FXML
    public TextField searchField;
    @FXML
    public Button searchBtn;
    @FXML
    public Button refreshBtn;

    // Table
    @FXML
    public TableView<TourPackage> packagesTable;
    @FXML
    public TableColumn<TourPackage, Integer> idColumn;
    @FXML
    public TableColumn<TourPackage, String> nameColumn;
    @FXML
    public TableColumn<TourPackage, String> destinationColumn;
    @FXML
    public TableColumn<TourPackage, String> durationColumn;
    @FXML
    public TableColumn<TourPackage, Integer> maxPaxColumn;
    @FXML
    public TableColumn<TourPackage, String> priceColumn;
    @FXML
    public TableColumn<TourPackage, String> statusColumn;
    @FXML
    public TableColumn<TourPackage, Void> actionsColumn;

    // Form
    @FXML
    public TitledPane formPane;
    @FXML
    public TextField nameField;
    @FXML
    public TextField destinationField;
    @FXML
    public Spinner<Integer> durationSpinner;
    @FXML
    public Spinner<Integer> maxPaxSpinner;
    @FXML
    public TextField priceField;
    @FXML
    public CheckBox isActiveCheckbox;
    @FXML
    public TextArea descriptionArea;
    @FXML
    public TextArea inclusionsArea;
    @FXML
    public Button cancelBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Label formMessageLabel;

    // Image Upload Controls
    @FXML
    public StackPane imageDropZone;
    @FXML
    public VBox dropPlaceholder;
    @FXML
    public ImageView imagePreview;
    @FXML
    public Button browseImageBtn;
    @FXML
    public Button removeImageBtn;
    @FXML
    public Label imageNameLabel;

    private final TourPackageRepository tourPackageRepository;
    private TourPackage selectedPackage = null;
    private boolean isEditMode = false;
    private File selectedImageFile = null;
    private String currentImagePath = null;

    // Path to the Images folder in resources
    private static final String IMAGES_FOLDER = "Images/packages/";

    public PackageManagementController() {
        this.tourPackageRepository = new TourPackageRepositoryImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupForm();
        setupImageUpload();
        loadPackages();
        setupEventHandlers();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().packageIdProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().packageNameProperty());
        destinationColumn.setCellValueFactory(cellData -> cellData.getValue().destinationProperty());
        durationColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getDurationDays() + " days"));
        maxPaxColumn.setCellValueFactory(cellData -> cellData.getValue().maxParticipantsProperty().asObject());
        priceColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty("₱" + String.format("%,d", cellData.getValue().getPrice())));
        statusColumn.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getIsActive() ? "Active" : "Inactive"));

        // Status column styling
        statusColumn.setCellFactory(column -> new TableCell<TourPackage, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Active".equals(item)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Actions column with Edit and Delete buttons
        actionsColumn.setCellFactory(column -> new TableCell<TourPackage, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox actionBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("action-button-edit");
                deleteBtn.getStyleClass().add("action-button-delete");
                actionBox.setAlignment(Pos.CENTER);

                editBtn.setOnAction(event -> {
                    TourPackage pkg = getTableView().getItems().get(getIndex());
                    editPackage(pkg);
                });

                deleteBtn.setOnAction(event -> {
                    TourPackage pkg = getTableView().getItems().get(getIndex());
                    deletePackage(pkg);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionBox);
                }
            }
        });
    }

    private void setupForm() {
        // Setup spinners
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 4));
        maxPaxSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 20));

        // Price field - numbers only
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void setupImageUpload() {
        // Setup drag and drop
        imageDropZone.setOnDragOver(this::handleDragOver);
        imageDropZone.setOnDragDropped(this::handleDragDropped);
        imageDropZone.setOnDragExited(event -> imageDropZone.getStyleClass().remove("drag-over"));

        // Click to browse
        imageDropZone.setOnMouseClicked(event -> browseImage());
        browseImageBtn.setOnAction(event -> browseImage());
        removeImageBtn.setOnAction(event -> removeImage());
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != imageDropZone && event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            if (files.size() == 1 && isImageFile(files.get(0))) {
                event.acceptTransferModes(TransferMode.COPY);
                if (!imageDropZone.getStyleClass().contains("drag-over")) {
                    imageDropZone.getStyleClass().add("drag-over");
                }
            }
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            List<File> files = db.getFiles();
            if (files.size() == 1 && isImageFile(files.get(0))) {
                setSelectedImage(files.get(0));
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
        imageDropZone.getStyleClass().remove("drag-over");
    }

    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Package Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File file = fileChooser.showOpenDialog(imageDropZone.getScene().getWindow());
        if (file != null && isImageFile(file)) {
            setSelectedImage(file);
        }
    }

    private void setSelectedImage(File file) {
        selectedImageFile = file;
        try {
            Image image = new Image(file.toURI().toString());
            imagePreview.setImage(image);
            imagePreview.setVisible(true);
            dropPlaceholder.setVisible(false);
            removeImageBtn.setVisible(true);
            imageNameLabel.setText(file.getName());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image: " + e.getMessage());
        }
    }

    private void removeImage() {
        selectedImageFile = null;
        currentImagePath = null;
        imagePreview.setImage(null);
        imagePreview.setVisible(false);
        dropPlaceholder.setVisible(true);
        removeImageBtn.setVisible(false);
        imageNameLabel.setText("");
    }

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg")
                || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp");
    }

    private String saveImageToResources(File imageFile) {
        if (imageFile == null) {
            return currentImagePath; // Keep existing image if no new one selected
        }

        try {
            // Get the source resources folder path
            URL resourceUrl = getClass().getResource("/Images/");
            if (resourceUrl == null) {
                // Fallback: use src/main/resources/Images directly
                String projectPath = System.getProperty("user.dir");
                Path imagesDir = Path.of(projectPath, "src", "main", "resources", "Images", "packages");
                Files.createDirectories(imagesDir);

                // Generate unique filename
                String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf('.'));
                String uniqueFileName = "pkg_" + UUID.randomUUID().toString().substring(0, 8) + extension;

                Path targetPath = imagesDir.resolve(uniqueFileName);
                Files.copy(imageFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                return IMAGES_FOLDER + uniqueFileName;
            }

            // If we have the resource URL, copy there
            Path imagesDir = Path.of(resourceUrl.toURI()).resolve("packages");
            Files.createDirectories(imagesDir);

            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf('.'));
            String uniqueFileName = "pkg_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            Path targetPath = imagesDir.resolve(uniqueFileName);
            Files.copy(imageFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            return IMAGES_FOLDER + uniqueFileName;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Warning", "Failed to save image: " + e.getMessage());
            return currentImagePath;
        }
    }

    private void setupEventHandlers() {
        addNewBtn.setOnAction(event -> showAddForm());
        searchBtn.setOnAction(event -> searchPackages());
        refreshBtn.setOnAction(event -> loadPackages());
        cancelBtn.setOnAction(event -> hideForm());
        saveBtn.setOnAction(event -> savePackage());

        // Search on enter key
        searchField.setOnAction(event -> searchPackages());
    }

    private void loadPackages() {
        ObservableList<TourPackage> packages = tourPackageRepository.getTourPackages();
        packagesTable.setItems(packages);
        searchField.clear();
    }

    private void searchPackages() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadPackages();
        } else {
            ObservableList<TourPackage> results = tourPackageRepository.searchTourPackages(searchTerm);
            packagesTable.setItems(results);
        }
    }

    private void showAddForm() {
        isEditMode = false;
        selectedPackage = null;
        clearForm();
        formPane.setText("Add New Package");
        formPane.setExpanded(true);
        formMessageLabel.setText("");
    }

    private void editPackage(TourPackage pkg) {
        isEditMode = true;
        selectedPackage = pkg;
        populateForm(pkg);
        formPane.setText("Edit Package - " + pkg.getPackageName());
        formPane.setExpanded(true);
        formMessageLabel.setText("");
    }

    private void deletePackage(TourPackage pkg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Package");
        alert.setHeaderText("Delete " + pkg.getPackageName() + "?");
        alert.setContentText("This action cannot be undone. If the package has bookings, it will be deactivated instead.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = tourPackageRepository.deleteTourPackage(pkg.getPackageId());
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Package deleted/deactivated successfully!");
                    loadPackages();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete package.");
                }
            }
        });
    }

    private void savePackage() {
        if (!validateForm()) {
            return;
        }

        int createdBy = 1; // Default to admin
        if (Model.getInstance().getAuthenticatedUser() != null) {
            createdBy = Model.getInstance().getAuthenticatedUser().getEmployeeId();
        }

        // Save image and get the path
        String imagePath = saveImageToResources(selectedImageFile);

        TourPackage pkg = new TourPackage(
                isEditMode ? selectedPackage.getPackageId() : 0,
                nameField.getText().trim(),
                descriptionArea.getText().trim(),
                destinationField.getText().trim(),
                durationSpinner.getValue(),
                maxPaxSpinner.getValue(),
                inclusionsArea.getText().trim(),
                Integer.parseInt(priceField.getText().trim()),
                isActiveCheckbox.isSelected(),
                createdBy,
                imagePath
        );

        boolean success;
        if (isEditMode) {
            success = tourPackageRepository.updateTourPackage(pkg);
        } else {
            success = tourPackageRepository.createTourPackage(pkg);
        }

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    isEditMode ? "Package updated successfully!" : "Package created successfully!");
            hideForm();
            loadPackages();
        } else {
            formMessageLabel.setText("Error saving package. Please try again.");
            formMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errors.append("• Package name is required\n");
        }
        if (destinationField.getText().trim().isEmpty()) {
            errors.append("• Destination is required\n");
        }
        if (priceField.getText().trim().isEmpty()) {
            errors.append("• Price is required\n");
        }

        if (errors.length() > 0) {
            formMessageLabel.setText(errors.toString());
            formMessageLabel.setStyle("-fx-text-fill: #e74c3c;");
            return false;
        }

        return true;
    }

    private void populateForm(TourPackage pkg) {
        nameField.setText(pkg.getPackageName());
        destinationField.setText(pkg.getDestination());
        durationSpinner.getValueFactory().setValue(pkg.getDurationDays());
        maxPaxSpinner.getValueFactory().setValue(pkg.getMaxParticipants());
        priceField.setText(String.valueOf(pkg.getPrice()));
        isActiveCheckbox.setSelected(pkg.getIsActive());
        descriptionArea.setText(pkg.getDescription());
        inclusionsArea.setText(pkg.getInclusions());

        // Load existing image if available
        currentImagePath = pkg.getImagePath();
        selectedImageFile = null;
        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream("/" + currentImagePath));
                if (image != null && !image.isError()) {
                    imagePreview.setImage(image);
                    imagePreview.setVisible(true);
                    dropPlaceholder.setVisible(false);
                    removeImageBtn.setVisible(true);
                    imageNameLabel.setText(currentImagePath.substring(currentImagePath.lastIndexOf('/') + 1));
                } else {
                    resetImagePreview();
                }
            } catch (Exception e) {
                resetImagePreview();
            }
        } else {
            resetImagePreview();
        }
    }

    private void resetImagePreview() {
        imagePreview.setImage(null);
        imagePreview.setVisible(false);
        dropPlaceholder.setVisible(true);
        removeImageBtn.setVisible(false);
        imageNameLabel.setText("");
    }

    private void clearForm() {
        nameField.clear();
        destinationField.clear();
        durationSpinner.getValueFactory().setValue(4);
        maxPaxSpinner.getValueFactory().setValue(20);
        priceField.clear();
        isActiveCheckbox.setSelected(true);
        descriptionArea.clear();
        inclusionsArea.clear();
        removeImage();
    }

    private void hideForm() {
        formPane.setExpanded(false);
        clearForm();
        isEditMode = false;
        selectedPackage = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
