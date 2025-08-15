package org.example.Server.Packets;

import org.example.Common.Product;
import org.example.Server.models.Store;

import java.util.List;

public class StoreUpdatePacket {
    public String storeName;
    public List<String> products;
}
