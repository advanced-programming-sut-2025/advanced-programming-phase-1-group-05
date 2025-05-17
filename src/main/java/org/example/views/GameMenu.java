package org.example.views;

import org.example.controllers.GameManager;
import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.controllers.StoreController;
import org.example.models.Enums.GameMenuCommands;
import org.example.models.Game;
import org.example.models.GameTile;
import org.example.models.Result;
import org.example.models.TimeAndDate;

import java.util.AbstractMap;
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
        else if (input.equals("time")) {
            Result result = gameMenuController.showTime();
            System.out.println(result.getMessage());
        }
        else if (input.equals("date")) {
            Result result = gameMenuController.showDate();
            System.out.println(result.getMessage());
        }
        else if (input.equals("datetime")) {
            Result result = gameMenuController.showDatetime();
            System.out.println(result.getMessage());
        }
        else if (input.equals("day of the week")) {
            Result result = gameMenuController.showDayOfTheWeek();
            System.out.println(result.getMessage());
        }
        else if (input.startsWith("cheat advance time")) {
            input = input.trim();
            try {
                String[] parts = input.split(" ");
                String hoursPart = parts[3];
                int hours = Integer.parseInt(hoursPart.replace("h", ""));
                if (hours < 0) {
                    System.out.println("Error: You cannot go back in time.");
                } else {
                    GameManager.getGameClock().advanceTime(hours * 60);
                    System.out.println("Time advanced by " + hours + " hours.");
                }
            } catch (Exception e) {
                System.out.println("Invalid format.m" + e.getMessage());
            }
        }
        else if (input.startsWith("cheat advance date")) {
            Pattern pattern = Pattern.compile("^cheat advance date (\\d+)d$");
            matcher = pattern.matcher(input.trim());

            if (matcher.matches()) {
                int days = Integer.parseInt(matcher.group(1));
                if (days <= 0) {
                    System.out.println("Error: Number of days must be positive.");
                    return;
                }
                for (int i = 0; i < days; i++) {
                    GameManager.getGameClock().advanceDay();
                }
                System.out.println("Advanced " + days + " day(s).");
            } else {
                System.out.println("Invalid format! Use: cheat advance date <X>d");
            }
        }

        else if (input.equals("season")) {
            Result result = gameMenuController.showSeason();
            System.out.println(result.getMessage());
        }
        else if (input.equals("weather")) {
            Result result = gameMenuController.showWeather();
            System.out.println(result.getMessage());
        }
        else if (input.equals("weather forecast")) {
            Result result = gameMenuController.weatherForecast();
            System.out.println(result.getMessage());
        }
        else if (input.startsWith("cheat weather set")) {
            Result result = gameMenuController.cheatWeatherSet(input);
            System.out.println(result.getMessage());
        }
        else if ((matcher = GameMenuCommands.PrintMap.getMatcher(input)) != null) {
            System.out.println(gameMenuController.printMap(matcher));
        }
        else if ((matcher = GameMenuCommands.CheatThor.getMatcher(input)) != null) {
            System.out.println(gameMenuController.cheatThor(matcher));
        }
        else if (input.equals("greenhouse build")) {
            Result result = gameMenuController.buildGreenHouse();
            System.out.println(result.getMessage());
        }
        else if (input.equals("help reading map")) {
            gameMenuController.helpReadingMap();
        }
        else if ((matcher = GameMenuCommands.Walk.getMatcher(input)) != null) {
           System.out.println(gameMenuController.walkPlayer(matcher));
        }
        else if ((matcher = GameMenuCommands.ShowEnergy.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showEnergy().getMessage());
        } else if ((matcher = GameMenuCommands.EnergySetCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.setEnergy(Integer.parseInt(matcher.group("value"))).getMessage());
        } else if ((matcher = GameMenuCommands.EnergyUnlimitedCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.unlimitedEnergy().getMessage());
        } else if ((matcher = GameMenuCommands.ShowInventory.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showInventory().getMessage());
        } else if ((matcher = GameMenuCommands.InventoryTrash.getMatcher(input)) != null) {
            //boolean hasFlag = matcher.group("-n") != null;
            System.out.println(gameMenuController.removeFromInventory(matcher.group("itemName"),
                    Integer.parseInt(matcher.group("number"))));
        } else if ((matcher = GameMenuCommands.EquipTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.equipTool(matcher.group("toolName")));
        } else if ((matcher = GameMenuCommands.ShowCurrentTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showCurrentTool());
        } else if ((matcher = GameMenuCommands.ShowAvailableTools.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showAvailableTools());
        } else if ((matcher = GameMenuCommands.UpgradeTool.getMatcher(input)) != null) {
            System.out.println(new StoreController().upgradeTool(matcher.group("toolName")));
        } else if ((matcher = GameMenuCommands.ShowCraftInfo.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showCraftInfo(matcher.group("craftName")));
        } else if ((matcher = GameMenuCommands.UseTool.getMatcher(input)) != null) {
            System.out.println(gameMenuController.useTool(matcher.group("direction")));
        } else if((matcher = GameMenuCommands.ShowCraftInfo.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showCraftInfo(matcher.group("craftName")));
        } else if((matcher = GameMenuCommands.PlowTile.getMatcher(input)) != null) {
            System.out.println(gameMenuController.plowTile(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))));
        } else if((matcher = GameMenuCommands.Plant.getMatcher(input)) != null) {
            System.out.println(gameMenuController.plantSeed(matcher.group("seed"),matcher.group("direction")));
        } else if((matcher = GameMenuCommands.ShowPlant.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showPlant(new AbstractMap.SimpleEntry<>(Integer.parseInt(matcher.group("x")),
                    Integer.parseInt(matcher.group("y")))));
        } else if((matcher = GameMenuCommands.HowMuchWater.getMatcher(input)) != null) {
            System.out.println(gameMenuController.howMuchWaterLeft());
        } else if((matcher = GameMenuCommands.FertilizeCrop.getMatcher(input)) != null) {
            System.out.println(gameMenuController.fertilizeCrop(matcher.group("fertilizer"),
                    new AbstractMap.SimpleEntry<>(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")))));
        } else if((matcher = GameMenuCommands.PlaceItem.getMatcher(input)) != null) {
            System.out.println(gameMenuController.placeItem(matcher.group("itemName"), matcher.group("direction")));
        } else if((matcher = GameMenuCommands.AddItemCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.addItemCheatCode(matcher.group("itemName"),
                    Integer.parseInt(matcher.group("count"))));
        } else if((matcher = GameMenuCommands.EatFood.getMatcher(input)) != null) {
            System.out.println(gameMenuController.eatFood(matcher.group("foodName")));
        }
        else if ((matcher = GameMenuCommands.BuildAnimalHouse.getMatcher(input)) != null) {
            System.out.println(storeController.buildAnimalHouse(matcher));
        }
        else if ((matcher = GameMenuCommands.BuyAnimal.getMatcher(input)) != null) {
            System.out.println(storeController.buyAnimal(matcher));
        }
        else if ((matcher = GameMenuCommands.PetAnimal.getMatcher(input)) != null) {
            System.out.println(gameMenuController.petAnimal(matcher));
        }
        else if ((matcher = GameMenuCommands.SetFriendshipCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.cheatSetFriendship(matcher));
        }
        else if (input.equals("animals")) {
            System.out.println(gameMenuController.printAnimalsInfo());
        }
        else if ((matcher = GameMenuCommands.ShepherdAnimal.getMatcher(input)) != null) {
            System.out.println(gameMenuController.shepherdAnimal(matcher));
        }
        else if ((matcher = GameMenuCommands.FeedHay.getMatcher(input)) != null) {
            System.out.println(gameMenuController.feedHay(matcher));
        }
        else if (input.equals("produces")) {
            System.out.println(gameMenuController.printUncollectedProduce());
        }
        else if ((matcher = GameMenuCommands.CollectProduce.getMatcher(input)) != null) {
            System.out.println(gameMenuController.collectProduce(matcher));
        }
        else if ((matcher = GameMenuCommands.SellAnimal.getMatcher(input)) != null) {
            System.out.println(gameMenuController.sellAnimal(matcher));
        }
        else if ((matcher = GameMenuCommands.StartFishing.getMatcher(input)) != null) {
            System.out.println(gameMenuController.startFishing(matcher));
        }
        else if (input.startsWith("cheat max fishing skill")) {
            Game.getCurrentPlayer().getFishingSkill().setLevel(4);
        }
        else if ((matcher = GameMenuCommands.ArtisanUSe.getMatcher(input)) != null) {
            System.out.println(gameMenuController.useArtisan(matcher));
        }
        else if ((matcher = GameMenuCommands.ArtisanGet.getMatcher(input)) != null) {
            System.out.println(gameMenuController.artisanGet(matcher));
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
        } else if ((matcher = GameMenuCommands.Talk.getMatcher(input)) != null) {
            System.out.println(gameMenuController.talkToPlayer(matcher).getMessage());
        } else if (input.matches("talk\\s+history\\s+-u\\s+\\S+")) {
            System.out.println(gameMenuController.talkHistory(input).getMessage());
        } else if (input.equals("gift list")) {
            System.out.println(gameMenuController.showGiftList());
        } else if ((matcher = GameMenuCommands.Gift.getMatcher(input)) != null) {
            System.out.println(gameMenuController.giftPlayer(matcher));
        }
        else if ((matcher = GameMenuCommands.FriendshipPointsCC.getMatcher(input)) != null) {
            System.out.println(gameMenuController.cheatAddFriendshipPoints(matcher));
        }
        else if ((matcher = GameMenuCommands.RateGift.getMatcher(input)) != null) {
            System.out.println(gameMenuController.
                    rateTheGift(Integer.parseInt(matcher.group("giftNumber")), Integer.parseInt(matcher.group("rate"))));
        } else if ((matcher = GameMenuCommands.GiftHistory.getMatcher(input)) != null) {
            System.out.println(gameMenuController.showGiftHistory(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.Hug.getMatcher(input)) != null) {
            System.out.println(gameMenuController.hugPlayer(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.GiveBouquet.getMatcher(input)) != null) {
            System.out.println(gameMenuController.giveBouquet(matcher.group("username")));
        }
        else if ((matcher = GameMenuCommands.AskMarriage.getMatcher(input)) != null) {
            System.out.println(gameMenuController.askMarriage(matcher));
        }
        else if (input.matches("respond\\s+(-accept|-reject)\\s+-u\\s+.*")) {
            System.out.println(gameMenuController.respondToProposal(input));
        } else if (input.equals("start trade")) {
            menuController.enterMenu("trade");
        }
        else if (input.matches("friendship\\s+NPC\\s+list")) {
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
        }
        else if (input.startsWith("set coordinates *")) {
            input = input.substring(input.indexOf('*') + 1).trim();
            String[] coordinates = input.split(" ");
            int x = Integer.parseInt(coordinates[0]), y = Integer.parseInt(coordinates[1]);
            Game.getCurrentPlayer().setCoordinate(x, y);
            GameTile tile = Game.getGameMap().getTile(x, y);
            tile.occupy();
            System.out.println("set player coordinates to " + x + ", " + y);
        }
        else if (input.equals("show money")) {
            System.out.println(Game.getCurrentPlayer().getGold());
        }
        else if (input.equals("get shipping bin coordinates")) {
            Game.getCurrentPlayer().getFarm().getShippingBin().getCoordinates();
        } else if((matcher = GameMenuCommands.PutBack.getMatcher(input)) != null) {
            System.out.println(gameMenuController.putBack());
        }
        else {
            System.out.println("Invalid Command!");
        }
    }

    @Override
    public String getMenuName() {
        return "Game Menu";
    }
}