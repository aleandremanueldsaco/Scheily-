package com.example.scheily;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

public class AddactivityController {

    @FXML private TextField titleField;
    @FXML private TextField eventField;
    @FXML private TextField locationField;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private ComboBox<String> startAmPmBox;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private ComboBox<String> endAmPmBox;
    @FXML private DatePicker datePicker;
    @FXML private ColorPicker colorPicker;

    private calendarController calendarController;
    private weeklyController weeklyController;
    private dayController dailyController; // 🔑 NEW: Reference for the daily view

    @FXML
    public void initialize() {
        // Time Spinners (1-12 hour clock)
        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 8));
        startMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 9));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        startAmPmBox.getItems().addAll("AM", "PM");
        endAmPmBox.getItems().addAll("AM", "PM");
        startAmPmBox.setValue("AM");
        endAmPmBox.setValue("AM");

        // Default color
        colorPicker.setValue(Color.web("#7ed6ff"));

        // Default date to today
        datePicker.setValue(LocalDate.now());
    }

    // ===================== Controller Setters =====================
    public void setCalendarController(calendarController controller) {
        this.calendarController = controller;
    }
    public void setWeeklyController(weeklyController controller) {
        this.weeklyController = controller;
    }
    public void setDailyController(dayController controller) {
        this.dailyController = controller;
    }

    // ===================== Handle Submission =====================
    @FXML
    private void handleSubmit() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Please enter an activity title.");
            return;
        }

        LocalDate date = datePicker.getValue();
        if (date == null) {
            showAlert("Please select a date.");
            return;
        }

        // --- Calculate Start Time ---
        int startHour12 = startHourSpinner.getValue();
        int startMinute = startMinuteSpinner.getValue();
        int startHour24 = to24Hour(startHour12, startAmPmBox.getValue());

        ZonedDateTime startTime = ZonedDateTime.now()
                .withYear(date.getYear())
                .withMonth(date.getMonthValue())
                .withDayOfMonth(date.getDayOfMonth())
                .withHour(startHour24)
                .withMinute(startMinute)
                .withSecond(0);

        // --- Calculate End Time (Not used in your CalendarActivity yet, but good practice) ---
        int endHour12 = endHourSpinner.getValue();
        int endMinute = endMinuteSpinner.getValue();
        int endHour24 = to24Hour(endHour12, endAmPmBox.getValue());

        ZonedDateTime endTime = startTime
                .withHour(endHour24)
                .withMinute(endMinute);

        // Basic validation: ensure end time is not before start time
        if (endTime.isBefore(startTime) && !endTime.with(ChronoField.DAY_OF_YEAR, endTime.getDayOfYear() + 1).isAfter(startTime)) {
            showAlert("End time must be after the start time.");
            return;
        }

        // --- Convert Color to Hex ---
        Color selected = colorPicker.getValue();
        String colorHex = String.format("#%02X%02X%02X",
                (int) (selected.getRed() * 255),
                (int) (selected.getGreen() * 255),
                (int) (selected.getBlue() * 255)
        );

        // --- Create Activity ---
        // NOTE: We are using titleField for clientName/title as per your previous implementation
        CalendarActivity newActivity = new CalendarActivity(
                startTime,
                titleField.getText(),
                100001, // arbitrary ID
                colorHex
        );

        // --- 🔑 Key Change: Pass Activity to ALL active controllers ---
        // Your controllers share the same List<CalendarActivity> and add the event.
        if (calendarController != null) {
            calendarController.addActivity(newActivity);
        }
        if (weeklyController != null) {
            weeklyController.addActivity(newActivity);
        }
        if (dailyController != null) {
            dailyController.addActivity(newActivity);
        }



        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    // Helper to convert 12-hour clock to 24-hour clock
    private int to24Hour(int hour12, String amPm) {
        if (amPm.equals("PM") && hour12 != 12) return hour12 + 12;
        if (amPm.equals("AM") && hour12 == 12) return 0;
        return hour12;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}