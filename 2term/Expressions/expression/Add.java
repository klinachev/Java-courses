package expression.expression;

import expression.types.typeMath;

public class Add<T extends Number> extends BinaryFunction<T> implements MyTripleExpression<T> {

    public Add(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        super(fir, sec, mode);
    }

    public T calc(T x, T y) {
        return math.add(x, y);
    }

}
