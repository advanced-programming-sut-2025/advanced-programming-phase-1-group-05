package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.models.Enums.Weather;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DialogueManager {
    private static final Map<String, Map<String, List<String>>> npcDialogues = new HashMap<>();
    private static final Random random = new Random();

    static {
        try {
            FileHandle file = Gdx.files.internal("jsonFiles/dialogues.json");
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            Map<String, Map<String, List<String>>> data = new Gson().fromJson(file.reader("UTF-8"), type);

            npcDialogues.putAll(data);
        } catch (Exception e) {
            System.out.println("Failed to load npc dialogues: " + e.getMessage());
        }
    }

    public static String getNpcDialogue(String npcName, String weather) {
        Map<String, List<String>> npcData = npcDialogues.getOrDefault(npcName, null);
        if (npcData == null) {
            return npcName +" just stares at you awkwardly";
        }
        List<String> options = npcData.getOrDefault(weather.toLowerCase(), npcData.get("daily"));
        if (options == null || options.isEmpty()) {
            return npcName + " just stares at you awkwardly";
        }
        return options.get(random.nextInt(options.size()));
    }
}
