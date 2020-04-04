package search;

import java.util.ArrayList;
import java.util.List;

import static search.BinarySearch.searchRec;

public class BinarySearchSpan {
    // Pre: 0 <= i < args.size - 2 --> a[i] >= a[i + 1]
    // Post: q, b: ∃c < args.size - 1: args[c] = args[args.size] --> q = min(c), b = max(c) - min(c),
    //             ∄c < args.size - 1: args[c] = args[args.size] --> b = 0,
    //                      q: args[q] > args[args.size - 1] ^ !(args[q + 1] > args[args.size - 1]).
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        int x = Integer.parseInt(args[0]);
        for (int i = 1; i < args.length; i++) {
            a.add(Integer.parseInt(args[i]));
        }
        // 0 <= i < a.size - 1 --> a[i] >= a[i + 1]
        int it1 = searchRec(a, x);
        // a[it1 - 1] > x ^ !(a[it1] > x) --> q = it1
        if (a.size() > it1 && a.get(it1) == x) {
            // ∃c < args.size - 1: args[c] = args[args.size]
            // 0 <= i < a.size - 1 --> a[i] >= a[i + 1]
            int it2 = searchRec(a, x - 1);
            // a[it2 - 1] > x - 1 ^ !(a[it2] > x - 1) --> a[it2 - 1] = a[it1] --> b = it2 - it1
            System.out.println(it1 + " " + (it2 - it1));
        } else {
            // ∄c < args.size - 1: args[c] = args[args.size] --> b = 0
            System.out.println(it1 + " 0");
        }
    }
}
