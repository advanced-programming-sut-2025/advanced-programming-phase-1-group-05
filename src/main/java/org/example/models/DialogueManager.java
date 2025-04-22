package org.example.models;

import org.example.models.Enums.Weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DialogueManager {
    private static final Map<String, Map<Weather, List<String>>> npcDialogues = new HashMap<>();
    private static final Random random = new Random();

}
