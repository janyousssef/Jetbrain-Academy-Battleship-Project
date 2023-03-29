package jan;

import static java.awt.geom.Point2D.distance;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Position {
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
