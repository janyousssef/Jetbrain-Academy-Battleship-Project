package jan;


public class Main {

    public static void main(String[] args) {
        GameBoard board1 = new GameBoard();
        System.out.println("Player 1, place your ships on the game field");
        GameBoard.print(board1.getCellsNoFog());
        while (Ship.getIndex() < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%s cells):\n%n", Ship.getCurrentShip().getName(), Ship.getCurrentShip().getLength());
            board1.placeNextShip();
        }

        GameBoard board2 = new GameBoard();
        Utils.promptEnterKey();
        System.out.println("Player 2, place your ships to the game field\n");
        GameBoard.print(board2.getCellsNoFog());
        System.out.println();
        Ship.resetIndex();
        while (Ship.getIndex() < 5) {
            System.out.printf("\nEnter the coordinates of the %s (%s cells):\n%n", Ship.getCurrentShip().getName(), Ship.getCurrentShip().getLength());
            board2.placeNextShip();
        }

        Utils.promptEnterKey();

        while (board1.numShipsSunk() < 5 && board2.numShipsSunk() < 5) {
            GameBoard.print(board2.getEmptyCells());
            System.out.println("---------------------");
            GameBoard.print(board1.getCellsNoFog());
            System.out.println("Player 1, it's your turn:");
            board1.shoot(board2.getCellsNoFog());
            if (board1.numShipsSunk() == 5 || board2.numShipsSunk() == 5) break;
            Utils.promptEnterKey();

            GameBoard.print(board1.getEmptyCells());
            System.out.println("---------------------");
            GameBoard.print(board2.getCellsNoFog());
            System.out.println("Player 2, it's your turn:");
            board2.shoot(board1.getCellsNoFog());
            if (board1.numShipsSunk() == 5 || board2.numShipsSunk() == 5) break;
            Utils.promptEnterKey();


        }
        System.out.println("You sank the last ship. You won. Congratulations!");

    }
}




