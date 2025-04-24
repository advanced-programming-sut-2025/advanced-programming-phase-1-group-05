package org.example.models.Enums;

public enum GameMenuCommands {
    ShowEnergy("show energy"),
    EnergySetCC("set energy -v (?<value>\\d+)"),
    EnergyUnlimitedCC("unlimited energy"),
    ShowInventory("show inventory"),
    EquipTool("equip tool (?<toolName>\\S+)"),
    ShowCurrentTool("show current tool"),
    ShowAvailableTools("show available tools"),
    UpgradeTool("upgrade tool (?<toolName>\\S+)"),
    UseTool("use tool -d (?<direction>\\S+)"),
    ShowCraftInfo("show craft info -n (?<craftName>\\S+)"),
    Plant("plant -s (?<seed>\\S+) -d (?<direction>\\S+)"),
    ShowPlant("show plant -l (?<x, y>\\S+)"),
    FertilizeCrop("fertilize -f (?<fertilizer>\\S+) -d (?<direction>\\S+)"),
    WaterCrop("how much water")

    private final String pattern;
    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }
}