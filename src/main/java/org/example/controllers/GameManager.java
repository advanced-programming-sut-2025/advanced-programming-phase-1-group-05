package org.example.controllers;

import org.example.models.*;

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
}
