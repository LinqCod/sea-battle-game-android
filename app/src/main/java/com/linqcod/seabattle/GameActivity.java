package com.linqcod.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int GAME_FIELD_ROWS = 10;
    private static final int GAME_FIELD_COLS = 10;

    GameField playerGameField;
    GameField enemyGameField;
    Button[][] playerCells;
    Button[][] enemyCells;

    private boolean isGameOver;
    private boolean canPlayerHit;

    private static final int SHIPS_COUNT = 10;
    private Ship[] playerShips;
    private Ship[] enemyShips;

    private int playerShipsCount;
    private int enemyShipsCount;
    private TextView playerShipsTV;
    private TextView enemyShipsTV;

    public static final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerShipsTV = findViewById(R.id.playerShipsCountTV);
        enemyShipsTV = findViewById(R.id.enemyShipsCountTV);

        startGame();
    }

    private void startGame() {
        playerShips = new Ship[]{new Ship(4), new Ship(3), new Ship(3),
                new Ship(2), new Ship(2), new Ship(2), new Ship(1), new Ship(1), new Ship(1), new Ship(1)};
        enemyShips =  new Ship[]{new Ship(4), new Ship(3), new Ship(3),
                new Ship(2), new Ship(2), new Ship(2), new Ship(1), new Ship(1), new Ship(1), new Ship(1)};

        isGameOver = false;
        canPlayerHit = true;

        playerShipsCount = SHIPS_COUNT;
        enemyShipsCount = SHIPS_COUNT;
        playerShipsTV.setText("Your ships left: " + playerShipsCount);
        enemyShipsTV.setText("Enemy ships left: " + enemyShipsCount);

        spawnPlayerShips();
        spawnEnemyShips();
    }

    private void spawnPlayerShips() {
        //Generate gameField and ships
        playerGameField = new GameField(GAME_FIELD_ROWS, GAME_FIELD_COLS);
        playerGameField.randShipsGenerate(SHIPS_COUNT, playerShips);

        //Display gameField
        playerCells = new Button[playerGameField.getFieldHeight()][playerGameField.getFieldWidth()];
        GridLayout field = findViewById(R.id.playerGameField);
        field.removeAllViews();
        field.setColumnCount(playerGameField.getFieldWidth());
        for (int i = 0; i < playerGameField.getFieldHeight(); i++)
            for (int j = 0; j < playerGameField.getFieldWidth(); j++) {
                int status = playerGameField.getFieldCell(j, i).getStatus();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                playerCells[i][j] = (Button) inflater.inflate(R.layout.small_cell, field, false);
                playerCells[i][j].setTag(-1);
                if(status == 1) {
                    playerCells[i][j].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    for (int k = 0; k < playerShips.length; k++) {
                        Cell[] shipCoords = playerShips[k].getShipCoords();
                        for (int l = 0; l < shipCoords.length; l++) {
                            if(shipCoords[l].getX() == j && shipCoords[l].getY() == i) {
                                playerCells[i][j].setTag(k);
                                //cells[i][j].setText(cells[i][j].getTag().toString());
                            }
                        }
                    }
                }
                field.addView(playerCells[i][j]);
            }
    }

    private void spawnEnemyShips() {
        //Generate gameField and ships
        enemyGameField = new GameField(GAME_FIELD_ROWS, GAME_FIELD_COLS);
        enemyGameField.randShipsGenerate(SHIPS_COUNT, enemyShips);

        //Display gameField
        enemyCells = new Button[enemyGameField.getFieldHeight()][enemyGameField.getFieldWidth()];
        GridLayout field = findViewById(R.id.enemyGameField);
        field.removeAllViews();
        field.setColumnCount(enemyGameField.getFieldWidth());
        for (int i = 0; i < enemyGameField.getFieldHeight(); i++)
            for (int j = 0; j < enemyGameField.getFieldWidth(); j++) {
                int status = enemyGameField.getFieldCell(j, i).getStatus();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final int y = i;
                final int x = j;
                enemyCells[i][j] = (Button) inflater.inflate(R.layout.cell, field, false);
                enemyCells[i][j].setTag(-1);
                enemyCells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(canPlayerHit) {
                            if(!enemyGameField.getFieldCell(x, y).isHitted()) {
                                //TAG(-1) - empty cell, TAG(0) - 4 ship, TAG(1,2) - 3 ship, TAG(3-5) - 2 ship, TAG(6-9) - 1 ship
                                int tag = (int)view.getTag();
                                if(tag == -1) {
                                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                    canPlayerHit = false;
                                    //Enemy turn
                                    enemyTurn();
                                }
                                else {
                                    enemyShips[tag].decreaseHealth();
                                    if(enemyShips[tag].getHealth() <= 0) {
                                        enemyShipDead();
                                    }
                                    view.setBackgroundColor(Color.RED);
                                }
                                enemyGameField.getFieldCell(x, y).hit();
                            }
                        }
                    }
                });
                if(status == 1) {
                    for (int k = 0; k < enemyShips.length; k++) {
                        Cell[] shipCoords = enemyShips[k].getShipCoords();
                        for (int l = 0; l < shipCoords.length; l++) {
                            if(shipCoords[l].getX() == j && shipCoords[l].getY() == i) {
                                enemyCells[i][j].setTag(k);
                            }
                        }
                    }
                }
                //cells[i][j].setText(cells[i][j].getTag().toString());

                field.addView(enemyCells[i][j]);
            }
    }

    private void enemyTurn() {
        int x = rand.nextInt(GAME_FIELD_COLS);
        int y = rand.nextInt(GAME_FIELD_ROWS);

        if(!playerGameField.getFieldCell(x, y).isHitted()) {
            //TAG(-1) - empty cell, TAG(0) - 4 ship, TAG(1,2) - 3 ship, TAG(3-5) - 2 ship, TAG(6-9) - 1 ship
            int tag = (int)playerCells[y][x].getTag();
            if(tag == -1) {
                playerCells[y][x].setBackgroundColor(Color.BLUE);
                canPlayerHit = true;
                return;
            }
            else {
                playerShips[tag].decreaseHealth();
                if(playerShips[tag].getHealth() <= 0) {
                    playerShipDead();
                }
                playerCells[y][x].setBackgroundColor(Color.RED);
            }
            playerGameField.getFieldCell(x, y).hit();
            enemyTurn();
        }
        else {
            enemyTurn();
        }
    }

    private void gameOver(String gameOverText) {

    }

    private void playerShipDead() {
        //TODO: draw hits around ship
        playerShipsTV.setText("Your ships left: " + --playerShipsCount);
        Toast.makeText(this, "YOUR SHIP DEAD", Toast.LENGTH_SHORT).show();
    }

    private void enemyShipDead() {
        //TODO: draw hits around ship
        enemyShipsTV.setText("Enemy ships left: " + --enemyShipsCount);
        Toast.makeText(this, "SHIP DEAD", Toast.LENGTH_SHORT).show();
    }
}