package org.example.models.Enums;

public enum EnclosureType {
    COOP(7, 4), // chicken, duck, rabbit, dinosaur
    BARN(6, 3);// cow, goat, sheep, pig


    private int rows;
    private int columns;
    EnclosureType(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }

    public static EnclosureType fromString(String enclosureType) {
        for (EnclosureType type : EnclosureType.values()) {
            if (type.name().equalsIgnoreCase(enclosureType)) {
                return type;
            }
        }
        return null;
    }
}