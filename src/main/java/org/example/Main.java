package org.example;

import java.io.IOException;
import java.util.Scanner;

import org.example.controllers.DBController;
import org.example.controllers.MenuController;
import org.example.models.Database;
import org.example.models.Enums.ArtisanType;
import org.example.models.Game;
import org.example.models.GameTile;

public class Main {
    public static void main(String[] args) throws IOException {
//        DBController.loadUsers();
        Scanner scanner = Game.getScanner();
        MenuController menuController = new MenuController(scanner);
        menuController.run();

    }
}