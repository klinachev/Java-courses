package expression;

import exceptions.OverflowException;

public class CheckedSubtract extends BinaryFunction implements CommonExpression {
    public CheckedSubtract(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) {
        if ((y < 0 && Integer.MAX_VALUE + y < x) || (y > 0 && Integer.MIN_VALUE + y > x)) {
            throw new OverflowException(x + "-" + y);
        }
        return x - y;
    }

    public int rank() {
        return 2;
    }

    public String symbol() {
        return "-";
    }
}
