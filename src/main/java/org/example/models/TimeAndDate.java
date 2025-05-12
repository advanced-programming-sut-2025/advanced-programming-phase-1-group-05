package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Enums.Season;

import java.sql.Time;
import java.util.Date;

public class TimeAndDate {
    int day = 1;
    private static Season season;
    int hour = 9;
    int minute = 0;
    public void nextDay() {}

    //every 28 days
    public void changeSeason(Season season) {}

    public void advanceTime(int minutes){
        minutes += minutes;
        while (minutes >= 60) {
            minutes -= 60;
            hour ++;
        }
        if (hour >= 24) {
            hour = 9;
            minute = 0;
            advanceDay();
        }
    }
    public void advanceDay() {
        day++;
        if (day > 28) {
            day = 1;
            season = season.next();
        }
        Game.getGameMap().growPlants();
        Game.getGameMap().setForagingItems();
        //TODO implement
        Game.getGameMap().setForagingMinerals();
        if(!Game.getCurrentPlayer().isEnergyUnlimited()) Game.getCurrentPlayer().resetEnergy();
        GameManager.nextDay();
    }
    public static Season getCurrentSeason() { return season; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
}