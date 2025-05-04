package org.example.models.Enums;

public enum GrowthStage {
    SEED(1),
    SPROUT(2),
    JUVENILE(3),
    MATURE(4),
    BLOOMING(5);

    private final int stageNumber;
    GrowthStage(int stageNumber) {
        this.stageNumber = stageNumber;
    }
    public int getStageNumber() {
        return stageNumber;
    }

}
