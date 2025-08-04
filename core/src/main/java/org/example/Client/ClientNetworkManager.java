//package org.example.Client;
//
//import com.esotericsoftware.kryonet.Client;
//import org.example.Common.Enums.MessageType;
//import org.example.Common.Request.TradeMessage;
//import org.example.Server.models.MyGame;
//
//import java.io.IOException;
//
//import static com.esotericsoftware.kryonet.rmi.ObjectSpace.registerClasses;
//
//public class ClientNetworkManager {
//    private Client client;
//    private String username;
//
//    public ClientNetworkManager() {
//        client = new Client();
//        registerClasses(client.getKryo());
//        client.start();
//        try {
//            client.connect(5000, "localhost", 54555, 54777);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void sendTradeRequest(TradeMessage msg) {
//        client.sendTCP(msg);
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//    // setupListeners(), registerClasses(), etc.
//}
package org.example.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientNetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientNetworkManager(String host, int port) throws Exception {
        socket = new Socket(host, port);

        // ✅ مهم: flush بعد از ساخت output stream
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        in = new ObjectInputStream(socket.getInputStream());

        System.out.println("✅ Connected to server: " + host + ":" + port);
    }

    public synchronized Object sendAndReceive(Object request) {
        try {
            out.writeObject(request);
            out.flush();
            return in.readObject();
        } catch (Exception e) {
            System.err.println("❌ Error in sendAndReceive:");
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void send(Object request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (Exception e) {
            System.err.println("❌ Error in send:");
            e.printStackTrace();
        }
    }
}
