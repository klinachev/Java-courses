package expression;

public class Multiply extends BinaryFunction implements CommonExpression {
    public Multiply(CommonExpression fir, CommonExpression sec) {
        first = fir;
        second = sec;
    }

    public int calc(int x, int y) {
        return x * y;
    }

    public int rank() {
        return 3;
    }

    public String symbol() {
        return "*";
    }

    @Override
    protected double calc(double x, double y) {
        return x * y;
    }


}
