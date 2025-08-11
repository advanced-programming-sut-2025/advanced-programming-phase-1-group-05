package org.example.Server.Packets;

import org.example.Common.Enums.Direction;

public class NpcMovePacket {
    public String npcName;
    public float x;
    public float y;
    public Direction direction;
}
