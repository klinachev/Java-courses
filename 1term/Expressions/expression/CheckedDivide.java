package expression;

import exceptions.DivideByZeroException;
import exceptions.OverflowException;

public class CheckedDivide extends BinaryFunction implements CommonExpression {
    public CheckedDivide(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) {
        if (y == 0) {
            throw new DivideByZeroException("Divide by zero");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(x + "/" + y);
        }
        return x / y;
    }

    public int rank() {
        return 4;
    }

    public String symbol() {
        return "/";
    }
}
