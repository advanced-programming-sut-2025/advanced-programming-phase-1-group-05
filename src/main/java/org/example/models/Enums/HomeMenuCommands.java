package org.example.models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HomeMenuCommands {
    CraftingRecipes("crafting show recipes"),
    CraftItem("crafting item -n (?<itemName>\\S+(\\s*\\S+)*)"),
    CookingRefrigerator("cooking refrigerator (?<action>\\S+) (?<item>\\S+)"),
    CookingPrepare("cooking prepare (?<recipeName>\\S+)"),
    ShowCookingRecipes("cooking show recipes"),
    ShowOptions("show options"),
    Back("back");

    private final String pattern;
    HomeMenuCommands(String pattern) {
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
