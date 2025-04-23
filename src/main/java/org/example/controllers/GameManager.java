package org.example.controllers;

import org.example.models.Enums.Season;
import org.example.models.Game;
import org.example.models.NPC;
import org.example.models.Player;
import org.example.models.TimeAndDate;

import java.util.Random;

public class GameManager {
    private static TimeAndDate gameClock;
    public GameManager(){
        gameClock = new TimeAndDate();
    }
    public static void nextDay(){
        checkNPCGifts();
        checkForThirdQuest();
    }

    public static void checkNPCGifts(){
        Random rand = new Random();
        for (Player player : Game.getAllPlayers()){
            for (NPC npc : Game.getAllNPCs()) {
                if (npc.getFriendshipLevel(player) >= 3 && rand.nextBoolean()) {
                    npc.sendGift(player);
                }
            }
        }
    }

    public static void checkForThirdQuest(){
        for (Player player : Game.getAllPlayers()){
            for (NPC npc : Game.getAllNPCs()) {
                if (gameClock.getDay() == npc.getDaysToUnlockThirdQuest() && npc.getNumOfUnlockedQuests(player) == 2) {
                    npc.unlockThirdQuest(player);
                }
            }
        }
    }
}
