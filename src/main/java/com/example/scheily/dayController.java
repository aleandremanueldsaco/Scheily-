package com.example.scheily;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class dayController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text monthYearText;

    @FXML
    private Text dayNameText;

    @FXML
    private GridPane calendar;

    private final List<CalendarActivity> addedActivities = new ArrayList<>();

    private final int hoursInDay = 8;
    private final LocalTime calendarStartTime = LocalTime.of(8, 0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        today = ZonedDateTime.now();
        dateFocus = today;
        drawCalendar();
    }

    @FXML
    void backOneDay(ActionEvent event) {
        dateFocus = dateFocus.minusDays(1);
        drawCalendar();
    }

    @FXML
    void forwardOneDay(ActionEvent event) {
        dateFocus = dateFocus.plusDays(1);
        drawCalendar();
    }

    @FXML
    private void month(ActionEvent event) throws IOException {
        loadScene(event, "calendar.fxml");
    }

    @FXML
    private void week(ActionEvent event) throws IOException {
        loadScene(event, "weeklyCalendar.fxml");
    }
    @FXML
    private void schedule(ActionEvent event) throws IOException {
        loadScene(event, "schedule.fxml");
    }
    @FXML
    private void Home(ActionEvent event) throws IOException {
        loadScene(event, "dashboard.fxml");
    }
    @FXML
    private void Todolist(ActionEvent event) throws IOException {
        loadScene(event, "todolist.fxml");
    }
    @FXML
    private void Logout(ActionEvent event) throws IOException {
        loadScene(event, "login-page.fxml");
    }


    private void loadScene(ActionEvent event, String fxmlFile) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void openAddActivityPopup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddActivity.fxml"));
            Parent root = loader.load();

            AddactivityController addActivityController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Add New Activity");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            drawCalendar();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawCalendar() {
        calendar.getChildren().clear();
        calendar.getColumnConstraints().clear();
        calendar.getRowConstraints().clear();

        monthYearText.setText(dateFocus.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        dayNameText.setText(dateFocus.format(DateTimeFormatter.ofPattern("EEEE dd")));

        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPercentWidth(15);
        calendar.getColumnConstraints().add(timeCol);

        ColumnConstraints dayCol = new ColumnConstraints();
        dayCol.setPercentWidth(85);
        calendar.getColumnConstraints().add(dayCol);

        for (int r = 0; r < hoursInDay; r++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / hoursInDay);
            calendar.getRowConstraints().add(row);
        }

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h a");
        for (int r = 0; r < hoursInDay; r++) {
            LocalTime t = calendarStartTime.plusHours(r);
            StackPane timeBox = new StackPane(new Text(t.format(timeFmt)));
            timeBox.setAlignment(Pos.TOP_RIGHT);
            timeBox.setPadding(new Insets(5, 10, 5, 5));
            timeBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");
            calendar.add(timeBox, 0, r);
        }

        for (int r = 0; r < hoursInDay; r++) {
            StackPane cell = new StackPane();
            cell.setMinSize(80, 50);
            cell.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");
            calendar.add(cell, 1, r);
        }

        drawActivities();
    }

    private void drawActivities() {
        List<CalendarActivity> dayActivities = getCalendarActivitiesDay(dateFocus);

        for (CalendarActivity act : dayActivities) {
            int hourIndex = act.getDate().getHour() - calendarStartTime.getHour();

            if (hourIndex < 0 || hourIndex >= hoursInDay) continue;

            StackPane eventPane = new StackPane();
            eventPane.setAlignment(Pos.TOP_LEFT);
            eventPane.setPadding(new Insets(5));

            String bgColor = getRgbaColor(act.getColorHex(), 0.8);
            String borderColor = getRgbaColor(act.getColorHex(), 1.0);

            eventPane.setStyle(
                    "-fx-background-color: " + bgColor + "; " +
                            "-fx-border-color: " + borderColor + "; " +
                            "-fx-border-width: 1; " +
                            "-fx-background-radius: 5; " +
                            "-fx-border-radius: 5;"
            );

            VBox infoBox = new VBox(2);
            Text title = new Text(act.getClientName());
            title.setStyle("-fx-font-weight: bold;");

            String startTime = act.getDate().format(DateTimeFormatter.ofPattern("h:mm a"));
            Text time = new Text(startTime);

            infoBox.getChildren().addAll(title, time);
            eventPane.getChildren().add(infoBox);

            GridPane.setColumnIndex(eventPane, 1);
            GridPane.setRowIndex(eventPane, hourIndex);

            GridPane.setMargin(eventPane, new Insets(2, 5, 2, 5));

            calendar.getChildren().add(eventPane);
        }
    }

    private String getRgbaColor(String hex, double opacity) {
        try {
            Color c = Color.web(hex);
            return String.format("rgba(%d, %d, %d, %.2f)",
                    (int) (c.getRed() * 255),
                    (int) (c.getGreen() * 255),
                    (int) (c.getBlue() * 255),
                    opacity);
        } catch (Exception e) {
            return String.format("rgba(200, 200, 200, %.2f)", opacity);
        }
    }

    private List<CalendarActivity> getCalendarActivitiesDay(ZonedDateTime day) {
        List<CalendarActivity> activitiesForDay = new ArrayList<>();
        for (CalendarActivity act : addedActivities) {
            if (act.getDate().toLocalDate().equals(day.toLocalDate())) {
                activitiesForDay.add(act);
            }
        }
        activitiesForDay.sort(Comparator.comparing(CalendarActivity::getDate));
        return activitiesForDay;
    }

    public void addActivity(CalendarActivity activity) {
        addedActivities.add(activity);
        drawCalendar();
    }
}