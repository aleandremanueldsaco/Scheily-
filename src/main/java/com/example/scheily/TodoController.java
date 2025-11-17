package com.example.scheily;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Import Button
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class TodoController {

    @FXML
    private ListView<ToDoItem> todoListView;

    @FXML
    public void initialize() {
        todoListView.setItems(InputController.getTodoList());

        todoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> listView) {
                return new ToDoListCell();
            }
        });
    }

    @FXML
    private void openInputView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addcontent.fxml"));
            VBox inputRoot = loader.load();

            Stage inputStage = new Stage();
            inputStage.setTitle("Add New To-Do Item");
            inputStage.setScene(new Scene(inputRoot));

            inputStage.initModality(Modality.WINDOW_MODAL);
            inputStage.show();

        } catch (IOException e) {
            System.err.println("Error loading InputView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshList() {
        todoListView.refresh();
    }

    // --- Custom Cell Implementation for the List View ---

    private static class ToDoListCell extends ListCell<ToDoItem> {
        private final VBox itemLayout;
        private final HBox headerBox;
        private final CheckBox checkBox;
        private final Label titleLabel;
        private final Label statusLabel;
        private final Label descriptionLabel;
        private final Button deleteButton; // NEW: Delete Button

        public ToDoListCell() {
            // 1. Initialize Components
            checkBox = new CheckBox();
            titleLabel = new Label();
            statusLabel = new Label();
            descriptionLabel = new Label();

            // NEW: Initialize and style the Delete Button
            deleteButton = new Button("X"); // Use 'X' or an icon
            deleteButton.setStyle("-fx-font-size: 10px; -fx-padding: 2 5; -fx-background-color: #f44336; -fx-text-fill: white;");

            // Set up styles for other components
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            descriptionLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
            descriptionLabel.setWrapText(true);

            // 2. Setup the Header (Checkbox, Title, Status, Delete Button)
            headerBox = new HBox(10);
            headerBox.getChildren().addAll(checkBox, titleLabel, statusLabel);
            headerBox.getChildren().add(deleteButton); // Add delete button to the header
            HBox.setHgrow(titleLabel, Priority.ALWAYS); // Title takes space
            headerBox.setStyle("-fx-alignment: center-left;");

            // 3. Setup the Main Layout (Header, Description)
            itemLayout = new VBox(5);
            itemLayout.getChildren().addAll(headerBox, descriptionLabel);
            itemLayout.setStyle("-fx-padding: 8px 10px;");

            // 4. Handle CheckBox Interaction (Mark as complete)
            checkBox.setOnAction(e -> {
                if (getItem() != null) {
                    toggleCompletedStyle(checkBox.isSelected());
                }
            });

            // 5. NEW: Handle Delete Button Action
            deleteButton.setOnAction(event -> {
                // Get the item associated with this cell
                ToDoItem itemToRemove = getItem();
                if (itemToRemove != null) {
                    // Remove the item from the shared ObservableList
                    boolean wasRemoved = InputController.getTodoList().remove(itemToRemove);
                    if (wasRemoved) {
                        System.out.println("Removed item: " + itemToRemove.getTitle());
                        // The ListView automatically updates because it's bound to the ObservableList.
                    }
                }
            });
        }

        private void toggleCompletedStyle(boolean isCompleted) {
            String strikeStyle = isCompleted ? "-fx-strikethrough: true; -fx-text-fill: gray;" : "-fx-strikethrough: false; -fx-text-fill: black;";
            titleLabel.setStyle(titleLabel.getStyle().replace("-fx-strikethrough: true;", "").replace("-fx-strikethrough: false;", "") + strikeStyle);
            descriptionLabel.setStyle(descriptionLabel.getStyle().replace("-fx-strikethrough: true;", "").replace("-fx-strikethrough: false;", "") + strikeStyle);
            statusLabel.setStyle(statusLabel.getStyle().replace("-fx-strikethrough: true;", "").replace("-fx-strikethrough: false;", "") + strikeStyle);
        }

        @Override
        protected void updateItem(ToDoItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                setStyle("");
            } else {
                // 1. Update Content
                titleLabel.setText(item.getTitle());
                descriptionLabel.setText(item.getDescription());
                statusLabel.setText("[" + item.getStatus() + "]");

                // 2. Apply ColorPicker color to the background
                Color color = item.getColor();
                String hexColor = String.format("#%02X%02X%02X",
                        (int)(color.getRed() * 255),
                        (int)(color.getGreen() * 255),
                        (int)(color.getBlue() * 255));

                setStyle("-fx-background-color: " + hexColor + "22; -fx-border-color: " + hexColor + "; -fx-border-width: 0 0 0 4;");

                // 3. Reset or apply completion styles
                checkBox.setSelected(false);
                toggleCompletedStyle(false);

                setGraphic(itemLayout);
            }
        }
    }
}