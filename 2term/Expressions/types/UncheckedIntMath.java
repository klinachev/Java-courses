package expression.types;

import static java.lang.Integer.parseInt;

public class UncheckedIntMath implements typeMath<Integer> {
    @Override
    public Integer add(Integer x, Integer y) {
        return x + y;
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    @Override
    public Integer mull(Integer x, Integer y) {
        return x * y;
    }

    @Override
    public Integer dec(Integer x, Integer y) {
        return x - y;
    }

    @Override
    public Integer div(Integer x, Integer y) {
        return x / y;
    }

    @Override
    public Integer min(Integer x, Integer y) {
        if (x > y) {
            return y;
        } else {
            return x;
        }
    }

    @Override
    public Integer max(Integer x, Integer y) {
        if (x > y) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public Integer parseString(String s) {
        return parseInt(s);
    }

    @Override
    public Integer negate(Integer x) {
        return -x;
    }
}
