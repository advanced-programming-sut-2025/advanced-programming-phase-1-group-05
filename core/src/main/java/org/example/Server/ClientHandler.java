package org.example.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            while (true) {
                Object message = in.readObject();
                PacketHandler.handle(message, out);
            }

        } catch (Exception e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }
}
