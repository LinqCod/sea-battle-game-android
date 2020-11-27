package com.linqcod.seabattle;

public class Ship {

    private int size;
    private int health;
    private Cell[] ship;

    public Ship(int size) {
        this.size = size;
        this.health = size;
    }

    public void decreaseHealth() {
        health--;
    }

    public int getHealth() {
        return health;
    }

    public void setShipCoords(Cell[] coords) {
        ship = coords;
    }

    public Cell[] getShipCoords() {
        return ship;
    }

    public int getSize() {
        return size;
    }
}
