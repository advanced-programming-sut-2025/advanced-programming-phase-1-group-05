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
//            Result result = registerController.registerUser(input);
//            System.out.println(result.getMessage());
            handleRegistration(input);
        } else {
            System.out.println("Invalid Command!");
        }
    }
    private void handleRegistration(String initialInput) {
        int attempts = 0;
        String currentInput = initialInput;

        while (attempts < MAX_PASSWORD_ATTEMPTS) {
            Matcher randomMatcher = Pattern.compile(RegisterMenuCommand.RANDOM_PASSWORD.getRegexPattern())
                    .matcher(currentInput);

            if (randomMatcher.matches()) {
                String randomPassword = registerController.generateRandomPassword();
                System.out.println("Your random password is: " + randomPassword);
                System.out.println("Do you want to use this password? (yes/no)");

                String response = scanner.nextLine().trim();
                if (response.equalsIgnoreCase("yes")) {
                    currentInput = initialInput.replaceFirst(
                            "-p\\s+\\S+\\s+\\S+",
                            "-p " + randomPassword + " " + randomPassword
                    );
                } else {
                    System.out.println("1. Generate another random password");
                    System.out.println("2. Enter password manually");
                    System.out.println("3. Back to registration menu");

                    String choice = scanner.nextLine().trim();
                    switch (choice) {
                        case "1":
                            continue;
                        case "2":
                            System.out.println("Please enter your password and confirmation:");
                            currentInput = scanner.nextLine().trim();
                            break;
                        case "3":
                            return;
                        default:
                            System.out.println("Invalid choice, returning to registration menu");
                            return;
                    }
                }
            }


            Result result = registerController.registerUser(currentInput);
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                showSecurityQuestions();
                return;
            }

            if (result.getMessage().equals("Password and confirm password do not match!")) {
                System.out.println("Please re-enter your password and confirmation:");
                System.out.println("Or type 'back' to return to registration menu");

                String newInput = scanner.nextLine().trim();
                if (newInput.equalsIgnoreCase("back")) {
                    return;
                }

                currentInput = newInput;
                attempts++;
            } else {
                return;
            }
        }

        System.out.println("Maximum password attempts reached. Returning to registration menu");
    }

    private void showSecurityQuestions() {
        System.out.println("Please select a security question:");
        System.out.println("1. What was your first pet's name?");
        System.out.println("2. What city were you born in?");
        System.out.println("3. What is your mother's maiden name?");

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