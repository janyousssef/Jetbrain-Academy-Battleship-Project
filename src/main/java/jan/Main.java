package jan;

import java.util.Arrays;
import java.util.Scanner;

import static java.awt.geom.Point2D.distance;
import static java.lang.Math.max;
import static java.lang.Math.min;

class Globals {
    static final char EMPTY_CELL = '~';
    static final char SHIP_CELL = 'O';
    static final char MISSED_CELL = 'M';
    static final char HIT_SHIP_CELL = 'X';
}

class Position {
    final int i;
    final int j;

    public Position(int i, int j) {

        this.i = i;
        this.j = j;
    }

    static boolean canPlace(int[][] cells, Position p1, Position p2) {

        return notOutsideBounds(p1) && notOutsideBounds(p2) && validCoordinates(cells, p1, p2);
    }

    private static boolean validCoordinates(int[][] cells, Position p1, Position p2) {
        boolean onSameRow = p1.i == p2.i;
        boolean onSameColumn = p1.j == p2.j;
        boolean enoughSpace = (int) (distance(p1.i, p1.j, p2.i, p2.j) + 1) == Ship.getCurrentShip().getLength();

        return (onSameRow || onSameColumn) && enoughSpace && noCollision(cells, p1, p2);
    }

    private static boolean noCollision(int[][] cells, Position p1, Position p2) {
        if (p1.i == p2.i) return noHorizontalCollision(cells, p1, p2);
        return noVerticalCollision(cells, p1, p2);
    }

    private static boolean noVerticalCollision(int[][] cells, Position p1, Position p2) {
        for (int index = min(p1.i, p2.i); index <= max(p1.i, p2.i); index++) {
            if (collidesWithOrTouchesOtherShips(index, p1.j, cells)) return false;
        }
        return true;
    }

    private static boolean noHorizontalCollision(int[][] cells, Position p1, Position p2) {
        for (int index = min(p1.j, p2.j); index <= max(p1.j, p2.j); index++) {
            if (collidesWithOrTouchesOtherShips(p1.i, index, cells)) return false;
        }
        return true;
    }

    private static boolean collidesWithOrTouchesOtherShips(int row, int column, int[][] cells) {
        boolean up = row == 1 || cells[row - 1][column] == Globals.EMPTY_CELL;
        boolean down = row == 10 || cells[row + 1][column] == Globals.EMPTY_CELL;
        boolean left = column == 1 || cells[row][column - 1] == Globals.EMPTY_CELL;
        boolean right = column == 10 || cells[row][column + 1] == Globals.EMPTY_CELL;

        return !(up && down && left && right);
    }

    static boolean notOutsideBounds(Position p) {
        boolean iCorrect = (p.i >= 1) && (p.i <= 10);
        boolean jCorrect = (p.j >= 1) && (p.j <= 10);

        return iCorrect && jCorrect;
    }

    public static void placeAndMoveToNextShip(int[][] cells, Position p1, Position p2) {
        if (p1.i == p2.i) {
            placeHorizontally(cells, p1, p2);
        } else {
            placeVertically(cells, p1, p2);
        }

    }


    private static void placeVertically(int[][] cells, Position p1, Position p2) {
        for (int index = min(p1.i, p2.i); index <= max(p1.i, p2.i); index++) {
            cells[index][p1.j] = Globals.SHIP_CELL;
        }
    }

    private static void placeHorizontally(int[][] cells, Position p1, Position p2) {
        for (int index = min(p1.j, p2.j); index <= max(p1.j, p2.j); index++) {
            cells[p1.i][index] = Globals.SHIP_CELL;
        }
    }
}


class Ship {

    private static final Ship[] ships = {new Ship(5, "Aircraft Carrier"), new Ship(4, "Battleship"), new Ship(3, "Submarine"), new Ship(3, "Cruiser"), new Ship(2, "Destroyer")};
    private static int index = 0;
    private final int length;
    private final String name;

    private Ship(int length, String name) {
        this.length = length;
        this.name = name;
    }

    static Ship[] getShips() {
        return ships;
    }

    static Ship getCurrentShip() {
        return getShips()[index];
    }

    public static int getIndex() {
        return index;
    }

    static void incrementIndex() {
        index += 1;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

}

class BoardPrinter {


    private final int LENGTH;
    private final int[][] cells;

    BoardPrinter(int LENGTH, int[][] cells) {
        this.LENGTH = LENGTH;
        this.cells = cells;
    }

