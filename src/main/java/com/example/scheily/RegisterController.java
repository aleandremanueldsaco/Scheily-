package com.example.scheily;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNamefield;

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField birthDatefield;

    @FXML
    private TextField courseField;

    @FXML
    private TextField yearLevelField;

    // ✅ Newly added fields (excluded from onRegister() logic)
    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    void onRegister(ActionEvent event) throws IOException {
        boolean valid = validateInputs();
        if (!valid) return;

        // These fields will be sent to the dashboard
        String firstName = firstNameField.getText();
        String lastName = lastNamefield.getText();
        String studentId = studentIdField.getText();
        String birthdate = birthDatefield.getText();
        String course = courseField.getText();
        String yearLevel = yearLevelField.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = loader.load();

        dashboardController profileController = loader.getController();
        profileController.setUserData(firstName, lastName, studentId, birthdate, course, yearLevel);

        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-page.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void initialize() {
        // Change prompt text on hover
        birthDatefield.setOnMouseEntered(event -> birthDatefield.setPromptText("MM/DD/YYYY"));
        birthDatefield.setOnMouseExited(event -> birthDatefield.setPromptText("Birthday..."));
    }

    // ✅ Validation method
    private boolean validateInputs() {
        boolean valid = true;

        // Reset styles
        resetStyles();

        // --- First Name ---
        if (firstNameField.getText().isEmpty()) {
            setInvalid(firstNameField);
            valid = false;
        } else {
            setValid(firstNameField);
        }

        // --- Last Name ---
        if (lastNamefield.getText().isEmpty()) {
            setInvalid(lastNamefield);
            valid = false;
        } else {
            setValid(lastNamefield);
        }

        // --- Student ID (numeric only) ---
        if (!studentIdField.getText().matches("\\d+")) {
            setInvalid(studentIdField);
            valid = false;
        } else {
            setValid(studentIdField);
        }

        // --- Birthdate (MM/DD/YYYY) ---
        if (!birthDatefield.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
            setInvalid(birthDatefield);
            valid = false;
        } else {
            setValid(birthDatefield);
        }

        // --- Course ---
        if (courseField.getText().isEmpty()) {
            setInvalid(courseField);
            valid = false;
        } else {
            setValid(courseField);
        }

        // --- Year Level ---
        if (yearLevelField.getText().isEmpty()) {
            setInvalid(yearLevelField);
            valid = false;
        } else {
            setValid(yearLevelField);
        }

        // --- Email (must be valid format) ---
        if (!emailField.getText().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            setInvalid(emailField);
            valid = false;
        } else {
            setValid(emailField);
        }

        // --- Username ---
        if (usernameField.getText().isEmpty()) {
            setInvalid(usernameField);
            valid = false;
        } else {
            setValid(usernameField);
        }

        // --- Password (at least 6 characters) ---
        if (passwordField.getText().length() < 6) {
            setInvalid(passwordField);
            valid = false;
        } else {
            setValid(passwordField);
        }

        return valid;
    }

    private void resetStyles() {
        firstNameField.setStyle("");
        lastNamefield.setStyle("");
        studentIdField.setStyle("");
        birthDatefield.setStyle("");
        courseField.setStyle("");
        yearLevelField.setStyle("");
        emailField.setStyle("");
        usernameField.setStyle("");
        passwordField.setStyle("");
    }

    private void setInvalid(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-background-color: #ffe6e6;");
    }

    private void setValid(TextField field) {
        field.setStyle("-fx-border-color: green; -fx-border-width: 2; -fx-background-color: #e6ffe6;");
    }
}
