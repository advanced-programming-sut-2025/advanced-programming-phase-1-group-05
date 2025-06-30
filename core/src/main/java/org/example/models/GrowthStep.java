package org.example.models;

import org.example.models.Enums.GrowthStage;

public class GrowthStep {
    private int days;

    public GrowthStep( int days) {
        this.days = days;
    }


    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
