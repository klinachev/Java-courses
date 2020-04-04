package expression;

import exceptions.OverflowException;

public class CheckedMultiply extends BinaryFunction implements CommonExpression {
    public CheckedMultiply(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public static boolean isExseption(int x, int a) {
        if (a != 0 && x != 0 && (a != -1 || x == Integer.MIN_VALUE) &&
                ((a > 0 && (Integer.MAX_VALUE / a < x || Integer.MIN_VALUE / a > x))
                || (a < 0 && (Integer.MAX_VALUE / a > x || Integer.MIN_VALUE / a < x)))) {
            return true;
        }
        return false;
    }

    public int calc(int x, int y) {
        if (isExseption(x, y)) {
            throw new OverflowException(x + "*" + y);
        }
        return x * y;
    }

    public int rank() {
        return 3;
    }

    public String symbol() {
        return "*";
    }


}
