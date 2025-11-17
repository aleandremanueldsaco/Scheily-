package com.example.scheily;

import javafx.scene.paint.Color; // Import JavaFX Color

public class ToDoItem {
    private String title;
    private String description; // New field
    private String status;      // New field (e.g., "Pending", "In Progress", "Completed")
    private Color color;        // New field

    public ToDoItem(String title, String description, String status, Color color) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.color = color;
    }

    // --- Getters and Setters ---
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public Color getColor() { return color; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(String status) { this.status = status; }
    public void setColor(Color color) { this.color = color; }
}