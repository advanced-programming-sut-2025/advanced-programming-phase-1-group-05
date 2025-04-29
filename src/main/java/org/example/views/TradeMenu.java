package org.example.views;

import org.example.controllers.MenuController;
import org.example.controllers.TradingController;
import org.example.models.Result;

import java.util.Scanner;

public class TradeMenu implements AppMenu {
    private final TradingController tradingController;
    private final MenuController menuController;

    public TradeMenu(TradingController tradingController, MenuController menuController){
        this.tradingController = tradingController;
        this.menuController = menuController;
    }

    @Override
    public void handleUserInput(String input) {
        if (input.matches("trade\\s+-u\\s+.*\\s+-t\\s+(offer|request)\\s+-a\\s+\\d+\\s+.*")){
            System.out.println(tradingController.trade(input).getMessage());
        }
        else if (input.matches("trade\\s+response\\s+(-accept|-reject)\\s+-i\\s+\\d+")){
            System.out.println(tradingController.respondToTrade(input));
        }
        else if (input.equals("trade list")){
            System.out.println(tradingController.showTradeList());
        }
        else if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        }
        else {
            System.out.println("Invalid command!");
        }
    }

    @Override
    public String getMenuName() {
        return "trade menu";
    }
}
