package com.example.tracker3.domain;

import com.example.tracker3.util.QuestionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

    private int id;
    private int order;
    private String query;
    private QuestionType type;
    private ArrayList<Alternative> alternatives;

    public Question(int id, int order, String query, QuestionType type, ArrayList<Alternative> alternatives) {
        this.id = id;
        this.order = order;
        this.query = query;
        this.type = type;
        this.alternatives = alternatives;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public ArrayList<Alternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(ArrayList<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", order=" + order +
                ", query='" + query + '\'' +
                ", type='" + type + '\'' +
                ", alternatives=" + alternatives +
                '}';
    }
}
