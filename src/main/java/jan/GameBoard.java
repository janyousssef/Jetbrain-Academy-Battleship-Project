package jan;

import java.util.Arrays;
import java.util.Scanner;

public class GameBoard {
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
            Arrays.fill(cellsWithFog[i], Globals.EMPTY_CELL);
            Arrays.fill(emptyCells[i], Globals.EMPTY_CELL);
        }
    }

    private void initializeFirstColumn() {

        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[i][0] = 'A' + i - 1;
            cellsWithFog[i][0] = 'A' + i - 1;
            emptyCells[i][0] = 'A' + i - 1;
        }

    }

    private void initializeFirstRow() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cellsNoFog[0][i] = i;
            cellsWithFog[0][i] = i;
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
