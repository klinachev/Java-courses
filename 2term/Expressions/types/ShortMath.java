package expression.types;

import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;

public class ShortMath implements typeMath<Short> {
    @Override
    public Short add(Short x, Short y) {
        int a = (x.intValue() + y.intValue());
        return (short) a;
    }

    @Override
    public Short count(Short x) {
        return (short)(Integer.bitCount(0xFFFF & x));
    }

    @Override
    public Short mull(Short x, Short y) {
        int a =  (x.intValue() * y.intValue());
        return (short) a;
    }

    @Override
    public Short dec(Short x, Short y) {
        int a =  (x.intValue() - y.intValue());
        return (short) a;
    }

    @Override
    public Short div(Short x, Short y) {
        int a =  (x.intValue() / y.intValue());
        return (short) a;
    }

    @Override
    public Short parseString(String s) {
        return (short)parseInt(s);
    }

    @Override
    public Short min(Short x, Short y) {
        if (x > y) {
            return y;
        } else {
            return x;
        }
    }

    @Override
    public Short max(Short x, Short y) {
        if (x > y) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public Short negate(Short x) {
        return (short)-(int)x;
    }
}
