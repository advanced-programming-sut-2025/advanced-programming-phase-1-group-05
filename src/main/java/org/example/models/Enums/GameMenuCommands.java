package org.example.models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands {
    Walk("walk -l (?<x>\\d+) (?<y>\\d+)"),
    CheatThor("cheat Thor -l (?<x>\\d+) (?<y>\\d+)"),
    PrintMap("print map(?: -l (?<x>\\d+) (?<y>\\d+) -s (?<size>\\d+))?"),
    ShowEnergy("show energy"),
    EnergySetCC("set energy -v (?<value>\\d+)"),
    EnergyUnlimitedCC("unlimited energy"),
    ShowInventory("show inventory"),
    InventoryTrash("inventory trash -i (?<itemName>\\S+) -n (?<number>\\d+)"),
    EquipTool("equip tool (?<toolName>\\S+(\\s*\\S+)*)"),
    ShowCurrentTool("show current tool"),
    ShowAvailableTools("show available tools"),
    UpgradeTool("upgrade tool (?<toolName>.+)"),
    UseTool("use tool -d (?<direction>\\S+)"),
    ShowCraftInfo("show craft info -n (?<craftName>\\S+(\\s*\\S+)*)"),
    Plant("plant -s (?<seed>\\S+) -d (?<x>\\d+) (?<y>\\d+)"),
    ShowPlant("show plant -l (?<x>\\d+) (?<y>\\d+)"),
    FertilizeCrop("fertilize -f (?<fertilizer>\\S+) -d (?<x>\\d+) (?<y>\\d+)"),
    HowMuchWater("how much water"),
    ShowCraftingRecipes("show crafting recipes"),
    PlaceItem("place item -n (?<itemName>.+?) -d (?<direction>\\S+)"),
    AddItemCC("cheat add item -n (?<itemName>\\S+) -c (?<count>\\d+)"),
    BuildAnimalHouse("build\\s+-a\\s+(?<buildingName>\\S+)\\s+-l\\s+(?<x>\\d+)\\s+(?<y>\\d+)"),
    BuyAnimal("buy\\s+animal\\s+-a\\s+(?<animalType>\\S+)\\s+-n\\s+(?<animalName>.+?)"),
    SetFriendshipCC("cheat\\s+set\\s+friendship\\s+-n\\s+(?<animalName>.+?)\\s+-c\\s+(?<amount>\\d+)"),
    FeedHay("feed\\s+hay\\s+-n\\s+(?<animalName>.+?)"),
    CollectProduce("collect\\s+produce\\s+-n\\s+(?<name>\\S+)"),
    SellAnimal("sell\\s+animal\\s+-n\\s+(?<animalName>.+?)"),
    StartFishing("fishing\\s+-p\\s+(?<fishingPole>.+?)"),
    ArtisanUSe("artisan use\\s+(?<args>.+)"),
    Gift("gift\\s+-u\\s+(?<username>\\S+)\\s+-i\\s+(?<itemName>.+?)\\s+-a\\s+(?<amount>\\d+)"),
    GiftHistory("gift\\s+history\\s+-u\\s+(?<username>.+?)"),
    RateGift("gift\\s+rate\\s+-i\\s+(?<giftNumber>\\d+?)\\s+-r\\s+(?<rate>\\d+?)"),
    AskMarriage("ask\\s+marriage\\s+-u\\s+(?<username>\\S+)"),
    Purchase("purchase\\s+(?<productName>.+?)(?:\\s+-n\\s+(?<count>\\d+))?"),
    CheatAddMoney("cheat\\s+add\\s+(?<count>\\d+?)\\s+dollars"),
    Sell("sell\\s+(?<productName>.+?)(?:\\s+-n\\s+(?<count>\\d+))?"),
    GiveBouquet("flower\\s+-u\\s+(?<username>\\S+)"),
    Hug("hug\\s+-u\\s+(?<username>\\S+)"),
    PlowTile("plow tile (?<x>\\d+) (?<y>\\d+)"),
    EatFood("eat (?<foodName>\\S+)");

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