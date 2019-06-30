package com.emozers.assistant2;

public class sentiment_score {
    String message;
    String score;

    public sentiment_score() {
    }

    public sentiment_score(String message, String score) {
        this.message = message;
        this.score = score;
    }

    public String getMessage() {
        return message;
    }

    public String getScore() {
        return score;
    }
}
