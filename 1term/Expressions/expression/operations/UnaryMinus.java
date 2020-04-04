package expression.operations;

import expression.CommonExpression;
import expression.UnaryFunction;

import static java.lang.Math.abs;

public class UnaryMinus extends UnaryFunction implements CommonExpression {

    public UnaryMinus(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "-";
    }

    @Override
    protected int calc(int x) {
        return (-x);
    }

    @Override
    protected double calc(double x) {
        return (-x);
    }

    @Override
    public int rank() {
        return 1;
    }
}
