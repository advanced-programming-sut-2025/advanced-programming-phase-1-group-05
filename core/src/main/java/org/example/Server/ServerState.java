package org.example.Server;

import com.esotericsoftware.kryonet.Connection;

import java.util.HashMap;
import java.util.Map;

public class ServerState {
    private static final Map<String, Connection> activeConnections = new HashMap<>();

    public static void addPlayer(String username, Connection connection) {
        activeConnections.put(username, connection);
    }

    public static void removePlayer(String username) {
        activeConnections.remove(username);
    }

    public static Connection getConnectionForUsername(String username) {
        return activeConnections.get(username);
    }

    public static void removeConnection(Connection connection) {
        // اگر کانکشن قطع شد ولی یوزرنیم نداشتیم
        activeConnections.values().remove(connection);
    }

    public static Map<String, Connection> getAllConnections() {
        return activeConnections;
    }
}
