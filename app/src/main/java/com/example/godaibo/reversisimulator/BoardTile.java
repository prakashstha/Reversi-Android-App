package com.example.godaibo.reversisimulator;

/**
 * Created by godaibo on 4/2/2015.
 */
public class BoardTile {

    public static int[] createDotArray(int dotSize, boolean isFull) {
        int[] dotColors = null;

        if (!isFull)
            dotColors = new int[4];
        else
            dotColors = new int[10];



        return dotColors;
    }


    public static int getDotIdBkg(int dotSize) {

        return 0;
    }

    public static int getDotIdDis(int dotSize) {

        return 0;
    }

    public static int getDotFileCmd(int dotSize) {

        return 0;
    }


    public static int getDotDelCmd(int dotSize) {

        return 0;
    }

}