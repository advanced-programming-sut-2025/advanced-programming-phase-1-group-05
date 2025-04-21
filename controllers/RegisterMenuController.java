package controllers;

import models.Enums.RegisterMenuCommand;
import models.Result;
import models.User;
import models.UserDatabase;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
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


        if (!isValidUsername(username)) {
            return new Result(false,
                    "Invalid username!");
        }

        if (!isValidEmail(email)) {
            return new Result(false,
                    "Invalid email format!");
        }

        Result passwordValidation = validatePasswordStrength(password);
        if (!passwordValidation.isSuccess()) {
            return passwordValidation;
        }

        if (!password.equals(confirmPassword)) {
            return new Result(false, "Password and confirm password do not match!");
        }
        if (input.equalsIgnoreCase("random password")) {
            String randomPassword = generateRandomPassword();
            return new Result(true, randomPassword);
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

    public String generateRandomPassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "?><,\"' ;:\\\\/|][}{+=)(*&^%$#!";
        String allChars = upperCase + lowerCase + numbers + specialChars;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = 0; i < 4; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }

        return new String(passwordArray);
    }

    public void saveSecurityQuestion(String questionNumber, String answer) {
//        currentUser.setSecurityQuestion(questionNumber);
//        currentUser.setSecurityAnswer(answer);
//        userRepository.save(currentUser);
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

    private boolean isValidPassword(String password) {
        String allowedSpecialChars = "?><,\"' ;:\\\\/|][}{+=)(*&^%$#!";
        String allowedCharsRegex = "[a-zA-Z0-9" + Pattern.quote(allowedSpecialChars) + "]+";


        if (!password.matches(allowedCharsRegex)) {
            return false;
        }

        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[" + Pattern.quote(allowedSpecialChars) + "].*");

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }

}