package expression.expression;

import expression.types.typeMath;

public class Min<T extends Number> extends BinaryFunction<T> implements MyTripleExpression<T> {
    public Min(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        super(fir, sec, mode);
    }

    public T calc(T x, T y) {
        return (T) math.min(x, y);
    }

}
