package jan;

import java.util.Arrays;

class BoardPrinter {

    private static final GameBoard gameBoard = GameBoard.getGameBoard();

    public BoardPrinter() {
    }

    void printBoard() {
        clearConsole();
        printFirstRow(gameBoard.getLENGTH(), gameBoard.getCells());
        printOtherRows(gameBoard.getLENGTH(), gameBoard.getCells());
    }

    private void clearConsole() {
        String EMPTY_LINES = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
        System.out.println(EMPTY_LINES);
    }

    private void printOtherRows(int LENGTH, int[][] cells) {
        for (int i = 1; i < LENGTH + 1; i++) {
            for (int j = 0; j < LENGTH + 1; j++) {
                System.out.print((char) cells[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void printFirstRow(int LENGTH, int[][] cells) {
        System.out.print((char) cells[0][0] + " ");
        for (int i = 1; i < LENGTH + 1; i++) {
            System.out.print(cells[0][i] + " ");
        }
        System.out.println();
    }
}

class GameBoard {
    private static final GameBoard gameBoard = new GameBoard();
    private static final BoardPrinter printer = new BoardPrinter();
    private final int LENGTH = 10;
    private final int[][] cells = new int[LENGTH + 1][LENGTH + 1];

    private GameBoard() {
        initializeBoard();
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    public int getLENGTH() {
        return LENGTH;
    }

    public int[][] getCells() {
        return cells;
    }

    public BoardPrinter getPrinter() {
        return printer;
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
        GameBoard board = GameBoard.getGameBoard();
        BoardPrinter printer = board.getPrinter();
        printer.printBoard();

    }
}




