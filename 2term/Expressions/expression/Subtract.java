package expression.expression;

import expression.types.typeMath;

public class Subtract<T extends Number> extends BinaryFunction<T> implements MyTripleExpression<T> {
    public Subtract(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        super(fir, sec, mode);
    }

    @Override
    public T calc(T x, T y) {
        return (T) math.dec(x, y);
    }
}
