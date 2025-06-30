package org.example.models;//package views;

import org.example.controllers.StoreController;
import org.example.models.Store;
import org.example.views.AppMenu;

import javax.swing.text.View;
import java.util.regex.Matcher;

public class StoreView implements AppMenu {
    private final Store store;
    private final StoreController controller;


    public StoreView(Store store, StoreController controller) {
        this.store = store;
        this.controller = controller;
    }

    @Override
    public void handleUserInput(String input) {
        Matcher matcher = null;
        //match user input and handle the command via store controller
    }

    @Override
    public String getMenuName() {
        return "";
    }
}
