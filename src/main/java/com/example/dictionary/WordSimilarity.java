package com.example.dictionary;

public class WordSimilarity {
    private String word;
    private int distance;

    WordSimilarity(String word, int distance) {
        this.word = word;
        this.distance = distance;
    }

    String getWord() {
        return word;
    }

    int getDistance() {
        return distance;
    }
}







