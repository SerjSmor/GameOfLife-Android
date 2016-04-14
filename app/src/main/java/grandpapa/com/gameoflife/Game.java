package grandpapa.com.gameoflife;

import android.util.Log;

import java.util.Random;

/**
 * Created by User on 03/03/2016.
 */
public class Game {
    private static final String TAG = Game.class.getSimpleName();
    public boolean[][] grid;/////////////////////////DELETE
    public boolean [][] gridNew;/////////////////////////DELETE
    int width;
    int height;
    int totalCells = 0;
    public Game(int x, int y){
        grid = new boolean[x][y];
        gridNew = new boolean[x][y];
        width = x;
        height = y;
    }


    public  void init(){
        for(int i = 0; i < width; i++ ){
            for(int j = 0; j < height; j++ ){
                grid[i][j] = false;
                gridNew[i][j] = false;
                Random random = new Random();
                int x = random.nextInt(5);
                if(x == 0){
                    //grid[i][j] = true;
                    //totalCells++;
                }
            }
        }
    }

    public void setCell(int i, int j, boolean isAlive ){
        grid[i][j] = isAlive;
    }
    public int numberOfCellsAlive(int i,int j){
        int cellNum = 0;
        int iPlus = i + 1 % width;
        int iMinus = i - 1 % width;
        int jPlus = j + 1 % height;
        int jMinus = j - 1 % height;
        if(iMinus < 0){
            iMinus += width;
        }
        if(jMinus < 0){
            jMinus += height;
        }
        if(iPlus == width){
            iPlus = 0;
        }
        if(jPlus == height){
            jPlus = 0;
        }
        try {

            //N
            if (grid[i][jMinus] == true) {
                cellNum++;
            }
            if (grid[i][jPlus] == true) {
                cellNum++;
            }
            if (grid[iMinus][j] == true) {
                cellNum++;
            }
            if (grid[iMinus][jMinus] == true) {
                cellNum++;
            }
            if (grid[iMinus][jPlus] == true) {
                cellNum++;
            }
            if (grid[iPlus][jMinus] == true) {
                cellNum++;
            }
            if (grid[iPlus][j] == true) {
                cellNum++;
            }
            if (grid[iPlus][jPlus] == true) {
                cellNum++;
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Log.d(TAG, "i:" + i + "j:" + j);

        }
        return cellNum;
        };
    public int totalCellsAlive(){
        return totalCells;
    }

    public boolean isAlive(int x, int y){
        return grid[x][y];
    }

    public void copyGrid(){
        for(int i = 0; i < width; i++ ){
            for(int j = 0; j < height; j++ ) {
                grid[i][j] = gridNew[i][j];
            }
        }

    }

    public void step(){
        totalCells = 0;
        for(int i = 0; i < width; i++ ){
            for(int j = 0; j < height; j++ ){
                int numOfCellsNotDead = numberOfCellsAlive(i, j);
                gridNew[i][j] = grid[i][j];
                switch (numOfCellsNotDead){
                    case 0:
                        gridNew[i][j] = false;
                        break;
                    case 1:
                        gridNew[i][j] = false;
                        break;
                    case 2:
                        gridNew[i][j] = true;
                        break;
                    case 3:
                        gridNew[i][j] = false;
                        break;
                    case 4:
                        gridNew[i][j] = true;
                        break;
                    default:
                        gridNew[i][j] = false;
                        break;

                }
                if(gridNew[i][j] == true){
                    totalCells++;
                }

            }

        }
        copyGrid();
//       Log.d(TAG, String.valueOf(10 % -7));
//        Log.d(TAG, String.valueOf(10 % -1));
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
