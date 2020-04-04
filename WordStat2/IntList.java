
import java.util.Arrays;

public class IntList {
    private int size, lastLine, sum;
    private int[] arr;


    public IntList(int value, int line) {
		arr = new int[8];
        size = 0;
        sum = 0;
        lastLine = -1;
        addLast(value, line);
    }

    public void add(int value) {
		sum++;
		if (size == arr.length) {
            arr = Arrays.copyOf(arr, 3 * arr.length / 2);
        }
        arr[size++] = value;
    }
    
    public void addLast(int value, int line) {
		if (line == lastLine) {
			arr[size - 1] = value;
			sum++;
			return;
		}
		lastLine = line;
		add(value);
    }
    

    public int getSize() {
        return size;
    }
    
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(sum);
        for (int i = 0; i < size; i++) {
			str.append(' ').append(arr[i]);
		}
        str.append(System.getProperty("line.separator"));
        return str.toString();
    }
}
