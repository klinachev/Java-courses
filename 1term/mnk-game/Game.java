package mnk;

public class Game {
    private final boolean log;
    private final Player[] players;
    private final int kol;

    Game(final boolean log, final Player player1, final Player player2) {
        this.log = log;
        kol = 2;
        this.players = new Player[2];
        players[0] = player1;
        players[1] = player2;

    }

    Game(final boolean log, final Player player1, final Player player2, final Player player3) {
        this.log = log;
        kol = 3;
        this.players = new Player[3];
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
    }

    Game(final boolean log, final Player player1, final Player player2, final Player player3, final Player player4) {
        this.log = log;
        kol = 4;
        this.players = new Player[4];
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;
    }

    int play(Board board) {
        while (true) {
            for (int i = 0; i < players.length; ++i) {
                final int result1 = move(board, players[i], i + 1);
                if (result1 != 5) {
                    return result1;
                }
            }
        }
    }

    private int move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == Result.WIN) {
            log("Player " + no + " won");
            return no;
        } else if (result == Result.LOSE) {
            log("Player " + no + " lose");
            return -no;
        } else if (result == Result.DRAW) {
            log("Draw");
            return 0;
        }
        return 5;
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
