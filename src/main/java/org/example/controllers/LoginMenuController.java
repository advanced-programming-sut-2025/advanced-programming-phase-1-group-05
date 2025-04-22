package org.example.controllers;


import org.example.models.Result;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LoginMenuController extends MenuController {
    private User currentUser;


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
        currentUser = null;
        return Result.success("Password changed successfully!");
    }
}