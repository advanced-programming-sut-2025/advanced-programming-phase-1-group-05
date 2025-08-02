package org.example.Client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Common.DataTransferObjects.PlayerUpdate;

public class ClientNetworkManager {
    private Client client;

    public ClientNetworkManager() throws Exception {
        client = new Client();
        client.start();

        Kryo kryo = client.getKryo();
        kryo.register(PlayerUpdate.class);

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof PlayerUpdate) {
                    PlayerUpdate update = (PlayerUpdate) object;
                    System.out.println("Other player moved: " + update.playerId + " to (" + update.x + ", " + update.y + ")");
                }
            }
        });

        client.connect(5000, "localhost", 54555, 54777);
    }

    public void sendPlayerPosition(String playerId, float x, float y) {
        PlayerUpdate update = new PlayerUpdate(playerId, x, y);
        client.sendTCP(update);
    }
}
