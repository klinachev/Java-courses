package expression.operations;

import expression.CommonExpression;
import expression.UnaryFunction;

import static java.lang.Math.abs;

public class Square extends UnaryFunction implements CommonExpression {

    public Square(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "square";
    }

    @Override
    protected int calc(int x) {
        return x * x;
    }

    @Override
    protected double calc(double x) {
        return x * x;
    }

    @Override
    public int rank() {
        return 1;
    }
}
