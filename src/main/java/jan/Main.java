package jan;

import java.util.Arrays;

class GameBoard {
    public static final String EMPTY_LINES = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    private static final int LENGTH = 10;
    private static final int[][] cells = new int[LENGTH + 1][LENGTH + 1];
    static GameBoard gameBoard = new GameBoard();

    private GameBoard() {
        initializeBoard();
    }

    static void printBoard() {
        clearConsole();
        printFirstRow();
        printOtherRows();
    }

    private static void clearConsole() {
        System.out.println(EMPTY_LINES);
    }

    private static void printOtherRows() {
        for (int i = 1; i < LENGTH + 1; i++) {
            for (int j = 0; j < LENGTH + 1; j++) {
                System.out.print((char) cells[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printFirstRow() {
        System.out.print((char) cells[0][0] + " ");
        for (int i = 1; i < LENGTH + 1; i++) {
            System.out.print(cells[0][i] + " ");
        }
        System.out.println();
    }

    private void initializeBoard() {
        initializeCells();
        initialize00Corner();
        initializeFirstColumn();
        initializeFirstRow();
    }

    private void initialize00Corner() {
        cells[0][0] = ' ';
    }

    private void initializeCells() {
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(cells[i], '~');
        }
    }

    private void initializeFirstColumn() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cells[i][0] = 'a' + i - 1;
        }
    }

    private void initializeFirstRow() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cells[0][i] = i;
        }
    }

}

public class Main {
    public static void main(String[] args) {
        GameBoard.printBoard();


    }
}




