package org.example.controllers;

import org.example.models.*;
import org.example.models.Enums.TileType;
import org.example.models.Tool.Tool;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenuController extends MenuController {
    private User currentUser;
    private static Map<String, Game> activeGames = new HashMap<>();
    private Game pendingGame;
    private List<User> selectedPlayers;
    boolean canChooseMap = false;
    private Item lastRingProposedWith = null;

    public GameMenuController(Scanner scanner, User currentUser) {
        super(scanner);
        this.currentUser = currentUser;
    }

    private NPC lastNPC = null;

    public Result newGame(String input) {
        // TODO : errors !!!
        Pattern pattern = Pattern.compile("^game new( -u(?<username>[\\w-]+)){1,3}$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.find()) {
            return Result.error("Invalid command format! Use: game new -u <username1> <username2> <username3>");
        }

        selectedPlayers = new ArrayList<>();
        selectedPlayers.add(currentUser);

        for (int i = 1; i <= 3; i++) {
            String username = matcher.group("username" + i);
            if (username != null) {
                User user = UserDatabase.getUserByUsername(username);
                if (user == null) {
                    return Result.error("User '" + username + "' not found!");
                }
                selectedPlayers.add(user);
                currentUser.addFriend(username);
            }
        }

        System.out.println("Please enter map number (1-3):");
        canChooseMap = true;
        pendingGame = new Game(); // ایجاد بازی موقت
        return Result.success("Waiting for map selection...");
    }

    public Result chooseMap(String input) {
        if (canChooseMap) {

        }
        return null;
    }

    public Result loadGame() {
        return null;
    }

    public Result exitGame() {
        return null;
    }

    public Result nextTurn() {
        return null;
    }

    public Result deleteGame() {
        return null;
    }

    //for showing current player's energy level
    public Result showEnergy() {
        System.out.println("energy :" + Game.getCurrentPlayer().getEnergy());
        return new Result(true, "");
    }

    //cheat code set energy
    public Result setEnergy(int value) {
        App.getCurrentPlayer().addEnergy(value);
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
        for (Item item : items.keySet()) {
            if (items.get(item) != 0) System.out.println(items.get(item) + " of " + item.getName());
            return new Result(true, "");
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
        for (Item i : App.getCurrentPlayer().getBackPack().getInventory().keySet()) {
            if (i instanceof Tool) {
                System.out.println("* " + i.getName());
                found = true;
            }
        }
        if (!found) return new Result(true, "You don't have any tools in your inventory");
        return new Result(true, "");
    }

    //upgrade tool
    public Result upgradeTool(String name) {
        //only available when in ahangary
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


    //show craft info
    public Result showCraftInfo(String name) {
        FruitAndVegetable plant = Game.getDatabase().getFruitAndVegetable(name);
        if (plant == null) return new Result(true, "Couldn’t find the plant you're looking for");
        else plant.showCropInformation();
        return new Result(true, "");
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

    public Result talkToPlayer(String input){
        String username = "", message;
        Player targetPlayer = Game.getPlayerByUsername(username);
        Player currentPlayer = Game.getCurrentPlayer();
        if (targetPlayer == null) return new Result(false, "Hmmm... either they moved away, or they never existed!");
        if (Math.abs(targetPlayer.getX() - currentPlayer.getX()) > 1 || Math.abs(targetPlayer.getY()-currentPlayer.getY()) > 1)
            return new Result(false, "You can't have a heart-to-heart with someone who's miles away!");
        return new Result(true, "");
    }

    public Result giftPlayer(String input){
        String username = "", itemName = "";
        int amount = 0;
        Item item = Game.getDatabase().getItem(itemName);
        Player currentPlayer = Game.getCurrentPlayer();
        Player targetPlayer = Game.getPlayerByUsername(username);
        currentPlayer.getBackPack().getInventory().remove(item , amount);
        currentPlayer.getBackPack().getInventory().put(item, amount);
        return new Result(true, "");
    }

    public Result giveFlower(String input){
        int uIndex = input.indexOf('u');
        input = input.substring(uIndex + 1);
        Player targetPlayer = Game.getCurrentPlayer();
        Player currentPlayer = Game.getCurrentPlayer();
        Item bouquet = Game.getDatabase().getItem("bouquet");
        currentPlayer.getBackPack().getInventory().remove(bouquet, 1);
        targetPlayer.getBackPack().getInventory().put(bouquet, 1);
        targetPlayer.changeLevel(currentPlayer, 3);
        return new Result(true, "");
    }

    public Result askMarriage(String input){
        String username = "", ringName = "";
        Player targetPlayer = Game.getPlayerByUsername(username);
        Item ring = Game.getDatabase().getItem(ringName);
        if (ring != null ) lastRingProposedWith = ring;
        Player currentPlayer = Game.getCurrentPlayer();
        currentPlayer.getBackPack().getInventory().remove(ring, 1);
        targetPlayer.getBackPack().getInventory().put(ring, 1);
        return new Result(true, "");
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
        if (input.contains("accept")) {
            currentPlayer.changeLevel(targetPlayer, 4);

            currentPlayer.getBackPack().getInventory().put(lastRingProposedWith, 1);
            targetPlayer.getBackPack().getInventory().put(lastRingProposedWith, 1);
        } else if (input.contains("reject")) {
            currentPlayer.changeLevel(targetPlayer, 0);
        }
        return new Result(true, "");
    }

    public Result NPCFriendshipLevels() {
        StringBuilder builder = new StringBuilder();
        Player currentPlayer = Game.getCurrentPlayer();
        builder.append("friendship status with NPCs: ");
        for (NPC npc : Game.getAllNPCs()) {
            builder.append("\n").append(npc.getName()).append(" friendship level: ").append(npc.getFriendshipLevel(currentPlayer))
                    .append(" friendship points: ").append(npc.getFriendshipPoints(currentPlayer));
        }
        return new Result(true, builder.toString());
    }

    public Result meetNPC(String npcName) {
        NPC npc = Game.getNPCByName(npcName);


        lastNPC = npc;
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
            return new Result(false, "Nice thought! But you can’t give a gift to thin air. Find " + npcName + " first!");
        if (item instanceof Tool<?>)
            return new Result(false, "Gifting your old tools? What’s next—handing out used socks?");

        if (npc.isFavorite(item))
            return new Result(true, "Wow, " + player.getName() + ", you know me so well. this " + itemName + " is my favorite.");
        return new Result(true, "Oh, a " + itemName + " ?Thanks, " + player.getName());
    }

    public Result showAllQuests() {
        Player player = Game.getCurrentPlayer();
        StringBuilder output = new StringBuilder();
        output.append(lastNPC.getName()).append(" quests for you");
        int index = 0;
        for (Map.Entry<Item, Integer> entry : lastNPC.getRequests().entrySet()) {
            index++;
            output.append("\n").append(index).append(". ").append(entry.getValue()).append(" ").append(entry.getKey().getName()).append("(s)");
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
        Map.Entry<Item, Integer> quest = lastNPC.getQuest(questIndex);
        if (quest == null) return new Result(false, "Quest not found");
        Player player = Game.getCurrentPlayer();
        if (Math.abs(player.getX() - lastNPC.getX()) > 1 || Math.abs(player.getY() - lastNPC.getY()) > 1) {
            return new Result(false, "You can't wrap this up from here. Get back to " + lastNPC.getName() + " first!");
        }
        if (lastNPC.isCompleted(questIndex)) {
            return new Result(false, "Another farmer's already taken care of that one. Why not lend a hand elsewhere?");
        }
        if (player.getItemQuantity(quest.getKey()) < quest.getValue()) {
            return new Result(false, "Whoops! You're still a few shy of the total. Harvest more and pop back over!");
        }
        Map.Entry<Item, Integer> reward = lastNPC.finishQuest(player, questIndex);
        return new Result(true, "You got " + reward.getValue() + " " + reward.getKey().getName() + "(s) from " + lastNPC.getName() + " for finishing this quest.");
    }

    public Result talkHistory(String input) {
        int uIndex = input.indexOf('u');
        input = input.substring(uIndex + 1).trim();
        Player player = Game.getPlayerByUsername(input);
        if (player == null) return new Result(false, "Talking to ghosts again? That player isn't real.");
        List<Message> messages = Game.getMessages(player, Game.getCurrentPlayer());
        if (messages.isEmpty()) return new Result(false, "Looks like you two haven't broken the ice yet");

        StringBuilder output = new StringBuilder();
        output.append("Talk history with ").append(input);
        for (Message message : messages) {
            output.append(message.getSender().getName()).append(": ").append(message.getMessage()).append("\n");
        }
        return new Result(true, output.toString());
    }

    //plant seed on a specific tile
    public Result plantSeed(String seed, Map.Entry<Integer,Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        //errors
        if (tile == null) return new Result(false, "Tile not found");
        if(tile.getTileType() != TileType.Soil) return new Result(false, "Tile is not plowed! Use your hoe to plow the tile!");
        if(!tile.isTileValidForPlanting()) return new Result(false, "You can't plant cause the tile is occupied!");
        boolean successful = Game.getCurrentPlayer().getFarmingSkill().plantSeed(new Seed(seed), tile);
        if(successful) return new Result(true, "Successfully planted " + seed);
        else return new Result(false, "That's not a valid seed!");
    }

    //show the plant on a specific tile
    public Result showPlant(Map.Entry<Integer,Integer> coordinates) {
        GameMap map = Game.getGameMap();
        GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
        if(tile.isTileValidForPlanting()) return new Result(true, "Nothing planted on this tile");

        FruitAndVegetable plant = Game.getGameMap().getPlantedFruit(coordinates);
        StringBuilder output = new StringBuilder();
        output.append("** plant information **\n")
                .append(plant.toString());
        return new Result(true, output.toString());
    }

    //fertilize crop
    public Result fertilizeCrop(String fertilizer, Map.Entry<Integer,Integer> coordinates) {
        GameMap map = Game.getGameMap();
        boolean successful = Game.getCurrentPlayer().getFarmingSkill().fertilizeCrop(coordinates, fertilizer);
        if(successful) return new Result(true, "Successfully fertilized with " + fertilizer);
        else return new Result(false, "You don't have that kind of fertilizer");
    }

    //how much water left
    public Result howMuchWaterLeft(){
        int waterLeft = Game.getCurrentPlayer().getWateringCan().getCapacity() -
                Game.getCurrentPlayer().getWateringCan().getWaterlevel();
        return new Result(true, waterLeft + " water units left in your watering can");
    }
    



}