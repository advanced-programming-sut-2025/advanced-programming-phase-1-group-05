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
        if (input.matches("trade\\s+-u\\s+.*\\s+-t\\s+(offer|request)\\s+-a\\s+\\d+\\s+.*")){
            System.out.println(tradingController.trade(input).getMessage());
        }
        else if (input.equals("trade list")){
            System.out.println(tradingController.showTradeList());
        }
    }

    @Override
    public String getMenuName() {
        return "trade menu";
    }
}
