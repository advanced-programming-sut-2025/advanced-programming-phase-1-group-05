package org.example.Common;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String username;
    public String password;
    public String nickName;
    public String gender;
    public String email;
    public String ans;
    private int gamesPlayed;
    public String plainPassword;
    public String avatarTexturePath;
    public Texture avatarTexture;

    private String securityQuestion;
    String securityAnswer;
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

    public String getPlainPassword() {
        return plainPassword;
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

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public void setAns(String ans) {
        this.ans = ans;
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

    public void setAvatarTexturePath(String avatarTexturePath) {
        this.avatarTexturePath = avatarTexturePath;
        avatarTexture = new Texture(avatarTexturePath);
    }
    public String getAvatarTexturePath() {
        return avatarTexturePath;
    }
    public Texture getAvatarTexture() {
        return avatarTexture;
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
            ", plainPassword='" + plainPassword + '\'' +
            ", haveSavedGame='" + haveSavedGame + '\'' +
            '}';
    }
}
