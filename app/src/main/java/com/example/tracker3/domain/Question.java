package com.example.tracker3.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

    private int id;
    private int order;
    private String query;
    private ArrayList<Alternative> alternatives;

    public Question(int id, int order, String query, ArrayList<Alternative> alternatives) {
        this.id = id;
        this.order = order;
        this.query = query;
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
                ", alternatives=" + alternatives +
                '}';
    }
}
