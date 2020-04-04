package mnk;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int m = 0, n = 0, k = 0;

        Scanner inp = new Scanner(System.in);
        boolean f = true;
        int[] mas = new int[3];
        while (f) {
            System.out.println("Enter n, m, k: ");
            f = false;
            Scanner read = new Scanner(inp.nextLine());
            for (int i = 0; i < 3; i++) {
                if (read.hasNextInt()) {
                    mas[i] = read.nextInt();
                    if (mas[i] <= 0) {
                        f = true;
                    }
                } else {
                    f = true;
                }
            }
            if (f) {
                System.out.println("Input is invalid");
            }
        }
        m = mas[0];
        n = mas[1];
        k = mas[2];
        final Game game = new Game(true, new RandomPlayer(m, n), new SequentialPlayer(m, n), new RandomPlayer(m, n), new HumanPlayer());
        int result;
        result = game.play(new MnkBoard(m, n, k, 4, true));
        System.out.println("Game result: " + result);
    }
}
