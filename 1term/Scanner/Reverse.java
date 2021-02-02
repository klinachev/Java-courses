import java.io.*;
import java.util.Arrays;

public class Reverse {
    public static void main(String[] args) {
        int i = -1, strt, j = 0; 
        int n = 2;
        int[] ints = new int[8];
        int[] razmer = new int[8];
		razmer[j] = -1;
        String str;
        try (Scanner in = new Scanner(System.in);) {
			while (true) {
				while (in.noIntInLine()) {
					j++;
					if (j == razmer.length) {
						razmer = Arrays.copyOf (razmer, (n * razmer.length));
					}
					razmer[j] = -1;
				}
				Integer a = in.nextInt();
				if (a == null) {
					break;
				}
				i++;
				if (i == ints.length) {
					ints = Arrays.copyOf (ints, (n * ints.length));
				}
				razmer[j]++;
				ints[i] = a;
			}
		} catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Input file error: " + e.getMessage());
        }
        strt = i;
        for (int k = j - 1; k >= 0; k--) {
            for (i = 0; i <= razmer[k]; i++) {
                System.out.print((ints[strt]) + " ");
                strt--;
            }
            System.out.println();
        }
    }
}
