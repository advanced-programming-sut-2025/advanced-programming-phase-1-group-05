package org.example.tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FarmingTester {
    public void test() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("testcases.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("INPUT:")) {
                String input = line.substring(6).trim();
                String expected = reader.readLine().substring(9).trim(); // Skip "EXPECTED:"
               // String actual = game.processCommand(input);
                //assertEquals(expected, actual);
            }
        }

    }

}
