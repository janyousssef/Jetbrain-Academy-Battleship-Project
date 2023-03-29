package jan;

public class BoardPrinter {


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
        System.out.print((char) cells[0][0] + "  ");
        for (int i = 1; i < LENGTH + 1; i++) {
            if (i < LENGTH) System.out.print(cells[0][i] + "  ");
            else System.out.print(cells[0][i]);
        }
        System.out.println();
    }
}
