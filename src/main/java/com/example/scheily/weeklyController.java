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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class weeklyController implements Initializable {

    ZonedDateTime dateFocus;   // start of current week
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private GridPane calendar; // ✅ use GridPane for hour/day layout

    // Store manually added activities
    private final List<CalendarActivity> addedActivities = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        today = ZonedDateTime.now();
        dateFocus = today.with(DayOfWeek.MONDAY); // start at Monday of this week
        drawCalendar();
    }

    // ===================== NAVIGATION =========================
    @FXML
    void backOneWeek(ActionEvent event) {
        dateFocus = dateFocus.minusWeeks(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneWeek(ActionEvent event) {
        dateFocus = dateFocus.plusWeeks(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    // ===================== ADD ACTIVITY POPUP =========================
    @FXML
    void openAddActivityPopup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddActivity.fxml"));
            Parent root = loader.load();

            AddactivityController addActivityController = loader.getController();
            addActivityController.setWeeklyController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New Activity");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void month(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("calendar.fxml"));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void day(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("dayCalendar.fxml"));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void schedule(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("schedule.fxml"));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    // ===================== WEEKLY CALENDAR DRAW =========================
    private void drawCalendar() {
        calendar.getChildren().clear();
        calendar.getColumnConstraints().clear();
        calendar.getRowConstraints().clear();


        ZonedDateTime startOfWeek = dateFocus;
        ZonedDateTime endOfWeek = dateFocus.plusDays(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d");
        month.setText(startOfWeek.format(formatter) + " – " + endOfWeek.format(formatter));
        year.setText(String.valueOf(startOfWeek.getYear()));

        int hours = 9; // 7 AM to 3 PM
        LocalTime startTime = LocalTime.of(7, 0);

        // ==== columns ==== (1 for time labels + 7 days)
        ColumnConstraints timeCol = new ColumnConstraints();
        timeCol.setPercentWidth(10);
        calendar.getColumnConstraints().add(timeCol);

        for (int c = 0; c < 7; c++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(90.0 / 7);
            calendar.getColumnConstraints().add(col);
        }

        // ==== rows ==== (1 for day headers + 9 for hours)
        RowConstraints headerRow = new RowConstraints();
        headerRow.setPercentHeight(10);
        calendar.getRowConstraints().add(headerRow);

        for (int r = 0; r < hours; r++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(90.0 / hours);
            calendar.getRowConstraints().add(row);
        }

        // ==== day headers ====
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int c = 0; c < 7; c++) {
            ZonedDateTime day = startOfWeek.plusDays(c);
            StackPane header = new StackPane(new Text(days[c]));
            header.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: gray;");
            header.setAlignment(Pos.CENTER);
            calendar.add(header, c + 1, 0); // shift by +1 column (time column first)

            if (day.toLocalDate().equals(today.toLocalDate())) {
                header.setStyle("-fx-background-color: #d0e7ff; -fx-border-color: gray;");
            }
        }
        StackPane corner = new StackPane();
        corner.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");
        calendar.add(corner, 0, 0);


        // ==== TIME COLUMN (left column) ====
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h a");
        for (int r = 0; r < hours; r++) {
            LocalTime t = startTime.plusHours(r);
            StackPane timeBox = new StackPane(new Text(t.format(timeFmt)));
            timeBox.setAlignment(Pos.TOP_LEFT);
            timeBox.setPadding(new Insets(5));

            // Manual borders (not gridLinesVisible)
            timeBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");
            calendar.add(timeBox, 0, r + 1);
        }

// ==== CALENDAR CELLS (every day × hour) ====
        for (int r = 0; r < hours; r++) {
            for (int c = 0; c < 7; c++) {

                StackPane cell = new StackPane();
                cell.setMinSize(80, 50);

                // This forces borders to ALWAYS stay visible
                cell.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5;");

                calendar.add(cell, c + 1, r + 1);
            }
        }

        drawActivities(startOfWeek);
    }

    private void drawActivities(ZonedDateTime startOfWeek) {
        List<CalendarActivity> weekActivities = getCalendarActivitiesWeek(startOfWeek)
                .values().stream().flatMap(List::stream).toList();

        for (CalendarActivity act : weekActivities) {
            int dayIndex = act.getDate().getDayOfWeek().getValue() - 1; // Monday = 0
            int hourIndex = act.getDate().getHour() - 7; // 7am starts row 0
            if (dayIndex < 0 || dayIndex >= 7 || hourIndex < 0 || hourIndex >= 9) continue;

            StackPane eventPane = new StackPane();
            eventPane.setAlignment(Pos.CENTER);
            eventPane.setPadding(new Insets(4));

            Rectangle rect = new Rectangle(110, 40);
            rect.setArcWidth(10);
            rect.setArcHeight(10);
            rect.setFill(Color.web(act.getColorHex(), 0.2));
            rect.setStroke(Color.web(act.getColorHex()));

            Text label = new Text(act.getClientName());
            eventPane.getChildren().addAll(rect, label);

            // +1 to both column (time col) and row (header row)
            GridPane.setColumnIndex(eventPane, dayIndex + 1);
            GridPane.setRowIndex(eventPane, hourIndex + 1);

            calendar.getChildren().add(eventPane);
        }
    }

    // ===================== HELPER METHODS =========================
    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();
        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            calendarActivityMap.computeIfAbsent(activityDate, k -> new ArrayList<>()).add(activity);
        }
        return calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesWeek(ZonedDateTime startOfWeek) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        for (CalendarActivity act : addedActivities) {
            ZonedDateTime actDate = act.getDate();
            if (!actDate.isBefore(startOfWeek) && !actDate.isAfter(startOfWeek.plusDays(6))) {
                calendarActivities.add(act);
            }
        }
        return createCalendarMap(calendarActivities);
    }


    public void addActivity(CalendarActivity activity) {
        addedActivities.add(activity);
        drawCalendar();
    }
    @FXML
    private void Logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-page.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void Todolist(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("todolist.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void Home(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
