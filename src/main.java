import java.lang.reflect.Array;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class main extends PApplet{
    static int solutions = 0;
    static int iterations = 0;
    static int maxIterations = 0;
    static int[][] bestBoard = new int[9][9];
    static int[] seed9 = new int[9];
    static int[] seed81 = new int[81];
    static boolean canRecurse = true;
    static int givenTiles = 30;
    static int windowSize = 1000;
    static int[][] board;

    public void setup(){
        stroke(0);
        fill(0);
    }

    public void settings(){
        size(windowSize,windowSize);
    }

    public void draw(){
        background(255);

        for (int y = 1; y < board.length; y++) {
            if(y%3==0){
                strokeWeight(10);
            }
            line(0, (float) (windowSize/ board.length)*y, windowSize, (float) (windowSize/ board.length)*y);
            strokeWeight(5);
            for (int x = 1; x < board[0].length; x++) {
                if(x%3==0){
                    strokeWeight(10);
                }
                line((float) (windowSize/ board.length)*x, 0 ,(float) (windowSize/ board.length)*x, windowSize);
                strokeWeight(5);
                //System.out.print(getInt(x, y, board)+ " ");
            }
            //System.out.println();
        }
        textSize((float) (windowSize/ board.length));
        for (int y = 1; y < 10; y++) {
            for (int x = 0; x < 9; x++) {
                text(0, (float) (windowSize/ board.length)*x,(float) (windowSize/ board.length)*y);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("main",args);
        Random rand = new Random();
        for (int i = 0; i < seed9.length; i++) {
            seed9[i]=i+1;
        }
        randomize(seed9, rand);
//        System.out.println(Arrays.toString(seed9));

        for (int i = 0; i < seed81.length; i++) {
            seed81[i]=i;
        }
        randomize(seed81, rand);
//        System.out.println(Arrays.toString(seed81));
        board = new int[][]{
//            {5, 0, 0, 4, 0, 0, 1, 0, 0},
//            {3, 0, 0, 0, 0, 5, 0, 0, 7},
//            {0, 9, 0, 0, 0, 3, 5, 0, 0},
//            {2, 0, 0, 7, 0, 0, 0, 0, 0},
//            {0, 0, 4, 0, 0, 8, 0, 0, 0},
//            {6, 0, 0, 0, 0, 0, 0, 0, 9},
//            {0, 0, 6, 0, 0, 0, 4, 0, 0},
//            {0, 0, 1, 0, 0, 9, 2, 0, 0},
//            {4, 0, 0, 0, 5, 0, 0, 8, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
        printBoard(board);
        System.out.println();
        System.out.println();
        Date dateStart = new Date();
        long start = dateStart.getTime();
        System.out.println(solve(board));

        reverseSolve(board, rand);

        System.out.println(81-maxIterations);
//        solveForSolutions(board);
//        System.out.println(solutions);
//        while(solutions>1){
//            int randX = rand.nextInt(9);
//            int randY = rand.nextInt(9);
//            if(getInt(randX, randY, board)==0){
//                continue;
//            }
//            setInt(randX, randY, board, 0);
//            solveForSolutions(board);
//            System.out.println(solutions);
//        }
        printBoard(bestBoard);
        solutions = 0;
        solveForSolutions(bestBoard);
        System.out.println(solutions);
        Date dateEnd = new Date();
        long end = dateEnd.getTime();

        System.out.println(end-start + " Milleseconds");
    }

    public static void solveForSolutions(int[][] board){
        int[] find = findBlank(board);
        if(find==null){
//            printBoard(board);
            solutions++;
            return;
        }
        for (int i = 0; i < 9; i++) {
            if(isValid(board, seed9[i], find)){
                setInt(find, board, seed9[i]);

                solveForSolutions(board);

                setInt(find, board, 0);
            }
        }
    }
    public static boolean solve(int[][] board){
        Random rand = new Random();
        int[] find = findBlank(board);
        if(find==null){
            printBoard(board);
            return true;
        }
        int[] localSeed = new int[seed9.length];
        System.arraycopy(seed9, 0, localSeed, 0, seed9.length);
        randomize(localSeed, rand);
        for (int j : localSeed) {
            if (isValid(board, j, find)) {
                setInt(find, board, j);

                if (solve(board)) {
                    return true;
                }

                setInt(find, board, 0);
            }
        }
        return false;
    }

    public static void reverseSolve(int[][] board, Random rand){
        int[] find = {rand.nextInt(9), rand.nextInt(9)};
        for (int j : seed81) {
            if(canRecurse) {
                int temp = getInt(j / 9, j % 9, board);
                if (temp == 0) {
                    continue;
                }
                setInt(j / 9, j % 9, board, 0);
                iterations++;
                solveForSolutions(board);
                if (solutions > 1) {
                    solutions = 0;
                    if (maxIterations < iterations) {
                        maxIterations = iterations;
                        bestBoard = board;
                        if (maxIterations >= 81-givenTiles) {
                            canRecurse = false;
                        }
                    }
                    setInt(j / 9, j % 9, board, temp);
                    iterations--;
                } else {
                    reverseSolve(board, rand);
                }
            }
        }
    }

    public static int getInt(int x, int y, int[][] board){
        return board[y][x];
    }

    public static int getInt(int[] pos, int[][] board){
        return board[pos[1]][pos[0]];
    }

    public static void setInt(int[] pos, int[][] board, int num){
        board[pos[1]][pos[0]] = num;
    }

    public static void setInt(int x, int y, int[][] board, int num){
        board[y][x] = num;
    }

    public static void randomize(int[] arr, Random rand){
        for (int i = 0; i < arr.length; i++) {
            int randomIndexToSwap = rand.nextInt(arr.length);
            int temp = arr[randomIndexToSwap];
            arr[randomIndexToSwap] = arr[i];
            arr[i] = temp;
        }
    }

    public static boolean isValid(int[][] board, int num, int[] pos){
        for (int y = 0; y < board.length; y++) {
            if(getInt(pos[0], y, board)!=0&&getInt(pos[0], y, board)==num){
                return false;
            }
        }

        for (int x = 0; x < board[0].length; x++) {
            if(getInt(x, pos[1], board)!=0&&getInt(x, pos[1], board)==num){
                return false;
            }
        }

        int sqrX = pos[0]/3;
        int sqrY = pos[1]/3;

        for (int y = sqrY*3; y < sqrY*3+3; y++) {
            for (int x = sqrX*3; x < sqrX*3+3; x++) {
                if(getInt(x, y, board)!=0&&getInt(x, y, board)==num){
                    return false;
                }
            }
        }

        return true;
    }

    public static void printBoard(int[][] board){
        for (int y = 0; y < board.length; y++) {
            if(y!=0&&y%3==0){
                System.out.println("- - - - - - - - - - -");
            }
            for (int x = 0; x < board[0].length; x++) {
                if(x!=0&&x%3==0){
                    System.out.print("| ");
                }
                System.out.print(getInt(x, y, board)+ " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public static int[] findBlank(int[][] board){
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                if(getInt(x, y, board)==0){
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }
}
