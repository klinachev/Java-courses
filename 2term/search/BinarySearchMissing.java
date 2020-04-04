package search;

import java.util.ArrayList;
import java.util.List;

import static search.BinarySearch.search;

public class BinarySearchMissing {
    public static void main(String[] args) {
        List<Integer> a = new ArrayList<>();
        int x = Integer.parseInt(args[0]);
        for (int i = 1; i < args.length; i++) {
            a.add(Integer.parseInt(args[i]));
        }
        int m = search(a, x);
        if (a.size() > m && a.get(m) == x) {
            System.out.println(m);
        } else {
            System.out.println(-m - 1);
        }
    }
}
