package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Enums.Season;

import java.sql.Time;
import java.util.Date;

public class TimeAndDate {
    int day = 1;
    Season season;
    int hour = 9;
    int minute = 0;
    public void nextDay() {}

    public TimeAndDate() {
        this.day = 1;
        this.season = Season.SPRING;
        this.hour = 9;
        this.minute = 0;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Season getSeason() {
        return season;
    }


    public int getTotalHours() {
        return day * 24 + hour;
    }

    public void advanceTime(int minutes){
        minute += minutes;
        while (minute >= 60) {
            minute -= 60;
            hour ++;
        }
        if (hour >= 22) {
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
        Game.getGameMap().setForagingMinerals();
        Game.setForecastedWeatherBySeason(season);
        Game.getGameMap().crowDamage();
        GameManager.nextDay();
    }
    public Season getCurrentSeason() { return season; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
}