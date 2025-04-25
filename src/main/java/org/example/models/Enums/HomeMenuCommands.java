package org.example.models.Enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HomeMenuCommands {
    Crafting("crafting"),
    CraftItem("craft item <item_name>");

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
