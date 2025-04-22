package models;

import java.util.ArrayList;

public class Game {
    private static GameMap gameMap = new GameMap();
    private static Player currentPlayer = null;
    private static ArrayList<Player> players = new ArrayList<Player>();
    public static Player getCurrentPlayer() {
        return currentPlayer;
    }
    public static void setCurrentPlayer(Player currentPlayer) {
        Game.currentPlayer = currentPlayer;
    }
    public static void addPlayer(Player player) {
        players.add(player);
    }
    public static GameMap getGameMap() {
        return gameMap;
    }
}
