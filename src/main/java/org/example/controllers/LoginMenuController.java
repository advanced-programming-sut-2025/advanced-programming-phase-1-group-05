package org.example.controllers;

import org.example.models.Result;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginMenuController extends MenuController {
    public static User currentUser;
    private boolean inPasswordRecovery = false;
    private boolean waitingForNewPassword = false;

    public LoginMenuController(Scanner scanner) {
        super(scanner);
    }

    public Result loginUser(String username, String password, boolean stayLoggedIn) {
        if (username == null || username.trim().isEmpty()) {
            return new Result(false, "Username cannot be empty!");
        }

        if (password == null || password.trim().isEmpty()) {
            return new Result(false, "Password cannot be empty!");
        }

        User user = UserDatabase.getUserByUsername(username);
        if (user == null) {
            return new Result(false, "Username not found!");
        }

        if (!user.getPassword().equals(password)) {
            return new Result(false, "Incorrect password!");
        }
        if (stayLoggedIn) {
            // پیاده‌سازی منطق ذخیره وضعیت ورود
        }
        this.currentUser = user;
        RegisterMenuController.currentUser = user;
        return new Result(true, "Logged in successfully!");

    }
    public Result initiatePasswordRecovery(String username) {
        this.currentUser = UserDatabase.getUserByUsername(username);
        if (this.currentUser == null) {
            return Result.error("Username not found!");
        }

        if (this.currentUser.getSecurityQuestion() == null ||
                this.currentUser.getSecurityAnswer() == null) {
            return Result.error("No security question set for this user!");
        }

        return Result.success("Security question: " + this.currentUser.getSecurityQuestion());
    }

    public Result verifySecurityAnswer(String answer) {
        if (currentUser == null) {
            return Result.error("No active password recovery session!");
        }
        if (!currentUser.getSecurityAnswer().equalsIgnoreCase(answer)) {
            currentUser = null;
            return Result.error("Incorrect answer! Try to login again.");
        }

        return Result.success("Please enter your new password:");
    }

    public Result setNewPassword(String newPassword) {
        if (currentUser == null) {
            return Result.error("No active password recovery session!");
        }

        currentUser.setPassword(newPassword);
        DBController.saveUsers();
        DBController.saveCurrentUser();
//        currentUser = null;
        return Result.success("Password changed successfully!");
    }
    public Result handleLoginCommand(String input) {
        String[] parts = input.split(" -");
        String username = null;
        String password = null;
        boolean stayLoggedIn = false;

        for (String part : parts) {
            if (part.startsWith("u ")) {
                username = part.substring(2).trim();
            } else if (part.startsWith("p ")) {
                password = part.substring(2).trim();
            } else if (part.equals("stay-logged-in")) {
                stayLoggedIn = true;
            }
        }

        if (username == null || password == null) {
            return Result.error("Invalid login command format!");
        }

        return loginUser(username, password, stayLoggedIn);
    }

    public Result startPasswordRecovery(String input) {
        try {
            String username = input.substring("forget password -u ".length()).trim();
            Result result = initiatePasswordRecovery(username);

            if (result.isSuccess()) {
                this.inPasswordRecovery = true;
            }

            return result;
        } catch (StringIndexOutOfBoundsException e) {
            return Result.error("Invalid command format! Use: forget password -u <username>");
        }
    }

    public Result handleSecurityAnswer(String input) {
        if (!input.startsWith("answer -a ")) {
            return Result.error("Please use the format: answer -a <your_answer>");
        }

        String answer = input.substring("answer -a ".length()).trim();
        Result result = verifySecurityAnswer(answer);

        if (result.isSuccess()) {
            this.inPasswordRecovery = false;
            this.waitingForNewPassword = true;
        } else {
            this.inPasswordRecovery = false;
        }

        return result;
    }

    public Result handleNewPassword(String input) {
        Result result = setNewPassword(input);
        this.waitingForNewPassword = false;
        return result;
    }

    public boolean isInPasswordRecovery() {
        return inPasswordRecovery;
    }

    public boolean isWaitingForNewPassword() {
        return waitingForNewPassword;
    }
}