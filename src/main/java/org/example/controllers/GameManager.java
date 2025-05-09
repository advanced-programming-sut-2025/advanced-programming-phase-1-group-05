package org.example.controllers;

import org.example.models.*;
import org.example.models.Enums.Season;

import java.util.Map;
import java.util.Random;

public class GameManager {
    private static TimeAndDate gameClock;
    public GameManager(){
        gameClock = new TimeAndDate();
    }

    public static void nextDay(){
        resetEnergy();
        checkNPCGifts();
        checkForThirdQuest();
        giveBackSoldItemGolds();
    }

    public static void resetEnergy() {
        for (Player player : Game.getAllPlayers()) {
            if (player.getProposalRejectionDaysLeft() > 0) {
                player.setEnergy(100);
                player.decrementProposalRejectionDaysLeft();
            }
            else player.setEnergy(200);
        }
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

    public static void giveBackSoldItemGolds(){
        for (Map.Entry<Player, Item> entry : Game.soldItems.entrySet()) {
            entry.getKey().addGold(entry.getValue().getPrice());
        }
        Game.soldItems.clear();
    }

    public static Season getSeason() {
        return gameClock.getCurrentSeason();
    }

    public static int getDay() {
        return gameClock.getDay();
    }

    public static String getDayOfTheWeek() {
        switch (getDay() % 7){
            case 1 -> {
                return "Saturday";
            }
            case 2 -> {
                return "Sunday";
            }
            case 3 -> {
                return "Monday";
            }
            case 4 -> {
                return "Tuesday";
            }
            case 5 -> {
                return "Wednesday";
            }
            case 6 -> {
                return "Thursday";
            }
            case 0 -> {
                return "Friday";
            }
        }
        return "wtf intellij";
    }

    public static int getCurrentHour() {
        return gameClock.getHour();
    }
}