package com.example.tracker3.domain;


public class Questionnaire {
    private final int id;
    private final String title;

    public Questionnaire(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "Research{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

