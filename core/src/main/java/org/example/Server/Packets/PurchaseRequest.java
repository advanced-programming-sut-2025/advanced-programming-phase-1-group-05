package org.example.Server.Packets;

import org.example.Common.Player;
import org.example.Common.Product;
import org.example.Server.models.Store;

import java.util.HashMap;
import java.util.Map;

public class PurchaseRequest {
    public String storeName;
    public String playerUsername;
    public Map<String, Integer> items;
}
