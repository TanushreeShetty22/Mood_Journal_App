package com.example.moodjournal;

public class MoodEntry {

    private String moodText;
    private String date;

    // Constructor
    public MoodEntry(String moodText, String date) {
        this.moodText = moodText;
        this.date = date;
    }

    // Getters and setters
    public String getMoodText() {
        return moodText;
    }

    public void setMoodText(String moodText) {
        this.moodText = moodText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date=date;
    }
}