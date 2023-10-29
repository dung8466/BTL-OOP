package com.example.dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangeDefinitionController {
    @FXML
    private Label selectedWordLabel;

    @FXML
    private TextField newDefinitionTextField;

    private DictionaryController dictionaryController;

    public void setDictionaryController(DictionaryController controller) {
        dictionaryController = controller;
    }

    public void setSelectedWord(String word) {
        selectedWordLabel.setText(word);
    }

    @FXML
    private void saveDefinitionButtonClicked() {
        String word = selectedWordLabel.getText();
        String newDefinition = newDefinitionTextField.getText();
        System.out.println(word);
        System.out.println(newDefinition);
        dictionaryController.saveDefinitionButtonClicked(word, newDefinition);
        Stage stage = (Stage) newDefinitionTextField.getScene().getWindow();

        // Close the stage
        stage.close();
    }
}
