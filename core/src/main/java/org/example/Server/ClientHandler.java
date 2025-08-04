package org.example.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("🔁 Client handler thread started");
        try {
            // ✨ اول OutputStream ساخته بشه
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // مهم!
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                Object message = in.readObject();
                System.out.println("📨 Received from client: " + message.getClass().getSimpleName());
                PacketHandler.handle(message, out); // 🎯 الآن باید کار کنه
            }

        } catch (Exception e) {
            System.out.println("❌ Client disconnected: " + e.getMessage());
        }
    }
}
