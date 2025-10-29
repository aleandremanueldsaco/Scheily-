package com.example.scheily;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OtpPopupController {

    @FXML private TextField otp1;
    @FXML private TextField otp2;
    @FXML private TextField otp3;
    @FXML private TextField otp4;
    @FXML private TextField otp5;
    @FXML private TextField otp6;

    @FXML
    private void handleResend() {
        System.out.println("Resending OTP...");

    }
    @FXML
    public void initialize() {
        TextField[] fields = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < fields.length; i++) {
            int next = i + 1;
            fields[i].textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.length() > 0 && next < fields.length) {
                    fields[next].requestFocus();
                }
            });
        }
    }
}