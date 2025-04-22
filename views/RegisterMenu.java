package views;

import controllers.MenuController;
import controllers.RegisterMenuController;
import models.Enums.RegisterMenuCommand;
import models.Result;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterMenu implements views.AppMenu {
    private final MenuController menuController;
    private final RegisterMenuController registerController;
    private final Scanner scanner;
    private static final int MAX_PASSWORD_ATTEMPTS = 3;

    public RegisterMenu(MenuController menuController, RegisterMenuController registerController,Scanner scanner) {
        this.menuController = menuController;
        this.registerController = registerController;
        this.scanner = scanner;
    }

    @Override
    public void handleUserInput(String input) {
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        }else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
            System.exit(0);
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.startsWith("register ")) {
            handleRegistration(input);
        } else {
            System.out.println("Invalid Command!");
        }
    }
    private void handleRegistration(String initialInput) {
        int attempts = 0;
        String currentInput = initialInput;

        while (attempts < MAX_PASSWORD_ATTEMPTS) {

            Result result = registerController.registerUser(currentInput);
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                showSecurityQuestions();
                return;
            }

            if (result.getMessage().equals("Password and confirm password do not match!")) {
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
                        handleRandomPasswordOption(currentInput);
                        return; // Exit this method as handleRandomPasswordOption will handle the flow
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid choice, please try again");
                        attempts++;
                        continue;
                }
                attempts++;
            } else {
                // Other validation errors
                return;
            }
        }

        System.out.println("Maximum password attempts reached. Returning to registration menu");
    }

    private String updatePasswordInCommand(String originalCommand, String passwordInput) {
        // Split both commands into parts
        String[] originalParts = originalCommand.split("\\s+");
        String[] passwordParts = passwordInput.split("\\s+");

        // Find the password parts in the new input
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
                i += 2; // skip the old password and confirmation
            } else {
                updatedCommand.append(originalParts[i]);
            }
        }

        return updatedCommand.toString();
    }

    private void handleRandomPasswordOption(String originalCommand) {
        String randomPassword = registerController.generateRandomPassword();
        System.out.println("Your random password is: " + randomPassword);
        System.out.println("Do you want to use this password? (yes/no)");

        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("yes")) {
                // Split the original command into parts and rebuild it with the new password
                String[] parts = originalCommand.split("\\s+");
                StringBuilder updatedCommand = new StringBuilder();

                // Rebuild the command with the new password
                for (int i = 0; i < parts.length; i++) {
                    if (i > 0) updatedCommand.append(" ");
                    if (parts[i].equals("-p") && i + 2 < parts.length) {
                        updatedCommand.append("-p ").append(randomPassword).append(" ").append(randomPassword);
                        i += 2; // skip the old password and confirmation
                    } else {
                        updatedCommand.append(parts[i]);
                    }
                }
                Result result = registerController.registerUser(updatedCommand.toString());
                System.out.println(result.getMessage());

                if (result.isSuccess()) {
                    showSecurityQuestions();
                }
                return;

            } else if (response.equals("no")) {
                System.out.println("Choose an option:");
                System.out.println("1. Generate another random password");
                System.out.println("2. Back to registration menu");

                String choice = scanner.nextLine().trim();
                if (choice.equals("1")) {
                    handleRandomPasswordOption(originalCommand); // Recursive call
                    return;
                } else if (choice.equals("2")) {
                    return;
                } else {
                    System.out.println("Invalid choice, please try again");
                }
            } else {
                System.out.println("Please answer with 'yes' or 'no'");
            }
        }
    }

    private void showSecurityQuestions() {
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
                    System.out.println("Answers do not match! Please try again.");
                    continue;
                }

                registerController.saveSecurityQuestion(questionNumber, answer);
                System.out.println("Security question saved successfully!");
                System.out.println("Registration completed successfully! Redirecting to login menu...");
                menuController.enterMenu("login");
                break;
            } else if (input.equalsIgnoreCase("back")) {
                return;
            } else {
                System.out.println("Invalid command format! Please use:");
                System.out.println("pick question -q <questionNumber> -a <answer> -c <confirmAnswer>");
            }
        }
    }

    @Override
    public String getMenuName() {
        return "Register Menu";
    }
}
