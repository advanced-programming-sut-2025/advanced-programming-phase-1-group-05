package models;

import models.Enums.Season;

import java.sql.Time;
import java.util.Date;

public class TimeAndDate {
    int day;
    Season season;
    Time time;
    public void nextDay() {}

    //every 28 days
    public void changeSeason(Season season) {}

    public Season getCurrentSeason() { return season; }
    public Time getCurrentTime() { return time; }
    public int getDay() { return day; }
}
