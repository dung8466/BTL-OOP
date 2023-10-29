package com.example.dictionary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.dictionary.Dictionary.SPLITTING_CHARACTERS;
import static com.example.dictionary.Dictionary.DATA_FILE_PATH;

public class DictionaryController {
    public ListView<String> listView;
    public WebView definitionView;
    private Map<String, Word> data = new HashMap<>();

    private String word;

    @FXML
    private MenuItem deleteWordMenuItem;

    @FXML
    private TextField searchTextField;
    private ObservableList<String> defaultData = FXCollections.observableArrayList();

    @FXML
    private VBox changeDefinitionView;

    @FXML
    private Label selectedWordLabel;

    @FXML
    private TextField newDefinitionTextField;

    public DictionaryController() {
        // Default constructor code, if needed
    }

    public DictionaryController(ListView<String> listView, WebView definitionView, Map<String, Word> data) {
        this.listView = listView;
        this.definitionView = definitionView;
        this.data = data;
        initListeners();
    }

    private void initListeners() {
        listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        word = newValue.trim();
                        deleteWordMenuItem.setVisible(true);
                        displayWordDefinition(word);
                    }
                }
        );
        defaultData.addAll(listView.getItems());
    }

    public void addWord(String word, String definition) {
        Word newWord = new Word(word, definition);
        data.put(word, newWord);
        listView.getItems().add(word);
    }

    public void deleteWord(String word) {
        data.remove(word);
        listView.getItems().remove(word);
        definitionView.getEngine().loadContent("");
    }

    @FXML
    public void deleteSelectedWord(ActionEvent event) {
        String word = listView.getSelectionModel().getSelectedItem();
        if (word != null) {
            deleteWord(word);
        }
        try {
            String filePath = DATA_FILE_PATH; // Update the file path accordingly
            deleteWordFromFile(filePath, word);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void deleteWordFromFile(String filePath, String wordToDelete) throws IOException {
        File inputFile = new File(filePath);
        File tempFile = new File("data/temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(wordToDelete)) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
        }

        // Delete the original file and rename the temp file
        if (inputFile.delete() && tempFile.renameTo(inputFile)) {
            System.out.println("Word deleted from file: " + wordToDelete);
        }
    }

    public void changeWordDefinition(String word, String newDefinition) {
        // Update the definition of an existing word
        if (data.containsKey(word)) {
            Word wordObj = data.get(word);
            wordObj.setDef(newDefinition);

            // Refresh the WebView with the new definition
            displayWordDefinition(word);
        }
    }

    @FXML
    private void changeDefinitionMenuItemClicked(ActionEvent event) {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("change-definition-view.fxml"));
        Parent root;
        try {
            root = loader.load();
            ChangeDefinitionController changeDefinitionController = loader.getController();
            changeDefinitionController.setDictionaryController(this);
            changeDefinitionController.setSelectedWord(selectedWord);

            // Create a new stage for the change definition view
            Stage changeDefinitionStage = new Stage();
            changeDefinitionStage.setScene(new Scene(root));
            changeDefinitionStage.setTitle("Change Definition");
            changeDefinitionStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // This method can be called when the "Save" button in the change definition view is clicked
    public void saveDefinitionButtonClicked(String word, String newDefinition) {
        changeWordDefinition(word, newDefinition);
        updateWordDefinitionToFile(word, newDefinition);
        listView.getItems().setAll(data.keySet());
    }

    public void displayWordDefinition(String word) {
        if (data.containsKey(word)) {
            String definition = data.get(word).getDef();
            definitionView.getEngine().loadContent(definition, "text/html");
        }
    }

    public void Speech() {
        word = listView.getSelectionModel().getSelectedItem();
        TextToSpeech.Speech(word);
    }

    public void soundAPI() {
        word = listView.getSelectionModel().getSelectedItem();
        Audio audio = new Audio();
        try {
            audio.play(audio.getAudio(word, "en"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addWordToFile(String word, String definition) {
        System.out.println(translateWord(word));
        String htmlContent = word + "<html>  <head>      </head>  <body>    " +
                "<i>" + word + "</i><br>    " +
                "<ul>      <li>        " +
                "<font color=\"#cc0000\"><b>" + definition + "</b></font>" +
                "      </li>    </ul>  </body></html>";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH, true))) {
            writer.write(htmlContent);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        addWord(word, definition);
    }

    public void updateWordDefinitionToFile(String word, String newDefinition) {
        // Update the definition of the word in the data file
        try {
            String filePath = DATA_FILE_PATH; // Update the file path accordingly
            File inputFile = new File(filePath);
            File tempFile = new File("data/temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    // Check if the line contains the word to be updated
                    if (currentLine.startsWith(word)) {
                        String updatedLine = word + "<html>  <head>      </head>  <body>    " +
                                "<i>" + word + "</i><br>    " +
                                "<ul>      <li>        " +
                                "<font color=\"#cc0000\"><b>" + newDefinition + "</b></font>" +
                                "      </li>    </ul>  </body></html>";
                        writer.write(updatedLine);
                    } else {
                        writer.write(currentLine);
                    }
                    writer.newLine();
                }
            }

            // Delete the original file and rename the temp file
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                System.out.println("Definition updated for word: " + word);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String translateWord(String word) {
        try {
            if (Objects.equals(DATA_FILE_PATH, "data/E_V.txt")) {
                return Translate.callUrlAndParseResult("en", "vn", word);
            }
            else {
                return Translate.callUrlAndParseResult("vn", "en", word);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @FXML
    private void addNewWord(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-word-view.fxml"));
            Parent root = loader.load();
            AddWordController addWordController = loader.getController();
            addWordController.setDictionaryController(this); // Pass the reference
            Stage addWordStage = new Stage();
            addWordStage.setScene(new Scene(root));
            addWordStage.setTitle("Add New Word");
            addWordStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void switchToEVDictionary(ActionEvent event) throws IOException {
        data.clear();
        listView.getItems().clear();
        definitionView.getEngine().loadContent("");
        initListeners();
        DATA_FILE_PATH = "data/E_V.txt";
        readData();
        defaultData.removeAll();
        defaultData.addAll(listView.getItems());
    }

    @FXML
    private void switchToVEDictionary(ActionEvent event) throws IOException {
        data.clear();
        listView.getItems().clear();
        definitionView.getEngine().loadContent("");
        initListeners();
        DATA_FILE_PATH = "data/V_E.txt";
        readData();
        defaultData.removeAll();
        defaultData.addAll(listView.getItems());
    }

    private void readData() throws IOException {
        FileReader fis = new FileReader(DATA_FILE_PATH);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SPLITTING_CHARACTERS);
            String word = parts[0];
            String definition = SPLITTING_CHARACTERS + parts[1];
            Word wordObj = new Word(word, definition);
            data.put(word, wordObj);
        }
        listView.getItems().addAll(data.keySet());
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchTextField.getText().toLowerCase().trim();
        if (defaultData.isEmpty()) {
            defaultData.addAll(listView.getItems());
        }
        if (searchText.isEmpty()) {
            listView.setItems(defaultData);
        } else {
            List<WordSimilarity> wordSimilarities = new ArrayList<>();
            System.out.println(searchText);
            for (String word : listView.getItems()) {
                int distance = calculateLevenshteinDistance(searchText, word.toLowerCase());

                if (word.toLowerCase().contains(searchText)) {
                    wordSimilarities.add(new WordSimilarity(word, distance));
                }
            }
            wordSimilarities.sort(Comparator.comparingInt(WordSimilarity::getDistance));
            List<String> sortedWords = wordSimilarities.stream()
                    .map(WordSimilarity::getWord)
                    .collect(Collectors.toList());
            listView.setItems(FXCollections.observableArrayList(sortedWords));
        }
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        return dp[m][n];
    }
}