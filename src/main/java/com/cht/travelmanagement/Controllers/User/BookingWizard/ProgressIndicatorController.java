package com.cht.travelmanagement.Controllers.User.BookingWizard;

import com.cht.travelmanagement.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressIndicatorController implements Initializable {

    public VBox step1_indicator;
    public VBox step2_indicator;
    public VBox step3_indicator;
    public VBox step4_indicator;
    public VBox step5_indicator;
    public VBox step6_indicator;

    // Colors
    private final Color COLOR_ACTIVE = Color.web("#007bff");
    private final Color COLOR_COMPLETED = Color.web("#28a745");
    private final Color COLOR_INACTIVE = Color.web("#E0E0E0");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        int currentStep = Model.getInstance().getUserViewFactory().getBookingStep().get();
        updateProgress(currentStep);


        Model.getInstance().getUserViewFactory().getBookingStep().addListener((obs, oldVal, newVal) -> {
            updateProgress(newVal.intValue());
        });
    }

    private void updateProgress(int currentStep) {
        updateSingleStep(step1_indicator, 1, currentStep);
        updateSingleStep(step2_indicator, 2, currentStep);
        updateSingleStep(step3_indicator, 3, currentStep);
        updateSingleStep(step4_indicator, 4, currentStep);
        updateSingleStep(step5_indicator, 5, currentStep);
        updateSingleStep(step6_indicator, 6, currentStep);
    }

    private void updateSingleStep(VBox indicator, int stepNumber, int currentStep) {
        StackPane stack = (StackPane) indicator.getChildren().get(0);
        Circle circle = (Circle) stack.getChildren().get(0);
        Label label = (Label) stack.getChildren().get(1);

        if (stepNumber < currentStep) {

            circle.setFill(COLOR_ACTIVE);
            circle.setStroke(COLOR_ACTIVE);
            label.setText("✓");
        }
        else if (stepNumber == currentStep) {
            circle.setFill(COLOR_ACTIVE);
            circle.setStroke(COLOR_ACTIVE);
            label.setText(String.valueOf(stepNumber)); // Keep number
        }
        else {
            circle.setFill(COLOR_INACTIVE);
            circle.setStroke(COLOR_INACTIVE);
            label.setText(String.valueOf(stepNumber)); // Keep number
        }
    }
}