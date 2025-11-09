package com.example.scheily;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ToDoListController {

    @FXML
    private TextField taskInput;

    @FXML
    private VBox todoListContainer;

    @FXML
    private Button addTaskButton;


    @FXML
    private void addTask() {
        String taskText = taskInput.getText().trim();

        if (taskText.isEmpty()) {
            showAlert("Please enter a task before adding.");
            return;
        }

        HBox taskItem = createTaskItem(taskText);
        todoListContainer.getChildren().add(taskItem);
        taskInput.clear();
    }


    private HBox createTaskItem(String taskText) {
        CheckBox checkBox = new CheckBox();
        Label taskLabel = new Label(taskText);
        taskLabel.getStyleClass().add("task-label");
        Button editButton = new Button("✎");
        Button deleteButton = new Button("🗑");

        HBox.setHgrow(taskLabel, Priority.ALWAYS);


        deleteButton.setOnAction(e -> todoListContainer.getChildren().removeIf(node -> node == editButton.getParent()));


        editButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(taskLabel.getText());
            dialog.setTitle("Edit Task");
            dialog.setHeaderText("Edit your task:");
            dialog.setContentText("Task:");
            dialog.showAndWait().ifPresent(taskLabel::setText);
        });


        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                taskLabel.getStyleClass().add("completed-task");
            } else {
                taskLabel.getStyleClass().remove("completed-task");
            }
        });

        HBox taskItem = new HBox(10, checkBox, taskLabel, editButton, deleteButton);
        taskItem.getStyleClass().add("task-item");
        return taskItem;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
