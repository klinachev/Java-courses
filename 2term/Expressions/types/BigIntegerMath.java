package expression.types;

import java.math.BigInteger;

public class BigIntegerMath implements typeMath<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger mull(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger dec(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger div(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    @Override
    public BigInteger parseString(String s) {
        return new BigInteger(s);
    }

    @Override
    public BigInteger count(BigInteger x) {
        return BigInteger.valueOf((x.bitCount()));
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        if (x.compareTo(y) >= 0) {
            return y;
        } else {
            return x;
        }
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        if (x.compareTo(y) >= 0) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public BigInteger negate(BigInteger x) {
        return x.negate();
    }
}
