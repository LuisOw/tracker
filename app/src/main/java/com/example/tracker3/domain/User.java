package com.example.tracker3.domain;

public class User {
    private String chosenName;
    private String username;
    private String accessToken;
    private String tokenType;


    public User(String chosenName, String username, String accessToken, String tokenType) {
        this.chosenName = chosenName;
        this.username = username;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    public String getChosenName() {
        return chosenName;
    }

    public void setChosenName(String chosenName) {
        this.chosenName = chosenName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "User{" +
                "chosenName='" + chosenName + '\'' +
                ", username='" + username + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
