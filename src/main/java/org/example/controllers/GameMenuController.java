package org.example.controllers;

import org.example.models.*;
import org.example.models.Enums.*;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Tool;

import javax.swing.plaf.PanelUI;
import java.util.*;
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
                UserDatabase.updateUser(user);
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
                currentUser.getUsername().equals(selectedPlayers.get(0).getUsername())|| done) {
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

        if (!currentUser.getUsername().equals(Game.getCurrentPlayer().getUsername())) {
            return Result.error("It's not your turn.");
        }

        Game.getCurrentPlayer().increaseEnergy(-50);

        Game.advanceToNextPlayer();
        //reset energy for next turn??
        if(!Game.getCurrentPlayer().isEnergyUnlimited()) {
            Game.getCurrentPlayer().resetEnergy();
        }

        return Result.success("Now it's " + Game.getCurrentPlayer().getName() + "'s turn." +
                Game.getCurrentPlayer().getNotifications());
    }

    public Result deleteGame() {
        Scanner scanner = new Scanner(System.in);
        int[] OK = new int[selectedPlayers.size()];
        System.out.println("Vote to delete the game (1 for yes, 0 for no):");
        for (int i = 0 ; i < selectedPlayers.size(); i++) {
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
        App.getCurrentPlayer().increaseEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    //cheat code unlimited energy
    public Result unlimitedEnergy() {
        App.getCurrentPlayer().setUnlimitedEnergy();
        return new Result(true, "** your energy is unlimited now **");
    }

    //showing the items in inventory
    public Result showInventory() {
        HashMap<Item, Integer> items = App.getCurrentPlayer().getBackPack().getInventory();
        StringBuilder output = new StringBuilder();
        for (Item item : items.keySet()) {
            if (items.get(item) != 0)
                output.append(items.get(item)).append(" of ")
                        .append(item.getName())
                        .append("\n");
            return new Result(true, output.toString());
        }
        return new Result(false, "Inventory is empty");
    }

    //removing from inventory
    public Result removeFromInventory(String name, int quantity, boolean flag) {
        Game.getCurrentPlayer().getTrashCan().removeFromInventory(name, quantity, flag);
        return new Result(true, quantity + " of " + name + " was removed from inventory");
    }

    //equip a certain tool
    public Result equipTool(String name) {
        HashMap<Item, Integer> items = App.getCurrentPlayer().getBackPack().getInventory();
        for (Item item : items.keySet()) {
            if (item.getName().equals(name)) {
                items.put(item, items.get(item) - 1);
                App.getCurrentPlayer().setCurrentItem(item);
                return new Result(true, "You're now equipped with " + item.getName());
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

    //showing current tool
    public Result showCurrentTool() {
        Item currentItem = App.getCurrentPlayer().getCurrentItem();
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
        for (Item i : App.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if (i instanceof Tool) {
                output.append("* ").append(i.getName()).append("\n");
                found = true;
            }
        }
        if (!found) return new Result(true, "You don't have any tools in your inventory");
        return new Result(true, output.toString());
    }

    //upgrade tool
    public Result upgradeTool(String name) {
        //TODO blacksmith stuff??
        HashMap<Item, Integer> items = App.getCurrentPlayer().getBackPack().getInventory();
        for (Item item : items.keySet()) {
            if (item.getName().equals(name)) {
                if (item instanceof Tool) {
                    //reduce money
                    ((Tool) item).upgradeLevel();
                    return new Result(true, item.getName() + " upgraded to level" + ((Tool) item).getLevel());
                } else {
                    return new Result(true, "Selected item is not a tool");
                }
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

    //use tool
    public Result useTool(String direction) {
        int[]dir = getDirections(direction);
        Item currentItem = Game.getCurrentPlayer().getCurrentItem();
        if(currentItem instanceof Tool) {
            String message = ((Tool)currentItem).use(new AbstractMap.SimpleEntry<>(Game.getCurrentPlayer().getCoordinate().getKey() + dir[0],
                    Game.getCurrentPlayer().getCoordinate().getKey() + dir[1])).getMessage();
            return new Result(true, message);
        } else return new Result(true, "You aren't equipped with any tool");
    }

    //show craft info
    public Result showCraftInfo(String name) {
        CropType cropType = CropType.fromString(name);
        TreeType treeType = TreeType.fromString(name);
        ForagingTreeSourceType foragingTreeSourceType = ForagingTreeSourceType.fromString(name);
        ForagingCrop foragingCrop = ForagingCrop.fromString(name);
        if(cropType != null) return new Result(true, cropType.getName());
        else if(treeType != null ) return new Result(true, treeType.toString());
        else if(foragingTreeSourceType != null ) return new Result(true, foragingTreeSourceType.toString());
        else if(foragingCrop != null ) return new Result(true, foragingCrop.toString());

        return new Result(true, "Couldn’t find the craft you're looking for");
    }

    public Result petAnimal(Matcher m) {
        String animalName = "";
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        animal.adjustFriendshipPoints(15);
        return Result.success("");
    }
    public Result cheatSetFriendship(Matcher m) {
        String animalName = m.group("animalName");
        int amount = Integer.parseInt(m.group("amount"));
        Animal animal = Game.getCurrentPlayer().getAnimal(animalName);
        if (animal == null ) return Result.error("animal doesn't exist or isn't yours");
        animal.setFriendshipPoints(amount);
        return Result.success("Friendship boosted! Nothing says ‘bonding’ like a little… code magic. Your animal is now contractually obligated to adore you");
    }

    public Result feedHay(Matcher m) {
        String animalName = m.group("animalName");
        Player player = Game.getCurrentPlayer();
        Animal animal = player.getAnimal(animalName);
        if (animal == null) return Result.error("animal doesn't exist or isn't yours");
        animal.setFeedingStatus(true);
        player.getBackPack().removeFromInventory(Game.getDatabase().getItem("Hay"), 1);
        animal.adjustFriendshipPoints(8);
        return Result.success("You offer food. The animal accepts. A bond is forged through snacks.");

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

        int basePrice = Game.getDatabase().getItem(animal.getType().toString()).getPrice();
        int price = (int) (basePrice * ((animal.getFriendshipPoints()/1000) + 0.3));
        Player player = Game.getCurrentPlayer();
        player.addGold(price);
        player.removeAnimal(animal);
        return Result.success("Your animal looked back one last time before leaving… but you were already gone.");
    }

    public Result startFishing(Matcher m) {
        String poleName = m.group("fishingPole");
        FishingPoleType pole = FishingPoleType.fromString(poleName);
        if (pole ==null) return Result.error("invalid pole type.");
        Player player = Game.getCurrentPlayer();
        if (player.getItemQuantity(Game.getDatabase().getItem(poleName)) <= 0)
            return Result.error("You don't have that pole in your inventory");
        int fishingLevel = player.getFishingSkill().getLevel();
        Fish caughtFish = Fish.getRandomFish(GameManager.getSeason(), fishingLevel);
        if (caughtFish == null) return Result.error("Your bobber danced, your hopes rose… and then? Nothing. The fish must be laughing underwater.");
        Random rand = new Random();
        double weatherCoefficient = Game.getCurrentWeather().getFishingCoefficient();
        int numOfFish =Math.min((int) (rand.nextDouble() * weatherCoefficient * (fishingLevel + 2)), 6) ;


        return Result.success("");
    }
    public Result cheatAddMoney(int amount){
        Player player = Game.getCurrentPlayer();
        player.addGold(amount);
        return Result.success("added " + amount + " gold");
    }
    public Result showFriendshipLevels() {
        StringBuilder builder = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        builder.append("friendship levels: \n");
        for (Player player : Game.getAllPlayers()) {
            if (!player.equals(currentPlayer)) {
                builder.append(player.getName()).append(" : ").append(currentPlayer.getFriendshipLevel(player));
            }
        }
        return new Result(true, builder.toString());
    }

    public Result talkToPlayer(String input) {
        String[] parts = input.split("\\s+");
        int uIndex = -1, mIndex = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("-u")) uIndex = i;
            else if (parts[i].equals("-m")) mIndex = i;
        }
        String username = parts[uIndex + 1], message = input.substring(mIndex + 1).trim();
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
        return new Result(true, (targetPlayer.getName()) +
                        "! A little birdie dropped off a message-check your inbox!");
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
        output.append("Talk history with ").append(input);
        for (Message message : messages) {
            output.append(message.getSender().getName()).append(": ").append(message.getMessage()).append("\n");
        }
        return new Result(true, output.toString());
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
        return new Result(true, targetPlayer.getName() +
                "! You've been gifted! Hope it's not rocks again.");
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
        return new Result(true, "");
    }

    public Result showGiftHistory(String username) {
        StringBuilder output = new StringBuilder();
        Player player = Game.getPlayerByUsername(username);
        for (Gift gift : Game.getAllGifts()) {
            if (gift.getSender().equals(player) || gift.getReceiver().equals(player)) {
                output.append(gift.getSender()).append(" gave ").append(gift.getReceiver()).append(" ")
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
        currentPlayer.changeFriendshipXP(60, targetPlayer);
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
        if (!currentPlayer.getGender().equals("boy"))
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
            targetPlayer.getBackPack().getInventory().remove(ring, 1);
            currentPlayer.setSpouse(targetPlayer);
            targetPlayer.setSpouse(currentPlayer);
            // TODO add messages here
        } else if (input.contains("reject")) {
            if (!targetPlayer.hasProposed(currentPlayer))
                return Result.error("That's a bold rejection for someone who hasn't been proposed to.");
            currentPlayer.changeLevel(targetPlayer, 0);
            targetPlayer.setProposalRejectionDaysLeft(7);
        }
        return new Result(true, "");
    }

    public Result NPCFriendshipLevels() {
        StringBuilder builder = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        builder.append("friendship status with NPCs: ");
        for (NPC npc : Game.getAllNPCs()) {
            builder.append("\n").append(npc.getName())
                    .append(" friendship level: ").append(npc.getFriendshipLevel(currentPlayer))
                    .append(" friendship points: ").append(npc.getFriendshipPoints(currentPlayer));
        }
        return new Result(true, builder.toString());
    }

    public Result meetNPC(String npcName) {
        NPC npc = Game.getNPCByName(npcName);
        Player player = Game.getCurrentPlayer();
        lastNPC = npc;
        npc.addFriendShipPoints(player, 20);
        return new Result(true, DialogueManager.getNpcDialogue(npcName, Game.getCurrentWeather().toString()));
    }

    public Result giftNPC(String input) {
        int CIndex = input.indexOf('C');
        int iIndex = input.indexOf('-');
        String npcName = input.substring(CIndex + 1, iIndex).trim();
        String itemName = input.substring(iIndex + 1).trim();
        NPC npc = Game.getNPCByName(npcName);
        if (npc == null) return new Result(false, "NPC not found");
        Item item = Game.getDatabase().getItem(itemName);
        if (item == null) return new Result(false, "Item not found");
        Player player = Game.getCurrentPlayer();
        npc.recieveGift(item, player);
        if (Math.abs(player.getX() - npc.getX()) > 1 || Math.abs(player.getY() - npc.getY()) > 1)
            return new Result(false,
                    "Nice thought! But you can’t give a gift to thin air. Find " + npcName + " first!");
        if (item instanceof Tool<?>)
            return new Result(false,
                    "Gifting your old tools? What’s next—handing out used socks?");

        if (npc.isFavorite(item)) {
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
        for (Map.Entry<Item, Integer> entry : lastNPC.getRequests().entrySet()) {
            index++;
            output.append("\n").append(index).append(". ")
                    .append(entry.getValue()).append(" ").append(entry.getKey().getName()).append("(s)");
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
        Map.Entry<Item, Integer> quest = lastNPC.getQuest(questIndex);
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
        if (player.getItemQuantity(quest.getKey()) < quest.getValue()) {
            return new Result(false,
                    "Whoops! You're still a few shy of the total. Harvest more and pop back over!");
        }
        Map.Entry<Item, Integer> reward = lastNPC.finishQuest(player, questIndex);
        return new Result(true,
                "You got " + reward.getValue() + " " + reward.getKey().getName() +
                        "(s) from " + lastNPC.getName() + " for finishing this quest.");
    }


    //plant seed on a specific tile
    public Result plantSeed(String seed, Map.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        //errors
        if (tile == null) return new Result(false, "Tile not found");
        if (tile.getTileType() != TileType.Soil)
            return new Result(false, "Tile is not plowed! Use your hoe to plow the tile!");
        if (!tile.isTileValidForPlanting()) return new Result(false,
                "You can't plant cause the tile is occupied!");
        boolean successful = Game.getCurrentPlayer().getFarmingSkill().plantSeed(seed, tile);
        if (successful) return new Result(true, "Successfully planted " + seed);
        else return new Result(false, "That's not a valid seed!");
    }

    //show the plant on a specific tile
    public Result showPlant(Map.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Item item = tile.getItemOnTile();
        if(item == null) return new Result(false, "Nothing planted on this tile");
        FruitAndVegetable plant = (FruitAndVegetable) item;
        StringBuilder output = new StringBuilder();
        output.append("** plant information **\n")
                .append(plant.toString());
        return new Result(true, output.toString());
    }

    //fertilize crop
    public Result fertilizeCrop(String fertilizer, Map.Entry<Integer, Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        Item item = tile.getItemOnTile();
        if(item == null) {
            tile.fertilze(fertilizer);
            return new Result(false, "Fertilized tile successfully!");
        }
        else if(item instanceof FruitAndVegetable) {
            FruitAndVegetable fruit = (FruitAndVegetable) item;
            if(fruit.getAge() == 0) {
                boolean successful = Game.getCurrentPlayer().getFarmingSkill().fertilizeCrop(coordinates, fertilizer);
                if (successful) return new Result(true, "Successfully fertilized with " + fertilizer);
                else return new Result(false, "You don't have that kind of fertilizer");
            } else return new Result(false, "You can only fertilize tile before or the day of planting!");
        } else return new Result(false, "Can't fertilize this tile");
    }

    //how much water left
    public Result howMuchWaterLeft() {
        int waterLeft = Game.getCurrentPlayer().getWateringCan().getCapacity() -
                Game.getCurrentPlayer().getWateringCan().getWaterlevel();
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
        Map.Entry<Integer, Integer> newCoordinates = new AbstractMap.SimpleEntry<>
                (item.getCoordinates().getKey() + directions[0],
                item.getCoordinates().getValue() + directions[1]);
        item.setCoordinates(newCoordinates);
        return new Result(true, "Item placed successfully");
    }

    //get coordinates changes based on direction input
    //TODO add four other directions
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
        }
        return null;
    }

    //add item cheat code
    public Result addItemCheatCode(String name, int count) {
        List<Item> items = Game.getDatabase().getItemDatabase();
        Item item = null;
        for (Item i : items) {
            if (i.getName().equals(name)) {
                item = i;
            }
        }
        if (item == null) return new Result(false, "** No item with that name exists **");
        Game.getCurrentPlayer().getBackPack().addToInventory(item, count);
        return new Result(true, "** " + count + " of " + name + " added to your inventory **");
    }

    //plow tile
    public Result plowTile(int x, int y) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(x, y);
        Item hoe = Game.getCurrentPlayer().getBackPack().getToolFromInventory("Hoe");
        if(tile.getTileType().equals(TileType.Flat))
            Game.getCurrentPlayer().getFarmingSkill().plowTile(tile, (Hoe)hoe);
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

            String weatherType = tokens[3].toUpperCase();

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
        GameMap map = Game.getGameMap();  // فرض بر اینه که Game singleton یا staticه

        try {
            if (matcher.group("x") != null && matcher.group("y") != null && matcher.group("size") != null) {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));
                int size = Integer.parseInt(matcher.group("size"));

                // بررسی معتبر بودن
                if (!map.isInBounds(x, y)) {
                    return new Result(false, "Coordinates out of map bounds.");
                }

                System.out.println("Printing map section at (" + x + "," + y + ") with size " + size + ":");
                map.printMapSection(x, y, size);
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

}