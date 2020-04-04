package expression.expression;

import expression.types.typeMath;

public class Negate<T extends Number> extends UnaryFunction<T> implements MyTripleExpression<T> {


    public Negate(MyTripleExpression<T> fir, typeMath<T> mode) {
        super(fir, mode);
    }

    @Override
    protected T calc(T x) {
        return math.negate(x);
    }
}
