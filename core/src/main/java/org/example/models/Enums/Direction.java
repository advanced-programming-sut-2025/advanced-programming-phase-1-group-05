package org.example.models.Enums;


public enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
