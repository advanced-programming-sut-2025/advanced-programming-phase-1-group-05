package models.Enums;

public enum GameMenuCommands {
    ShowEnergy("energy show"),
    EnergySetCC("energy set -v (?<value>\\d+)"),
    EnergyUnlimitedCC("energy unlimited"),
    ShowInventory("inventory show");


    private final String pattern;
    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }
}
