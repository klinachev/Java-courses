package mnk;

public class ProxyPosition implements Position {
    private Position position;

    ProxyPosition(Position board) {
        this.position = board;
    }

    public boolean isValid(Move move) {
        return position.isValid(move);
    }

    public Cell getCell(int r, int c) {
        return position.getCell(r, c);
    }

    public int rowCount() {
        return position.rowCount();
    }

    public int columnCount() {
        return position.columnCount();
    }
}
