package org.example;

import java.io.IOException;
import java.util.Scanner;

import org.example.controllers.MenuController;
import org.example.models.Game;

public class Main2 {
    public static void main(String[] args) throws IOException {
//        DBController.loadUsers();
        Scanner scanner = Game.getScanner();
        MenuController menuController = new MenuController(scanner);
        menuController.run();

    }
}
