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
    //int minute = 0;
    float timeAccumulator = 0;
    public void nextDay() {}

    public TimeAndDate() {
        this.season = Season.SPRING;
        //this.minute = 0;
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
       // this.minute = minute;
    }

    public Season getSeason() {
        return season;
    }


    public int getTotalHours() {
        return day * 24 + hour;
    }

    public boolean advanceTime(float seconds){
        timeAccumulator += seconds;
        while (timeAccumulator >= 15f) {
            timeAccumulator -= 15f;
            hour ++;
            System.out.println("advanced an hour");
        }
        if (hour >= 22) {
            //advanceDay();
            return true;
        }
        return false;
    }
    public void advanceDay() {
        System.out.println("Advance day start");
//        if (MyGame.getForecastedWeather() != Weather.Sunny) {
//            MyGame.currentWeather = MyGame.getForecastedWeather();
//        }
        day++;
        hour = 9;
        //minute = 0;
        if (day > 28) {
            day = 1;
            season = season.next();
        }
        System.out.println("Growing plants");
        MyGame.getGameMap().growPlants();

        System.out.println("Setting foraging items");
        MyGame.getGameMap().setForagingItems();

        System.out.println("Setting minerals");
        MyGame.getGameMap().setForagingMinerals();
        System.out.println("Setting weather");
        MyGame.setForecastedWeatherBySeason(season);
        System.out.println("Crow damage");
        MyGame.getGameMap().crowDamage();
        System.out.println("Next day game manager");
        GameManager.nextDay();
        System.out.println("advance day end");
    }
    public Season getCurrentSeason() { return season; }
    public int getDay() { return day; }
    public int getHour() { return hour; }

//   // public int getMinute() {
//        return minute;
//    }
}
