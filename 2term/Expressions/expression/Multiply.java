package expression.expression;

import expression.types.typeMath;

public class Multiply<T extends Number> extends BinaryFunction<T> implements MyTripleExpression<T> {
    public Multiply(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        super(fir, sec, mode);
    }



    public T calc(T x, T y) {
        return math.mull(x, y);
    }
}
