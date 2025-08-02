package org.example.Common.DataTransferObjects;

import org.example.Common.Enums.GameMessageType;

public class GameMessage {
    public GameMessageType type;
    public String playerId;
    public float newX, newY;
    public String itemName;

    public GameMessage() {}
}
