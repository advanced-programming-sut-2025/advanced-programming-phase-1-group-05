package controllers;

import models.Enums.RegisterMenuCommand;
import models.Result;
import models.User;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenuController {
    private final Scanner scanner;

    public RegisterMenuController(Scanner scanner) {
        this.scanner = scanner;
    }

    public Result registerUser(String input) {
        Matcher matcher = Pattern.compile(RegisterMenuCommand.REGISTER.getRegexPattern())
                .matcher(input);

        if (!matcher.matches()) {
            return new Result(false, "Invalid command format for register!");
        }

        String username = matcher.group("username");
        String password = matcher.group("password");
        String confirmPassword = matcher.group("confirmPassword");
        String nickname = matcher.group("nickname");
        String email = matcher.group("email");
        String gender = matcher.group("gender");

        if (!isValidPassword(password)) {
            return new Result(false, "Password is invalid!");
        }

        Result passwordValidation = validatePasswordStrength(password);
        if (!passwordValidation.isSuccess()) {
            return passwordValidation;
        }

        if (!password.equals(confirmPassword)) {
            return new Result(false, "Password and confirm password do not match!");
        }
        if (!isValidUsername(username)) {
            return new Result(false, "Invalid username! Only letters, numbers, and '-' are allowed.");
        }
        if (!isValidEmail(email)) {
            return new Result(false, "Invalid email format!");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNickName(nickname);
        newUser.setGender(gender);

        return new Result(true, "User registered successfully!");
    }

    public Result validatePasswordStrength(String password) {
        String specialChars = "?><,\"' ;:\\\\/|][}{+=)(*&^%$#!";
        if (password.length() < 8) {
            return new Result(false, "Password must be at least 8 characters long.");
        }

        if (!password.matches(".*[a-z].*")) {
            return new Result(false, "Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*[A-Z].*")) {
            return new Result(false, "Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*\\d.*")) {
            return new Result(false, "Password must contain at least one digit.");
        }
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (specialChars.contains(String.valueOf(c))) {
                hasSpecialChar = true;
                break;
            }
        }
        if (!hasSpecialChar) {
            return new Result(false, "Password must contain at least one special character.");
        }

        return new Result(true, "Password is strong.");
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9-]+$");
    }
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return password.matches(passwordRegex);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";
        return email.matches(emailRegex);
    }
}