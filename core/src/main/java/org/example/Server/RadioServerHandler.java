package org.example.Server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import org.example.Server.Packets.RadioPackets.RadioAudioDataPacket;
import org.example.Server.Packets.RadioPackets.RadioJoinPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadioServerHandler {
    private final Server server;
    private static final Map<String, List<String>> radioListeners = new HashMap<>();

    public RadioServerHandler(Server server) {
        this.server = server;
    }
    public void received(Connection c, Object object) {
        if (object instanceof RadioJoinPacket) {
            RadioJoinPacket joinPacket = (RadioJoinPacket) object;
            radioListeners.computeIfAbsent(joinPacket.ownerUsername, k-> new ArrayList<>())
                .add(joinPacket.listenerUsername);
        }
        else if (object instanceof RadioAudioDataPacket) {
            RadioAudioDataPacket audioDataPacket = (RadioAudioDataPacket) object;
            List<String> listeners = radioListeners.get(audioDataPacket.ownerUsername);
            if (listeners!= null) {
                for (Connection connection : server.getConnections()) {
                    String username = ServerMain.getUsernameFromConnection(connection);
                    if (listeners.contains(username)) {
                        connection.sendTCP(audioDataPacket);
                    }
                }
            }
        }
    }
}
