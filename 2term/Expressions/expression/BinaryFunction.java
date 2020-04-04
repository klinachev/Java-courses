package expression.expression;

import expression.types.typeMath;

public abstract class BinaryFunction<T extends Number> implements MyTripleExpression<T> {
    protected typeMath<T> math;
    protected MyTripleExpression<T> first, second;

    protected BinaryFunction(MyTripleExpression<T> fir, MyTripleExpression<T> sec, typeMath<T> mode) {
        first = fir;
        second = sec;
        math = mode;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(first.evaluate(x, y, z), second.evaluate(x, y, z));
    }

    protected abstract T calc(T evaluate, T evaluate1);

}
