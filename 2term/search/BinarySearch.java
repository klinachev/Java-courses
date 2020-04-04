package search;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    // Pre: 0 <= i < a.size - 1 --> a[i] >= a[i + 1]
    // Post: R: a[R - 1] > x ^ !(a[R] > x)
    public static int searchRec(List<Integer> a, int x) {
        // -1 < a.size ^ Pre
        return recSearch(a, -1, a.size(), x);
    }

    // Pre: -1 <= l < r <= a.size - 1 ^ (l < i < r --> a[i] >= a[i + 1])
    // Post: R: a[R - 1] > x ^ !(a[R] > x)
    private static int recSearch(List<Integer> a, int l, int r, int x) {
        if (l + 1 == r) {
            // a[r] <= x ^ a[r - 1] > x
            // --> Post
            return r;
        }
        int m = (l + r) / 2;
        // m = (l + r) / 2
        // a[r] <= x ^ a[l] > x
        if (a.get(m) <= x) {
            // a[m] <= x
            r = m;
            //a[r] <= x ^ l < r < r' --> a[a.size - 1] <= x
        } else {
            // a[m] > x
            l = m;
            // a[l] > x ^ r > l > l'
        }
        // a[r] <= x ^ !(a[l] <= x) ^ (r <= r' ^ l >= l') ^ (l < r)
        // --> Pre
        return recSearch(a, l, r, x);
    }

    // Pre: 0 <= i < a.size - 1 --> a[i] >= a[i + 1]
    // Post: R: a[R - 1] > x ^ !(a[R] > x)
    public static int search(List<Integer> a, int x) {
        int l = -1, r = a.size();
        // r = a.size ^ l == -1
        // I: !(a[r] > x) ^ !(a[l] <= x)
        while (l + 1 != r) {
            int m = (l + r) / 2;
            // m = (l + r) / 2
            //a[r] <= x ^ a[l] > x
            if (a.get(m) <= x) {
                // a[m] <= x
                r = m;
                //a[r] <= x ^ r < r'
            } else {
                // a[m] > x
                l = m;
                // a[l] > x ^ l > l'
            }
            // I
        }
        // I ^ l + 1 == r --> a[r - 1] > x
        // --> Post
        return r;
    }

    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        int x = Integer.parseInt(args[0]);
        for (int i = 1; i < args.length; i++) {
            a.add(Integer.parseInt(args[i]));
        }
        System.out.println(searchRec(a, x));
        //System.out.println(search2(a, x));
    }
}
