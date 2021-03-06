package com.example.chouyutain.project;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by 1630155 on 2017-06-05.
 */

public class MainActivity extends Activity{

    private static final int MINES_IN_GAME = 10;
    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;
    private int revealedTiles;
    private Tile[][] grid;
    private GridLayout gridLayout;
    private TextView mineCount;
    private static int foundMines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.newGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        gridLayout = (GridLayout) findViewById(R.id.grid);
        mineCount = (TextView) findViewById(R.id.MineCount);

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        else {
            newGame();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        grid = (Tile[][])savedInstanceState.getSerializable("GAME_STATE");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("GAME_STATE", grid);
        super.onSaveInstanceState(outState);
    }

    private void newGame() {
        grid = new Tile[WIDTH][HEIGHT];

        int[] setPositions = generateRandoms();

        revealedTiles = 100;
        foundMines = 10;

        updateText();

        for (int i=0;i<WIDTH;i++) {
            for (int j=0;j<HEIGHT;j++) {

                boolean isMine = false;
                final int x = i;
                final int y = j;

                Button button = getButtonByCoordinate(x, y);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCellClicked(x, y);
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onCellLongClicked(x, y);
                        return false;
                    }
                });

                for (int z = 0; z < 10; z++) {
                    if (y * 10 + x == setPositions[z]) {
                        isMine = true;
                    }
                }

                grid[x][y] = new Tile(isMine);
            }
        }

        for (int x=0;x<WIDTH;x++) {
            for (int y=0;y<HEIGHT;y++) {
                int mineCount = 0;
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (i >= 0 && i < WIDTH && j >= 0 && j < HEIGHT) {
                            if (!(i == x && j == y)) {
                                if (grid[i][j].isMine) {
                                    grid[x][y].neigboringMines++;
                                }
                            }
                        }
                    }
                }
                grid[x][y].SetDraw();
            }
        }

        refresh();
    }

    public Button getButtonByCoordinate(int x, int y) {
        return (Button) gridLayout.getChildAt(x * HEIGHT + y);
    }

    public int[] generateRandoms() {
        List minePositions = new ArrayList();
        int[] randomPositions = new int[10];

        for (int i = 0; i < 100; i++) {
            minePositions.add(i);
        }

        Collections.shuffle(minePositions);

        for (int i = 0; i < MINES_IN_GAME; i++) {
            randomPositions[i] = (int)minePositions.get(i);
        }

        return randomPositions;
    }

    public void onCellClicked(int x, int y) {
        if ( exposeCell(x, y)) {
            refresh();
        }
    }

    public void onCellLongClicked(int x, int y) {
        flagCell(x, y);
        refresh();
    }

    public void flagCell(int x, int y) {

        if (grid[x][y].isFlagged) {
            foundMines++;
            updateText();
            grid[x][y].isFlagged = false;
            Button button = getButtonByCoordinate(x, y);
            button.setBackgroundResource(R.drawable.button_up);
        }

        else if (!grid[x][y].Explosion) {
            foundMines--;
            updateText();
            grid[x][y].isFlagged = true;
            Button button = getButtonByCoordinate(x, y);
            button.setBackgroundResource(R.drawable.flag);
        }
    }

    public void updateText() {
        mineCount.setText("剩餘地雷 : " + foundMines );
    }

    public boolean exposeCell(int x, int y) {



        if (grid[x][y].Explosion) {
            // DO NOTHING
            return false;
        }

        else if (!grid[x][y].isFlagged){

            grid[x][y].Explosion = true;
           // revealedTiles++;
        }

        if (!grid[x][y].isFlagged && grid[x][y].isMine) {


            Toast.makeText(getApplicationContext(), "YOU LOSE", Toast.LENGTH_SHORT).show();
            GameOver();
            Button button = getButtonByCoordinate(x, y);
            button.setBackgroundResource(R.drawable.wrong_mine);
            return false;
        }



        if (grid[x][y].neigboringMines == 0 ) {
            for (int i = x - 1; i <= x+1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 0 && i < WIDTH && j >= 0 && j < HEIGHT) {
                        if (!(i == x && j == y)&&!grid[x][y].isMine) {
                            //revealedTiles+=1;
                            exposeCell(i, j);
                        }
                    }
                }
            }
        }
        for (int i = 0; i <10; i++) {
            for (int j =0; j <10 ; j++) {

                    if (grid[x][y].Explosion) {
                        revealedTiles+=1;

                    }

            }
        }


        if (revealedTiles/100 == 90 ) {

            Toast.makeText(getApplicationContext(), "YOU WIN", Toast.LENGTH_SHORT).show();
            GameOver();
            return false;
        }

        return true;
    }

    public void refresh() {
        for (int i=0;i<10;i++) {
            for (int j=0;j<10;j++) {

                final int x = i;
                final int y = j;

                if(grid[x][y].Explosion) {

                    Button button = getButtonByCoordinate(x, y);
                    button.setBackgroundResource(grid[x][y].myDraw);
                }
                else if (grid[x][y].isFlagged){
                    Button button = getButtonByCoordinate(x, y);
                    button.setBackgroundResource(R.drawable.flag);
                }
                else {
                    Button button = getButtonByCoordinate(x, y);
                    button.setBackgroundResource(R.drawable.button_up);
                }
            }
        }
    }

    public void GameOver() {
        for (int i=0;i<10;i++) {
            for (int j=0;j<10;j++) {

                final int x = i;
                final int y = j;

                Button button = getButtonByCoordinate(x, y);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });

                if(grid[x][y].isFlagged && !grid[x][y].isMine) {
                    button.setBackgroundResource(R.drawable.wrong_mine);
                }
                else if (grid[x][y].isMine) {
                    button.setBackgroundResource(R.drawable.mine);
                }
            }
        }
    }
}
