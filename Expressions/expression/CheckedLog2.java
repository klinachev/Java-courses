package expression;


import exceptions.ArgumentException;

public class CheckedLog2  extends UnaryFunction implements CommonExpression {

    public CheckedLog2(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "log2 ";
    }

    @Override
    protected int calc(int x) {
        if (x <= 0) {
            throw new ArgumentException("log2 argument zero or less");
        }
        int i;
        for (i = 0; 1 < x; i++) {
            x /= 2;
        }
        return (i);
    }

    @Override
    public int rank() {
        return 1;
    }
}
