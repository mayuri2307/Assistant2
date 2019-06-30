package com.emozers.assistant2;

public class emotions
{
    String neutral,anger,contempt,disgust,fear,happiness,sadness,surprise;

    public emotions() {
    }

    public emotions(String neutral, String anger, String contempt, String disgust, String fear, String happiness, String sadness, String surprise) {
        this.neutral = neutral;
        this.anger = anger;
        this.contempt = contempt;
        this.disgust = disgust;
        this.fear = fear;
        this.happiness = happiness;
        this.sadness = sadness;
        this.surprise = surprise;
    }

    public String getNeutral() {
        return neutral;
    }

    public String getAnger() {
        return anger;
    }

    public String getContempt() {
        return contempt;
    }

    public String getDisgust() {
        return disgust;
    }

    public String getFear() {
        return fear;
    }

    public String getHappiness() {
        return happiness;
    }

    public String getSadness() {
        return sadness;
    }

    public String getSurprise() {
        return surprise;
    }
}
