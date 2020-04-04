package expression.types;

import expression.exceptions.OverflowException;

import static java.lang.Long.parseLong;

public class LongMath implements typeMath<Long> {
    @Override
    public Long add(Long x, Long y) {
        if ((x > 0 && Long.MAX_VALUE - x < y) || (x < 0 && Long.MIN_VALUE - x > y)) {
            throw new OverflowException(x + "+" + y);
        }
        return x + y;
    }

    @Override
    public Long count(Long x) {
        return (long) Long.bitCount(x);
    }

    @Override
    public Long mull(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long dec(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long div(Long x, Long y) {
        return x / y;
    }

    @Override
    public Long min(Long x, Long y) {
        if (x > y) {
            return y;
        } else {
            return x;
        }
    }

    @Override
    public Long max(Long x, Long y) {
        if (x > y) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public Long parseString(String s) {
        return parseLong(s);
    }

    @Override
    public Long negate(Long x) {
        return -x;
    }
}
