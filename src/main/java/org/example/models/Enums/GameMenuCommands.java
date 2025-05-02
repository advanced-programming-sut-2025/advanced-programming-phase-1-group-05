package org.example.models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    ShowEnergy("show energy"),
    EnergySetCC("set energy -v (?<value>\\d+)"),
    EnergyUnlimitedCC("unlimited energy"),
    ShowInventory("show inventory"),
    InventoryTrash("inventory trash -i (?<itemName>\\S+) (-n (?<number>\\d+))?"),
    EquipTool("equip tool (?<toolName>\\S+)"),
    ShowCurrentTool("show current tool"),
    ShowAvailableTools("show available tools"),
    UpgradeTool("upgrade tool (?<toolName>\\S+)"),
    UseTool("use tool -d (?<direction>\\S+)"),
    ShowCraftInfo("show craft info -n (?<craftName>\\S+)"),
    Plant("plant -s (?<seed>\\S+) -d (?<direction>\\S+)"),
    ShowPlant("show plant -l (?<x, y>\\S+)"),
    FertilizeCrop("fertilize -f (?<fertilizer>\\S+) -d (?<direction>\\S+)"),
    WaterCrop("how much water"),
    ShowCraftingRecipes("show crafting recipes"),
    PlaceItem("place item -n (?<item_name>\\S+) -d (?<direction>\\S+)"),
    AddItemCC("cheat add item -n (?<item_name>\\S+) -c (?<count>\\d+)"),
    GiftHistory("gift\\s+history\\s+-u\\s+(?<username>.+?)");
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