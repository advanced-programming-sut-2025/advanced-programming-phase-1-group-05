package org.example.Client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.example.Server.Packets.RadioPackets.RadioAudioDataPacket;
import org.example.Server.Packets.RadioPackets.RadioJoinPacket;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.ByteArrayInputStream;

public class RadioClientHandler {
    private final Client client;
    private final String thisPlayerUsername;
    public RadioClientHandler(Client client, String username) {
        this.client = client;
        this.thisPlayerUsername = username;
    }

    public void joinRadio(String ownerUsername) {
        RadioJoinPacket packet = new RadioJoinPacket();
        packet.ownerUsername = ownerUsername;
        packet.listenerUsername = thisPlayerUsername;
        client.sendTCP(packet);
    }

    public void received(Connection c, Object object) {
        if (object instanceof RadioAudioDataPacket) {
            RadioAudioDataPacket audioPacket = (RadioAudioDataPacket) object;
            playAudioFromBytes(audioPacket.audioData);
        }
    }

    private void playAudioFromBytes(byte[] audioData) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bais);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
