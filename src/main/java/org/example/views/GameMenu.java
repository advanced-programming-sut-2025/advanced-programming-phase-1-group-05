package org.example.views;

import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.controllers.StoreController;
import org.example.models.Enums.GameMenuCommands;
import org.example.models.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final GameMenuController gameMenuController;
    private final StoreController storeController = new StoreController();

    public GameMenu(MenuController menuController, GameMenuController gameMenuController) {
        this.menuController = menuController;
        this.gameMenuController = gameMenuController;
    }

    @Override
    public void handleUserInput(String input) {
        Matcher matcher;
        if (GameMenuController.canChooseMap) {
            if (!input.startsWith("game map")) {
                System.out.println("You should choose map!");
            }
        }
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
        else if (input.equalsIgnoreCase("user logout")){
            Result result = menuController.logoutUser();
            System.out.println(result.getMessage());
        }
        else if (input.startsWith("delete game")) {
            Result result = gameMenuController.deleteGame();
            System.out.println(result.getMessage());
        }
        else if ((matcher = GameMenuCommands.BuildAnimalHouse.getMatcher(input)) != null) {
            System.out.println(storeController.buildAnimalHouse(matcher));
        }
        else if ((matcher = GameMenuCommands.CollectProduce.getMatcher(input)) != null) {
            System.out.println(gameMenuController.collectProduce(matcher));
        }
        else if ((matcher = GameMenuCommands.Purchase.getMatcher(input)) != null) {
            System.out.println(storeController.purchase(matcher));
        } else if ((matcher = GameMenuCommands.CheatAddMoney.getMatcher(input)) != null) {
            System.out.println(gameMenuController.cheatAddMoney(Integer.parseInt(matcher.group("count"))));
        } else if (input.equals("show all products")) {
            System.out.println(storeController.showAllProducts());
        } else if (input.equals("show all available products")) {
            System.out.println(storeController.showAvailableProducts());
        } else if ((matcher = GameMenuCommands.Sell.getMatcher(input)) != null) {
            System.out.println(storeController.sell(matcher));
        } else if (input.equals("friendships")) {
            System.out.println(gameMenuController.showFriendshipLevels().getMessage());
        } else if (input.matches("talk\\s+-u\\s+\\S+-m\\s+.*")) {
            System.out.println(gameMenuController.talkToPlayer(input).getMessage());
        } else if (input.matches("talk\\s+history\\s+-u\\s+\\S+")) {
            System.out.println(gameMenuController.talkHistory(input).getMessage());
        } else if (input.equals("gift list")) {
            System.out.println(gameMenuController.showGiftList());
        } else if ((matcher = GameMenuCommands.Gift.getMatcher(input)) != null) {
            System.out.println(gameMenuController.giftPlayer(matcher));
        } else if ((matcher = GameMenuCommands.RateGift.getMatcher(input)) != null) {
            System.out.println(gameMenuController.
                    rateTheGift(Integer.parseInt(matcher.group("giftNumber")), Integer.parseInt(matcher.group("rate"))));
        } else if ((matcher = GameMenuCommands.GiftHistory.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showGiftHistory(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.Hug.getMatcher(input)) != null) {
            System.out.println(gameMenuController.hugPlayer(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.GiveBouquet.getMatcher(input)) != null) {
            System.out.println(gameMenuController.giveBouquet(matcher.group("username")));
        } else if (input.matches("respond\\s+(-accept|-reject)\\s+-u\\s+.*")) {
            System.out.println(gameMenuController.respondToProposal(input));
        } else if (input.equals("start trade")) {
            menuController.enterMenu("trade");
        }
        else if (input.equals("friendship\\s+NPC\\s+list")) {
            System.out.println(gameMenuController.NPCFriendshipLevels().getMessage());
        } else if (input.matches("meet\\s+NPC\\s+\\S+")) {
            int CIndex = input.indexOf('C');
            input = input.substring(CIndex + 1).trim();
            System.out.println(gameMenuController.meetNPC(input).getMessage());
        } else if (input.matches("gift\\s+NPC\\s+\\S+\\s+-i\\s+.*")) {
            System.out.println(gameMenuController.giftNPC(input).getMessage());
        } else if (input.matches("quests\\s+list")) {
            System.out.println(gameMenuController.showAllQuests().getMessage());
        } else if (input.matches("quests\\s+finish\\s+-i\\s+\\d+")) {
            System.out.println(gameMenuController.finishQuest(input).getMessage());
        } else if ((matcher = GameMenuCommands.ShowEnergy.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showEnergy().getMessage());
        } else if ((matcher = GameMenuCommands.EnergySetCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.setEnergy(Integer.parseInt(matcher.group("value"))).getMessage());
        } else if ((matcher = GameMenuCommands.EnergyUnlimitedCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.unlimitedEnergy().getMessage());
        } else if ((matcher = GameMenuCommands.ShowInventory.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showInventory().getMessage());
        } else if ((matcher = GameMenuCommands.InventoryTrash.getMatcher(input)) != null) {
            boolean hasFlag = matcher.group("-n") != null;
            System.out.println(gameMenuController.removeFromInventory(matcher.group("itemName"),
                    Integer.parseInt(matcher.group("number")), hasFlag));
        } else if ((matcher = GameMenuCommands.EquipTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.equipTool(matcher.group("toolName")));
        } else if ((matcher = GameMenuCommands.ShowCurrentTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showCurrentTool());
        } else if ((matcher = GameMenuCommands.ShowAvailableTools.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showAvailableTools());
        } else if ((matcher = GameMenuCommands.UpgradeTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.upgradeTool(matcher.group("toolName")));
        } else if ((matcher = GameMenuCommands.ShowCraftInfo.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showCraftInfo(matcher.group("name")));
        } else if ((matcher = GameMenuCommands.UseTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.useTool(matcher.group("direction")));
        } else {
            System.out.println("Invalid Command!");
        }
    }

    @Override
    public String getMenuName() {
        return "Game Menu";
    }
}