package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Enums.Season;
import org.example.models.Enums.Weather;

import java.util.*;

public class Game {
    private static Scanner scanner = new Scanner(System.in);
    private static Player currentPlayer;
    private static GameMap gameMap = new GameMap();
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static List<Message> messages = new ArrayList<>();
    private static List<Gift> gifts = new ArrayList<>();
    public static Weather currentWeather = Weather.Sunny;
    private static Weather forecastedWeather = Weather.Sunny;
    private static final Database database = new Database();
    public static int currentPlayerIndex = 0;
    public static boolean canBuildGreenHouse = false;
//    public static boolean greenHouseBuilt = false;
    public static Map<Player, Item> soldItems = new HashMap<>();

    // TODO make the game methods nonstatic
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
    public static List<Player> getAllPlayers() {
        return players;
    }

    public static void setForecastedWeatherBySeason(Season season) {
        List<Weather> possibleWeathers = switch (season) {
            case SPRING -> List.of(Weather.Sunny, Weather.Rain, Weather.Storm);
            case SUMMER -> List.of(Weather.Sunny, Weather.Storm);
            case FALL -> List.of(Weather.Sunny, Weather.Rain);
            case WINTER -> List.of(Weather.Snow, Weather.Storm, Weather.Sunny);
        };
        Random random = new Random();
        forecastedWeather = possibleWeathers.get(random.nextInt(possibleWeathers.size()));
    }

    public static Weather getForecastedWeather() {
        return forecastedWeather;
    }


    //    public static void startTheGame() {
//        database.initializeStoresAndItems();
//        database.initializePlantDatabase();
//        database.loadNPCs();
//        Player.initializeFriendships(players);
//    }
    public static void advanceToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            GameManager.getGameClock().advanceTime(60);
        }

        currentPlayer = players.get(currentPlayerIndex);
    }

    public static void initializeFarms() {
        int[][] farmCoords = {
                {0, 0},     // A
                {0, 70},    // B
                {70, 0},    // C
                {70, 70}    // D
        };
        int i = 0;
        for (Player player : players) {
            int[] coords = farmCoords[i++];
            player.setFarm(coords[0], coords[1]);
        }
    }

    public static Result startTheGame() {
        try {
//            database.initializeStoresAndItems();
//            database.initializePlantDatabase();
//            database.loadNPCs();/


            initializeFarms();
            Player.initializeFriendships(players);
            GameManager.getGameClock().setDay(1);
            GameManager.getGameClock().setSeason(Season.SPRING);
            GameManager.getGameClock().setHour(9);
            GameManager.getGameClock().setMinute(0);
            if (!players.isEmpty()) {
                currentPlayer = players.get(0);
            }
            for (NPC npc : getAllNPCs()) {
                npc.initializeFriendships();
            }
            return Result.success("Game started successfully! Current player: " +
                    (currentPlayer != null ? currentPlayer.getUsername() : "None"));
        } catch (Exception e) {
            return Result.error("Failed to start game: " + e.getMessage());
        }
    }
    public static List<NPC> getAllNPCs() {
        return database.getNPCs();
    }
    public static NPC getNPCByName(String npcName) {
        for (NPC npc: getAllNPCs()) {
            if (npc.getName().equalsIgnoreCase(npcName)) {
                return npc;
            }
        }
        return null;
    }
    public static Weather getCurrentWeather() {
        return currentWeather;
    }
    public static Player getPlayerByUsername(String username){
        for (Player player : players){
            if (player.getUsername().equals(username)) return player;
        }
        return null;
    }
    public static Database getDatabase() {
        return database;
    }
    public static void addMessage(Message message) {
        messages.add(message);
    }
    public static List<Message> getMessages(Player player1, Player player2) {
        List<Message> commonMessages = new ArrayList<>();
        for (Message message : messages) {
            if ((message.getSender().equals(player1) && message.getReceiver().equals(player2))
            || (message.getSender().equals(player2) && message.getReceiver().equals(player1)) )
                commonMessages.add(message);
        }
        return commonMessages;
    }
    public static Scanner getScanner() {
        return scanner;
    }
    public static void addGift(Gift gift) {
        gifts.add(gift);
    }
    public static Gift getGiftById(int id){
        for (Gift gift : gifts){
            if (gift.Id == id) return gift;
        }
        return null;
    }
    public static List<Gift> getAllGifts(){
        return gifts;
    }
    public static void setForecastedWeather(Weather forecastedWeather) {
        Game.forecastedWeather = forecastedWeather;
    }
}
