package models;

public class User {
    String username;
    String password;
    String nickName;
    String gender;

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
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
