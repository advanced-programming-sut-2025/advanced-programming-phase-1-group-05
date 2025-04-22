package org.example.models;

import org.example.models.Building.Building;

import java.util.*;

public class NPC {
    private String name;
    private Map<Player, Integer> frendshipLevel = new HashMap<>();
    private List<Item> favorites = new ArrayList<>();
    private Map<Item, Integer> requests = new LinkedHashMap<>();
    private Map<Item, Integer> rewards = new LinkedHashMap<>();
    private Building residence; // will implement this after the building class is done
    public NPC(String name, List<Item> favorites, Map<Item, Integer> requests,
               Map<Item, Integer> rewards) {
        this.name = name;
        this.favorites.addAll(favorites);
        this.requests.putAll(requests);
        this.rewards.putAll(rewards);
        for (Player player : Game.getAllPlayers()){
            frendshipLevel.put(player, 0);
        }
        // this.residence = residence
    }
    public void recieveGift(Item gift, Player player) {
        // this method will check if the gift is a favorite
        // and will add to friendshipLevel of the player accordingly
    }

    public String speak(){
        // returns the dialogue
        return "";
    }

    public void sendRequest(){
        // a Random item from requests will be chosen
    }
}