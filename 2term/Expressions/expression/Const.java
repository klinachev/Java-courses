package expression.expression;

public class Const<T extends Number> implements MyTripleExpression<T> {
    private T val;

    public Const(T val) {
        this.val = val;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return val;
    }


}
