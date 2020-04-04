package expression.operations;

import expression.BinaryFunction;
import expression.CommonExpression;

public class rightShift extends BinaryFunction implements CommonExpression {
    public rightShift(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    @Override
    public int rank() {
        return 4;
    }

    @Override
    protected String symbol() {
        return ">>";
    }

    @Override
    protected int calc(int x, int y) {
        return x >> y;
    }

    @Override
    protected double calc(double x, double y) {
        throw new NoSuchMethodError();
    }
}
