package org.example.views;

import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.models.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final GameMenuController gameMenuController;

    private final String giftHistoryRegex = "gift\\s+history\\s+-u\\s+(?<username>.+?)";
    private final String rateGiftRegex = "gift\\s+rate\\s+-i\\s+(?<giftNumber>\\d+?)\\s+-r\\s+(?<rate>\\d+?)";
    public GameMenu(MenuController menuController, GameMenuController gameMenuController) {
        this.menuController = menuController;
        this.gameMenuController = gameMenuController;
    }

    @Override
    public void handleUserInput(String input) {
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.startsWith("game new")) {
            Result result = gameMenuController.newGame(input);
            System.out.println(result.getMessage());
        } else if (input.startsWith("game map")) {
            Result result = gameMenuController.chooseMap(input);
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("load game")) {
            Result result = gameMenuController.loadGame();
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("exit game")) {
            Result result = gameMenuController.exitGame();
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("next turn")) {
            Result result = gameMenuController.nextTurn();
            System.out.println(result.getMessage());
        }
        else if (input.equals("friendships")){
            System.out.println(gameMenuController.showFriendshipLevels().getMessage());
        }
        else if (input.matches("talk\\s+-u\\s+\\S+-m\\s+.*")){
            System.out.println(gameMenuController.talkToPlayer(input).getMessage());
        }
        else if (input.matches("talk\\s+history\\s+-u\\s+\\S+")){
            System.out.println(gameMenuController.talkHistory(input).getMessage());
        }
        else if (input.equals("gift list")) {
            System.out.println(gameMenuController.showGiftList());
        }
        else if (input.matches(rateGiftRegex)) {
            Matcher m = Pattern.compile(rateGiftRegex).matcher(input);
            System.out.println(gameMenuController.rateTheGift(Integer.parseInt(m.group("giftNumber")), Integer.parseInt(m.group("rate"))));
        }
        else if (input.matches(giftHistoryRegex)){
            Matcher m = Pattern.compile(giftHistoryRegex).matcher(input);
            System.out.println(gameMenuController.showGiftHistory(m.group("username")));
        }
        else if (input.equals("hug\\s+-u\\s+\\S+")){
            System.out.println();
        }
        else if (input.matches("respond\\s+(-accept|-reject)\\s+-u\\s+.*")){
            System.out.println(gameMenuController.respondToProposal(input));
        }
        else if (input.equals("friendship\\s+NPC\\s+list")){
            System.out.println(gameMenuController.NPCFriendshipLevels().getMessage());
        }
        else if (input.matches("meet\\s+NPC\\s+\\S+")){
            int CIndex = input.indexOf('C');
            input = input.substring(CIndex + 1).trim();
            System.out.println(gameMenuController.meetNPC(input).getMessage());
        }
        else if (input.matches("gift\\s+NPC\\s+\\S+\\s+-i\\s+.*")){
            System.out.println(gameMenuController.giftNPC(input).getMessage());
        }
        else if (input.matches("quests\\s+list")){
            System.out.println(gameMenuController.showAllQuests().getMessage());
        }
        else if (input.matches("quests\\s+finish\\s+-i\\s+\\d+")){
            System.out.println(gameMenuController.finishQuest(input).getMessage());
        }else {
            System.out.println("Invalid Command!");
        }
    }
    @Override
    public String getMenuName() {
        return "Game Menu";
    }
}