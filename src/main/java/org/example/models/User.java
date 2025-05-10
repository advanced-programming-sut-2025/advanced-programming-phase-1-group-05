package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    String password;
    String nickName;
    String gender;
    String email;
    private int gamesPlayed;

    private String securityQuestion;
    private String securityAnswer;
    public static boolean haveSavedGame = false;

    private List<String> friends = new ArrayList<>();

    public void addFriend(String username) {
        if (!friends.contains(username)) {
            friends.add(username);
        }
    }

    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getNickName() {
        return nickName;
    }

    public String getGender() {
        return gender;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickName + '\'' +
                ", gamesPlayed='" + gamesPlayed + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", haveSavedGame='" + haveSavedGame + '\'' +
                '}';
    }
}
