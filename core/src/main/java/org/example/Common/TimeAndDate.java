package org.example.Common;

import org.example.Server.controllers.GameManager;
import org.example.Common.Enums.Season;
import org.example.Common.Enums.Weather;
import org.example.Server.models.MyGame;

import java.io.Serializable;

public class TimeAndDate implements Serializable {
    int day = 1;
    Season season;
    public int hour = 9;
    int minute = 0;
    float timeAccumulator = 0;
    public void nextDay() {}

    public TimeAndDate() {
        this.season = Season.SPRING;
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

    public void advanceTime(int seconds){
        timeAccumulator += seconds;
        if (timeAccumulator >= 42f) {
            minute += 60;
            timeAccumulator = 0;
        }
        while (minute >= 60) {
            minute -= 60;
            hour ++;
        }
        if (hour >= 23) {
            advanceDay();
        }
    }
    public void advanceDay() {
        if (MyGame.getForecastedWeather() != Weather.Sunny) {
            MyGame.currentWeather = MyGame.getForecastedWeather();
        }
        day++;
        hour = 9;
        minute = 0;
        if (day > 28) {
            day = 1;
            season = season.next();
        }
        MyGame.getGameMap().growPlants();
        MyGame.getGameMap().setForagingItems();
        MyGame.getGameMap().setForagingMinerals();
        MyGame.setForecastedWeatherBySeason(season);
        MyGame.getGameMap().crowDamage();
        GameManager.nextDay();
    }
    public Season getCurrentSeason() { return season; }
    public int getDay() { return day; }
    public int getHour() { return hour; }

    public int getMinute() {
        return minute;
    }
}
