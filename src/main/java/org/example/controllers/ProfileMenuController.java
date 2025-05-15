package org.example.controllers;

import org.example.models.*;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ProfileMenuController extends MenuController {
    private static User currentUser;

    private static final String USERNAME_REGEX = "^\\s*change\\s+username\\s+-u\\s+(?<username>.+?)\\s*$";
    private static final String NICKNAME_REGEX = "^\\s*change\\s+nickname\\s+-u\\s+(?<nickname>\\S+)\\s*$";
    private static final String EMAIL_REGEX = "^\\s*change\\s+email\\s+-e\\s+(?<email>\\S+)\\s*$";
    private static final String PASSWORD_REGEX = "^\\s*change\\s+password\\s+-p\\s+(?<newPassword>\\S+)\\s+-o\\s+" +
            "(?<oldPassword>\\S+)\\s*$";
    private static final String USERINFO_REGEX = "^\\s*user\\s+info\\s*$";

    public ProfileMenuController(Scanner scanner, User currentUser) {
        super(scanner);
        ProfileMenuController.currentUser = currentUser;
    }

    public Result handleProfileCommand(String input) {
        if (input.matches(USERNAME_REGEX)) {
            Matcher matcher = Pattern.compile(USERNAME_REGEX).matcher(input);
            if (matcher.find()) {
                return changeUsername(matcher.group("username"));
            }
        }
        else if (input.matches(NICKNAME_REGEX)) {
            Matcher matcher = Pattern.compile(NICKNAME_REGEX).matcher(input);
            if (matcher.find()) {
                return changeNickname(matcher.group("nickname"));
            }
        }
        else if (input.matches(EMAIL_REGEX)) {
            Matcher matcher = Pattern.compile(EMAIL_REGEX).matcher(input);
            if (matcher.find()) {
                return changeEmail(matcher.group("email"));
            }
        }
        else if (input.matches(PASSWORD_REGEX)) {
            Matcher matcher = Pattern.compile(PASSWORD_REGEX).matcher(input);
            if (matcher.find()) {
                return changePassword(
                        matcher.group("newPassword"),
                        matcher.group("oldPassword")
                );
            }
        }
        else if (input.trim().equals("user info")) {
            return showUserInfo();
        }
        else if (input.startsWith("change gender")) return Result.error("Oops!You can't change this one ;)");
        else if (input.matches(USERINFO_REGEX)) {
            showUserInfo();
        }

        return Result.error("Invalid profile command!");
    }

    private Result changeUsername(String newUsername) {
        if (newUsername == null || newUsername.trim().isEmpty()) {
            return Result.error("Username cannot be empty!");
        }
        if (currentUser.getUsername().equals(newUsername)) {
            return Result.error("Your username must be different from last one!");
        }

        if (UserDatabase.usernameExists(newUsername)) {
            return Result.error("Username already exists!");
        }

        if (!isValidUsername(newUsername)) {
            return Result.error("Invalid username format! Must contain letters, numbers and hyphen only");
        }

        currentUser.setUsername(newUsername);
        UserDatabase.updateUser(currentUser);
        DBController.saveUsers();
        DBController.saveCurrentUser();
        return Result.success("Username changed successfully!");
    }

    private Result changePassword(String newPassword, String oldPassword) {
        if (currentUser.getPassword().equals(newPassword)) {
            return Result.error("Your pass must be different from last one!");
        }

        if (!currentUser.getPassword().equals(oldPassword)) {
            return Result.error("Old password is incorrect!");
        }

        Result validation = validatePasswordStrength(newPassword);
        if (!validation.isSuccess()) {
            return validation;
        }

        currentUser.setPassword(newPassword);
        UserDatabase.updateUser(currentUser);
        DBController.saveUsers();
        DBController.saveCurrentUser();
        return Result.success("Password changed successfully!");
    }

    private Result changeEmail(String newEmail) {
        if (!isValidEmail(newEmail)) {
            return Result.error("Invalid email format!");
        }
        if (currentUser.getEmail().equals(newEmail)) {
            return Result.error("Your email must be different from last one!");
        }

        currentUser.setEmail(newEmail);
        UserDatabase.updateUser(currentUser);
        DBController.saveUsers();
        DBController.saveCurrentUser();
        return Result.success("Email changed successfully!");
    }

    private Result changeNickname(String newNickname) {
        if (newNickname == null || newNickname.trim().isEmpty()) {
            return Result.error("Nickname cannot be empty!");
        }
        if (currentUser.getNickName().equals(newNickname)) {
            return Result.error("Your nickname must be different from last one!");
        }

        currentUser.setNickName(newNickname);
        UserDatabase.updateUser(currentUser);
        DBController.saveUsers();
        DBController.saveCurrentUser();
        return Result.success("Nickname changed successfully!");
    }

    private Result showUserInfo() {
        StringBuilder info = new StringBuilder();
//        System.out.println(Game.getAllPlayers());
        info.append("Username: ").append(currentUser.getUsername()).append("\n");
        info.append("Nickname: ").append(currentUser.getNickName()).append("\n");
        for (Player player : Game.getAllPlayers()) {
            if (player.getUsername().equals(currentUser.getUsername())) {
                info.append("Money: ").append(player.getGold()).append("\n");
            } else {
                info.append("This user hadn't any game!\n");
            }
        }
        if (Game.getAllPlayers() == null || Game.getAllPlayers().isEmpty() || Game.getCurrentPlayer() == null)
            info.append("Money: 0\n");
        else info.append("Money: " + Game.getCurrentPlayer().getGold()).append("\n");
        info.append("Games played: ").append(currentUser.getGamesPlayed()).append("\n");
        return Result.success(info.toString());
    }

    public Result validatePasswordStrength(String password) {
        String specialChars = "?><,\"' ;:\\\\/|][}{+=)(*&^%$#!";
        if (password.length() < 8) {
            return new Result(false,
                    "Password is not Strength, it must be at least 8 characters long.");
        }

        if (!password.matches(".*[a-z].*")) {
            return new Result(false,
                    "Password is not Strength, it must contain at least one lowercase letter.");
        }

        if (!password.matches(".*[A-Z].*")) {
            return new Result(false,
                    "Password is not Strength, it must contain at least one uppercase letter.");
        }

        if (!password.matches(".*\\d.*")) {
            return new Result(false,
                    "Password is not Strength, it must contain at least one digit.");
        }
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (specialChars.contains(String.valueOf(c))) {
                hasSpecialChar = true;
                break;
            }
        }
        if (!hasSpecialChar) {
            return new Result(false,
                    "Password is not Strength, it must contain at least one special character.");
        }

        return new Result(true, "Password is strong.");
    }


    private boolean isValidUsername(String username) {
        String allowedCharsRegex = "^[a-zA-Z0-9-]+$";

        boolean hasLower = username.matches(".*[a-z].*");
        boolean hasUpper = username.matches(".*[A-Z].*");
        boolean hasHyphen = username.contains("-");

        return username.matches(allowedCharsRegex) && hasLower && hasUpper && hasHyphen;
    }


    private boolean isValidEmail(String email) {
        String localPartRegex = "^[a-zA-Z0-9][a-zA-Z0-9-_.]*[a-zA-Z0-9]";
        String domainPartRegex = "([a-zA-Z0-9-]+\\.[a-zA-Z]{2,})$";
        String emailRegex = localPartRegex + "@" + domainPartRegex;

        if (email.split("@").length != 2) {
            return false;
        }

        String invalidChars = "?><,\"' ;:\\\\/|][}{+=)(*&^%$#!";
        for (char c : email.toCharArray()) {
            if (invalidChars.indexOf(c) != -1) {
                return false;
            }
        }

        if (email.contains("..")) {
            return false;
        }

        return email.matches(emailRegex);
    }

}