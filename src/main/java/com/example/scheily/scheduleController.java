package com.example.scheily;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class scheduleController {

    @FXML
    private GridPane scheduleGrid;

    // ====================================================================
    // 1. CONFIGURATION AND INITIAL DATA
    // ====================================================================

    // ObservableList allows dynamic additions and observes changes to automatically refresh the grid
    private final ObservableList<ScheduleEntry> scheduleData = FXCollections.observableArrayList(
            // OOP (Pink: #FFC0CB)
            new ScheduleEntry("OOP", "WAC 203", LocalTime.of(9, 0), LocalTime.of(10, 30), DayOfWeek.MONDAY, "#FFC0CB"),
            new ScheduleEntry("OOP", "WAC 203", LocalTime.of(10, 30), LocalTime.of(12, 0), DayOfWeek.WEDNESDAY, "#FFC0CB"),
            new ScheduleEntry("OOP", "WAC 203", LocalTime.of(9, 0), LocalTime.of(10, 30), DayOfWeek.THURSDAY, "#FFC0CB"),
            new ScheduleEntry("OOP", "WAC 203", LocalTime.of(10, 30), LocalTime.of(12, 0), DayOfWeek.FRIDAY, "#FFC0CB"),

            // DSA (Light Blue: #87CEEB)
            new ScheduleEntry("DSA", "WAC 301", LocalTime.of(9, 0), LocalTime.of(10, 30), DayOfWeek.TUESDAY, "#87CEEB"),
            new ScheduleEntry("DSA", "WAC 301", LocalTime.of(9, 0), LocalTime.of(10, 30), DayOfWeek.FRIDAY, "#87CEEB"),
            new ScheduleEntry("DSA", "WAC 301", LocalTime.of(15, 0), LocalTime.of(16, 30), DayOfWeek.FRIDAY, "#87CEEB"), // 3:00 PM

            // ART APP (Purple: #9370DB)
            new ScheduleEntry("ART APP", "TBA", LocalTime.of(10, 30), LocalTime.of(12, 0), DayOfWeek.MONDAY, "#9370DB"),
            new ScheduleEntry("ART APP", "TBA", LocalTime.of(10, 30), LocalTime.of(12, 0), DayOfWeek.THURSDAY, "#9370DB"),

            // PLATFORM TECH (Light Green: #90EE90)
            new ScheduleEntry("PLATFORM TECH", "WAC 301", LocalTime.of(15, 0), LocalTime.of(16, 30), DayOfWeek.WEDNESDAY, "#90EE90"), // 3:00 PM

            // PATHFIT (Gold/Yellow: #FFD700)
            new ScheduleEntry("PATHFIT", "TBA", LocalTime.of(13, 0), LocalTime.of(15, 0), DayOfWeek.SUNDAY, "#FFD700") // 1:00 PM
    );

    private static final LocalTime SCHEDULE_START = LocalTime.of(7, 0); // Start time 7:00 AM
    private static final LocalTime SCHEDULE_END = LocalTime.of(17, 0); // End time 5:00 PM
    private static final int TIME_SLOT_MINUTES = 30; // 30-minute intervals
    private static final int DAYS_IN_WEEK = 7;

    // ====================================================================
    // 2. INITIALIZATION & GRID POPULATION
    // ====================================================================

    @FXML
    public void initialize() {
        // Listen for changes in the schedule data list and refresh the grid
        scheduleData.addListener((javafx.collections.ListChangeListener<ScheduleEntry>) change -> populateScheduleGrid());

        // --- ADDED: Set Column Constraints to ensure uniform width ---
        setupGridConstraints();

        populateScheduleGrid();
    }

    /**
     * Sets up uniform column widths for the schedule grid.
     * Column 0 (Time) gets a fixed, smaller width. Columns 1-7 (Days) share the remaining width equally.
     */
    private void setupGridConstraints() {
        scheduleGrid.getColumnConstraints().clear();

        // Column 0: Time labels (Fixed width)
        ColumnConstraints timeColumn = new ColumnConstraints();
        timeColumn.setPercentWidth(10.0); // Allocate 10% of width for time labels
        timeColumn.setHgrow(Priority.ALWAYS);
        scheduleGrid.getColumnConstraints().add(timeColumn);

        // Columns 1-7: Days (Share remaining 90% equally)
        double dayWidth = 90.0 / DAYS_IN_WEEK;
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            ColumnConstraints dayColumn = new ColumnConstraints();
            dayColumn.setPercentWidth(dayWidth); // Share 90% equally among 7 days
            dayColumn.setHgrow(Priority.ALWAYS);
            dayColumn.setHalignment(HPos.CENTER);
            scheduleGrid.getColumnConstraints().add(dayColumn);
        }
    }


    /**
     * Creates the main schedule grid structure, including time labels and day headers,
     * and then places class blocks based on the scheduleData.
     */
    private void populateScheduleGrid() {
        scheduleGrid.getChildren().clear(); // Clear existing content before redrawing

        // Setup Grid appearance
        // --- CHANGE: Disable global grid lines so we can control borders manually ---
        scheduleGrid.setGridLinesVisible(false);
        scheduleGrid.setHgap(0);
        scheduleGrid.setVgap(0);

        // --- Add Day Headers (Row 0, Columns 1-7) ---
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.getStyleClass().add("day-label");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            // --- ADDED: Apply border to day headers ---
            dayLabel.getStyleClass().add("day-header-cell");
            scheduleGrid.add(dayLabel, i + 1, 0); // +1 offset for the time column

        }

        // --- Add Time Labels and Grid Cells ---
        LocalTime currentTime = SCHEDULE_START;

        // Use a formatter that only shows the hour and AM/PM
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("h:mm a");
        int rowCount = 1; // Start from row 1 (Row 0 is for day headers)

        while (currentTime.isBefore(SCHEDULE_END)) {

            // Only show the label on the hour (e.g., 7:00 am, 8:00 am)
            if (currentTime.getMinute() == 0) {
                Label timeLabel = new Label(currentTime.format(hourFormatter).replace(":00", ""));
                timeLabel.getStyleClass().add("time-label");

                // Set vertical alignment to BASELINE. This pulls the text up
                // so the baseline (bottom) of the text sits just above the grid line.
                GridPane.setValignment(timeLabel, javafx.geometry.VPos.BASELINE);

                timeLabel.setPadding(new Insets(0, 5, 0, 5));
                timeLabel.setAlignment(Pos.BOTTOM_RIGHT);
                timeLabel.setMaxWidth(Double.MAX_VALUE);

                scheduleGrid.add(timeLabel, 0, rowCount);
            }

            // Add the background grid cells for all 7 days (visual separation)
            for (int i = 0; i < DAYS_IN_WEEK; i++) {
                VBox cell = new VBox();
                cell.getStyleClass().add("grid-cell");
                // --- ADDED: Apply border to day cells ---
                cell.getStyleClass().add("day-grid-cell");
                scheduleGrid.add(cell, i + 1, rowCount);
            }

            currentTime = currentTime.plusMinutes(TIME_SLOT_MINUTES);
            rowCount++;
        }

        // --- Place Class Blocks from Data ---
        for (ScheduleEntry entry : scheduleData) {
            int dayColumn = entry.day().getValue();

            long minutesFromStart = SCHEDULE_START.until(entry.startTime(), java.time.temporal.ChronoUnit.MINUTES);
            int startRow = (int) (minutesFromStart / TIME_SLOT_MINUTES) + 1;

            long durationMinutes = entry.startTime().until(entry.endTime(), java.time.temporal.ChronoUnit.MINUTES);
            int rowSpan = (int) (durationMinutes / TIME_SLOT_MINUTES);

            VBox classBlock = createClassBlock(entry);
            scheduleGrid.add(classBlock, dayColumn, startRow, 1, rowSpan);
            GridPane.setMargin(classBlock, new Insets(1));
        }
    }

    /**
     * Creates the visual VBox element containing the class details.
     */
    private VBox createClassBlock(ScheduleEntry entry) {
        Label nameLabel = new Label(entry.name());
        nameLabel.getStyleClass().add("class-name");

        Label codeLabel = new Label(entry.code());

        // Format time to match the style: 9:00am - 10:30am
        DateTimeFormatter blockTimeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        Label timeLabel = new Label(entry.startTime().format(blockTimeFormatter).toLowerCase().replace(" ", "") +
                " - " + entry.endTime().format(blockTimeFormatter).toLowerCase().replace(" ", ""));

        VBox block = new VBox(0, nameLabel, codeLabel, timeLabel);
        block.getStyleClass().add("class-block");
        block.setAlignment(Pos.TOP_LEFT);

        // Apply dynamic styling based on the entry's colorHex
        Color color = Color.web(entry.colorHex());

        // Set Border Color and Background Color
        String darkerColor = color.darker().toString().substring(2, 8).toUpperCase(); // Get hex without opacity

        block.setStyle("-fx-background-color: " + entry.colorHex() + ";"
                + "-fx-border-color: #" + darkerColor + ";"
                + "-fx-border-width: 1px;" // Explicit border width
        );

        return block;
    }

    // ====================================================================
    // 3. MODAL CONTROL
    // ====================================================================

    /**
     * Handles the 'Add Meeting' button click to open the modal.
     */
    @FXML
    private void openModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Addevents.fxml"));
            VBox modalContent = loader.load();

            // CRITICAL: Get the new controller and set it up
            AddeventController addEventController = loader.getController();

            // Create the modal stage
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(scheduleGrid.getScene().getWindow());
            modalStage.setTitle("Add New Class Entry");
            modalStage.setScene(new Scene(modalContent));

            // CRITICAL: Pass the stage and a submission callback to the new controller
            addEventController.setup(modalStage, this::addNewEntryToSchedule);

            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Addevents.fxml: " + e.getMessage());
        }
    }

    /**
     * Callback method executed by AddEventController upon successful submission.
     */
    private void addNewEntryToSchedule(ScheduleEntry newEntry) {
        scheduleData.add(newEntry);
        // The listener in initialize() handles the grid repaint automatically
    }
}