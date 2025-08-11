package org.example.Server.Packets;

public class ScoreboardUpdatePacket {
    public String username;
    public int gold;
    public int quests;
    public int skill;

    public ScoreboardUpdatePacket() {}

    public ScoreboardUpdatePacket(String username, int gold, int quests, int skill) {
        this.username = username;
        this.gold = gold;
        this.quests = quests;
        this.skill = skill;
    }
}

