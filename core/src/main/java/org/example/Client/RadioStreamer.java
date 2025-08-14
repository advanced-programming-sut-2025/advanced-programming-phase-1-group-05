package org.example.Client;

import com.esotericsoftware.kryonet.Client;
import org.example.Server.Packets.RadioPackets.RadioAudioDataPacket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Arrays;

public class RadioStreamer {
    private final String ownerUsername;

    public RadioStreamer(Client client, String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public void streamAudio(String filePath) {
        try(AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filePath))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = ais.read(buffer)) != -1) {
                RadioAudioDataPacket packet = new RadioAudioDataPacket();
                packet.ownerUsername = ownerUsername;
                packet.audioData = Arrays.copyOf(buffer, bytesRead);
                GameClient.client.sendTCP(packet);
                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
