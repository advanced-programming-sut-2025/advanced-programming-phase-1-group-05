package org.example.models;

import org.example.controllers.GameManager;
import org.example.models.Enums.Season;

import java.sql.Time;
import java.util.Date;

public class TimeAndDate {
    int day;
    Season season;
    int hour;
    int minute;
    private int minutes;

    public void nextDay() {}
    public TimeAndDate() {
        this.day = 1;
        this.season = Season.SPRING; // مقداردهی پیش‌فرض فصل
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

    public int getMinute() {
        return minute;
    }

    //every 28 days
    public void changeSeason(Season season) {}

    public void advanceTime(int minutes){
        this.minutes += minutes;
        while (this.minutes >= 60) {
            this.minutes -= 60;
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
        GameManager.nextDay();
        Game.setForecastedWeatherBySeason(season);
    }
    public Season getCurrentSeason() { return season; }
    public int getDay() { return day; }
    public int getHour() { return hour; }
}