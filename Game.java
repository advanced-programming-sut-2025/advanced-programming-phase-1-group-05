package models;

import java.util.ArrayList;

public class Game {
    private static Player currentPlayer = null;
    private static ArrayList<Player> players = new ArrayList<Player>();
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player currentPlayer) {
        Game.currentPlayer = currentPlayer;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
}
