package com.example.chouyutain.project;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 1630155 on 2017-06-07.
 */

public class Tile implements /*Parcelable,*/ Serializable{
    public boolean Explosion;
    public boolean isMine;
    public boolean isFlagged;
    public int myDraw;
    public int neigboringMines;

    public Tile() {
        Explosion = false;
        isMine = false;
        isFlagged = false;
        neigboringMines = 0;
        myDraw = R.drawable.button_down;
    }

    public Tile(boolean mine){
        isMine = mine;
        Explosion = false;
        isFlagged = false;
        neigboringMines = 0;
    }

    public void SetDraw() {
        switch (neigboringMines) {
            case (1):
                myDraw = R.drawable.one;
                break;
            case (2):
                myDraw = R.drawable.two;
                break;
            case (3):
                myDraw = R.drawable.three;
                break;
            case (4):
                myDraw = R.drawable.four;
                break;
            case (5):
                myDraw = R.drawable.five;
                break;
            case (6):
                myDraw = R.drawable.six;
                break;
            case (7):
                myDraw = R.drawable.seven;
                break;
            case (8):
                myDraw = R.drawable.eight;
                break;
            default:
                myDraw = R.drawable.button_down;
        }

        if (isMine) {
            myDraw = R.drawable.mine;
        }
    }
}
