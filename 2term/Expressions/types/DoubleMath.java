package expression.types;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class DoubleMath implements typeMath<Double> {
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double mull(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double dec(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double div(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double parseString(String s) {
        return parseDouble(s);
    }

    @Override
    public Double count(Double x) {
        return (double) (Long.bitCount(Double.doubleToLongBits(x)));
    }

    @Override
    public Double min(Double x, Double y) {
        if (x >= y) {
            return y;
        } else {
            return x;
        }
    }

    @Override
    public Double max(Double x, Double y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public Double negate(Double x) {
        return -x;
    }
}
