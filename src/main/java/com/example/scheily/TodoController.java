package com.example.scheily;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;

/**
 * The Controller handles all application logic, state management,
 * and data manipulation for the Todo application.
 * It manages the ObservableList of Tasks.
 */
public class TodoController {

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();
    private final Map<String, String> colorMap = createColorMap();
    private String selectedColor = colorMap.get("Red");

    // --- Getters for View access ---
    public ObservableList<Task> getTaskList() {
        return taskList;
    }

    public Map<String, String> getColorMap() {
        return colorMap;
    }

    public String getSelectedColor() {
        return selectedColor;
    }

    // --- Mutators for View interaction ---
    public void setSelectedColor(String hex) {
        this.selectedColor = hex;
    }

    /**
     * Utility method to create a map of color names to hex codes.
     */
    private Map<String, String> createColorMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Red", "#F87171");
        map.put("Lime", "#4ADE80");
        map.put("Blue", "#60A5FA");
        map.put("Yellow", "#FACC15");
        return map;
    }

    /**
     * Adds a new task to the list based on modal input fields, called by the View's submit action.
     */
    public void addTask(TextField titleField, TextArea descriptionArea, ComboBox<String> statusCombo, VBox inputModalContainer) {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String status = statusCombo.getValue();

        if (!title.isEmpty()) {
            Task newTask = new Task(title, description, status, selectedColor);
            taskList.add(newTask);

            // Reset fields and hide modal (This is a cross-concern, but necessary for clean UI flow)
            titleField.clear();
            descriptionArea.clear();
            statusCombo.setValue("To-do");
            inputModalContainer.setVisible(false);
        } else {
            // Show an alert if validation fails
            Alert alert = new Alert(Alert.AlertType.WARNING, "Title cannot be empty!", ButtonType.OK);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Deletes a specific task from the list, called by the TaskCell's delete button action.
     */
    public void deleteTask(Task task) {
        if (task != null) {
            taskList.remove(task);
        }
    }
}