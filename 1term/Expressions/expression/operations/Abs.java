package expression.operations;

import expression.CommonExpression;
import expression.UnaryFunction;

import static java.lang.Math.abs;

public class Abs extends UnaryFunction implements CommonExpression {
    public Abs(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "abs";
    }

    @Override
    protected int calc(int x) {
        return abs(x);
    }

    @Override
    protected double calc(double x) {
        return abs(x);
    }

    @Override
    public int rank() {
        return 1;
    }
}
