package org.example.controllers;

import org.example.models.Enums.RegisterMenuCommand;
import org.example.models.Result;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenuController {
    private final Scanner scanner;
    public static User currentUser;
    private static final int MAX_PASSWORD_ATTEMPTS = 3;

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

        if (UserDatabase.usernameExists(username)) {
            return new Result(false, "Username already exists!");
        }

        createUser(username,password,nickname,email,gender);


        return new Result(true, "User registered successfully!");
    }

    public static void createUser(String username,String password,String nickname
                                    ,String email,String gender) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setNickName(nickname);
        newUser.setEmail(email);
        newUser.setGender(gender);
        DBController.saveUsers();
        RegisterMenuController.currentUser = newUser;
        DBController.saveCurrentUser();
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
            default: return "Custom security question";
        }
    }
    private boolean isValidUsername(String username) {
        String allowedCharsRegex = "^[a-zA-Z0-9-]+$";

        boolean hasLower = username.matches(".*[a-z].*");
        boolean hasUpper = username.matches(".*[A-Z].*");
        boolean hasNumber = username.matches(".*.[0-9].*");
        boolean hasHyphen = username.contains("-");

        return username.matches(allowedCharsRegex) && hasLower && hasUpper && hasHyphen && hasNumber;
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

        boolean hasUpperCase = email.matches(".*[A-Z].*");
        boolean hasNumber = email.matches(".*.[0-9].*");
        boolean hasHyphen = email.contains("-");
        boolean valid1 = email.contains(".com");
        boolean valid2 = email.contains("ir");
        boolean valid3 = email.contains(".org");





        return email.matches(emailRegex) && hasUpperCase && hasNumber && hasHyphen && (valid1 || valid2 || valid3);
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

    public Result handleRegistration(String initialInput) {
        int attempts = 0;
        String currentInput = initialInput;

        while (attempts < MAX_PASSWORD_ATTEMPTS) {
            Result result = registerUser(currentInput);
            if (result.isSuccess()) {
                return result; // موفقیت‌آمیز
            }

            if (result.getMessage().equals("Password and confirm password do not match!")) {
                System.out.println("Password and confirm password do not match!");
                System.out.println("1. Re-enter password manually");
                System.out.println("2. Generate random password");
                System.out.println("3. Back to registration menu");

                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1":
                        System.out.println("Please enter your new password and confirmation:");
                        System.out.println("Format: -p <newPassword> <confirmPassword>");
                        String newPasswordInput = scanner.nextLine().trim();
                        currentInput = updatePasswordInCommand(currentInput, newPasswordInput);
                        break;
                    case "2":
                        return handleRandomPasswordOption(currentInput);
                    case "3":
                        return new Result(false, "Returning to registration menu");
                    default:
                        System.out.println("Invalid choice, please try again");
                        attempts++;
                        continue;
                }
                attempts++;
            } else {
                return result; // خطاهای دیگر
            }
        }
        return new Result(false, "Maximum password attempts reached");
    }

    public String updatePasswordInCommand(String originalCommand, String passwordInput) {
        String[] originalParts = originalCommand.split("\\s+");
        String[] passwordParts = passwordInput.split("\\s+");

        String newPassword = "";
        String newConfirm = "";
        for (int i = 0; i < passwordParts.length; i++) {
            if (passwordParts[i].equals("-p") && i + 2 < passwordParts.length) {
                newPassword = passwordParts[i+1];
                newConfirm = passwordParts[i+2];
                break;
            }
        }

        if (newPassword.isEmpty()) {
            return originalCommand;
        }

        StringBuilder updatedCommand = new StringBuilder();
        for (int i = 0; i < originalParts.length; i++) {
            if (i > 0) updatedCommand.append(" ");
            if (originalParts[i].equals("-p") && i + 2 < originalParts.length) {
                updatedCommand.append("-p ").append(newPassword).append(" ").append(newConfirm);
                i += 2;
            } else {
                updatedCommand.append(originalParts[i]);
            }
        }
        return updatedCommand.toString();
    }

    public Result handleRandomPasswordOption(String originalCommand) {
        String randomPassword = generateRandomPassword();
        System.out.println("Your random password is: " + randomPassword);
        System.out.println("Do you want to use this password? (yes/no)");

        while (true) {
            String response = scanner.nextLine().trim();
            if (response.equals("yes")) {
                String updatedCommand = updatePasswordInCommand(originalCommand,
                        "-p " + randomPassword + " " + randomPassword);
                Result result = registerUser(updatedCommand);
                return result;
            } else if (response.equals("no")) {
                System.out.println("Choose an option:");
                System.out.println("1. Generate another random password");
                System.out.println("2. Back to registration menu");

                String choice = scanner.nextLine().trim();
                if (choice.equals("1")) {
                    return handleRandomPasswordOption(originalCommand);
                } else if (choice.equals("2")) {
                    return new Result(false, "Returning to registration menu");
                } else {
                    System.out.println("Invalid choice, please try again");
                }
            } else {
                System.out.println("Please answer with 'yes' or 'no'");
            }
        }
    }

    public Result showSecurityQuestions() {
        System.out.println("Please select a security question:");
        System.out.println("1. What was your first pet's name?");
        System.out.println("2. What city were you born in?");
        System.out.println("3. What is your favorite color?");

        while (true) {
            String input = scanner.nextLine().trim();
            Matcher matcher = Pattern.compile(RegisterMenuCommand.PICK_QUESTION.getRegexPattern())
                    .matcher(input);

            if (matcher.matches()) {
                String questionNumber = matcher.group("questionNumber");
                String answer = matcher.group("answer");
                String confirmAnswer = matcher.group("confirmAnswer");

                if (!answer.equals(confirmAnswer)) {
                    return new Result(false, "Answers do not match!");
                }

                saveSecurityQuestion(questionNumber, answer);
                return new Result(true, "Registration completed successfully!");
            } else if (input.equalsIgnoreCase("back")) {
                return new Result(false, "Returning to registration menu");
            } else {
                System.out.println("Invalid command format!");
            }
        }
    }

}