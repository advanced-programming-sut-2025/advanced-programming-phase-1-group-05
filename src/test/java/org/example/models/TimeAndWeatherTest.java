package org.example.models;
import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.controllers.RegisterMenuController;
import org.example.views.AppMenu;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimeAndWeatherTest {

//    AppMenu menu = MenuController.; // فرض بر اینه که کلاس Game دارای executeCommand هست

    private final GameMenuController gameMenuController =
            new GameMenuController(Game.getScanner(), RegisterMenuController.currentUser);

    @Test
    void testInitialTime() {
        String result = gameMenuController.showTime().getMessage();
        assertEquals("Current time: 09:00", result);
    }

    @Test
    void testInitialDate() {
        String result = gameMenuController.showDate().getMessage();
        assertEquals("Current date: Day 1 of Spring", result);
    }

    @Test
    void testInitialDateTime() {
        String result = gameMenuController.showDatetime().getMessage();
        assertEquals("Current date and time: Day 1 of Spring, 09:00", result);
    }

    @Test
    void testDayOfTheWeek() {
        String result = gameMenuController.showDayOfTheWeek().getMessage();
        assertEquals("Today is: Saturday", result);
    }
//
    @Test
    void testAdvanceTimeBackwardsFails() {
        String result = gameMenuController.cheatAdvanceTime("cheat advance time -2h").getMessage();
        assertEquals("Error: You cannot go back in time.", result);
    }

    @Test
    void testAdvanceTimeForward2Hours() {
        String result = gameMenuController.cheatAdvanceTime("cheat advance time 2h").getMessage();
        assertEquals("Time advanced by 2 hours.", result);

        String timeCheck = gameMenuController.showTime().getMessage();
        assertEquals("Current time: 11:00", timeCheck);
    }

    @Test
    void testAdvanceDateInvalidFormat() {
        String result = gameMenuController.cheatAdvanceDate("cheat advance date -2d").getMessage();
        assertEquals("Invalid format! Use: cheat advance date <X>d", result);
    }

//
    @Test
    void testSeasonRemainsSpringAfter2Days() {
        String result = gameMenuController.showSeason().getMessage();
        assertEquals("Current season: SPRING", result);
    }
//
    @Test
    void testWeatherCheck() {
        String result = gameMenuController.showWeather().getMessage();
        assertEquals("Current weather: Sunny ☀️", result);
    }
//
    @Test
    void testSetWeatherSunny() {
        String result = gameMenuController.cheatWeatherSet("cheat weather set SUNNY").getMessage();
        assertEquals("Forecasted weather for tomorrow set to: Sunny ☀️", result);
    }

     //
    @Test
    void testAdvanceTime12Hours() {
        String result = gameMenuController.cheatAdvanceTime("cheat advance time 12h").getMessage();
        assertEquals("Time advanced by 12 hours.", result);

        String dateCheck = gameMenuController.showDate().getMessage();
        assertEquals("Current date: Day 1 of Spring", dateCheck);
    }
//
    @Test
    void testWeatherStillSunnyAfterTimeAdvance() {
        String result = gameMenuController.showWeather().getMessage();
        assertEquals("Current weather: Sunny ☀️", result);
    }
}

