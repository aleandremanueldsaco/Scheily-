package com.example.scheily;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class calendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;


    private final List<CalendarActivity> addedActivities = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    // ===================== NAVIGATION =========================
    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    // ===================== SWITCH TO WEEKLY VIEW =========================
    @FXML
    private void week(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("weeklyCalendar.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void schedule(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("schedule.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    // ===================== ADD ACTIVITY POPUP =========================
    @FXML
    void openAddActivityPopup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddActivity.fxml"));
            Parent root = loader.load();

            AddactivityController addActivityController = loader.getController();
            addActivityController.setCalendarController(this);

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

    // ===================== CALENDAR DRAW =========================
    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();


        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }

        int dateOffset = ZonedDateTime.of(
                dateFocus.getYear(),
                dateFocus.getMonthValue(),
                1, 0, 0, 0, 0,
                dateFocus.getZone()
        ).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);


                        List<CalendarActivity> activitiesForDay = calendarActivityMap.get(currentDate);
                        if (activitiesForDay != null) {
                            createCalendarActivity(activitiesForDay, rectangleHeight, rectangleWidth, stackPane);
                        }


                        if (today.getYear() == dateFocus.getYear() &&
                                today.getMonth() == dateFocus.getMonth() &&
                                today.getDayOfMonth() == currentDate) {
                            rectangle.setStroke(Color.BLUE);
                        }
                    }
                }

                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities,
                                        double rectangleHeight,
                                        double rectangleWidth,
                                        StackPane stackPane) {

        VBox calendarActivityBox = new VBox();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> System.out.println(calendarActivities));
                break;
            }

            CalendarActivity act = calendarActivities.get(k);
            String formattedTime = act.getDate().toLocalTime().format(timeFormatter);
            Text text = new Text(act.getClientName() + ", " + formattedTime);

            VBox box = new VBox(text);
            box.setStyle(
                    "-fx-background-color: " + act.getColorHex() + "; " +
                            "-fx-padding: 3; -fx-background-radius: 5;"
            );
            calendarActivityBox.getChildren().add(box);
        }

        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.85);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            calendarActivityMap.computeIfAbsent(activityDate, k -> new ArrayList<>()).add(activity);
        }
        return calendarActivityMap;
    }


    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> monthActivities = new ArrayList<>();

        for (CalendarActivity activity : addedActivities) {
            ZonedDateTime activityDate = activity.getDate();
            if (activityDate.getMonthValue() == dateFocus.getMonthValue() &&
                    activityDate.getYear() == dateFocus.getYear()) {
                monthActivities.add(activity);
            }
        }

        return createCalendarMap(monthActivities);
    }


    public void addActivity(CalendarActivity activity) {
        addedActivities.add(activity);
        calendar.getChildren().clear();
        drawCalendar();
    }
}
