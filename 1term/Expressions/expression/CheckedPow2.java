package expression;
import exceptions.ArgumentException;
import exceptions.OverflowException;

public class CheckedPow2  extends UnaryFunction implements CommonExpression {

    public CheckedPow2(CommonExpression fir) {
        first = fir;
    }

    @Override
    protected String symbol() {
        return "pow2 ";
    }

    @Override
    protected int calc(int x) {
        if (x > 30) {
            throw new OverflowException("pow2 " + x);
        }
        if (x < 0) {
            throw new ArgumentException("pow2 argument zero or less");
        }
        int a = 1, y = 2;
        while (x > 0) {
            if (x % 2 == 0) {
                x /= 2;
                y *= y;
            } else {
                x--;
                a *= y;
            }
        }
        return (a);
    }

    @Override
    public int rank() {
        return 1;
    }
}
