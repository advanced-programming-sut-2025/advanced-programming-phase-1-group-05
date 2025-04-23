package org.example.controllers;

import org.example.models.*;
import org.example.models.Tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class GameMenuController {
    private NPC lastNPC = null;

    public Result newGame(String[] usernames) {
        return null;
    }

    public Result chooseMap(int mapNumber) {
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
        }
        return new Result(true, "");
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
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
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

    }



    public Result meetNPC(String npcName) {
        NPC npc = Game.getNPCByName(npcName);


        lastNPC = npc;
        return new Result(true, DialogueManager.getNpcDialogue(npcName, Game.getCurrentWeather().toString()));
    }

    public Result giftNPC(String input){
        int CIndex = input.indexOf('C');
        int iIndex = input.indexOf('-');
        String npcName = input.substring(CIndex + 1, iIndex).trim();
        String itemName = input.substring(iIndex + 1).trim();
        NPC npc = Game.getNPCByName(npcName);
        if (npc == null) return new Result(false, "NPC not found");
        Item item = Game.getItemByName(itemName);
        if (item == null) return new Result(false, "Item not found");
        Player player = Game.getCurrentPlayer();
        npc.recieveGift(item, player);
        if (Math.abs(player.getX() - npc.getX()) > 1 || Math.abs(player.getY() - npc.getY()) > 1) return new Result(false, "Nice thought! But you can’t give a gift to thin air. Find "+ npcName +" first!")
        if (item instanceof Tool<?>) return new Result(false, "Gifting your old tools? What’s next—handing out used socks?");

        if (npc.isFavorite(item)) return new Result(true, "Wow, " + player.getName() + ", you know me so well. this " + itemName + " is my favorite.");
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
    public Result finishQuest(String input){
        int iIndex = input.indexOf('-') + 1;
        input = input.substring(iIndex + 1).trim();
        int questIndex;
        try {
            questIndex = Integer.parseInt(input);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Invalid quest index");
        }
        Map.Entry<Item, Integer> quest = lastNPC.getQuest(questIndex);
        if(quest == null) return new Result(false, "Quest not found");
        Player player = Game.getCurrentPlayer();
        if (Math.abs(player.getX() - lastNPC.getX()) > 1 || Math.abs(player.getY() - lastNPC.getY()) > 1) {
            return new Result(false,"You can't wrap this up from here. Get back to " + lastNPC.getName() + " first!");
        }
        if (lastNPC.isCompleted(questIndex)) {
            return new Result(false, "Another farmer's already taken care of that one. Why not lend a hand elsewhere?");
        }
        if (player.getItemQuantity(quest.getKey()) < quest.getValue()) {
            return new Result(false, "Whoops! You're still a few shy of the total. Harvest more and pop back over!");
        }
        Map.Entry<Item, Integer> reward = lastNPC.finishQuest(player, questIndex);
        return new Result(true, "You got " + reward.getValue() + " " + reward.getKey().getName() + "(s) from " + lastNPC.getName() +" for finishing this quest.");
    }
}