package com.example.scheily;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;



import java.io.IOException;

public class LoginController {

    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openOTPPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("otp-popup.fxml"));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("OTP Verification");
        popupStage.setScene(new Scene(root));
        popupStage.showAndWait();
    }
    @FXML
    private void openRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

