package org.example.Client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import org.example.Common.DataTransferObjects.PlayerUpdate;

import java.io.IOException;

public class GameClient {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();

        Kryo kryo = client.getKryo();
        kryo.register(PlayerUpdate.class);

        client.connect(5000, "localhost", 54555, 54777);  // change "localhost" to your server's IP if on LAN

        // Send initial position update
        PlayerUpdate update = new PlayerUpdate("friend-" + System.currentTimeMillis(), 0, 0);
        client.sendTCP(update);

        // Listen for updates from server
        client.addListener(new com.esotericsoftware.kryonet.Listener() {
            public void received(com.esotericsoftware.kryonet.Connection connection, Object object) {
                if (object instanceof PlayerUpdate u) {
                    System.out.println("Received update: " + u.playerId + " is at (" + u.x + ", " + u.y + ")");
                }
            }
        });

        // Example: move right every second
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(1000);
                    update.x += 1;
                    client.sendTCP(update);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
