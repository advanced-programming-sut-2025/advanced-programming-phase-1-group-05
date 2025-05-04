package org.example.models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    ShowEnergy("show energy"),
    EnergySetCC("set energy -v (?<value>\\d+)"),
    EnergyUnlimitedCC("unlimited energy"),
    ShowInventory("show inventory"),
    InventoryTrash("inventory trash -i (?<itemName>\\.+) (-n (?<number>\\d+))?"),
    EquipTool("equip tool (?<toolName>\\S+)"),
    ShowCurrentTool("show current tool"),
    ShowAvailableTools("show available tools"),
    UpgradeTool("upgrade tool (?<toolName>\\.+)"),
    UseTool("use tool -d (?<direction>\\S+)"),
    ShowCraftInfo("show craft info -n (?<craftName>\\S+)"),
    Plant("plant -s (?<seed>\\S+) -d (?<direction>\\S+)"),
    ShowPlant("show plant -l (?<x, y>\\S+)"),
    FertilizeCrop("fertilize -f (?<fertilizer>\\S+) -d (?<direction>\\S+)"),
    WaterCrop("how much water"),
    ShowCraftingRecipes("show crafting recipes"),
    PlaceItem("place item -n (?<item_name>\\.+) -d (?<direction>\\S+)"),
    AddItemCC("cheat add item -n (?<item_name>\\.+) -c (?<count>\\d+)"),
    Gift("gift\\s+-u\\s+\\s+(?<username>.+?)\\s+-i\\s+(?<itemName>.+?)\\s+-a\\s+(?<amount>\\d+)"),
    GiftHistory("gift\\s+history\\s+-u\\s+(?<username>.+?)"),
    RateGift("gift\\s+rate\\s+-i\\s+(?<giftNumber>\\d+?)\\s+-r\\s+(?<rate>\\d+?)"),
    Purchase("purchase\\s+(?<productName>.+?)(?:\\s+-n\\s+(?<count>\\d+))?"),
    CheatAddMoney("cheat\\s+add\\s+(?<count>\\d+?)\\s+dollars"),
    Sell("sell\\s+<?productName>.+?(?:\\s+-n\\s+(?<count>\\d+))?"),
    GiveBouquet("flower\\s+-u\\s+(?<username>.+?)"),
    Hug("hug\\s+-u\\s+(?<username>.+?)");

    private final String pattern;
    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }
    public Matcher getMatcher(String input){
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if(matcher.matches()){
            return matcher;
        }
        return null;
    }
}