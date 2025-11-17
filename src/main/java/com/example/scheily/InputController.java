package com.example.scheily;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Import VBox to get the parent container
import javafx.scene.paint.Color;
import javafx.stage.Stage; // Import Stage

public class InputController {

    private static final ObservableList<ToDoItem> todoList = FXCollections.observableArrayList();

    @FXML
    private TextField titleInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ColorPicker colorPicker;

    // The messageLabel has been removed from the class fields.

    // We add the root container to access the Stage
    @FXML
    private VBox rootPane;

    /**
     * Initializes the controller. Sets up the ComboBox with options.
     */
    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll(
                "Pending",
                "In Progress",
                "Completed",
                "On Hold"
        );
        statusComboBox.getSelectionModel().select("Pending");
        colorPicker.setValue(Color.web("#ADD8E6"));
    }

    /**
     * Called when the "Add To-Do" button is clicked.
     */
    @FXML
    private void addItem(ActionEvent event) {
        String title = titleInput.getText().trim();
        String description = descriptionInput.getText().trim();
        String status = statusComboBox.getValue();
        Color color = colorPicker.getValue();

        if (title.isEmpty() || description.isEmpty() || status == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing To-Do Details");
            alert.setContentText("Please ensure you have entered a **Title**, a **Description**, and selected a **Status** before adding the item.");
            alert.showAndWait();
            return;
        }

        // 1. Create the new item
        ToDoItem newItem = new ToDoItem(title, description, status, color);

        // 2. Add it to the shared list
        todoList.add(newItem);

        // 3. Clear inputs (Optional if closing, but good practice)
        titleInput.clear();
        descriptionInput.clear();
        statusComboBox.getSelectionModel().select("Pending");
        colorPicker.setValue(Color.web("#ADD8E6"));

        // 4. Close the current window (Stage)
        // Get the Stage from any node in the current scene graph
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

        System.out.printf("Item successfully added and input window closed.%n");
    }

    /**
     * Static method to allow the DisplayController to access the list.
     */
    public static ObservableList<ToDoItem> getTodoList() {
        return todoList;
    }
}