package com.example.tracker3;


public class Research {
    private int id;
    private String title;
    private String description;
    private String state;

    public Research(int id, String title, String description, String state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Research{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

