package org.example.Common;

import com.badlogic.gdx.graphics.Texture;
import org.example.Client.GameAssetManager;

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
    public int totalGold;
    public int totalQuests;
    public int totalSkill;

    private String securityQuestion;
    String securityAnswer;
    public static boolean haveSavedGame = false;

    {
        setAvatarTexturePath("NPCs/sebastian/avatar.png");
    }
    private List<String> friends = new ArrayList<>();

    public User() {

    }
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
        avatarTexture = GameAssetManager.getInstance().getOrLoadTexture(avatarTexturePath);
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

    public void updateUserInfo(int amount, String type) {
        switch (type) {
            case "gold": {
                this.totalGold += amount;
                break;
            }
            case "quests": {
                this.totalQuests += amount;
                break;
            }
            case "skill": {
                this.totalSkill += amount;
                break;
            }
        }
    }

    public int getInfo(String type) {
        switch (type) {
            case "gold": {
                return totalGold;
            }
            case "quests": {
                return totalQuests;
            }
            case "skill": {
                return totalSkill;
            }
        }
        return 0;
    }
}
