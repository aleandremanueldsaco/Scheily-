package com.example.scheily;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

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

}
