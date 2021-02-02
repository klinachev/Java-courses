import java.io.*;
import java.util.Comparator;
import java.util.Arrays;

class Stroka{
    long sum;
    int pos;
    String line;

    public Stroka(long sum, int pos, String line) {
        this.sum = sum;
        this.pos = pos;
        this.line = line;
    }
}

public class ReverseSort {
    public static void main(String[] args) {
        Stroka[] m = new Stroka[1];
        int msize = -1, sch = -1;
		long sum = 0;
		int[] ints = new int[1];
        try (Scanner reader = new Scanner(System.in);) {
			while (true) {
				while (reader.noIntInLine()) {
					sch++;
					ints = Arrays.copyOf(ints, sch);
					Arrays.sort(ints);
					StringBuilder str = new StringBuilder();
					for (int i = sch - 1; i >= 0; i--) {
						str.append(ints[i]).append(' ');
					}
					str.append('\n');
					msize++;
					if (msize == m.length) {
						m = Arrays.copyOf(m, 2 * m.length);
					}
					m[msize] = new Stroka(sum, msize, str.toString());
					ints = new int[1];
					sch = -1;
					sum = 0;
				}
				Integer a = reader.nextInt();
				if (a == null) {
					break;
				}
				sch++;
				if (sch == ints.length) {
					ints = Arrays.copyOf(ints, 2 * ints.length);
				}
				ints[sch] = a;
				sum += ints[sch];
			}
		} catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Input file error: " + e.getMessage());
        }
        msize++;
        m = Arrays.copyOf(m, msize);
        Arrays.sort(m, new Comparator<>() {
            public int compare(Stroka s1, Stroka s2) {
                if (s1.sum == s2.sum) {
                    return (s2.pos - s1.pos);
                }
                if (s1.sum > s2.sum) {
                    return -1;
                }
                return 1;
            }
        });

        for (int i = 0; i < msize; i++) {
            System.out.print(m[i].line);
        }
    }
}
