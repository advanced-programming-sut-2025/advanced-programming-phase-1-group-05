package org.example.models.Enums;

import org.example.models.Product;

import java.util.List;

public enum AnimalType {
    COOP(7, 4), // chicken, duck, rabbit, dinosaur
    BARN(6, 3);// cow, goat, sheep, pig


    private int rows;
    private int columns;
    AnimalType(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
}