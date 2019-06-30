package com.emozers.assistant2;

public class InstantMessage {
    private String message;
    private String author;

    public InstantMessage(String message,String author) {
        this.message = message;
        this.author=author;
    }
    // there is the requirement by the firebase to create a constructor with no arguments and have getter and setter methods

    public InstantMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
