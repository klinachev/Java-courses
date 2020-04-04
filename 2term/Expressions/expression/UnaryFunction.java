package expression.expression;

import expression.types.typeMath;

public abstract class UnaryFunction<T extends Number> implements MyTripleExpression<T> {
    protected typeMath<T> math;
    protected MyTripleExpression<T> first;

    public UnaryFunction(MyTripleExpression<T> fir, typeMath<T> mode) {
        first = fir;
        math = mode;
    }

    @Override
    public T evaluate(T x, T y, T z){
        return calc(first.evaluate(x, y, z));
    }

    protected abstract T calc(T x);
}
