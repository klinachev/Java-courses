package expression;

import exceptions.ArgumentException;

public class CheckedLog extends BinaryFunction implements CommonExpression {
    public CheckedLog(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) {
        if (x <= 0 || y <= 0) {
            throw new ArgumentException("log argument zero or less" + x + "//" +y);
        }
        if (y == 1) {
            throw new ArgumentException("log second argument equal to one" + x + "//" +y);
        }
        int i;
        for (i = 0; y <= x; i++) {
            x /= y;
        }
        return (i);
    }

    public int rank() {
        return 6;
    }

    public String symbol() {
        return "//";
    }

}
