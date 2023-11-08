package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.example.dictionary.Dictionary.DATA_FILE_PATH;
import static com.example.dictionary.Dictionary.SPLITTING_CHARACTERS;

public class GameController {

    @FXML
    private Label wordLabel;
    @FXML
    private Button definitionButtons0;
    @FXML
    private Button definitionButtons1;
    @FXML
    private Button definitionButtons2;
    @FXML
    private Button definitionButtons3;
    @FXML
    private Label scoreLabel;

    @FXML
    private WebView definitionTextField0;
    @FXML
    private WebView definitionTextField1;
    @FXML
    private WebView definitionTextField2;
    @FXML
    private WebView definitionTextField3;

    private Map<String, Word> data;

    private List<Word> wordList;
    private int currentRound = 0;
    private int score = 0;
    private int correctDefinitionIndex;

    private Random random;

    private DictionaryController dictionaryController;


    public void setDictionaryController(DictionaryController dictionaryController) {
        this.dictionaryController = dictionaryController;
    }

    public void initialize() throws IOException {
        random = new Random();
        dictionaryController = new DictionaryController();
        data = dictionaryController.getData();
        wordList = new ArrayList<>();
        readData();
        startNewRound();
    }

    private void startNewRound() {
        if (currentRound < 10) {
            // Get a random word and its correct definition
            int randomWordIndex = random.nextInt(wordList.size());
            Word randomWord = wordList.get(randomWordIndex);
            String correctDefinition = randomWord.getDef();
            wordLabel.setText(randomWord.getWord());

            // Generate three random incorrect definitions
            List<String> definitions = generateRandomDefinitions(correctDefinition, 3);
            definitions.add(correctDefinition);

            // Shuffle the list to randomize the order of definitions
            shuffleList(definitions);

            // Set the text of buttons with definitions
//            definitionButtons0.setText(extractTextWithinBTags(definitions.get(0)));
//            definitionButtons1.setText(extractTextWithinBTags(definitions.get(1)));
//            definitionButtons2.setText(extractTextWithinBTags(definitions.get(2)));
//            definitionButtons3.setText(extractTextWithinBTags(definitions.get(3)));
//            definitionTextField0.setText("1");
//            definitionTextField1.setText("2");
//            definitionTextField2.setText("3");
//            definitionTextField3.setText("4");


            definitionTextField0.getEngine().loadContent(extractTextWithinBTags(definitions.get(0)), "text/html");
            definitionTextField1.getEngine().loadContent(extractTextWithinBTags(definitions.get(1)), "text/html");
            definitionTextField2.getEngine().loadContent(extractTextWithinBTags(definitions.get(2)), "text/html");
            definitionTextField3.getEngine().loadContent(extractTextWithinBTags(definitions.get(3)), "text/html");


            // Store the index of the correct definition
            correctDefinitionIndex = definitions.indexOf(correctDefinition);

            currentRound++;
        } else {
            // End of the game, you can display the final score or take some other action
            wordLabel.setText("Game Over");
        }
    }

    public void readData() throws IOException {
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
        wordList.addAll(data.values());
    }

    @FXML
    public void checkAnswer(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int clickedIndex = Integer.parseInt(clickedButton.getId().substring(17));

        if (clickedIndex == correctDefinitionIndex) {
            score++;

        }

        scoreLabel.setText("Score: " + score);

        // Start a new round
        startNewRound();
    }

    private List<String> generateRandomDefinitions(String correctDefinition, int count) {
        List<String> definitions = new ArrayList<>();
        List<String> word = new ArrayList<>(data.keySet());
        List<String> allDefinitions = new ArrayList<>();

        for (String w : word) {
            Word wordObj = data.get(w);
            if (wordObj != null) {
                String definition = wordObj.getDef();
//                String extractedText = extractTextWithinBTags(definition);
                allDefinitions.add(definition);
//                allDefinitions.add(extractedText);
            }
        }
        while (definitions.size() < count) {
            int randomIndex = random.nextInt(allDefinitions.size());
            String randomDefinition = allDefinitions.get(randomIndex);

            if (!randomDefinition.equals(correctDefinition) && !definitions.contains(randomDefinition)) {
                definitions.add(randomDefinition);
            }
        }

        return definitions;
    }

    private void shuffleList(List<String> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            String temp = list.get(index);
            list.set(index, list.get(i));
            list.set(i, temp);
        }
    }

    private String extractTextWithinBTags(String definition) {
//        // Parse the HTML content using JSoup
//        Document doc = Jsoup.parse(definition);
//
//        String cleanedHtml = doc.outerHtml().replaceFirst(".*<html>", "<html>");
//        // Select all <b> elements within the parsed document
//        Elements boldElements = Jsoup.parse(cleanedHtml).select("b");
////        Elements boldElements = doc.select("html");
//
//        // Extract and concatenate the text within <b> elements
//        StringBuilder extractedText = new StringBuilder();
//        for (Element element : boldElements) {
//            element.select("i").remove();
//            extractedText.append(element.text());
//        }
//
//        // Return the text within <b> tags
//        return extractedText.toString();
        Document doc = Jsoup.parse(definition);

        // Remove content before <html> tag
        Element htmlElement = doc.select("html").first();
        if (htmlElement != null) {
            // Remove <i> tags and get outer HTML of <html> element
            Objects.requireNonNull(htmlElement.select("i").first()).remove();

            // Return the extracted HTML
            return htmlElement.outerHtml();
        }

        return "";
    }
}
