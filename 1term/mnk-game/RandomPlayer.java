package mnk;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random;
    private final int m,n;

    public RandomPlayer(final Random random, int cols, int rows) {
        this.random = random;
        this.n = rows;
        this.m = cols;
    }

    public RandomPlayer(int cols, int rows) {
        this(new Random(), cols, rows);
    }

    public RandomPlayer() {
        this(new Random(), 3, 3);
    }

    @Override
    public Move move(final ProxyPosition position, final Cell cell) {
        while (true) {
            int r = random.nextInt(n);
            int c = random.nextInt(m);
            final Move move = new Move(r, c, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }
}
