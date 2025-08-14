package org.example.Client;

import com.esotericsoftware.kryonet.Connection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Server.LLM;
import org.example.Server.Packets.NPCDialoguePacket;
import org.example.Server.Packets.NpcDialogueRequest;
import org.example.Server.ServerMain;
import org.example.Server.models.NPC;

import java.lang.reflect.Type;
import java.util.*;

public class DialogueManager {

    private Map<String, Connection> playerConnections;

    public DialogueManager(Map<String, Connection> playerConnection) {
        playerConnections = playerConnection;
    }

    public void handleNpcInteraction(NpcDialogueRequest request) {
        Connection conn = playerConnections.get(request.username);
        if (conn == null) return;

        String prompt = generateNpcPrompt(request.NpcName, request.username, request.hour, request.weather);

        // Generate response via API call
        String response = LLM.generateResponse(prompt);

        NPCDialoguePacket packet = new NPCDialoguePacket();
        packet.npcName = request.NpcName;
        packet.message = response;

        conn.sendTCP(packet);
    }

    private String generateNpcPrompt(String npcName, String playerUsername, int hour, String weather) {
        return String.format(
            "You are %s, an NPC in stardew valley. Interact naturally with the player %s. " +
                "Give a unique, friendly response appropriate for hour %d and %s weather. less than 150 characters!",
            npcName, playerUsername, hour, weather
        );
    }
}
