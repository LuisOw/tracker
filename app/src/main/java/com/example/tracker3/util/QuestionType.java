package com.example.tracker3.util;

public enum QuestionType {
    MULTIPLA_ESCOLHA("multipla_escolha"),
    DESCRITIVA("descritiva");

    private String name;

    QuestionType(String name) {
        this.name = name;
    }
}
