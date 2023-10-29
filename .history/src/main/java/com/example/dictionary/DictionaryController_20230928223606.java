package com.example.dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DictionaryController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label clickMeLabel;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    // Add the onClickMeButtonClick method here
    @FXML
    protected void onClickMeButtonClick() {
        clickMeLabel.setText("You clicked me!");
    }


}