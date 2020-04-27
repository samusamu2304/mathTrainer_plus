package com.boala.mathtrainer;

public class Result {
    private String text,textCorrect;
    private boolean isCorrect;
    private double points;

    Result(String text, String textCorrect, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.textCorrect = textCorrect;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    String getTextCorrect() {
        return textCorrect;
    }

    public void setTextCorrect(String textCorrect) {
        this.textCorrect = textCorrect;
    }

    boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
