package com.example.scheily;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class AddeventController {

    // --- Modal UI Fields (connected via FXML load) ---
    @FXML private TextField nameField;
    @FXML private TextField codeField;


    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private ComboBox<String> startAmPm;

    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private ComboBox<String> endAmPm;

    // REPLACED MenuButton with ComboBox (Dropdown)
    @FXML private ComboBox<String> dayComboBox;

    @FXML private ColorPicker colorPicker;

    // Helper map to quickly convert ComboBox text (e.g., "Monday") to DayOfWeek enum (MONDAY)
    private final Map<String, DayOfWeek> dayOfWeekMap = new HashMap<>();

    // Callback to pass the new entry back to the main controller
    private Consumer<ScheduleEntry> onSubmit;
    private Stage modalStage;

    /**
     * Sets the stage and the callback function for submission.
     */
    public void setup(Stage stage, Consumer<ScheduleEntry> submitHandler) {
        this.modalStage = stage;
        this.onSubmit = submitHandler;
    }

    @FXML
    public void initialize() {
        // Automatically called after the FXML fields are injected
        initializeModalComponents();

        // Populate the conversion map (e.g., "MONDAY" -> DayOfWeek.MONDAY)
        for (DayOfWeek day : DayOfWeek.values()) {
            // Converts "MONDAY" to "Monday" for the key to match ComboBox text exactly
            String capitalizedName = day.toString().substring(0, 1) + day.toString().substring(1).toLowerCase();
            dayOfWeekMap.put(capitalizedName, day);
        }
    }

    /**
     * Initializes Spinners and ComboBoxes in the modal.
     */
    private void initializeModalComponents() {
        // 1. Initialize AM/PM ComboBoxes
        startAmPm.setItems(FXCollections.observableArrayList("AM", "PM"));
        endAmPm.setItems(FXCollections.observableArrayList("AM", "PM"));
        startAmPm.getSelectionModel().select("AM");
        endAmPm.getSelectionModel().select("AM");

        // 2. Initialize Time Spinners (default values matching original examples)
        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 9));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 10));

        startMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 30, 5));

        // Allow manual input in spinners
        startHourSpinner.setEditable(true);
        startMinuteSpinner.setEditable(true);
        endHourSpinner.setEditable(true);
        endMinuteSpinner.setEditable(true);

        // 3. Initialize ColorPicker (Default color is Pink #FFC0CB)
        colorPicker.setValue(Color.web("#FFC0CB"));

        // 4. Initialize Day ComboBox (Dropdown)
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        dayComboBox.setItems(FXCollections.observableArrayList(days));
        dayComboBox.setPromptText("Select Day");
    }

    // REMOVED: handleDaySelection is no longer needed as ComboBox manages selection automatically.

    /**
     * Handles the 'X' button or general close action on the modal.
     */
    @FXML
    private void closeModal() {
        if (modalStage != null) {
            modalStage.close();
        }
    }

    /**
     * Handles the 'SUBMIT' button click in the modal to add a new schedule entry.
     */
    @FXML
    private void handleEntrySubmit() {
        try {
            // --- Input Validation and Parsing ---
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            // Get value from ComboBox
            String selectedDayText = dayComboBox.getValue();

            // The ComboBox value will be null if the prompt text is showing and nothing is selected
            if (name.isEmpty() || code.isEmpty() || selectedDayText == null) {
                System.err.println("Error: Title, Subject Code, and Day are required.");
                return;
            }

            // Parse time components
            int startH = startHourSpinner.getValue();
            int startM = startMinuteSpinner.getValue();
            int endH = endHourSpinner.getValue();
            int endM = endMinuteSpinner.getValue();

            String startMeridiem = startAmPm.getValue();
            String endMeridiem = endAmPm.getValue();

            // Convert to 24-hour time
            LocalTime startTime = parseTime(startH, startM, startMeridiem);
            LocalTime endTime = parseTime(endH, endM, endMeridiem);

            if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
                System.err.println("Error: Invalid time format or Start Time is not before End Time.");
                return;
            }

            // --- Extract DayOfWeek from the ComboBox text ---
            DayOfWeek day = dayOfWeekMap.get(selectedDayText);

            if (day == null) {
                System.err.println("Error: Could not parse selected day: " + selectedDayText);
                return;
            }

            Color selectedColor = colorPicker.getValue();
            String color = String.format("#%02x%02x%02x",
                    (int) (selectedColor.getRed() * 255),
                    (int) (selectedColor.getGreen() * 255),
                    (int) (selectedColor.getBlue() * 255)).toUpperCase();



            // --- Create Entry and Call Callback ---
            ScheduleEntry newEntry = new ScheduleEntry(name, code, startTime, endTime, day, color);

            if (onSubmit != null) {
                onSubmit.accept(newEntry);
            }

            // --- Close Modal ---
            closeModal();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("An unexpected error occurred during submission: " + e.getMessage());
        }
    }

    /**
     * Helper to convert 12-hour format to LocalTime.
     */
    private LocalTime parseTime(int hour, int minute, String ampm) {
        if (ampm == null) return null;

        int finalHour = hour;
        if (ampm.equals("PM") && hour != 12) {
            finalHour += 12;
        } else if (ampm.equals("AM") && hour == 12) {
            finalHour = 0; // Midnight 12 AM is 00:00
        }

        // Basic check for valid hour range (0-23)
        if (finalHour < 0 || finalHour > 23 || minute < 0 || minute > 59) return null;

        return LocalTime.of(finalHour, minute);
    }
}