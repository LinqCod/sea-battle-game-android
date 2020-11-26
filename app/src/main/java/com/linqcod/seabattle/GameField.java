package com.linqcod.seabattle;

import android.util.Log;

public class GameField {

    private final int fieldHeight;
    private final int fieldWidth;

    private final Cell[][] gameField;

    public GameField(int height, int width) {
        fieldHeight = height;
        fieldWidth = width;

        gameField = new Cell[height][width];

        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {
                gameField[i][j] = new Cell(j, i, 0);
            }
        }

    }

    public int cellsNotEmptyAroundCount(int x, int y) {

        int count = 0;

        if((x > 0 && x < getFieldWidth() - 1 && y > 0 && y < getFieldHeight() - 1)) {
            for(int i = y - 1; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if(!(i == y && j == x)) {
                        if(getFieldCell(j, i).getStatus() != 0) {
                            count++;
                        }
                    }
                }
            }
        }
        else if(x == 0 || x == getFieldWidth() - 1) {
            if(y == 0 || y == getFieldHeight() - 1) {
                if(getFieldCell(Math.abs(x - 1), y).getStatus() != 0 ||
                        getFieldCell(Math.abs(x - 1), Math.abs(y - 1)).getStatus() != 0 ||
                        getFieldCell(x, Math.abs(y - 1)).getStatus() != 0) {
                    count++;
                }
            }
            else {
                for(int i = y - 1; i <= y + 1; i++) {
                    if(x == 0) {
                        for (int j = x; j <= x + 1; j++) {
                            if(!(i == y && j == x)) {
                                if(getFieldCell(j, i).getStatus() != 0) count++;
                            }
                        }
                    }
                    else if(x == getFieldWidth() - 1) {
                        for (int j = x - 1; j <= x; j++) {
                            if(!(i == y && j == x)) {
                                if(getFieldCell(j, i).getStatus() != 0) count++;
                            }
                        }
                    }
                }
            }
        }
        else if(y == 0) {
            for(int i = y; i <= y + 1; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if(!(i == y && j == x)) {
                        if(getFieldCell(j, i).getStatus() != 0) count++;
                    }
                }
            }
        }
        else if(y == getFieldHeight() - 1) {
            for(int i = y - 1; i <= y; i++) {
                for (int j = x - 1; j <= x + 1; j++) {
                    if(!(i == y && j == x)) {
                        if(getFieldCell(j, i).getStatus() != 0) count++;
                    }
                }
            }
        }

        return count;
    }

    public void randShipsGenerate(int shipsCount, Ship[] ships) {
        for(int i = 0; i < shipsCount; i++) {
            randSpawnShip(ships[i]);
        }
    }

    private void randSpawnShip(Ship ship) {
        Cell startCell = randChooseStartCell();
        setFieldCell(startCell);

        int orientation = MainActivity.rand.nextInt(2);
        if(orientation == 0)
            placeShipHorizontally(ship, startCell);
        else
            placeShipVertically(ship, startCell);
    }

    private void placeShipHorizontally(Ship ship, Cell startCell) {

        Cell[] cells = new Cell[ship.getSize()];
        cells[0] = startCell;

        Cell currentCell = startCell;
        int cellsPlaced = 1;
        //Go left
        while(cellsPlaced < ship.getSize()) {
            if(currentCell.getX() > 0) {
                if(cellsNotEmptyAroundCount(currentCell.getX() - 1, currentCell.getY()) == 1) {
                    currentCell = new Cell(currentCell.getX() - 1, currentCell.getY(), 1);
                    setFieldCell(currentCell);
                    cells[cellsPlaced] = currentCell;
                    cellsPlaced++;
                }
                else break;
            }
            else break;
        }

        //Go right if ship is not placed
        if(cellsPlaced < ship.getSize()) {
            currentCell = startCell;
            while(cellsPlaced < ship.getSize()) {
                if(currentCell.getX() < getFieldWidth() - 1) {
                    if(cellsNotEmptyAroundCount(currentCell.getX() + 1, currentCell.getY()) == 1) {
                        currentCell = new Cell(currentCell.getX() + 1, currentCell.getY(), 1);
                        setFieldCell(currentCell);
                        cells[cellsPlaced] = currentCell;
                        cellsPlaced++;

                    }
                    else break;
                }
                else break;
            }
        }

        if(cellsPlaced != ship.getSize()) {
            Log.d("Test",startCell.getX() + " " + startCell.getY() + " " + ship.getSize());
            for(int i = 0; i < ship.getSize(); i++) {
                if(cells[i] != null)
                    cells[i].setStatus(0);
            }
            startCell = randChooseStartCell();
            setFieldCell(startCell);
            placeShipHorizontally(ship, startCell);
        }
    }

    private void placeShipVertically(Ship ship, Cell startCell) {

        Cell[] cells = new Cell[ship.getSize()];
        cells[0] = startCell;

        Cell currentCell = startCell;
        int cellsPlaced = 1;
        //Go up
        while(cellsPlaced < ship.getSize()) {
            if(currentCell.getY() > 0) {
                if(cellsNotEmptyAroundCount(currentCell.getX(), currentCell.getY() - 1) == 1) {
                    currentCell = new Cell(currentCell.getX(), currentCell.getY() - 1, 1);
                    setFieldCell(currentCell);
                    cells[cellsPlaced] = currentCell;
                    cellsPlaced++;
                }
                else break;
            }
            else break;
        }

        //Go right if ship is not placed
        if(cellsPlaced < ship.getSize()) {
            currentCell = startCell;
            while(cellsPlaced < ship.getSize()) {
                if(currentCell.getY() < getFieldHeight() - 1) {
                    if(cellsNotEmptyAroundCount(currentCell.getX(), currentCell.getY() + 1) == 1) {
                        currentCell = new Cell(currentCell.getX(), currentCell.getY() + 1, 1);
                        setFieldCell(currentCell);
                        cells[cellsPlaced] = currentCell;
                        cellsPlaced++;
                    }
                    else break;
                }
                else break;
            }
        }

        if(cellsPlaced != ship.getSize()) {
            Log.d("Test",startCell.getX() + " " + startCell.getY() + " " + ship.getSize());
            for(int i = 0; i < ship.getSize(); i++) {
                if(cells[i] != null)
                    cells[i].setStatus(0);
            }
            startCell = randChooseStartCell();
            setFieldCell(startCell);
            placeShipVertically(ship, startCell);
        }
    }

    private Cell randChooseStartCell() {
        int startX = MainActivity.rand.nextInt(getFieldWidth());
        int startY = MainActivity.rand.nextInt(getFieldHeight());

        if(getFieldCell(startX, startY).getStatus() == 0 && cellsNotEmptyAroundCount(startX, startY) == 0) {
            return new Cell(startX, startY, 1);
        }
        else {
            return randChooseStartCell();
        }
    }

    public Cell getFieldCell(int x, int y) {
        return gameField[y][x];
    }

    public void setFieldCell(Cell cell) {
        gameField[cell.getY()][cell.getX()] = cell;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }
}
