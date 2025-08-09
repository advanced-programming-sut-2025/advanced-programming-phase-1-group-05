package org.example.Server.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.example.Client.*;
import org.example.Common.*;
import org.example.Common.Enums.*;
import org.example.Common.Network.*;
import org.example.Main;
import org.example.Server.managers.LobbyManager;
import org.example.Server.models.*;
import org.example.Common.Tool.FishingPole;
import org.example.Common.Tool.Hoe;
import org.example.Common.Tool.Tool;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenuController extends MenuController {
    public static User currentUser = Main.currentUser;
    public static List<Player> selectedPlayers;
    private static Map<String, MyGame> activeGames = new HashMap<>();
    private MyGame pendingGame;
    public static boolean canChooseMap = false;
    public static boolean canDeleteGame = false;
    public static boolean[] canExitGame;
    private static Map<Integer, Integer> playerMapChoices = new HashMap<>();
    private GameMap map = MyGame.getGameMap();
    private List<User> players = new ArrayList<>();
    private static final Map<String, String> playerMapSelections = new HashMap<>();
    private GameScreen view;
    public static boolean canCheatThor = false;
    private final ClientNetworkManager connection = Main.getMain().getNetworkManager();

//    public GameMenuController(User currentUser, ClientNetworkManager connection) {
//        GameMenuController.currentUser = currentUser;
//        this.connection = connection;
//    }

//    public GameMenuController(User currentUser) {
//        this(currentUser, null);
//    }

    private NPC lastNPC = null;

    //LOBBY
    private static List<Lobby> activeLobbies = new ArrayList<>();
    private Lobby currentLobby = null;

    public Result submitAllMapSelections(Map<String, String> selections) {
        Set<String> chosenMaps = new HashSet<>(selections.values());

        if (chosenMaps.size() < selections.size()) {
            return Result.error("Each player must choose a unique map!");
        }

        playerMapSelections.clear();
        playerMapSelections.putAll(selections);

        for (Player player : selectedPlayers) {
            String mapName = selections.get(player.getUsername());
            if (mapName == null) return Result.error("Missing map for player: " + player.getUsername());

            int mapNum;
            switch (mapName.toLowerCase()) {
                case "map1": mapNum = 1; break;
                case "map2": mapNum = 2; break;
                case "map3": mapNum = 3; break;
                case "map4": mapNum = 4; break;
                default: return Result.error("Invalid map: " + mapName);
            }

            player.setMapNum(mapNum);
        }

        // ست کردن بازیکن فعلی
        for (Player player : selectedPlayers) {
            if (player.getUsername().equals(currentUser.getUsername())) {
                MyGame.setCurrentPlayer(player);
                break;
            }
        }

        MyGame.getAllPlayers().clear();
        MyGame.getAllPlayers().addAll(selectedPlayers);

        return MyGame.startTheGame();
    }

    public static String getMapForPlayer(String username) {
        return playerMapSelections.get(username);
    }

    public Result nextTurn() {
//        MyGame game = activeGames.get(currentUser.getUsername());
//        if (game == null) {
//            return Result.error("There is no active game.");
//        }
//        pendingGame = game;

        MyGame.getCurrentPlayer().increaseEnergy(-50);

        MyGame.advanceToNextPlayer();

        if (!MyGame.getCurrentPlayer().isEnergyUnlimited()) {
            MyGame.getCurrentPlayer().resetEnergy();
        }

        return Result.success("Now it's " + MyGame.getCurrentPlayer().getUsername() + "'s turn.");
    }

    private Result terminateGame() {
        try {
            pendingGame = null;
            activeGames.values().removeIf(game ->
                    selectedPlayers.contains(currentUser));

            for (Player player : selectedPlayers) {
                UserDatabase.setUserInGame(player.getUsername(), false);
            }

            selectedPlayers.clear();
            canChooseMap = false;
            MyGame.currentPlayerIndex = 0;
            canDeleteGame = false;

            MyGame.getAllPlayers().clear();

            return Result.success("Game deleted successfully!");
        } catch (Exception e) {
            return Result.error("Error deleting game: " + e.getMessage());
        }
    }

    //cheat code set energy
    public Result setEnergy(int value) {
        MyGame.getCurrentPlayer().setEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    public Result petAnimal(Animal animal) {
        Player currentPlayer = MyGame.getCurrentPlayer();
        if (Math.abs(animal.getX() - currentPlayer.getXX()) > 100 ||
            Math.abs(animal.getY() - currentPlayer.getYY()) > 100) {
            return Result.error("You need to get closer to the animal.");
        }
        animal.adjustFriendshipPoints(15);
        animal.setPetToday(true);
        return Result.success("You gently pet "+ animal.getName() +". It seems happy and lets out a content sound.");
    }

    public Result feedHay(Animal animal) {
        Player player = MyGame.getCurrentPlayer();
        Item hay = MyGame.getDatabase().getItem("Hay");
        if (player.getItemQuantity(hay) < 1)
            return Result.error("you don't have enough hay");
        animal.setFeedingStatus(true);
        player.getBackPack().removeFromInventory(hay, 1);
        animal.adjustFriendshipPoints(8);
        return Result.success("You offer food. The animal accepts. A bond is forged through snacks.");

    }

    public Result collectProduce(Animal animal) {
        if (animal == null)
            return Result.error("Selected animal doesn't exist or isn't yours");
        List<Product> products = animal.getUnCollectedProducts();
        if (products.isEmpty()) return Result.error("no uncollected products found");
        for (Product product : products) {
            MyGame.getCurrentPlayer().getBackPack().addToInventory(product, 1);
        }
        products.clear();
        animal.adjustFriendshipPoints(5);
        return new Result(true, "collected successfully");

    }

    public Result sellAnimal(AnimalActor animalActor) {
        Animal animal = animalActor.getAnimal();
        String animalName = animal.getName();
        int basePrice = MyGame.getDatabase().getItem(animal.getType().name()).getPrice();
        int price = (int) (basePrice * (((double) animal.getFriendshipPoints() / 1000) + 0.3));
        Player player = MyGame.getCurrentPlayer();
        player.addGold(price);
        player.removeAnimal(animal);
        view.removeAnimalActor(animalActor);
        return Result.success( animalName +" looked back one last time before leaving… but you were already gone.");
    }

    public FishType getRandomFish(FishingPole pole) {
        Player player = MyGame.getCurrentPlayer();
        int fishingLevel = player.getFishingSkill().getLevel();
        FishType caughtFish = FishType.getRandomFish(GameManager.getSeason(), fishingLevel);
        return caughtFish;
    }

    public Result cheatAddMoney(int amount) {
        Player player = MyGame.getCurrentPlayer();
        player.addGold(amount);
        return Result.success("added " + amount + " gold");
    }

    public Result talkToPlayer(Player targetPlayer, String message) {
        Player currentPlayer = MyGame.getCurrentPlayer();

        if (targetPlayer == null) return new Result(false, "Hmmm... either they moved away, or they never existed!");
        if (Math.abs(targetPlayer.getXX() - currentPlayer.getXX()) > 100 || Math.abs(targetPlayer.getYY() - currentPlayer.getYY()) > 100)
            return new Result(false, "You can't have a heart-to-heart with someone who's miles away!");
        //MyGame.addMessage(new Message(currentPlayer, targetPlayer, message));
        currentPlayer.changeFriendshipXP(20, targetPlayer);
        if (currentPlayer.isMarriedTo(targetPlayer)) {
            currentPlayer.increaseEnergy(50);
        }
        targetPlayer.addNotification(currentPlayer.getName() + " says: \"" + message + "\" to you!");
        return new Result(true, "");
    }


    public Result giftPlayer(Player targetPlayer, Item item, int amount) {
        Player currentPlayer = MyGame.getCurrentPlayer();
        if (Math.abs(targetPlayer.getYY() - currentPlayer.getYY()) > 100 ||
                Math.abs(targetPlayer.getXX() - currentPlayer.getXX()) > 100)
            return Result.error("You can't just throw gifts across the valley... get closer first!");
        if (currentPlayer.getItemQuantity(item) < amount)
            return Result.error("You hold out your gift... and reality holds out a calculator.");
        if (currentPlayer.getFriendshipLevel(targetPlayer) < 1)
            return Result.error("Maybe get to know them a little better before tossing gifts their way?");

        currentPlayer.getBackPack().getInventory().remove(item, amount);
        targetPlayer.getBackPack().getInventory().put(item, amount);
        MyGame.addGift(new Gift(currentPlayer, targetPlayer, item, amount));
        targetPlayer.addNotification(targetPlayer.getName() +
                "! You've been gifted! Hope it's not rocks again.");
        return new Result(true, "You handed over the gift with a smile. Let's hope they like it!");
    }
    public Result rateTheGift(Gift gift, int rating) {

        if (rating < 1 || rating > 5)
            return new Result(false, "enter a number between 1 to 5.");
        Player currentPlayer = MyGame.getCurrentPlayer();
        if (gift == null || !gift.getReceiver().equals(currentPlayer))
            return new Result(false,
                    "You stare into your empty hands and give it a " + rating + ". Interesting.");
        Player targetPlayer = gift.getSender();
        currentPlayer.changeFriendshipXP(((rating - 3) * 30 + 15), targetPlayer);
        gift.setRating(rating);
        return new Result(true, "rated successfully!");
    }

    public List<Gift> getReceivedGifts(Player otherPlayer) {
        Player player = MyGame.getCurrentPlayer();
        List<Gift> receivedGifts = new ArrayList<>();
        for (Gift gift : MyGame.getAllGifts()) {
            System.out.println("checking " + gift.getItem());
            if (gift.getSender().equals(otherPlayer) && gift.getReceiver().equals(player)) {
                receivedGifts.add(gift);
                System.out.println("recieved");
            }
        }
        return receivedGifts;
    }

    public List<Gift> getSentGifts(Player otherPlayer) {
        Player player = MyGame.getCurrentPlayer();
        List<Gift> sentGifts = new ArrayList<>();
        for (Gift gift : MyGame.getAllGifts()) {
            System.out.println("checking " + gift.getItem());
            System.out.println("actual sender : " + gift.getSender().getUsername() + " expecting sender : " + player.getUsername());
            if (gift.getSender().equals(player) && gift.getReceiver().equals(otherPlayer)) {
                sentGifts.add(gift);
                System.out.println("sent");
            }
        }
        return sentGifts;
    }

    public Result giveBouquet(Player targetPlayer){
        Player currentPlayer = MyGame.getCurrentPlayer();
        Item bouquet = MyGame.getDatabase().getItem("bouquet");
        if (targetPlayer == null)
            return Result.error("Bouquet in hand, heart full of hope... too bad that player doesn't even exist.");
//        if (Math.abs(targetPlayer.getXX() - currentPlayer.getXX()) > 1 ||
//                Math.abs(targetPlayer.getYY() - currentPlayer.getYY()) > 1)
//            return Result.error
//                    ("You wave the bouquet around like a romantic maniac, but there's no one nearby to impress");
        if (!currentPlayer.canGiveBouquet(targetPlayer))
            return Result.error("You try to hand over the bouquet-they smile politely and change the subject");
        if (currentPlayer.getItemQuantity(bouquet) < 1)
            return Result.error("You reach for the bouquet... but your inventory says 'not today, Romeo'.");
        currentPlayer.getBackPack().getInventory().remove(bouquet, 1);
        targetPlayer.getBackPack().getInventory().put(bouquet, 1);
        targetPlayer.changeLevel(currentPlayer, 3);
        return new Result(true,
                "They accepted the bouquet! Quick, act cool before your face turns red.");
    }

    public Result askMarriage(Player targetPlayer) {
        Item ring = MyGame.getDatabase().getItem("Wedding Ring");
        Player currentPlayer = MyGame.getCurrentPlayer();
        if (targetPlayer == null) return new Result(false,
                "Imaginary partners don't make great spouses.");
        if (Math.abs(currentPlayer.getXX() - targetPlayer.getXX()) > 100 ||
                Math.abs(currentPlayer.getYY() - targetPlayer.getYY()) > 100)
            return new Result(false, "Your love might be strong, but your range isn't. Get closer!");
        if (ring == null || currentPlayer.getItemQuantity(ring) == 0)
            return new Result(false, "You reach for the ring... but your pockets are full of nothing");
        if (!currentPlayer.canAskMarriage(targetPlayer))
            return new Result(false, "Slow down, lovebird-you're still just friendly acquaintances");
        if (!currentPlayer.getGender().equalsIgnoreCase("male"))
            return new Result(false,
                    "Only the boys can propose... for now. Rules of the valley, not mine!");

        currentPlayer.proposed(targetPlayer);
        return new Result(true, "Now we wait...");
    }

    public Result giftNPC(NPC npc, Item item) {
        Player player = MyGame.getCurrentPlayer();
        String itemName = item.getName();
        if (item instanceof Tool<?>)
            return new Result(false,
                    "Gifting your old tools? What’s next-handing out used socks?");
        lastNPC = npc;
        if (npc.isFavorite(itemName)) {
            npc.addFriendShipPoints(player, 200);
            return new Result(true,
                    "Wow, " + player.getName() + ", you know me so well. this " + itemName + " is my favorite.");
        }
        player.getBackPack().removeFromInventory(item, 1);
        npc.addFriendShipPoints(player, 50);
        return new Result(true, "Oh, a " + itemName + " ? Thanks, " + player.getName());
    }

    //plant seed on a specific tile
    public Result plantSeed(Item item, GameTile tile) {
        //errors
//        if (tile == null) return new Result(false, "Tile not found");
//        if (tile.getX() == MyGame.getCurrentPlayer().getCoordinate().getKey() && tile.getY() == MyGame.getCurrentPlayer().getCoordinate().getValue()) {
//            return new Result(false, "You stare at your boots. The boots stare back. Nothing grows.");
//        }
        if(item instanceof FruitAndVegetable || item instanceof Tree) {
            return new Result(false,"");
        }
        String seed = item.getName();
        boolean successful = MyGame.getCurrentPlayer().getFarmingSkill().plantSeed(seed, tile);
        if (successful) {
            if (tile.getTileType() != TileType.Soil)
                return new Result(false, "Tile is not plowed! Use your hoe to plow the tile!");
            if (!tile.isTileValidForPlanting()) return new Result(false,
                "You can't plant cause the tile is occupied!");
            MyGame.getCurrentPlayer().getBackPack().removeFromInventory(
                    MyGame.getCurrentPlayer().getBackPack().getFromInventory(seed), 1
            );
            return new Result(true, "Successfully planted " + seed);
        } else if (MyGame.getCurrentPlayer().getBackPack().getFromInventory(seed) == null)
            return new Result(false, "You don't have that seed in your inventory!");
        return new Result(false, "That's not a valid seed!");
    }

    //fertilize crop
    public Result fertilizeCrop(String fertilizer, GameTile tile) {
        Item item = tile.getItemOnTile();
        if (item == null && tile.getTileType() == TileType.Soil) {
            tile.fertilze(fertilizer);
            return new Result(true, "Fertilized tile successfully!");
        } else if (item instanceof FruitAndVegetable) {
            FruitAndVegetable fruit = (FruitAndVegetable) item;
            if (fruit.getAge() == 0) {
                boolean successful = MyGame.getCurrentPlayer().getFarmingSkill().fertilizeCrop(tile, fertilizer);
                if (successful) return new Result(true, "Successfully fertilized with " + fertilizer);
                else return new Result(false, "You don't have that kind of fertilizer");
            } else return new Result(false, "You can only fertilize tile before or the day of planting!");
        } else return new Result(false, "Can't fertilize this tile");
    }

    //place item on ground
    public Result placeItem(Item item, GameTile tile) {
        if (tile.getItemOnTile() != null)
            return new Result(false, "The tile is already occupied.");
        tile.setItemOnTile(item);
        MyGame.getCurrentPlayer().getBackPack().removeFromInventory(tile.getItemOnTile(), 1);
        return new Result(true, "Item placed successfully");
    }

    //add item cheat code
    public Result addItemCheatCode(String name, int count) {
        Item item = null;
        if (MyGame.getDatabase().getItem(name) != null) item = MyGame.getDatabase().getItem(name);
        else if (CropType.fromString(name) != null) item = CropType.fromString(name);
        else if (ForagingTreeSourceType.fromString(name) != null) item = ForagingTreeSourceType.fromString(name);
        else if (ForagingCrop.fromString(name) != null) item = ForagingCrop.fromString(name);
        else if (ForagingSeedType.fromString(name) != null) item = ForagingSeedType.fromString(name);
            //else if(CraftType.fromString(name) != null) item = CraftType.fromString(name);
            // else if(CookingRecipeType.fromString(name) != null) item = CookingRecipeType.fromString(name);
        else if (FishType.fromString(name) != null) item = FishType.fromString(name);
        else if (MineralType.fromString(name) != null) item = MineralType.fromString(name);

        if (item == null) return new Result(false, "** No item with that name exists **");
        if (count <= 0) return new Result(false, "** Not a valid count **");
        if (item.getName().contains("Pack")) {
            if (item.getName().equals("Large Pack")) {
                MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Big);
            } else if (item.getName().equals("Deluxe Pack")) {
                MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Deluxe);
            }
            return new Result(true, "Upgraded successfully");
            //change trash can
        } else if (item.getName().contains("Trash Can")) {
            if (item.getName().equals("Copper Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Brass);
            } else if (item.getName().equals("Steel Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iron);
            } else if (item.getName().equals("Gold Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Gold);
            } else if (item.getName().equals("Iridium Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iridium);
            }
            return new Result(true, "Upgraded successfully");
            //add craft recipes to learnt recipes
        } else if (item.getName().contains("Recipe")) {
            if (item.getName().equals("Dehydrator Recipe")) {
                //MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Dehydrator);
            } else if (item.getName().equals("Grass Starter Recipe")) {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.GrassStarter);
            } else if (item.getName().equals("Fish Smoker Recipe")) {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.FishSmoker);
            } else {
                String recipeName = item.getName().replace("Recipe", "").trim();
                CookingRecipeType type = CookingRecipeType.fromString(recipeName);
                System.out.println(type);
                MyGame.getCurrentPlayer().getBackPack().addLearntCookingRecipe(type);
            }
            return new Result(true, "Recipe added successfully");
        }
        MyGame.getCurrentPlayer().getBackPack().addToInventory(item, count);
        return new Result(true, "** " + count + " of " + name + " added to your inventory **");

    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Result cheatWeatherSet(String input) {
        try {
            String[] tokens = input.trim().split("\\s+");
            if (tokens.length != 4) {
                return Result.error("Invalid format. Usage: cheat weather set <Type>");
            }

            String weatherType = tokens[3];

            Weather newWeather;
            try {
                newWeather = Weather.valueOf(weatherType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return Result.error("Invalid weather type. Valid types: SUNNY, RAIN, STORM, SNOW");
            }

            MyGame.currentWeather = newWeather;

            MyGame.setForecastedWeather(newWeather);

            return Result.success("Weather changed to: " + newWeather);

        } catch (Exception e) {
            return Result.error("Error while setting weather: " + e.getMessage());
        }
    }

    public Result eatFood(Item food) {
        if (food instanceof Food) {
            GameAssetManager.playSfx("eat");
            int energy = ((Food) food).getEnergy();
            if(((Food) food).getRecipeType().Buff()) MyGame.getCurrentPlayer().setEnergy(200);
            else MyGame.getCurrentPlayer().increaseEnergy(energy);
            MyGame.getCurrentPlayer().getBackPack().removeFromInventory(food, 1);
            MyGame.getCurrentPlayer().setCurrentItem(null);
            return new Result(true, "You consumed the food successfully!");
        }
        else if (food instanceof  Product && ((Product) food).getDescription().equalsIgnoreCase("fish")) {
            GameAssetManager.playSfx("eat");
            MyGame.getCurrentPlayer().increaseEnergy(100);
            MyGame.getCurrentPlayer().getBackPack().removeFromInventory(food, 1);
            MyGame.getCurrentPlayer().setCurrentItem(null);
            return new Result(true, "You consumed the fish successfully!");
        }else return new Result(false, "That's...not edible.");
    }

    private static class Node {
        int x, y;
        Node parent;

        public Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }


    public Result loadGame() {
//        if (!currentUser.getUsername().equals(MyGame.getCurrentPlayer().getUsername())
//                || !currentUser.getUsername().equals(selectedPlayers.get(0).getUsername())) {
//            for (int i = 0; i < selectedPlayers.size(); i++) {
//                if (currentUser.getUsername().equals(selectedPlayers.get(i).getUsername())) {
//                    canExitGame[i] = true;
//                }
//            }
//        }
        User.haveSavedGame = true;
        if (User.haveSavedGame) {
            DBController.loadAllUsers(); // یا UserDatabase.loadUsers();
            UserDatabase.loadUsers();
            DBController.loadGameState();
            if (RegisterMenuController.currentUser != null) {
                Player currentPlayer = MyGame.getPlayerByUsername(RegisterMenuController.currentUser.getUsername());
                if (currentPlayer != null) {
                    MyGame.setCurrentPlayer(currentPlayer);
                    System.out.println("Current player set to: " + currentPlayer.getUsername());
                } else {
                    System.err.println("Could not find a player for the logged in user.");
                }
            }
            return Result.success("Game loaded successfully :)");
        }
        return Result.error("There is no Game to continue!");
    }

    public Result newGame(String input) {
        Pattern pattern = Pattern.compile(
            "^game new(?: -u (?<username>[\\w-]+))" +
                "(?: -u (?<username2>[\\w-]+))?" +
                "(?: -u (?<username3>[\\w-]+))?" +
                "(?<extra> -u [\\w-]+)*$"
        );

        Matcher matcher = pattern.matcher(input.trim());

        if (!matcher.matches()) {
            return Result.error("Invalid command format! Correct format: 'game new -u <username> [-u <username2>] [-u <username3>]'");
        }
        if (matcher.group("extra") != null) {
            return Result.error("Maximum 3 usernames allowed!");
        }

        if (matcher.group("username") == null) {
            return Result.error("At least one username must be provided!");
        }

        selectedPlayers = new ArrayList<>();
        selectedPlayers.add(new Player(currentUser));

        for (int i = 1; i <= 3; i++) {
            String username = matcher.group("username" + (i == 1 ? "" : i));
            if (username != null) {
                User user = UserDatabase.getUserByUsername(username);
                if (user == null) {
                    return Result.error("User '" + username + "' not found!");
                }
                if (UserDatabase.isUserInGame(username)) {
                    return Result.error("User '" + username + "' is already in another game!");
                }
                Player player = new Player(user);
                selectedPlayers.add(player);
                currentUser.addFriend(username);
                user.incrementGamesPlayed();
            }
        }

//        canChooseMap = true;
        canExitGame = new boolean[selectedPlayers.size()];
        Arrays.fill(canExitGame, false);

        for (Player player : selectedPlayers) {
            UserDatabase.setUserInGame(player.getUsername(), true);
        }

        MyGame.currentPlayerIndex = 0;
        playerMapChoices.clear();
        MyGame.getAllPlayers().addAll(selectedPlayers);
        DBController.savePlayersToFile();

        return Result.success("Now you can play Game:)");
    }

    public Result startGameIfReady() {
        if (canChooseMap) {
            return new Result(false, "Game already started or in progress.");
        }
        if (selectedPlayers == null || selectedPlayers.isEmpty()) {
            return new Result(false, "At least 3 players are needed to start the game.");
        }

        if (selectedPlayers.size() < 3) {
            return new Result(false, "At least 3 players are needed to start the game.");
        }

        canChooseMap = true;
        return new Result(true, "All players added. Please choose your maps.");
    }

    public Result deleteGame() {
        if (selectedPlayers.isEmpty()) {
            return Result.error("No active game to delete!");
        }
        Result result = terminateGame();

        if (result.isSuccess()) {
            // پاک کردن فایل players.json
            FileHandle file = Gdx.files.local("players.json");
            if (file.exists()) {
                file.writeString("", false);
            }
        }

        return result;
    }

    private boolean canWalk(int x, int y) {
        for (Player player : MyGame.getAllPlayers()) {
            if (player.getFarm() != null) {
                if (player.getFarm().isInFarm(x, y) && !player.getFarm().isOwner(MyGame.getCurrentPlayer())) {
                    return false;
                }
            }
        }

        return true;
    }

    //todo - greenhouse
    public Result buildGreenHouse() {
        Player currentPlayer = MyGame.getCurrentPlayer();
        MyGame.canBuildGreenHouse = true;
        Item item = MyGame.getDatabase().getItem("wood");
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                GameTile.greenHouseBuilt = true;
            }
        }
        if (currentPlayer.getMapNum() == 1) {
            currentPlayer.getBackPack().removeFromInventory(item, -500);
            currentPlayer.addGold(-1000);
        } else if (currentPlayer.getMapNum() == 2) {
            currentPlayer.getBackPack().removeFromInventory(item, -500);
            currentPlayer.addGold(-1000);
        } else if (currentPlayer.getMapNum() == 3) {
            currentPlayer.getBackPack().removeFromInventory(item, -500);
            currentPlayer.addGold(-1000);
        } else if (currentPlayer.getMapNum() == 4) {
            currentPlayer.getBackPack().removeFromInventory(item, -500);
            currentPlayer.addGold(-1000);
        }
        System.out.println("after building GreenHouse : Gold-> " + currentPlayer.getGold());
        System.out.println("after building GreenHouse : Wood-> " + currentPlayer.getItemQuantity(item));

        return new Result(true, "Green House built!");
    }
    // بررسی قابل امکان رد شدن از یک تایل
    private boolean isWalkable(int x, int y) {
        GameTile tile = GameMap.getTile(x, y);
        return tile != null &&
                tile.getTileType() != TileType.Water &&
                tile.getTileType() != TileType.Stone &&
                !tile.isOccupied() &&
                canWalk(x, y);
    }

    // بازسازی مسیر از آخرین گره
    private List<Point> reconstructPath(Node node) {
        List<Point> path = new ArrayList<>();
        while (node != null) {
            path.add(new Point(node.x, node.y));
            node = node.parent;
        }
        Collections.reverse(path);
        return path.subList(1, path.size()); //delete start location
    }


    //handle cheat codes TODO add yours!!
    public Result handleCheatCodes(String command) {
        Matcher matcher = null;
        if((matcher = GameMenuCommands.AddItemCC.getMatcher(command)) != null) {
            String itemName = matcher.group("itemName");
            int count = Integer.parseInt(matcher.group("count"));
            return addItemCheatCode(itemName, count);
        }else if ((matcher = GameMenuCommands.CheatThor.getMatcher(command)) != null || command.equalsIgnoreCase("cheat Thor")) {
            canCheatThor = true;

            if (view != null) {
                view.triggerLightningEffect();
            }

            return Result.success("⚡ Thor's wrath has been unleashed! ⚡");
        } else if ((matcher = GameMenuCommands.AdvanceDate.getMatcher(command)) != null) {
            int day = Integer.parseInt(matcher.group("x"));
            for (int i = 0 ;i < day; i++) {
                GameManager.getGameClock().advanceDay();
            }
            return Result.success("advanced date!");
        } else if ((matcher = GameMenuCommands.AdvanceTime.getMatcher(command)) != null) {
            int time = Integer.parseInt(matcher.group("x"));
            GameManager.getGameClock().advanceTime(time*60);
            return Result.success("advanced time!");
        } else if (command.startsWith("cheat weather set")) {
            return cheatWeatherSet(command);
        } else if((matcher = GameMenuCommands.EnergySetCC.getMatcher(command)) != null) {
            int value = Integer.parseInt(matcher.group("value"));
            return setEnergy(value);
        }
        else if ((matcher = GameMenuCommands.CheatAddMoney.getMatcher(command)) != null) {
            int amount = Integer.parseInt(matcher.group("count"));
            return cheatAddMoney(amount);
        }
        return new Result(false, "Invalid command.");
    }


    public Item getItemByName(String itemName) {
        StringBuilder sb = new StringBuilder();
        sb.append(itemName.substring(0, 1).toUpperCase()).append(itemName.substring(1));
        String name = sb.toString();
        Item item = null;
        if (MyGame.getDatabase().getItem(name) != null) item = MyGame.getDatabase().getItem(name);
        else if (CropType.fromString(name) != null) item = CropType.fromString(name);
        else if (ForagingTreeSourceType.fromString(name) != null) item = ForagingTreeSourceType.fromString(name);
        else if (ForagingCrop.fromString(name) != null) item = ForagingCrop.fromString(name);
        else if (ForagingSeedType.fromString(name) != null) item = ForagingSeedType.fromString(name);
        else if (FishType.fromString(name) != null) item = FishType.fromString(name);
        else if (MineralType.fromString(name) != null) item = MineralType.fromString(name);
        return item;
    }
}
