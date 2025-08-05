package org.example.Server.models.Building;

import org.example.Common.Enums.BuildingType;

import java.io.Serializable;

public class Building implements Serializable {
    BuildingType type;


    public BuildingType getType() {
        return type;
    }
}
