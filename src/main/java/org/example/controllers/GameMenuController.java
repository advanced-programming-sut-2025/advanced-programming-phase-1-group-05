package org.example.controllers;

import org.example.models.*;
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

    public GameMenuController(Scanner scanner, User currentUser) {
        super(scanner);
        this.currentUser = currentUser;
    }
    public Result showCraftInfo(String name){
        return null;
    }

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

    public Result chooseMap(String input){
        if (canChooseMap){

        }
        return null;
    }

    public Result loadGame(){
        return null;
    }

    public Result exitGame(){
        return null;
    }

    public Result nextTurn(){
        return null;
    }

    public Result deleteGame(){
        return null;
    }

    //for showing current player's energy level
    public Result showEnergy() {
        System.out.println("energy :" + Game.getCurrentPlayer().getEnergy());
        return new Result(true, "");
    }

    //cheat code set energy
    public Result setEnergy(int value){
        App.getCurrentPlayer().addEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    //cheat code unlimited energy
    public Result unlimitedEnergy(){
        App.getCurrentPlayer().setUnlimitedEnergy();
        return new Result(true, "** your energy is unlimited now **");
    }

    //showing the items in inventory
    public Result showInventory(){
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(items.get(item) != 0) System.out.println(items.get(item) + " of " + item.getName());
        }
        return new Result(true, "");
    }

    //removing from inventory
    public Result removeFromInventory(String name, int quantity, boolean flag){
        Game.getCurrentPlayer().getTrashCan().removeFromInventory(name, quantity, flag);
        return new Result(true, quantity + " of " + name + " was removed from inventory");
    }

    //equip a certain tool
    public Result equipTool(String name){
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(item.getName().equals(name)){
                items.put(item, items.get(item) - 1);
                App.getCurrentPlayer().setCurrentItem(item);
                return new Result(true, "You're now equipped with " + item.getName());
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

    //showing current tool
    public Result showCurrentTool(){
        Item currentItem = App.getCurrentPlayer().getCurrentItem();
        if(currentItem instanceof Tool) {
            return new Result(true, "You are equipped with " + currentItem.getName());
        } else {
            return new Result(true, "You are not equipped with any tool");
        }
    }

    //showing available tools
    public Result showAvailableTools(){
        boolean found = false;
        for(Item i : App.getCurrentPlayer().getInventory().keySet()){
            if(i instanceof Tool) {
                System.out.println("* " + i.getName());
                found = true;
            }
        }
        if(!found) return new Result(true, "You don't have any tools in your inventory");
        return new Result(true, "");
    }

    //upgrade tool
    public Result upgradeTool(String name){
        //only available when in ahangary
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(item.getName().equals(name)){
                if(item instanceof Tool) {
                    //reduce money
                    ((Tool) item).upgradeLevel();
                    return new Result(true, item.getName() + " upgraded to level" + ((Tool)item).getLevel());
                } else {
                    return new Result(true, "Selected item is not a tool");
                }
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }


    public Result meetNPC(String npcName){
        NPC npc = Game.getNPCByName(npcName);


        return new Result(true, DialogueManager.getNpcDialogue(npcName, Game.getCurrentWeather().toString()));
    }



}