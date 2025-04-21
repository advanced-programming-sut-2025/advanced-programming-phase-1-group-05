package models.Enums;

public enum GameMenuCommands {
    ShowEnergy("show energy"),
    EnergySetCC("set energy -v (?<value>\\d+)"),
    EnergyUnlimitedCC("unlimited energy"),
    ShowInventory("show inventory"),
    EquipTool("equip tool (?<toolName>\\S+)"),
    ShowCurrentTool("show current tool"),
    ShowAvailableTools("show available tools"),
    UpgradeTool("upgrade tool (?<toolName>\\S+)"),
    UseTool("use tool -d (?<direction>\\S+)");

    private final String pattern;
    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }
}
