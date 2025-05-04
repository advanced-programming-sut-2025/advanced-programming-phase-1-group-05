package org.example;

import org.example.models.Enums.GrowthStage;

public class GrowthStep {
    private GrowthStage stage;
    private int days;

    public GrowthStep(GrowthStage stage, int days) {
        this.stage = stage;
        this.days = days;
    }

    public GrowthStage getStage() {
        return stage;
    }

    public void setStage(GrowthStage stage) {
        this.stage = stage;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
