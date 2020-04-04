package mnk;

import java.util.Arrays;
import java.util.Map;

public class MnkBoard implements Position, Board{
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.A, '|',
            Cell.B, '-',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private final int m, n, k;
    private long empty;
    private final int countPlayers;
    private Cell turn;
    private final boolean rhombus;

    MnkBoard(int cols, int rows, int val, int countPlayers, boolean rhombus) {
        this.n = rows;
        this.m = cols;
        this.k = val;
        this.countPlayers = countPlayers;
        this.rhombus = rhombus;
        if (rhombus) {
            int dop = 0, n1 = (n) / 2, m1 = (m) / 2, n2 = (n - 1) / 2, m2 = (m - 1) / 2;
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < m; c++) {
                    if (((n2 * m2 - r * m2 - c * n2 <= 0) && ((n2 * m2 - (n - r - 1) * m2 - (m - c - 1) * n2 <= 0))
                            && (n1 * m2 - r * m2 + c * n1 >= 0) && (n2 * m1 + r * m1 - c * n2 >= 0))) {
                        dop++;
                    }
                }
            }
            empty = dop;
            System.out.println(empty);
        } else {
            this.empty = n * m;
        }
        this.cells = new Cell[n][m];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public ProxyPosition getPosition() {
        return new ProxyPosition(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public int rowCount() {
        return n;
    }

    @Override
    public int columnCount() {
        return m;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        empty--;
        int moveRow = move.getRow();
        int moveCol = move.getColumn();
        Cell val = move.getValue();
        cells[moveRow][moveCol] = val;
        int min = moveCol;
        while (min >= 0 && cells[moveRow][min] == val) {
            min--;
        }
        int max = moveCol;
        while (max < m && cells[moveRow][max] == val) {
            max++;
        }
        if (max - min > k) {
            return Result.WIN;
        }
        min = moveRow;
        max = moveRow;
        while (min >= 0 && cells[min][moveCol] == val) {
            min--;
        }
        while (max < n && cells[max][moveCol] == val) {
            max++;
        }
        if (max - min > k) {
            return Result.WIN;
        }
        min = 0;
        max = 0;
        while (moveRow + min >= 0 && moveCol + min >= 0 && cells[moveRow + min][moveCol + min] == val) {
            min--;
        }
        while (moveRow + max < n && moveCol + max < m && cells[moveRow + max][moveCol + max] == val) {
            max++;
        }
        if (max - min > k) {
            return Result.WIN;
        }
        int left = 0, rigth = 0;
        while (moveRow - left < n && moveCol + left >= 0 && cells[moveRow - left][moveCol + left] == val) {
            left--;
        }
        while (moveRow - rigth >= 0 && moveCol + rigth < m && cells[moveRow - rigth][moveCol + rigth] == val) {
            rigth++;
        }
        if (rigth - left > k) {
            return Result.WIN;
        }

        if (empty == 0) {
            return Result.DRAW;
        }

        switch (turn) {
            case X:
                turn = Cell.O;
                break;
            case O:
                if (countPlayers > 2) {
                    turn = Cell.A;
                } else {
                    turn = Cell.X;
                }
                break;
            case A:
                if (countPlayers > 3) {
                    turn = Cell.B;
                } else {
                    turn = Cell.X;
                }
                break;
            case B:
                turn = Cell.X;
                break;
        }
        //turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        boolean f = true;
        if (rhombus) {
            int a = move.getRow(), b = move.getColumn();
            int n1 = (n) / 2, m1 = (m) / 2, n2 = (n - 1) / 2, m2 = (m - 1) / 2;
            f = ((n2 * m2 - a * m2 - b * n2 <= 0) && ((n2 * m2 - (n - a - 1) * m2 - (m - b - 1) * n2 <= 0))
                    && (n1 * m2 - a * m2 + b * n1 >= 0) && (n2 * m1 + a * m1 - b * n2 >= 0));
        }
        return f && 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getColumn() && move.getColumn() < m
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {

        int length = (String.valueOf(Integer.max(m, n)) + 1).length();
        final String Digit = "%" + length + "d";
        final String Char = "%" + length + "c";
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format(Char, ' '));
        for (int c = 0; c < m; c++) {
            sb.append(String.format(Digit, c));
        }
        int n1 = (n) / 2, m1 = (m) / 2, n2 = (n - 1) / 2, m2 = (m - 1) / 2;
        for (int r = 0; r < n; r++) {
            sb.append("\n");
            sb.append(String.format(Digit, r));
            for (int c = 0; c < m; c++) {
                if (!rhombus || ((n2 * m2 - r * m2 - c * n2 <= 0) && ((n2 * m2 - (n - r - 1) * m2 - (m - c - 1) * n2 <= 0))
                        && (n1 * m2 - r * m2 + c * n1 >= 0) && (n2 * m1 + r * m1 - c * n2 >= 0))) {
                    sb.append(String.format(Char, SYMBOLS.get(cells[r][c])));
                } else {
                    sb.append(String.format(Char, ' '));
                }
            }
        }
        return sb.toString();
    }
}
