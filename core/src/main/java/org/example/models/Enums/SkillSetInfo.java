package org.example.models.Enums;

public enum SkillSetInfo {
    Farming("Farming", "Levels are gained by harvesting crops\nand caring for animals."),
    Mining("Mining", "Mining skill is increased by breaking\nrocks (done with a Pickaxe)"),
    Foraging("Foraging", "Foraging skill includes both gathered\nforaged goods, and wood from chopped trees."),
    Fishing("Fishing", "Fishing is completing the fishing mini-game\nor catching fish in a Crab Pot."),
    Combat("Combat", "Combat is a skill tied to the player's\nability to fight against monsters.");

    private final String skillName;
    private final String skillDescription;
    SkillSetInfo(String skillName, String skillDescription) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
    }
    public String getSkillName() {
        return skillName;
    }
    public String getSkillDescription() {
        return skillDescription;
    }
}
