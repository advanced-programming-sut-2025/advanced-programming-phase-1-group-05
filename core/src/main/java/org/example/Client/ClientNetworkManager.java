package org.example.Client;

import com.esotericsoftware.kryonet.Client;
import org.example.Common.Enums.MessageType;
import org.example.Common.Request.TradeMessage;
import org.example.Server.models.MyGame;

import java.io.IOException;

import static com.esotericsoftware.kryonet.rmi.ObjectSpace.registerClasses;

public class ClientNetworkManager {
    private Client client;
    private String username;

    public ClientNetworkManager() {
        client = new Client();
        registerClasses(client.getKryo());
        client.start();
        try {
            client.connect(5000, "localhost", 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTradeRequest(TradeMessage msg) {
        client.sendTCP(msg);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client getClient() {
        return client;
    }

    // setupListeners(), registerClasses(), etc.
}
