package org.example.views;

import org.example.controllers.TradingController;

import java.util.Scanner;

public class TradeMenu implements AppMenu {
    private TradingController tradingController;
    private Scanner scanner;
    public TradeMenu(TradingController tradingController, Scanner scanner){
        this.tradingController = tradingController;
        this.scanner = scanner;
    }

    @Override
    public void handleUserInput(String input) {
    }

    @Override
    public String getMenuName() {
        return "trade menu";
    }
}
