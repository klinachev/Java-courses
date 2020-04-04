package expression.types;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

import static java.lang.Integer.parseInt;

public class IntMath implements typeMath<Integer> {
    @Override
    public Integer add(Integer x, Integer y) {
        if ((x > 0 && Integer.MAX_VALUE - x < y) || (x < 0 && Integer.MIN_VALUE - x > y)) {
            throw new OverflowException(x + "+" + y);
        }
        return x + y;
    }

    public static boolean isMalException(int x, int a) {
        return a != 0 && x != 0 && (a != -1 || x == Integer.MIN_VALUE) &&
                ((a > 0 && (Integer.MAX_VALUE / a < x || Integer.MIN_VALUE / a > x))
                        || (a < 0 && (Integer.MAX_VALUE / a > x || Integer.MIN_VALUE / a < x)));
    }

    @Override
    public Integer count(Integer x) {
        return Integer.bitCount(x);
    }

    @Override
    public Integer mull(Integer x, Integer y) {
        if (isMalException(x, y)) {
            throw new OverflowException(x + "*" + y);
        }
        return x * y;
    }

    @Override
    public Integer dec(Integer x, Integer y) {
        if ((y < 0 && Integer.MAX_VALUE + y < x) || (y > 0 && Integer.MIN_VALUE + y > x)) {
            throw new OverflowException(x + "-" + y);
        }
        return x - y;
    }

    @Override
    public Integer div(Integer x, Integer y) {
        if (y == 0) {
            throw new DivideByZeroException("Divide by zero");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(x + "/" + y);
        }
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
        if (x == Integer.MIN_VALUE)  {
            throw new OverflowException("-" + x);
        }
        return -x;
    }
}
