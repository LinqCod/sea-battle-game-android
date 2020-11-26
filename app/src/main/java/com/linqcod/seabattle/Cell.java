package com.linqcod.seabattle;

public class Cell {
    private int x;
    private int y;

    private int status;

    public Cell(int x, int y, int status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
