package jan;


import java.io.IOException;
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

    public static boolean sunkAShip(Position p, int[][] cells) {
        for (int i = 0; (i + p.i) >= 0 && (i + p.i) <= 10 && !(cells[i + p.i][p.j] == Globals.EMPTY_CELL || cells[i + p.i][p.j] == Globals.MISSED_CELL); i++) {
            if (cells[i + p.i][p.j] == Globals.SHIP_CELL) return false;
        }
        for (int i = 0; (i + p.i) >= 0 && (i + p.i) <= 10 && !(cells[i + p.i][p.j] == Globals.EMPTY_CELL || cells[i + p.i][p.j] == Globals.MISSED_CELL); i--) {
            if (cells[i + p.i][p.j] == Globals.SHIP_CELL) return false;
        }
        for (int i = 0; (i + p.j) >= 0 && (i + p.j) <= 10 && !(cells[p.i][i + p.j] == Globals.EMPTY_CELL || cells[p.i][i + p.j] == Globals.MISSED_CELL); i++) {
            if (cells[p.i][i + p.j] == Globals.SHIP_CELL) return false;
        }
        for (int i = 0; (i + p.j) >= 0 && (i + p.j) <= 10 && !(cells[p.i][i + p.j] == Globals.EMPTY_CELL || cells[p.i][i + p.j] == Globals.MISSED_CELL); i--) {
            if (cells[p.i][i + p.j] == Globals.SHIP_CELL) return false;
        }

        return true;
    }

    public static void resetIndex() {
        index = 0;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

}

class BoardPrinter {


    BoardPrinter() {

    }

    void printBoard(int[][] cells) {
        printFirstRow(cells.length - 1, cells);
        printOtherRows(cells.length - 1, cells);
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
    private static final BoardPrinter printer = new BoardPrinter();
    private final int LENGTH = 10;
    private final int[][] cellsNoFog = new int[LENGTH + 1][LENGTH + 1];
    private final int[][] emptyCells = new int[LENGTH + 1][LENGTH + 1];
    private final int[][] cellsWithFog = new int[LENGTH + 1][LENGTH + 1];

    private int numShipsSunk = 0;


    public GameBoard() {
        initializeBoard();
    }

    private static Position getPositionFromUser() {
        int i, j;
        String temp = sc.next();
        i = Character.toLowerCase(temp.charAt(0)) - 'a' + 1;
        j = Integer.parseInt(temp.substring(1));
        return new Position(i, j);

    }

    static void print(int[][] cells) {
        printer.printBoard(cells);
    }

    public int[][] getCellsNoFog() {
        return cellsNoFog;
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
        emptyCells[0][0] = ' ';

    }

    private void initializeCells() {
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(cellsNoFog[i], Globals.EMPTY_CELL);
        }
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(cellsWithFog[i], Globals.EMPTY_CELL);
        }
        for (int i = 0; i < LENGTH + 1; i++) {
            Arrays.fill(emptyCells[i], Globals.EMPTY_CELL);
        }
    }

    private void initializeFirstColumn() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[i][0] = 'A' + i - 1;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsWithFog[i][0] = 'A' + i - 1;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            emptyCells[i][0] = 'A' + i - 1;
        }
    }

    private void initializeFirstRow() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[0][i] = i;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsWithFog[0][i] = i;
        }
        for (int i = 1; i < LENGTH + 1; i++) {
            emptyCells[0][i] = i;
        }
    }

    public void placeNextShip() {

        Position p1 = getPositionFromUser();

        Position p2 = getPositionFromUser();

        if (Position.canPlace(cellsNoFog, p1, p2)) {
            Position.placeAndMoveToNextShip(cellsNoFog, p1, p2);
            print(cellsNoFog);
            Ship.incrementIndex();
        } else {
            System.out.println("\nError! Unable to place ship");
        }


    }

    public void shoot(int[][] cells) {
        Position p = getPositionFromUser();
        if (!Position.notOutsideBounds(p)) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return;
        }

        if (hitShip(p, cells).equals("new hit") || hitShip(p, cells).equals("re-hit")) {
            boolean newHit = hitShip(p, cells).equals("new hit");
            cellsWithFog[p.i][p.j] = Globals.HIT_SHIP_CELL;
            cells[p.i][p.j] = Globals.HIT_SHIP_CELL;
            print(cellsWithFog);

            if (newHit && Ship.sunkAShip(p, cells)) {
                numShipsSunk++;
                System.out.println("You sank a ship! Specify a new target:");
            } else {
                System.out.println("You hit a ship!");
            }

        } else {
            cellsNoFog[p.i][p.j] = Globals.MISSED_CELL;
            cellsWithFog[p.i][p.j] = Globals.MISSED_CELL;
            cells[p.i][p.j] = Globals.MISSED_CELL;
            print(cellsWithFog);
            System.out.println("You missed!");

        }
    }

    private String hitShip(Position p, int[][] cells) {
        if (cells[p.i][p.j] == Globals.SHIP_CELL) return "new hit";
        if (cells[p.i][p.j] == Globals.HIT_SHIP_CELL) return "re-hit";
        return "no hit";

    }

    public int numShipsSunk() {
        return numShipsSunk;
    }

    public int[][] getEmptyCells() {
        return emptyCells;
    }
}

public class Main {
    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameBoard board1 = new GameBoard();
        System.out.println("Player 1, place your ships on the game field");
        GameBoard.print(board1.getCellsNoFog());
        while (Ship.getIndex() < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%s cells):\n%n", Ship.getCurrentShip().getName(), Ship.getCurrentShip().getLength());
            board1.placeNextShip();
        }

        GameBoard board2 = new GameBoard();
        promptEnterKey();
        System.out.println("Player 2, place your ships to the game field\n");
        GameBoard.print(board2.getCellsNoFog());
        System.out.println();
        Ship.resetIndex();
        while (Ship.getIndex() < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%s cells):\n%n", Ship.getCurrentShip().getName(), Ship.getCurrentShip().getLength());
            board2.placeNextShip();
        }

        promptEnterKey();

        while (board1.numShipsSunk() < 5 && board2.numShipsSunk() < 5) {
            GameBoard.print(board2.getEmptyCells());
            System.out.println("---------------------");
            GameBoard.print(board1.getCellsNoFog());
            System.out.println("Player 1, it's your turn:");
            board1.shoot(board2.getCellsNoFog());
            if (board1.numShipsSunk() == 5 || board2.numShipsSunk() == 5) break;
            promptEnterKey();

            GameBoard.print(board1.getEmptyCells());
            System.out.println("---------------------");
            GameBoard.print(board2.getCellsNoFog());
            System.out.println("Player 2, it's your turn:");
            board2.shoot(board1.getCellsNoFog());
            if (board1.numShipsSunk() == 5 || board2.numShipsSunk() == 5) break;
            promptEnterKey();


        }
        System.out.println("You sank the last ship. You won. Congratulations!");
    }
}




