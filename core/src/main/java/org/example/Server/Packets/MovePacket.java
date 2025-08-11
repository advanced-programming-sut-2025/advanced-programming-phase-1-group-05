package org.example.Server.Packets;

import org.example.Common.Enums.Direction;

public class MovePacket {
    public String playerUsername;
    public Direction direction;
    public float delta;

    public MovePacket() {}

}
