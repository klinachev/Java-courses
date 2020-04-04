package expression.expression;

import expression.types.typeMath;

public class Max<T extends Number> extends BinaryFunction<T> implements MyTripleExpression<T> {
    public Max(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        super(fir, sec, mode);
    }

    public T calc(T x, T y) {
        return (T) math.max(x, y);
    }

}
