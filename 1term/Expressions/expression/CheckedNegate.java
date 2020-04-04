package expression;

import exceptions.OverflowException;

public class CheckedNegate extends UnaryFunction implements CommonExpression {

    public CheckedNegate(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "-";
    }

    @Override
    protected int calc(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("-" + x);
        }
        return (-x);
    }

    @Override
    public int rank() {
        return 1;
    }
}
