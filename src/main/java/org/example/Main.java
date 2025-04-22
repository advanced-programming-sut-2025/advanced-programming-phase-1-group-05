package org.example;

import java.util.Scanner;
import controllers.MenuController;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MenuController menuController = new MenuController(scanner);
        menuController.run();
    }
}