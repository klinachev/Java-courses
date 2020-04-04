package expression.expression;


import expression.types.typeMath;

public class Count<T extends Number> extends UnaryFunction<T> implements MyTripleExpression<T> {


    public Count(MyTripleExpression<T> fir, typeMath<T> mode) {
        super(fir, mode);
    }

    @Override
    protected T calc(T x) {
        return math.count(x);
    }
}
