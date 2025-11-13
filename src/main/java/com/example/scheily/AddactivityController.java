package com.example.scheily;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.ZonedDateTime;

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
    @FXML private ColorPicker colorPicker; // 🎨 replaced color buttons

    private calendarController calendarController;
    private weeklyController weeklyController;

    @FXML
    public void initialize() {
        // Time Spinners
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
    }

    public void setCalendarController(calendarController controller) {
        this.calendarController = controller;
    }
    public void setCalendarController(weeklyController controller) {
        this.weeklyController = controller;
    }

    @FXML
    private void handleSubmit() {
        if (calendarController == null) return;

        LocalDate date = datePicker.getValue();
        if (date == null) {
            showAlert("Please select a date.");
            return;
        }

        int startHour = startHourSpinner.getValue();
        int startMinute = startMinuteSpinner.getValue();

        if (startAmPmBox.getValue().equals("PM") && startHour != 12) startHour += 12;
        if (startAmPmBox.getValue().equals("AM") && startHour == 12) startHour = 0;

        ZonedDateTime dateTime = ZonedDateTime.now()
                .withYear(date.getYear())
                .withMonth(date.getMonthValue())
                .withDayOfMonth(date.getDayOfMonth())
                .withHour(startHour)
                .withMinute(startMinute)
                .withSecond(0);

        // Convert color to hex
        Color selected = colorPicker.getValue();
        String colorHex = String.format("#%02X%02X%02X",
                (int) (selected.getRed() * 255),
                (int) (selected.getGreen() * 255),
                (int) (selected.getBlue() * 255)
        );

        CalendarActivity newActivity = new CalendarActivity(
                dateTime,
                titleField.getText(),
                100001,
                colorHex
        );

        calendarController.addActivity(newActivity);

        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
