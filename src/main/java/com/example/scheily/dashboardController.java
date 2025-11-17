package com.example.scheily;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class dashboardController {

    @FXML
    private Text firstname;

    @FXML
    private Text lastname;

    @FXML
    private Text studentId;

    @FXML
    private Text birthDate;

    @FXML
    private Text course;

    @FXML
    private Text yearLevelfield;

    @FXML
    private Button uploadButton;

    @FXML
    private ImageView photoView;

    public void setUserData(String Firstname, String Lastname, String Studentid, String birthdate, String Course, String yearlevelfield) {
        firstname.setText(Firstname);
        lastname.setText(Lastname);
        studentId.setText(Studentid);
        birthDate.setText(birthdate);
        course.setText(Course);
        yearLevelfield.setText(yearlevelfield);
    }

    @FXML
    private void onUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            photoView.setImage(image);
            photoView.setPreserveRatio(false);
            photoView.setFitWidth(photoView.getFitWidth());
            photoView.setFitHeight(photoView.getFitHeight());
        }

    }
    @FXML
    private void Todolist(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("todolist.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void calendar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("calendar.fxml"));
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
    @FXML
    private void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login-page.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
