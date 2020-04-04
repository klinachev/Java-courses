package expression;

import exceptions.ArgumentException;
import exceptions.OverflowException;

public class CheckedPow extends BinaryFunction implements CommonExpression {
    public CheckedPow(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) throws ArgumentException, OverflowException {
        int a = 1, b = x;
        if (y < 0) {
            throw new ArgumentException("second pow argument -1 or less");
        }
        if (x == 0 && y == 0) {
            throw new ArgumentException("both pow arguments equal to zero");
        }
        if (x == 0) {
            return 0;
        }
        if (y == 0) {
            return 1;
        }
        while (y > 0) {
            if (y % 2 == 0) {
                if (CheckedMultiply.isExseption(b, b)) {
                    throw new OverflowException(x + "**" + y);
                }
                y /= 2;
                b *= b;
            } else {
                if (CheckedMultiply.isExseption(b, a)) {
                    throw new OverflowException(x + "**" + y);
                }
                y--;
                a *= b;
            }
        }
        return a;
    }

    public int rank() {
        return 5;
    }

    public String symbol() {
        return "**";
    }

}
