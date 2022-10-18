
package jan;

import java.util.Arrays;
import java.util.Scanner;

import static java.awt.geom.Point2D.distance;
import static java.lang.Math.*;

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
        boolean enoughSpace = (int)(distance(p1.i, p1.j,p2.i,p2.j)+1) == Ship.getCurrentShip().getLength();
        return (onSameRow || onSameColumn) && enoughSpace && noCollision(cells, p1, p2);
    }

    private static boolean noCollision(int[][] cells, Position p1, Position p2) {
        if (p1.i == p2.i) {
            for (int index = min(p1.j, p2.j); index <= max(p1.j, p2.j); index++) {
                if (collidesOrTouchesOtherShip(p1.i, index, cells)) return false;
            }
        } else {
                for (int index = min(p1.i, p2.i); index <= max(p1.i, p2.i); index++) {
                    if (collidesOrTouchesOtherShip(index, p1.j, cells)) return false;
                }
            }
            return true;
        }

    private static boolean collidesOrTouchesOtherShip(int row, int column, int[][] cells) {
        boolean up = row == 1 || cells[row -1][column] == '~';
        boolean down = row == 10 || cells[row +1][column] == '~';
        boolean left = column == 1 || cells[row][column-1] == '~';
        boolean right = column == 10 || cells[row][column+1] == '~';
        return !(up && down && left && right);
    }

    private static boolean notOutsideBounds (Position p){
            boolean iCorrect = (p.i >= 1) && (p.i <= 10);
            boolean jCorrect = (p.j >= 1) && (p.j <= 10);

            return iCorrect && jCorrect;
        }

    public static void placeAndMoveToNextShip(int[][] cells, Position p1, Position p2) {
        if (p1.i == p2.i) {
            for (int index = min(p1.j, p2.j); index <= max(p1.j, p2.j); index++) {
                cells[p1.i][index]='O';
            }
        } else {
            for (int index = min(p1.i, p2.i); index <= max(p1.i, p2.i); index++) {
                cells[index][p1.j]='O';
            }
        }
        Ship.incrementIndex();
    }
}


class Ship {

    private static int index = 0;
    private final int length;
    private final String name;

    private Ship(int length, String name) {
        this.length = length;
        this.name = name;
    }

    static Ship[] getShips() {
        return new Ship[]{new Ship(5, "Aircraft Carrier"), new Ship(4, "Battleship"), new Ship(3, "Submarine"), new Ship(3, "Cruiser"), new Ship(2, "Destroyer")};
    }

    static Ship getCurrentShip() {
        return getShips()[index];
    }

    public static int getIndex() {
        return index;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    static void incrementIndex() {
        index += 1;
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
        //clearConsole();
    }

    private void clearConsole() {
        String EMPTY_LINES = "\n";
        System.out.println(EMPTY_LINES);
    }

    private void printOtherRows(int LENGTH, int[][] cells) {
        for (int i = 1; i < LENGTH + 1; i++) {
            for (int j = 0; j < LENGTH + 1; j++) {
                if(j<LENGTH)System.out.print((char) cells[i][j] + " ");
                else System.out.print((char)cells[i][j]);
            }
            System.out.println();
        }
    }

    private void printFirstRow(int LENGTH, int[][] cells) {
        System.out.print((char) cells[0][0] + " ");
        for (int i = 1; i < LENGTH + 1; i++) {
            if(i<LENGTH)System.out.print(cells[0][i] + " ");
            else System.out.print(cells[0][i]);
        }
        System.out.println();
    }
}

class GameBoard {
    private static final GameBoard gameBoard = new GameBoard();
    private static final BoardPrinter printer = new BoardPrinter(gameBoard.LENGTH, gameBoard.cells);
    private final int LENGTH = 10;
    private final int[][] cells = new int[LENGTH + 1][LENGTH + 1];


    private GameBoard() {
        initializeBoard();
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }


    void print() {
        printer.printBoard();
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
            cells[i][0] = 'A' + i - 1;
        }
    }

    private void initializeFirstRow() {
        for (int i = 1; i < LENGTH + 1; i++) {
            cells[0][i] = i;
        }
    }

    public void placeNextShip() {
        Scanner sc = new Scanner(System.in);
        int i1, i2, j1, j2;

        String temp = sc.next();
        i1 = Character.toLowerCase(temp.charAt(0)) - 'a' + 1;
        j1 = Integer.parseInt(temp.substring(1));

        temp = sc.next();
        i2 = Character.toLowerCase(temp.charAt(0)) - 'a' + 1;
        j2 = Integer.parseInt(temp.substring(1));
        Position p1 = new Position(i1, j1);
        Position p2 = new Position(i2, j2);

        if (Position.canPlace(cells, p1, p2)) {
            Position.placeAndMoveToNextShip(cells, p1, p2);
            this.print();
        } else {
            System.out.println("\nError! Unable to place ship");
        }


    }
    public void test() {
        cells[5][6] = 'z';


    }
}

public class Main {
    public static void main(String[] args) {
        GameBoard board = GameBoard.getGameBoard();
        board.print();

        while (Ship.getIndex()<5){
            System.out.println();
            System.out.println(String.format("Enter the coordinates of the %s (%s cells):\n",Ship.getCurrentShip().getName(),Ship.getCurrentShip().getLength()));
            board.placeNextShip();
        }




    }
}




