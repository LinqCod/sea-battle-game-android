package com.linqcod.seabattle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int GAME_FIELD_ROWS = 10;
    private static final int GAME_FIELD_COLS = 10;

    private static final int SHIPS_COUNT = 10;
    private static final Ship[] ships = new Ship[]{new Ship(4), new Ship(3), new Ship(3),
            new Ship(2), new Ship(2), new Ship(2), new Ship(1), new Ship(1), new Ship(1), new Ship(1)};

    public static final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        spawnShips(this);
    }

    private static void spawnShips(GameActivity activity) {
        //Generate gameField and ships
        GameField gameField = new GameField(GAME_FIELD_ROWS, GAME_FIELD_COLS);
        gameField.randShipsGenerate(SHIPS_COUNT, ships);

        //Display gameField
        Button[][] cells = new Button[gameField.getFieldHeight()][gameField.getFieldWidth()];
        GridLayout field = (GridLayout) activity.findViewById(R.id.gameField);
        field.removeAllViews();
        field.setColumnCount(gameField.getFieldWidth());
        for (int i = 0; i < gameField.getFieldHeight(); i++)
            for (int j = 0; j < gameField.getFieldWidth(); j++) {
                int status = gameField.getFieldCell(j, i).getStatus();
                LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                cells[i][j] = (Button) inflater.inflate(R.layout.cell, field, false);
                if(status == 1) {
                    cells[i][j].setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
                }
                field.addView(cells[i][j]);
            }
    }
}