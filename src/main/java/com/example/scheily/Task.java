package com.example.scheily;

import java.util.UUID;

/**
 * Represents a single detailed to-do item with title, description,
 * status, and a user-selected color.
 */
public class Task {
    private final String id;
    private final String title;
    private final String description;
    private final String status; // e.g., "To-do", "In Progress", "Done"
    private final String colorHex; // Hex code for the accent color

    public Task(String title, String description, String status, String colorHex) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = status;
        this.colorHex = colorHex;
    }

    // --- Getters for Task properties ---
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getColorHex() { return colorHex; }

    @Override
    public String toString() {
        return title;
    }
}