    void printBoard() {
        printFirstRow(LENGTH, cells);
        printOtherRows(LENGTH, cells);
    }

    private void printOtherRows(int LENGTH, int[][] cells) {
        for (int i = 1; i < LENGTH + 1; i++) {
            for (int j = 0; j < LENGTH + 1; j++) {
                if (j < LENGTH) System.out.print((char) cells[i][j] + " ");
                else System.out.print((char) cells[i][j]);
            }
            System.out.println();
        }
    }

    private void printFirstRow(int LENGTH, int[][] cells) {
        System.out.print((char) cells[0][0] + " ");
        for (int i = 1; i < LENGTH + 1; i++) {
            if (i < LENGTH) System.out.print(cells[0][i] + " ");
            else System.out.print(cells[0][i]);
        }
        System.out.println();
    }
}

class GameBoard {
    private static final Scanner sc = new Scanner(System.in);
    private static final GameBoard gameBoard = new GameBoard();
    private static final BoardPrinter noFogPrinter = new BoardPrinter(gameBoard.LENGTH, gameBoard.cellsNoFog);
    private static final BoardPrinter fogPrinter = new BoardPrinter(gameBoard.LENGTH, gameBoard.cellsWithFog);
    private final int LENGTH = 10;
    private final int[][] cellsNoFog = new int[LENGTH + 1][LENGTH + 1];
    private final int[][] cellsWithFog = new int[LENGTH + 1][LENGTH + 1];


    private GameBoard() {
        initializeBoard();
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    private static Position getPositionFromUser() {
        int i, j;
        String temp = sc.next();
        i = Character.toLowerCase(temp.charAt(0)) - 'a' + 1;
        j = Integer.parseInt(temp.substring(1));
        return new Position(i, j);

    }

    void printNoFog() {
        noFogPrinter.printBoard();
    }

    void printWithFog() {
        fogPrinter.printBoard();
    }

    private void initializeBoard() {
        initializeCells();
        initialize00Corner();
        initializeFirstColumn();
        initializeFirstRow();
    }

    private void initialize00Corner() {
        cellsNoFog[0][0] = ' ';
        cellsWithFog[0][0] = ' ';
    }

    private void initializeCells() {
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(cellsNoFog[i], Globals.EMPTY_CELL);
        }
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(cellsWithFog[i], Globals.EMPTY_CELL);
        }
    }

    private void initializeFirstColumn() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[i][0] = 'A' + i - 1;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsWithFog[i][0] = 'A' + i - 1;
        }
    }

    private void initializeFirstRow() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[0][i] = i;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsWithFog[0][i] = i;
        }
    }

    public void placeNextShip() {

        Position p1 = getPositionFromUser();

        Position p2 = getPositionFromUser();

        if (Position.canPlace(cellsNoFog, p1, p2)) {
            Position.placeAndMoveToNextShip(cellsNoFog, p1, p2);
            //Position.placeAndMoveToNextShip(cellsWithFog, p1, p2);
            this.printNoFog();
            Ship.incrementIndex();
        } else {
            System.out.println("\nError! Unable to place ship");
        }


    }

    public void shoot() {
        System.out.println("Take a shot!");
        Position p = getPositionFromUser();
        if (!Position.notOutsideBounds(p)) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return;
        }

        if (hitShip(p)) {
            cellsNoFog[p.i][p.j] = Globals.HIT_SHIP_CELL;
            cellsWithFog[p.i][p.j] = Globals.HIT_SHIP_CELL;

            System.out.println("You hit a ship!");
            printNoFog();
        } else {
            cellsNoFog[p.i][p.j] = Globals.MISSED_CELL;
            cellsWithFog[p.i][p.j] = Globals.MISSED_CELL;

            System.out.println("You missed!");
            printNoFog();
        }

    }

    private boolean hitShip(Position p) {
        return cellsNoFog[p.i][p.j] == Globals.SHIP_CELL;
    }
//    public void test() {
//        cellsNoFog[5][6] = 'z';
//
//
//    }
}

public class Main {
    public static void main(String[] args) {
        GameBoard board = GameBoard.getGameBoard();
        board.printNoFog();
        while (Ship.getIndex() < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%s cells):\n%n", Ship.getCurrentShip().getName(), Ship.getCurrentShip().getLength());
            board.placeNextShip();
        }

        System.out.println("The game starts!");
        board.printWithFog();

        while (true) {
            board.shoot();

        }

    }
}




