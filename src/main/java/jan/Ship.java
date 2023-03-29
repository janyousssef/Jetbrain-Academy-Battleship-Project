package jan;

public class Ship {

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
