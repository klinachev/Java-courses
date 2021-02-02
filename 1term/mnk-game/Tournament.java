package mnk;

public class Tournament {
    private final boolean log;
    private final int num;
    private final Player[] players;
    private final int m, n, k;

    public Tournament(final boolean log, int num, final Player[] players, int cols, int rows, int val) {
        this.k = val;
        this.m = cols;
        this.n = rows;
        this.log = log;
        this.num = num;
        this.players = players;
    }

    public int[] play(Board board, int count) {
        int[] st = new int[num];
        for (int col = 0; col < count; col++) {
            for (int i = 0; i < num; ++i) {
                for (int j = i + 1; j < num; ++j) {
                    Game game = new Game(false, players[i], players[j]);
                    int res = game.play(new MnkBoard(m, n, k, players.length, false));
                    switch (res) {
                        case (0):
                            st[i]++;
                            st[j]++;
                            break;
                        case (1):
                            st[i] += 3;
                            break;
                        case (2):
                            st[j] += 3;
                            break;
                    }
                }
            }
        }
        return  st;
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
