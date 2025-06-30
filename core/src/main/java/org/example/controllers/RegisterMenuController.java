package org.example.controllers;

import org.example.models.Result;
import org.example.models.User;
import org.example.models.UserDatabase;
import org.example.controllers.DBController;

public class RegisterMenuController {
    public static User currentUser;

    public RegisterMenuController() {
    }

    // متدی برای ثبت‌نام با داده‌های واردشده توسط UI
    public Result registerUser(String username, String nickname, String email, String password, String confirmPassword, String gender) {
        System.out.println("Checking username: " + username);
        System.out.println("Valid username? " + isValidUsername(username));
        System.out.println("Valid email? " + isValidEmail(email));
        if (username == null || username.trim().isEmpty()) {
            return new Result(false, "Username cannot be empty!");
        }
        if (password == null || password.isEmpty()) {
            return new Result(false, "Password cannot be empty!");
        }
        if (!isValidUsername(username)) {
            return new Result(false, "Invalid username!");
        }
        if (!isValidEmail(email)) {
            return new Result(false, "Invalid email format!");
        }
        Result passwordValidation = validatePasswordStrength(password);
        if (!passwordValidation.isSuccess()) {
            return passwordValidation;
        }
        if (!password.equals(confirmPassword)) {
            return new Result(false, "Password and confirm password do not match!");
        }
        if (UserDatabase.usernameExists(username)) {
            return new Result(false, "Username already exists!");
        }
        createUser(username, password, nickname, email, gender);
        return new Result(true, "User registered successfully!");
    }

    public static void createUser(String username, String password, String nickname, String email, String gender) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(DBController.hashPassword(password));
        newUser.setNickName(nickname);
        newUser.setEmail(email);
        newUser.setGender(gender);
        currentUser = newUser;
        UserDatabase.addUser(newUser);
        DBController.saveAllUsers();
    }

    public void saveSecurityQuestion(String questionNumber, String answer) {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently being registered");
        }
        String questionText = getQuestionText(questionNumber);
        currentUser.setSecurityQuestion(questionText);
        currentUser.setSecurityAnswer(answer.trim());
        UserDatabase.addUser(currentUser);
    }

    private String getQuestionText(String questionNumber) {
        switch (questionNumber) {
            case "1": return "What was your first pet's name?";
            case "2": return "What city were you born in?";
            case "3": return "What is your favorite color?";
            default: return questionNumber;
        }
    }

    private boolean isValidUsername(String username) {
        String allowedCharsRegex = "^[a-zA-Z0-9-]+$";
        boolean hasLower = username.matches(".*[a-z].*");
        boolean hasUpper = username.matches(".*[A-Z].*");
        boolean hasNumber = username.matches(".*[0-9].*");
        boolean hasHyphen = username.contains("-");
        return username.matches(allowedCharsRegex) && hasLower && hasUpper && hasHyphen && hasNumber;
    }

    private boolean isValidEmail(String email) {
        if (email == null || !email.contains("@") || email.split("@").length != 2) {
            return false;
        }
        String localPartRegex = "^[a-zA-Z0-9][a-zA-Z0-9-_.]*[a-zA-Z0-9]";
        String domainPartRegex = "([a-zA-Z0-9-]+\\.[a-zA-Z]{2,})$";
        String emailRegex = localPartRegex + "@" + domainPartRegex;
        String invalidChars = "?><,\"' ;:\\\\|][}{+=)(*&^%$#!";
        for (char c : email.toCharArray()) {
            if (invalidChars.indexOf(c) != -1) {
                return false;
            }
        }
        if (email.contains("..")) {
            return false;
        }
        boolean hasUpperCase = email.matches(".*[A-Z].*");
        boolean hasNumber = email.matches(".*[0-9].*");
        boolean hasHyphen = email.contains("-");
        boolean valid1 = email.contains(".com");
        boolean valid2 = email.contains("ir");
        boolean valid3 = email.contains(".org");
        return email.matches(emailRegex) && hasUpperCase && hasNumber && hasHyphen && (valid1 || valid2 || valid3);
    }

    public Result validatePasswordStrength(String password) {
        String specialChars = "?><,\"' ;:\\\\|][}{+=)(*&^%$#!";
        if (password.length() < 8) {
            return new Result(false, "Password is not strong enough, it must be at least 8 characters long.");
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
}
