package org.example;

import java.util.Scanner;

import org.example.controllers.DBController;
import org.example.controllers.MenuController;

public class Main {
    public static void main(String[] args) {
//        DBController.loadUsers();
        Scanner scanner = new Scanner(System.in);
        MenuController menuController = new MenuController(scanner);
        menuController.run();
    }
}