package org.example.controllers;

import org.example.models.*;
import org.example.models.Enums.*;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Tool;
import org.w3c.dom.Node;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenuController extends MenuController {
    public static User currentUser;
    public static List<Player> selectedPlayers;
    private static Map<String, Game> activeGames = new HashMap<>();
    private Game pendingGame;
    public static boolean canChooseMap = false;
    public static boolean canDeleteGame = false;
    public static boolean canLoadGame = false;
    public static boolean[] canExitGame;
    private static Map<Integer, Integer> playerMapChoices = new HashMap<>();
    private GameMap map = Game.getGameMap();

    public GameMenuController(Scanner scanner, User currentUser) {
        super(scanner);
        GameMenuController.currentUser = currentUser;
    }

    private NPC lastNPC = null;

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

        canChooseMap = true;
        canExitGame = new boolean[selectedPlayers.size()];
        Arrays.fill(canExitGame, false);

        for (Player player : selectedPlayers) {
            UserDatabase.setUserInGame(player.getUsername(), true);
        }

        Game.currentPlayerIndex = 0;
        playerMapChoices.clear();
        Game.getAllPlayers().addAll(selectedPlayers);

        return Result.success(selectedPlayers.get(0).getUsername() +
                ", please choose your map (1-4):");
    }

    public Result chooseMap(String input) {
        if (!canChooseMap) {
            return Result.error("Not in map selection phase!");
        }
        Pattern pattern = Pattern.compile("^game map (?<mapNumber>[1-4])$");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return Result.error("Map num must be 1-4!");
        }
        int mapNumber = Integer.parseInt(matcher.group("mapNumber"));

        Player currentPlayer = selectedPlayers.get(Game.currentPlayerIndex);
        currentPlayer.setMapNum(mapNumber);

        Game.setCurrentPlayer(selectedPlayers.get(Game.currentPlayerIndex));
        playerMapChoices.put(Game.currentPlayerIndex, mapNumber);

        Game.currentPlayerIndex++;
        if (Game.currentPlayerIndex < selectedPlayers.size()) {
            return Result.success("Player " + selectedPlayers.get(Game.currentPlayerIndex).getUsername() +
                    ", please choose your map (1-4):");
        } else {
            canChooseMap = false;
            pendingGame = new Game();
            Result startResult = Game.startTheGame();
            if (startResult.isSuccess()) {
//                activeGames.put(currentUser.getUsername(), pendingGame);
                for (Player player : selectedPlayers) {
                    activeGames.put(player.getUsername(), pendingGame);
                }
                Game.setCurrentPlayer(selectedPlayers.get(0));
            }
            Game.setCurrentPlayer(selectedPlayers.get(0));
            return startResult;
        }
    }


    public Result loadGame() {
        if (!canLoadGame) {
            return Result.error("You should first exit pending Game!");
        }
        if (!currentUser.getUsername().equals(Game.getCurrentPlayer().getUsername())
                || !currentUser.getUsername().equals(selectedPlayers.get(0).getUsername())) {
            for (int i = 0; i < selectedPlayers.size(); i++) {
                if (currentUser.getUsername().equals(selectedPlayers.get(i).getUsername())) {
                    canExitGame[i] = true;
                }
            }
        }
        if (User.haveSavedGame) {
            DBController.loadGameState();
            return Result.success("Game loaded successfully :)");
        }
        return Result.error("There is no Game to continue!");
    }

    public Result exitGame() {
        boolean done = false;
        for (int i = 0; i < selectedPlayers.size(); i++) {
            if (currentUser.getUsername().equals(selectedPlayers.get(i).getUsername())) {
                if (canExitGame[i]) {
                    done = true;
                }
            }
        }
        if (currentUser.getUsername().equals(Game.getCurrentPlayer().getUsername()) ||
                currentUser.getUsername().equals(selectedPlayers.get(0).getUsername()) || done) {
            pendingGame = null;
            canLoadGame = true;
            DBController.saveGameState();
            return Result.success("Exited Game successfully!");
        }
        return Result.error("You can't exit the game!");
    }


    public Result nextTurn() {
        Game game = activeGames.get(currentUser.getUsername());
        if (game == null) {
            return Result.error("There is no active game.");
        }
        pendingGame = game;

//        if (!currentUser.getUsername().equals(Game.getCurrentPlayer().getUsername())) {
//            return Result.error("It's not your turn.");
//        }

        Game.getCurrentPlayer().increaseEnergy(-50);

        Game.advanceToNextPlayer();
        //reset energy for next turn??
        if (!Game.getCurrentPlayer().isEnergyUnlimited()) {
            Game.getCurrentPlayer().resetEnergy();
        }

        return Result.success("Now it's " + Game.getCurrentPlayer().getUsername() + "'s turn." +
                Game.getCurrentPlayer().getNotifications());
    }

    public Result deleteGame() {
        Scanner scanner = Game.getScanner();
        int[] OK = new int[selectedPlayers.size()];
        System.out.println("Vote to delete the game (1 for yes, 0 for no):");
        for (int i = 0; i < selectedPlayers.size(); i++) {
            Player player = selectedPlayers.get(i);
            System.out.print(player.getUsername() + "'s vote: ");
            OK[i] = scanner.nextInt();
        }
        for (int i = 0; i < selectedPlayers.size(); i++) {
            if (OK[i] != 1) {
                return Result.error("Game deletion canceled! Not all players agreed.");
            }
        }

        return terminateGame();
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
            Game.currentPlayerIndex = 0;
            canDeleteGame = false;

            Game.getAllPlayers().clear();

            return Result.success("Game deleted successfully!");
        } catch (Exception e) {
            return Result.error("Error deleting game: " + e.getMessage());
        }
    }

    //for showing current player's energy level
    public Result showEnergy() {
        return new Result(true, "energy :" + Game.getCurrentPlayer().getEnergy());
    }

    //cheat code set energy
    public Result setEnergy(int value) {
        Game.getCurrentPlayer().setEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    //cheat code unlimited energy
    public Result unlimitedEnergy() {
        Game.getCurrentPlayer().setUnlimitedEnergy();
        return new Result(true, "** your energy is unlimited now **");
    }

    //showing the items in inventory
    public Result showInventory() {
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getBackPack().getInventory();
        StringBuilder output = new StringBuilder();
        if (items.isEmpty()) return new Result(false, "Inventory is empty");
        for (Item item : items.keySet()) {
            if (items.get(item) != 0)
                output.append(items.get(item)).append(" of ")
                        .append(item.getName())
                        .append("\n");

        }
        return new Result(true, output.toString());

    }

    //removing from inventory
    public Result removeFromInventory(String name, int quantity) {
        Game.getCurrentPlayer().getTrashCan().removeFromInventory(name, quantity);
        ItemLevel itemLevel = Game.getCurrentPlayer().getTrashCan().getLevel();
        Item itemToRemove = null;
        for(Item item : Game.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if(item.getName().equals(name)) {
                itemToRemove = item;
            }
        }
        if (itemToRemove != null) {
            int moneyAdded = (int) (itemToRemove.getPrice()*itemLevel.getTrashcanCoeff());
            Game.getCurrentPlayer().addGold(moneyAdded);
        }
        return new Result(true, quantity + " of " + name + " was removed from inventory");
    }

    //equip a certain tool
    public Result equipTool(String name) {
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getBackPack().getInventory();
        for (Item item : items.keySet()) {
            if (item.getName().equals(name)) {
                items.put(item, items.get(item) - 1);
                Game.getCurrentPlayer().setCurrentItem(item);
                return new Result(true, "You're now equipped with " + item.getName());
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

    //showing current tool
    public Result showCurrentTool() {
        Item currentItem = Game.getCurrentPlayer().getCurrentItem();
        if (currentItem instanceof Tool) {
            return new Result(true, "You are equipped with " + currentItem.getName());
        } else {
            return new Result(true, "You are not equipped with any tool");
        }
    }

    //showing available tools
    public Result showAvailableTools() {
        boolean found = false;
        StringBuilder output = new StringBuilder();
        for (Item i : Game.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if (i instanceof Tool) {
                output.append("* ").append(i.getName()).append("\n");
                found = true;
            }
        }
        if (!found) return new Result(true, "You don't have any tools in your inventory");
        return new Result(true, output.toString());
    }


    //use tool
    public Result useTool(String direction) {
        int[] dir = getDirections(direction);
        Item currentItem = Game.getCurrentPlayer().getCurrentItem();
        if (currentItem instanceof Tool) {
            String message = ((Tool) currentItem).use(new AbstractMap.SimpleEntry<>(Game.getCurrentPlayer().getCoordinate().getKey() + dir[1],
                    Game.getCurrentPlayer().getCoordinate().getValue() + dir[0])).getMessage();
            return new Result(true, message);
        } else return new Result(true, "You aren't equipped with any tool");
    }

    //show craft info
    public Result showCraftInfo(String name) {
        CropType cropType = CropType.fromString(name);
        TreeType treeType = TreeType.fromString(name);
        ForagingTreeSourceType foragingTreeSourceType = ForagingTreeSourceType.fromString(name);
        ForagingCrop foragingCrop = ForagingCrop.fromString(name);
        MineralType mineralType = MineralType.fromString(name);
        if (cropType != null) return new Result(true, cropType.toString());
        else if (treeType != null) return new Result(true, treeType.toString());
        else if (foragingTreeSourceType != null) return new Result(true, foragingTreeSourceType.toString());
        else if (foragingCrop != null) return new Result(true, foragingCrop.toString());
        else if (mineralType != null) return new Result(true, mineralType.toString());

        return new Result(true, "Couldn’t find the craft you're looking for");
    }

    public Result petAnimal(Matcher m) {
        String animalName = m.group("animalName");
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        if (animal == null) {
            return Result.error("animal doesn't exist or isn't yours");
        }
        Player currentPlayer = Game.getCurrentPlayer();
        if (Math.abs(animal.getX() - currentPlayer.getX()) > 1 ||
                Math.abs(animal.getY() - currentPlayer.getY()) > 1) {
            return Result.error("You need to get closer to the animal. animal coordinates: " + animal.getX() + " " + animal.getY());
        }
        animal.adjustFriendshipPoints(15);
        animal.setPetToday(true);
        return Result.success("You gently pet "+ animal.getName() +". It seems happy and lets out a content sound.");
    }

    public Result cheatSetFriendship(Matcher m) {
        String animalName = m.group("animalName");
        int amount = Integer.parseInt(m.group("amount"));
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        if (animal == null) return Result.error("animal doesn't exist or isn't yours");
        animal.setFriendshipPoints(amount);
        return Result.success("Friendship boosted! Nothing says ‘bonding’ like a little… code magic. Your animal is now contractually obligated to adore you");
    }

    public Result printAnimalsInfo() {

        Player player = Game.getCurrentPlayer();
        List<Animal> animals = player.getAllAnimals();
        StringBuilder output = new StringBuilder();
        output.append("Your animals: ").append("\n");
        for (Animal animal : animals) {
            output.append(animal.getName()).append("\t").append(animal.getFriendshipPoints()).append("\n");
        }
        return Result.success(output.toString());
    }

    public Result shepherdAnimal(Matcher m) {
        String animalName = m.group("animalName");
        int x = Integer.parseInt(m.group("x"));
        int y = Integer.parseInt(m.group("y"));
        Player player = Game.getCurrentPlayer();
        Animal animal = player.getAnimal(animalName);
        if (animal == null) return Result.error("animal doesn't exist or isn't yours");
        GameTile tile = Game.getGameMap().getTile(x, y);
        if (tile == null || tile.isOccupied() || !player.getFarm().containsTile(x , y)) {
            return Result.error("invalid tile");
        }
        animal.setXY(x, y);
        animal.shepherd();
        animal.setFeedingStatus(true);
        return Result.success( animal.getName()  +" follows your lead, trotting along obediently.");
    }
    public Result feedHay(Matcher m) {
        String animalName = m.group("animalName");
        Player player = Game.getCurrentPlayer();
        Animal animal = player.getAnimal(animalName);
        Item hay = Game.getDatabase().getItem("Hay");
        if (animal == null) return Result.error("animal doesn't exist or isn't yours");
        if (player.getItemQuantity(hay) < 1)
            return Result.error("you don't have enough hay");
        animal.setFeedingStatus(true);
        player.getBackPack().removeFromInventory(hay, 1);
        animal.adjustFriendshipPoints(8);
        return Result.success("You offer food. The animal accepts. A bond is forged through snacks.");

    }

    public Result printUncollectedProduce() {
        Player player = Game.getCurrentPlayer();
        List<Animal> animals = player.getAllAnimals();
        StringBuilder builder = new StringBuilder();
        for (Animal animal : animals) {
            for (Product product : animal.getUnCollectedProducts()) {
                builder.append(animal.getType()).append(animal.getName()).append(" has produced ").append(product.getName()).append("\n");
            }
        }
        return Result.success(builder.toString());
    }

    public Result collectProduce(Matcher m) {
        String animalName = m.group("name");
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        if (animal == null)
            return Result.error("Selected animal doesn't exist or isn't yours");

        List<Product> products = animal.getUnCollectedProducts();
        if (products.isEmpty()) return Result.error("no uncollected products found");
        for (Product product : products) {
            Game.getCurrentPlayer().getBackPack().addToInventory(product, 1);
        }
        products.clear();
        animal.adjustFriendshipPoints(5);
        return new Result(true, "collected successfully");

    }

    public Result sellAnimal(Matcher m) {
        String animalName = m.group("animalName");
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        if (animal == null)
            return Result.error("Selected animal doesn't exist or isn't yours");

        int basePrice = Game.getDatabase().getItem(animal.getType().name()).getPrice();
        int price = (int) (basePrice * ((animal.getFriendshipPoints() / 1000) + 0.3));
        Player player = Game.getCurrentPlayer();
        player.addGold(price);
        player.removeAnimal(animal);
        return Result.success( animal.getName() +" looked back one last time before leaving… but you were already gone.");
    }

    public Result startFishing(Matcher m) {
        String poleName = m.group("fishingPole");
        FishingPoleType pole = FishingPoleType.fromString(poleName);
        if (pole == null) return Result.error("invalid pole type.");
        Player player = Game.getCurrentPlayer();
        if (player.getItemQuantity(Game.getDatabase().getItem(poleName)) <= 0)
            return Result.error("You don't have that pole in your inventory");
        int fishingLevel = player.getFishingSkill().getLevel();
        Fish caughtFish = Fish.getRandomFish(GameManager.getSeason(), fishingLevel);
        if (caughtFish == null)
            return Result.error("Your bobber danced, your hopes rose… and then? Nothing. The fish must be laughing underwater.");
        Random rand = new Random();
        double weatherCoefficient = Game.getCurrentWeather().getFishingCoefficient();
        int numOfFish = Math.min((int) (rand.nextDouble() * weatherCoefficient * (fishingLevel + 2)), 6);
        int qualityScore = (int) ((rand.nextDouble() * (fishingLevel + 2) * pole.getFishingCoefficient()) / (7 - weatherCoefficient));
        ItemLevel level;
        if (qualityScore <= 0.5) level = ItemLevel.Normal;
        else if (qualityScore <= 0.7) level = ItemLevel.Brass;
        else if (qualityScore <= 0.9) level = ItemLevel.Gold;
        else level = ItemLevel.Iridium;
        Product product = new Product(caughtFish.getName(), caughtFish.getPrice(), -1, null, List.of(), Map.of());
        product.setItemLevel(level);
        Result result = player.getBackPack().addToInventory(product, numOfFish);
        player.getFishingSkill().increaseCapacity();
        if (result.isSuccess())
            return Result.success("You caught " + numOfFish + " " + level.toString() + " " + caughtFish.getName());
        return result;

    }

    public Result useArtisan(Matcher m) {
        String args = m.group("args");
        ArtisanType artisan = ArtisanType.getArtisan(args);
        if (artisan == null)
            return Result.error("That machine doesn’t seem to exist. Are you sure it’s real?");

        Player player = Game.getCurrentPlayer();
//        if (!player.getBackPack().hasThisCraft(artisan.getCraftType()))
//            return Result.error("Nope, artisan machines don’t have Wi-Fi. Go stand next to it!");
        if (artisan != null) artisan.useArtisan(args);
        return Result.success("");
    }

    public Result artisanGet(Matcher m) {
        String args = m.group("artisanName");
        ArtisanType artisan = ArtisanType.getArtisan(args);
        if (artisan == null)
            return Result.error("That machine doesn’t seem to exist. Are you sure it’s real?");
        List<ArtisanProduct> products = artisan.getProducts();
        StringBuilder builder = new StringBuilder();
        if (artisan.products.isEmpty())
            return Result.error("There's nothing in the machine-unless you're trying to process thin air");
        if (products.isEmpty())
            return Result.error("Hold tight! The machine’s still working on it.");
        for (ArtisanProduct product : products) {
            Game.getCurrentPlayer().getBackPack().addToInventory(product, 1);
            builder.append("added " + product.getName() + " to your inventory");
            artisan.products.remove(product);
        }
        return Result.success(builder.toString());
    }

    public Result cheatAddMoney(int amount) {
        Player player = Game.getCurrentPlayer();
        player.addGold(amount);
        return Result.success("added " + amount + " gold");
    }

    public Result showFriendshipLevels() {
        StringBuilder builder = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        for (Player player : Game.getAllPlayers()) {
            if (!player.equals(currentPlayer)) {
                builder.append(player.getName()).append("\tfriendship level: ").append(currentPlayer.getFriendshipLevel(player));
                builder.append("\t friendship points: ").append(currentPlayer.getFriendshipPoints(player)).append("\n");
            }
        }
        return new Result(true, builder.toString());
    }

    public Result talkToPlayer(Matcher m) {

        String username = m.group("username"), message = m.group("message");
        Player targetPlayer = Game.getPlayerByUsername(username);
        Player currentPlayer = Game.getCurrentPlayer();

        if (targetPlayer == null) return new Result(false, "Hmmm... either they moved away, or they never existed!");
        if (Math.abs(targetPlayer.getX() - currentPlayer.getX()) > 1 || Math.abs(targetPlayer.getY() - currentPlayer.getY()) > 1)
            return new Result(false, "You can't have a heart-to-heart with someone who's miles away!");
        Game.addMessage(new Message(currentPlayer, targetPlayer, message));
        currentPlayer.changeFriendshipXP(20, targetPlayer);
        if (currentPlayer.isMarriedTo(targetPlayer)) {
            currentPlayer.increaseEnergy(50);
        }
        targetPlayer.addNotification(targetPlayer.getName() + "! A little birdie dropped off a message-check your inbox!");
        return new Result(true, "");
    }

    public Result talkHistory(String input) {
        int uIndex = input.indexOf('u');
        input = input.substring(uIndex + 1).trim();
        Player player = Game.getPlayerByUsername(input);
        if (player == null) return new Result(false,
                "Talking to ghosts again? That player isn't real.");
        List<Message> messages = Game.getMessages(player, Game.getCurrentPlayer());
        if (messages.isEmpty()) return new Result(false,
                "Looks like you two haven't broken the ice yet");

        StringBuilder output = new StringBuilder();
        output.append("Talk history with ").append(input).append("\n");
        for (Message message : messages) {
            output.append(message.getSender().getName()).append(": ").append(message.getMessage()).append("\n");
        }
        return new Result(true, output.toString());
    }

    public Result cheatAddFriendshipPoints(Matcher m) {
        Player currentPlayer = Game.getCurrentPlayer();
        Player targetPlayer = Game.getPlayerByUsername(m.group("username"));
        if (targetPlayer == null) return Result.error("");
        int amount = Integer.parseInt(m.group("amount"));
        currentPlayer.changeFriendshipXP(amount, targetPlayer);
        return Result.success("added " + amount + " friendship points to " + targetPlayer.getName());
    }

    public Result giftPlayer(Matcher matcher) {
        String username = matcher.group("username"), itemName = matcher.group("itemName");
        int amount = Integer.parseInt(matcher.group("amount"));
        Item item = Game.getDatabase().getItem(itemName);
        Player currentPlayer = Game.getCurrentPlayer();
        Player targetPlayer = Game.getPlayerByUsername(username);
        if (targetPlayer == null) return Result.error("Invisible friends don't accept gifts, you know!");
        if (Math.abs(targetPlayer.getY() - currentPlayer.getY()) > 1 ||
                Math.abs(targetPlayer.getX() - currentPlayer.getX()) > 1)
            return Result.error("You can't just throw gifts across the valley... get closer first!");
        if (currentPlayer.getItemQuantity(item) < amount)
            return Result.error("You hold out your gift... and reality holds out a calculator.");
        if (currentPlayer.getFriendshipLevel(targetPlayer) < 1)
            return Result.error("Maybe get to know them a little better before tossing gifts their way?");

        currentPlayer.getBackPack().getInventory().remove(item, amount);
        targetPlayer.getBackPack().getInventory().put(item, amount);
        Game.addGift(new Gift(currentPlayer, targetPlayer, item, amount));
        targetPlayer.addNotification(targetPlayer.getName() +
                "! You've been gifted! Hope it's not rocks again.");
        return new Result(true, "You handed over the gift with a smile. Let's hope they like it!");
    }

    public Result showGiftList() {
        StringBuilder output = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        for (Gift gift : Game.getAllGifts()) {
            if (gift.getReceiver().equals(currentPlayer))
                output.append(gift.getId()).append(".  ").append(gift.getAmount()).append(" ")
                        .append(gift.getName()).append(" (s)\n");
        }
        if (output.toString().isEmpty())
            return Result.success("Still waiting for that surprise delivery. It'll happen... probably");
        return Result.success(output.toString());
    }

    public Result rateTheGift(int giftNumber, int rating) {

        if (rating < 1 || rating > 5)
            return new Result(false, "Your rating confused the chickens. Please try again.");
        Gift gift = Game.getGiftById(giftNumber);
        Player currentPlayer = Game.getCurrentPlayer();
        if (gift == null || !gift.getReceiver().equals(currentPlayer))
            return new Result(false,
                    "You stare into your empty hands and give it a " + rating + ". Interesting.");
        Player targetPlayer = gift.getSender();
        currentPlayer.changeFriendshipXP(((rating - 3) * 30 + 15), targetPlayer);
        return new Result(true, "They say don’t look a gift horse in the mouth-but you just did.");
    }

    public Result showGiftHistory(String username) {
        StringBuilder output = new StringBuilder();
        Player player = Game.getPlayerByUsername(username);
        for (Gift gift : Game.getAllGifts()) {
            if (gift.getSender().equals(player) || gift.getReceiver().equals(player)) {
                output.append(gift.getSender().getName()).append(" gave ").append(gift.getReceiver().getName()).append(" ")
                        .append(gift.getAmount()).append(" ").append(gift.getName()).append(" (s).\n");
            }
        }
        return new Result(true, output.toString());
    }

    public Result hugPlayer(String username) {
        Player targetPlayer = Game.getPlayerByUsername(username);
        Player currentPlayer = Game.getCurrentPlayer();
        if (targetPlayer == null)
            return new Result(false,
                    "You open your arms wide... but there's no one by that name to recieve it");
        if (Math.abs(targetPlayer.getX() - currentPlayer.getX()) > 1 ||
                Math.abs(targetPlayer.getY() - currentPlayer.getY()) > 1)
            return new Result(false, "They're not here to catch your hug. Maybe next time!");
        if (!currentPlayer.canHug(targetPlayer))
            return Result.error("They awkwardly sidestep the hug. Friendship takes time, pal.");
        currentPlayer.changeFriendshipXP(60, targetPlayer);
        if (currentPlayer.isMarriedTo(targetPlayer)) {
            currentPlayer.increaseEnergy(50);
        }
        return new Result(true, "You hugged them tight. Even the cows felt the love.");
    }

    public Result giveBouquet(String username) {
        Player currentPlayer = Game.getCurrentPlayer();
        Player targetPlayer = Game.getPlayerByUsername(username);
        Item bouquet = Game.getDatabase().getItem("bouquet");
        if (targetPlayer == null)
            return Result.error("Bouquet in hand, heart full of hope... too bad that player doesn't even exist.");
        if (Math.abs(targetPlayer.getX() - currentPlayer.getX()) > 1 ||
                Math.abs(targetPlayer.getY() - currentPlayer.getY()) > 1)
            return Result.error
                    ("You wave the bouquet around like a romantic maniac, but there's no one nearby to impress");
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

    public Result askMarriage(Matcher m) {
        String username = m.group("username");
        Player targetPlayer = Game.getPlayerByUsername(username);
        Item ring = Game.getDatabase().getItem("Wedding Ring");
        Player currentPlayer = Game.getCurrentPlayer();
        if (targetPlayer == null) return new Result(false,
                "Imaginary partners don't make great spouses.");
        if (Math.abs(currentPlayer.getX() - targetPlayer.getX()) > 1 ||
                Math.abs(currentPlayer.getY() - targetPlayer.getY()) > 1)
            return new Result(false, "Your love might be strong, but your range isn't. Get closer!");
        if (ring == null || currentPlayer.getItemQuantity(ring) == 0)
            return new Result(false, "You reach for the ring... but your pockets are full of nothing");
        if (!currentPlayer.canAskMarriage(targetPlayer))
            return new Result(false, "Slow down, lovebird-you're still just friendly acquaintances");
        if (!currentPlayer.getGender().equals("male"))
            return new Result(false,
                    "Only the boys can propose... for now. Rules of the valley, not mine!");

        currentPlayer.proposed(targetPlayer);
        return new Result(true, "Now we wait...");
    }

    public Result respondToProposal(String input) {
        String[] parts = input.split("\\s+");
        int uIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("-u")) {
                uIndex = i;
                break;
            }
        }
        String username = parts[uIndex + 1];
        Player targetPlayer = Game.getPlayerByUsername(username);
        Player currentPlayer = Game.getCurrentPlayer();
        Item ring = Game.getDatabase().getItem("Wedding Ring");
        if (targetPlayer == null)
            return Result.error("");
        if (input.contains("accept")) {
            if (!targetPlayer.hasProposed(currentPlayer))
                return Result.error("You dramatically accept... nothing. Nobody proposed, drama queen.");
            currentPlayer.changeLevel(targetPlayer, 4);
            currentPlayer.getBackPack().getInventory().put(ring, 1);
            if (targetPlayer.getItemQuantity(ring) < 1)
                return Result.error("You tried to accept, but their ring mysteriously disappeared. Awkward...");
            targetPlayer.getBackPack().getInventory().remove(ring, 1);
            currentPlayer.setSpouse(targetPlayer);
            targetPlayer.setSpouse(currentPlayer);
            return Result.success("You said yes! The wedding bells will ring soon.");
        } else if (input.contains("reject")) {
            if (!targetPlayer.hasProposed(currentPlayer))
                return Result.error("That's a bold rejection for someone who hasn't been proposed to.");
            currentPlayer.changeLevel(targetPlayer, 0);
            targetPlayer.setProposalRejectionDaysLeft(7);
            return Result.success("Well... that could’ve gone better.");
        }
        return new Result(true, "");
    }

    public Result NPCFriendshipLevels() {
        StringBuilder builder = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        builder.append("friendship status with NPCs: ");
        for (NPC npc : Game.getAllNPCs()) {
            builder.append("\n").append(npc.getName()).append("\t")
                    .append("friendship level: ").append(npc.getFriendshipLevel(currentPlayer))
                    .append(" friendship points: ").append(npc.getFriendshipPoints(currentPlayer));
        }
        return new Result(true, builder.toString());
    }

    public Result meetNPC(String npcName) {
        NPC npc = Game.getNPCByName(npcName);
        Player player = Game.getCurrentPlayer();
        if (npc == null) return new Result(false, "NPC not found");
        if (Math.abs(player.getX() - npc.getX()) > 1 || Math.abs(player.getY() - npc.getY()) > 1)
            return new Result(false,
                    "You're talking to thin air. That NPC must be off doing NPC things.");
        lastNPC = npc;
        npc.addFriendShipPoints(player, 20);
        return new Result(true, DialogueManager.getNpcDialogue(npcName, Game.getCurrentWeather().name()));
    }

    public Result giftNPC(String input) {
        int CIndex = input.indexOf('C');
        int iIndex = input.indexOf('-');
        String npcName = input.substring(CIndex + 1, iIndex).trim();
        String itemName = input.substring(iIndex + 2).trim();
        NPC npc = Game.getNPCByName(npcName);
        if (npc == null) return new Result(false, "NPC not found");
        Player player = Game.getCurrentPlayer();
        Item item = player.getBackPack().getFromInventory(itemName);
        if (Game.getDatabase().getItem(itemName) == null && item == null) return new Result(false, "Item not found");
        if (item == null)
            return Result.error("You don't have that item in your inventory");
        npc.recieveGift(item, player);
        if (Math.abs(player.getX() - npc.getX()) > 1 || Math.abs(player.getY() - npc.getY()) > 1)
            return new Result(false,
                    "Nice thought! But you can’t give a gift to thin air. Find " + npcName + " first!");
        if (item instanceof Tool<?>)
            return new Result(false,
                    "Gifting your old tools? What’s next—handing out used socks?");
        lastNPC = npc;
        if (npc.isFavorite(itemName)) {
            npc.addFriendShipPoints(player, 200);
            return new Result(true,
                    "Wow, " + player.getName() + ", you know me so well. this " + itemName + " is my favorite.");
        }
        npc.addFriendShipPoints(player, 50);
        return new Result(true, "Oh, a " + itemName + " ? Thanks, " + player.getName());
    }

    public Result showAllQuests() {
        Player player = Game.getCurrentPlayer();
        StringBuilder output = new StringBuilder();
        if (lastNPC == null) return Result.error("No NPC chosen");
        output.append(lastNPC.getName()).append(" quests for you");
        int index = 0;
        for (Map.Entry<String, Integer> entry : lastNPC.getRequests().entrySet()) {
            index++;
            output.append("\n").append(index).append(". ")
                    .append(entry.getValue()).append(" ").append(entry.getKey()).append("(s)");
            if (index == lastNPC.getNumOfUnlockedQuests(player)) break;
        }

        return new Result(true, output.toString());

    }

    public Result finishQuest(String input) {
        int iIndex = input.indexOf('-') + 1;
        input = input.substring(iIndex + 1).trim();
        int questIndex;
        try {
            questIndex = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid quest index");
        }
        if (lastNPC == null) return Result.error("No NPC chosen");
        Map.Entry<String, Integer> quest = lastNPC.getQuest(questIndex);
        if (quest == null) return new Result(false, "Quest not found");
        Player player = Game.getCurrentPlayer();
        if (Math.abs(player.getX() - lastNPC.getX()) > 1 || Math.abs(player.getY() - lastNPC.getY()) > 1) {
            return new Result(false,
                    "You can't wrap this up from here. Get back to " + lastNPC.getName() + " first!");
        }
        if (lastNPC.isCompleted(questIndex)) {
            return new Result(false,
                    "Another farmer's already taken care of that one. Why not lend a hand elsewhere?");
        }
        Item item = Game.getDatabase().getItem(quest.getKey());
        if (player.getItemQuantity(item) < quest.getValue()) {
            return new Result(false,
                    "Whoops! You're still a few shy of the total. Harvest more and pop back over!");
        }
        Map.Entry<String, Integer> reward = lastNPC.finishQuest(player, questIndex);
        if (reward.getKey().equalsIgnoreCase("gold coin"))
            player.addGold(reward.getValue());
        return new Result(true,
                "You got " + reward.getValue() + " " + reward.getKey() +
                        "(s) from " + lastNPC.getName() + " for finishing this quest.");
    }


    //plant seed on a specific tile
    public Result plantSeed(String seed, String direction) {
        int[] dir = getDirections(direction);
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(Game.getCurrentPlayer().getCoordinate().getKey() + dir[1], Game.getCurrentPlayer().getCoordinate().getValue() + dir[0]);
        //errors
        if (tile == null) return new Result(false, "Tile not found");
        if (tile.getX() == Game.getCurrentPlayer().getCoordinate().getKey() && tile.getY() == Game.getCurrentPlayer().getCoordinate().getValue()) {
            return new Result(false, "You stare at your boots. The boots stare back. Nothing grows.");
        }
        if (tile.getTileType() != TileType.Soil)
            return new Result(false, "Tile is not plowed! Use your hoe to plow the tile!");
        if (!tile.isTileValidForPlanting()) return new Result(false,
                "You can't plant cause the tile is occupied!");
        boolean successful = Game.getCurrentPlayer().getFarmingSkill().plantSeed(seed, tile);
        if (successful) {
            Game.getCurrentPlayer().getBackPack().removeFromInventory(
                    Game.getCurrentPlayer().getBackPack().getFromInventory(seed), 1
            );
            return new Result(true, "Successfully planted " + seed);
        } else if (Game.getCurrentPlayer().getBackPack().getFromInventory(seed) == null)
            return new Result(false, "You don't have that seed in your inventory!");
        return new Result(false, "That's not a valid seed!");
    }

    //show the plant on a specific tile
    public Result showPlant(Map.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Item item = tile.getItemOnTile();
        if (item == null) return new Result(false, "Nothing planted on this tile");
        if (item instanceof FruitAndVegetable) {
            FruitAndVegetable plant = (FruitAndVegetable) item;
            StringBuilder output = new StringBuilder();
            output.append("** plant information **\n")
                    .append(plant.toString());
            return new Result(true, output.toString());

        } else if (item instanceof Tree) {
            Tree tree = (Tree) item;
            StringBuilder output = new StringBuilder();
            output.append("** tree information **\n")
                    .append(tree.toString());
            return new Result(true, output.toString());
        } else {
            return new Result(false, "Nothing planted on this tile");
        }
    }

    //fertilize crop
    public Result fertilizeCrop(String fertilizer, Map.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Item item = tile.getItemOnTile();
        if (item == null) {
            tile.fertilze(fertilizer);
            return new Result(false, "Fertilized tile successfully!");
        } else if (item instanceof FruitAndVegetable) {
            FruitAndVegetable fruit = (FruitAndVegetable) item;
            if (fruit.getAge() == 0) {
                boolean successful = Game.getCurrentPlayer().getFarmingSkill().fertilizeCrop(coordinates, fertilizer);
                if (successful) return new Result(true, "Successfully fertilized with " + fertilizer);
                else return new Result(false, "You don't have that kind of fertilizer");
            } else return new Result(false, "You can only fertilize tile before or the day of planting!");
        } else return new Result(false, "Can't fertilize this tile");
    }

    //how much water left
    public Result howMuchWaterLeft() {
        int waterLeft = Game.getCurrentPlayer().getWateringCan().getWaterlevel();
        return new Result(true, waterLeft + " water units left in your watering can");
    }


    //place item on ground
    public Result placeItem(String itemName, String direction) {
        Item item = null;
        int[] directions = getDirections(direction);
        for (Item i : Game.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if (i.getName().equals(itemName)) {
                item = i;
            }
        }
        if (item == null) return new Result(false, "No such item in your inventory");
        else if (directions == null) return new Result(false, "Invalid direction");
        GameMap map = Game.getGameMap();
        GameTile tile = GameMap.getTile(Game.getCurrentPlayer().getCoordinate().getKey() + directions[1],
                Game.getCurrentPlayer().getCoordinate().getValue() + directions[0]);
        if (tile != null && tile.getItemOnTile() != null)
            return new Result(false, "The tile is already occupied.");
        assert tile != null;
        tile.setItemOnTile(item);
        Game.getCurrentPlayer().getBackPack().removeFromInventory(tile.getItemOnTile(), 1);
        return new Result(true, "Item placed successfully");
    }

    //get coordinates changes based on direction input
    public int[] getDirections(String direction) {
        int[] directions = new int[2];
        if (direction.equals("up")) {
            directions[1] = -1;
            return directions;
        } else if (direction.equals("down")) {
            directions[1] = 1;
            return directions;
        } else if (direction.equals("left")) {
            directions[0] = -1;
            return directions;
        } else if (direction.equals("right")) {
            directions[0] = 1;
            return directions;
        } else if (direction.equals("up-right")) {
            directions[0] = 1;
            directions[1] = -1;
            return directions;
        } else if (direction.equals("up-left")) {
            directions[0] = -1;
            directions[1] = -1;
            return directions;
        } else if (direction.equals("down-right")) {
            directions[0] = 1;
            directions[1] = 1;
            return directions;
        } else if (direction.equals("down-left")) {
            directions[0] = -1;
            directions[1] = 1;
            return directions;
        }
        return null;
    }

    //add item cheat code
    public Result addItemCheatCode(String name, int count) {
        Item item = null;
        if (Game.getDatabase().getItem(name) != null) item = Game.getDatabase().getItem(name);
        else if (CropType.fromString(name) != null) item = CropType.fromString(name);
        else if (ForagingTreeSourceType.fromString(name) != null) item = ForagingTreeSourceType.fromString(name);
        else if (ForagingCrop.fromString(name) != null) item = ForagingCrop.fromString(name);
        else if (ForagingSeedType.fromString(name) != null) item = ForagingSeedType.fromString(name);
            //else if(CraftType.fromString(name) != null) item = CraftType.fromString(name);
            // else if(CookingRecipeType.fromString(name) != null) item = CookingRecipeType.fromString(name);
        else if (Fish.fromString(name) != null) item = Fish.fromString(name);
        else if (MineralType.fromString(name) != null) item = MineralType.fromString(name);

        if (item == null) return new Result(false, "** No item with that name exists **");
        if (count <= 0) return new Result(false, "** Not a valid count **");
        if (item.getName().contains("Pack")) {
            if (item.getName().equals("Large Pack")) {
                Game.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Big);
            } else if (item.getName().equals("Deluxe Pack")) {
                Game.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Deluxe);
            }
            return new Result(true, "Upgraded successfully");
            //change trash can
        } else if (item.getName().contains("Trash Can")) {
            if (item.getName().equals("Copper Trash Can")) {
                Game.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Brass);
            } else if (item.getName().equals("Steel Trash Can")) {
                Game.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iron);
            } else if (item.getName().equals("Gold Trash Can")) {
                Game.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Gold);
            } else if (item.getName().equals("Iridium Trash Can")) {
                Game.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iridium);
            }
            return new Result(true, "Upgraded successfully");
            //add craft recipes to learnt recipes
        } else if (item.getName().contains("Recipe")) {
            if (item.getName().equals("Dehydrator Recipe")) {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Dehydrator);
            } else if (item.getName().equals("Grass Starter Recipe")) {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.GrassStarter);
            } else if (item.getName().equals("Fish Smoker Recipe")) {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.FishSmoker);
            } else {
                String recipeName = item.getName().replace("Recipe", "").trim();
                CookingRecipeType type = CookingRecipeType.fromString(recipeName);
                System.out.println(type);
                Game.getCurrentPlayer().getBackPack().addLearntCookingRecipe(type);
            }
            return new Result(true, "Recipe added successfully");
        }
        Game.getCurrentPlayer().getBackPack().addToInventory(item, count);
        return new Result(true, "** " + count + " of " + name + " added to your inventory **");

    }

    //plow tile
    public Result plowTile(int x, int y) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(x, y);
        Item hoe = Game.getCurrentPlayer().getBackPack().getToolFromInventory("Hoe");
        if (tile.getTileType().equals(TileType.Flat))
            Game.getCurrentPlayer().getFarmingSkill().plowTile(tile, (Hoe) hoe);
        return new Result(true, "Tile plowed successfully");
    }


    public Result showDatetime() {
        int hour = GameManager.getCurrentHour();
        int day = GameManager.getDay();
        String season = GameManager.getSeason().name().toLowerCase();
        return Result.success("Current date and time: Day " + day + " of " + capitalize(season) +
                ", " + String.format("%02d:00", hour));
    }

    public Result showDate() {
        int day = GameManager.getDay();
        String season = GameManager.getSeason().name().toLowerCase();
        return Result.success("Current date: Day " + day + " of " + capitalize(season));
    }

    public Result showTime() {
        int hour = GameManager.getCurrentHour();
        return Result.success("Current time: " + String.format("%02d:00", hour));
    }

    public Result showDayOfTheWeek() {
        String dayOfWeek = GameManager.getDayOfTheWeek();
        return Result.success("Today is: " + dayOfWeek);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Result showSeason() {
        Season currentSeason = GameManager.getGameClock().getCurrentSeason();
        return Result.success("Current season: " + currentSeason.name());
    }


    public Result showWeather() {
        Weather currentWeather = Game.getCurrentWeather();
        return Result.success("Current weather: " + currentWeather.toString());
    }


    public Result weatherForecast() {
        Weather forecast = Game.getForecastedWeather();
        return Result.success("Forecasted weather for tomorrow: " + forecast.toString());
    }

    public Result cheatWeatherSet(String input) {
        try {
            String[] tokens = input.trim().split("\\s+");
            if (tokens.length != 4) {
                return Result.error("Invalid format. Usage: cheat weather set <Type>");
            }

            String weatherType = tokens[3];

            Weather newForecastedWeather;
            try {
                newForecastedWeather = Weather.valueOf(weatherType);
            } catch (IllegalArgumentException e) {
                return Result.error("Invalid weather type. Valid types: SUNNY, RAIN, STORM, SNOW");
            }

            Game.setForecastedWeather(newForecastedWeather);
            return Result.success("Forecasted weather for tomorrow set to: " + newForecastedWeather);

        } catch (Exception e) {
            return Result.error("Error while setting forecasted weather: " + e.getMessage());
        }
    }

    public Result printMap(Matcher matcher) {
//        GameMap map = Game.getGameMap();

        try {
            if (matcher.group("x") != null && matcher.group("y") != null && matcher.group("size") != null) {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));
                int size = Integer.parseInt(matcher.group("size"));

                if (!map.isInBounds(x, y)) {
                    return new Result(false, "Coordinates out of map bounds.");
                }

                System.out.println("Printing map section at (" + x + "," + y + ") with size " + size + ":");
                map.printMapSection1(x, y, size);
                map.printMapSection2(x, y, size);
                map.printMapSection3(x, y, size);
                map.printMapSection4(x, y, size);
                return new Result(true, "Map section printed.");
            } else {
                System.out.println("Printing full map:");
                map.printFullMap();
                return new Result(true, "Full map printed.");
            }
        } catch (Exception e) {
            return new Result(false, "Invalid input format for map printing.");
        }
    }

    public Result eatFood(String foodName) {
        Item food = Game.getCurrentPlayer().getBackPack().getFromInventory(foodName);
        if (food == null) return new Result(false, "You don't have that food in your inventory.");
        else if (food instanceof Food) {
            int energy = ((Food) food).getEnergy();
            if(((Food) food).getRecipeType().Buff()) Game.getCurrentPlayer().setEnergy(200);
            else Game.getCurrentPlayer().increaseEnergy(energy);
            Game.getCurrentPlayer().getBackPack().removeFromInventory(food, 1);
            return new Result(true, "You consumed the food successfully!");
        } else return new Result(false, "That's...not edible.");
    }


    public void helpReadingMap() {
        System.out.println("Building :" + "🏠");
        System.out.println("Lake :" + "🌊");
        System.out.println("Soil :" + "🟫");
        System.out.println("Flat :" + "🟩");
        System.out.println("Tree :" + "\uD83C\uDF33");
        System.out.println("Stone :" + "\uD83E\uDEA8");
        System.out.println("Mine :" + "⛰\uFE0F");
        System.out.println("Green House :" + "\uD83C\uDF38");
        System.out.println("Cheat Thor :" + "O");
    }


    public Result cheatThor(Matcher matcher) {
        int i = 0, j = 0;
        boolean done = false;

            if (matcher.group("x") != null && matcher.group("y") != null) {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));
                i = x;
                j = y;
                //todo: اگر گلخانه بود تاثیر نداره
                if (Game.getGameMap().getTile(x, y).getItemOnTile() instanceof Tree) {
                    ((Tree)Game.getGameMap().getTile(x, y).getItemOnTile()).thunderEffect(GameMap.getTile(x, y));
                }
                if (Game.getGameMap().getTile(x, y).getTileType().equals(TileType.GreenHouse)) {
                    return new Result(false, "Cheat Thor failed!");
                } else Game.getGameMap().getTile(x, y).setTileType(TileType.CheatThor);
            }

        return new Result(true, "Cheat Thor in " + "(" + i + ", " + j + ")");
    }

    private List<Point> findShortestPath(int startX, int startY, int targetX, int targetY) {
        Queue<Node> queue = new LinkedList<>();
        boolean[][] visited = new boolean[GameMap.MAP_WIDTH][GameMap.MAP_HEIGHT];
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}}; // راست، پایین، چپ، بالا

        queue.add(new Node(startX, startY, null));
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            //بررسی اینکه به مقصدمون رسیدیم یا نرسیدیم
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            // بررسی خانه های اطراف
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (Game.getGameMap().isInBounds(newX, newY) &&
                        !visited[newX][newY] &&
                        isWalkable(newX, newY)) {

                    visited[newX][newY] = true;
                    queue.add(new Node(newX, newY, current));
                }
            }
        }

        return Collections.emptyList();
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



    private boolean canWalk(int x, int y) {
        for (Player player : Game.getAllPlayers()) {
            if (player.getFarm() != null) {
                if (player.getFarm().isInFarm(x, y) && !player.getFarm().isOwner(Game.getCurrentPlayer())) {
                    return false;
                }
            }
        }

        return true;
    }

    public Result buildGreenHouse() {
        Player currentPlayer = Game.getCurrentPlayer();
        Game.canBuildGreenHouse = true;
        Item item = Game.getDatabase().getItem("wood");
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                GameTile.greenHouseBuilt = true;
//                if(Game.getGameMap().getTile(i,j).getTileType().equals(TileType.GreenHouse)) {
//                    if (GameTile.greenHouseBuilt && i >= 0 && i <= 50 &&
//                            j >= 0 && j <= 50 && currentPlayer.getMapNum() == 1) {
//                        GameTile.greenHouseBuilt = true;
//                    }
//                    else if (GameTile.greenHouseBuilt && i >= 50 && i <= 99 &&
//                            j >= 0 && j <= 50 && currentPlayer.getMapNum() == 2) {
//                        GameTile.greenHouseBuilt = true;
//
//                    }
//                    else if (GameTile.greenHouseBuilt && i >= 0 && i <= 50 &&
//                            j >= 50 && j <= 99 && currentPlayer.getMapNum() == 3) {
//                        GameTile.greenHouseBuilt = true;
//                    }
//                    else if (GameTile.greenHouseBuilt && i >= 50 && i <= 99 &&
//                            j >= 50 && j <= 99 && currentPlayer.getMapNum() == 4) {
//                        GameTile.greenHouseBuilt = true;
//                    }
//                }
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

    public Result walkPlayer(Matcher matcher) {
        try {
            if (matcher.group("x") != null && matcher.group("y") != null) {
                int targetX = Integer.parseInt(matcher.group("x"));
                int targetY = Integer.parseInt(matcher.group("y"));

                if (!GameMap.isInBounds(targetX, targetY)) {
                    return new Result(false, "Target coordinates are out of bounds.");
                }

                // بررسی مزرعه دیگران
                if (!canWalk(targetX, targetY)) {
                    return new Result(false, "You cannot enter another player's farm!");
                }

                Player currentPlayer = Game.getCurrentPlayer();
                int startX = currentPlayer.getX();
                int startY = currentPlayer.getY();

                //بررسی کردن موانع
                GameTile targetTile = GameMap.getTile(targetX, targetY);
                if (targetTile == null || targetTile.getTileType() == TileType.Water ||
                        targetTile.getTileType() == TileType.Stone || targetTile.isOccupied()) {
                    return new Result(false, "Target tile is blocked.");
                }

                // یافتن کوتاه‌ترین مسیر با BFS
                List<Point> path = findShortestPath(startX, startY, targetX, targetY);

                if (path.isEmpty()) {
                    return new Result(false, "No valid path to target.");
                }

                int tilesWalked = path.size();
                int turns = countTurns(path);
                int energyCost = (int)((tilesWalked + (10 * turns))/20.0);

                boolean faint = false;
                if (Game.getCurrentPlayer().getEnergy() < energyCost) {
                    faint = true;
                }
                Game.getCurrentPlayer().increaseEnergy(-energyCost);

                if(faint) return new Result(false, "You fainted while walking!");
                Point finalStep = path.get(path.size() - 1);

                GameTile previousTile = GameMap.getTile(currentPlayer.getX(), currentPlayer.getY());
                if (previousTile != null) {
                    previousTile.setTileType(TileType.Flat);
                    previousTile.setOccupied(false);
                }

                currentPlayer.setCoordinate(finalStep.x, finalStep.y);

                GameTile newTile = GameMap.getTile(finalStep.x, finalStep.y);
                if (newTile != null) {
                    newTile.setTileType(TileType.Player);
                    newTile.setOccupied(true);
                }
                return new Result(true, "Player moved to (" + finalStep.x + "," + finalStep.y + ")");
            }
        } catch (Exception e) {
            return new Result(false, "Invalid input format.");
        }
        return new Result(false, "Invalid command.");
    }

//    // برای بررسی امکان حرکت به مزرعه دیگران
//    private boolean canWalk(int x, int y) {
//        for (Player player : Game.getAllPlayers()) {
//            if (player != Game.getCurrentPlayer() && player.getFarm() != null) {
//                if (player.getFarm().isInFarm(x, y)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


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

    //put back item in hand
    public Result putBack(){
        Item item = Game.getCurrentPlayer().getCurrentItem();
        if(item == null) return new Result(false, "You don't have anything in hand!");
        Game.getCurrentPlayer().getBackPack().addToInventory(item, 1);
        return new Result(true, item.getName() + " is back in you inventory!");
    }

    //count turns the player makes when walking
    private int countTurns(List<Point> path) {
        if (path.size() < 2) return 0;

        int turns = 0;
        int prevDx = path.get(1).x - path.get(0).x;
        int prevDy = path.get(1).y - path.get(0).y;

        for (int i = 2; i < path.size(); i++) {
            int dx = path.get(i).x - path.get(i - 1).x;
            int dy = path.get(i).y - path.get(i - 1).y;
            if (dx != prevDx || dy != prevDy) {
                turns++;
            }
            prevDx = dx;
            prevDy = dy;
        }
        return turns;
    }


}