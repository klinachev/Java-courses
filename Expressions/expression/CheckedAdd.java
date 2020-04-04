package expression;

import exceptions.OverflowException;

public class CheckedAdd extends BinaryFunction implements TripleExpression {
    public CheckedAdd(TripleExpression fir, TripleExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) {
        if ((x > 0 && Integer.MAX_VALUE - x < y) || (x < 0 && Integer.MIN_VALUE - x > y)) {
            throw new OverflowException(x + "+" + y);
        }
        return x + y;
    }

    public int rank() {
        return 1;
    }

    public String symbol() {
        return "+";
    }

}
