package mnk;

public class SequentialPlayer implements Player {
    private final int m,n;

    public SequentialPlayer(int cols, int rows) {
        this.n = rows;
        this.m = cols;
    }

    public SequentialPlayer() {
        this.m = 3;
        this.n = 3;
    }

    @Override
    public Move move(final ProxyPosition position, final Cell cell) {
  //      Board board = (Board) position;
//        board.makeMove();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}
