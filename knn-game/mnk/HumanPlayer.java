package mnk;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final ProxyPosition position, final Cell cell) {
        start:
        while (true) {
            out.println("Position");
            // out.println(position);
            String a = "none";
            switch (cell) {
                case X:
                    a = "first";
                    break;
                case O:
                    a = "second";
                    break;
                case A:
                    a = "third";
                    break;
                case B:
                    a = "fourth";
                    break;

            }
            out.println(a + " player's  move");
            out.println("Enter row and column");
            int[] m = new int[2];
            Scanner read = new Scanner(in.nextLine());
            for (int i = 0; i < 2; i++) {
                if (read.hasNextInt()) {
                    m[i] = read.nextInt();
                } else {
                    out.println("Input is invalid");
                    continue start;
                }
            }
            final Move move = new Move(m[0], m[1], cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }
}
