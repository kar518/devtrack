package com.devtrack.model;

import jakarta.persistence.*;

@Entity
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String language;
    private String difficulty;
    private String date;

    // 🔥 NEW FIELDS
    private String topic;
    private int timeSpent; // in minutes
    private String struggleLevel; // Low / Medium / High

    // GETTERS & SETTERS

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    // NEW

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }

    public String getStruggleLevel() { return struggleLevel; }
    public void setStruggleLevel(String struggleLevel) { this.struggleLevel = struggleLevel; }
}