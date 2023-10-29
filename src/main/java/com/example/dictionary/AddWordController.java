package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

import static com.example.dictionary.Dictionary.DATA_FILE_PATH;

public class AddWordController {
//    @FXML
//    private TextField newWordField;
//    @FXML
//    private TextField newDefinitionField;
//
//    public void addWord(ActionEvent event) {
//        String newWord = newWordField.getText();
//        String newDefinition = newDefinitionField.getText();
//
//        // Call the addWordToFile method from DictionaryController to add the new word and definition
//        DictionaryController controller = new DictionaryController();
//        controller.addWordToFile(newWord, newDefinition);
//
//        // Close the "Add New Word" window
//        Stage stage = (Stage) newWordField.getScene().getWindow();
//        stage.close();
//    }
    @FXML
    private TextField newWordField;

    @FXML
    private TextField newDefinitionField;

    private DictionaryController dictionaryController;

    public void setDictionaryController(DictionaryController dictionaryController) {
        this.dictionaryController = dictionaryController;
    }

    public void addWord(ActionEvent event) {
        String newWord = newWordField.getText();
        String newDefinition = newDefinitionField.getText();

        // Call the addWordToFile method from DictionaryController
        dictionaryController.addWordToFile(newWord, newDefinition);

        // Close the "Add New Word" window
        Stage stage = (Stage) newWordField.getScene().getWindow();
        stage.close();
    }

}
