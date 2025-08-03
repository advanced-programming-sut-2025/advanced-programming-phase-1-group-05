package org.example.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientNetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientNetworkManager(String host, int port) throws Exception {
        // اتصال به سرور
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("✅ Connected to server: " + host + ":" + port);
    }

    // ارسال درخواست و دریافت پاسخ
    public synchronized Object sendAndReceive(Object request) {
        try {
            out.writeObject(request);
            out.flush();
            return in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ارسال درخواست بدون نیاز به پاسخ
    public synchronized void send(Object request) {
        try {
            out.writeObject(request);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
